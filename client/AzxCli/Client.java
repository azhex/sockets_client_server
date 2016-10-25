package AzxCli;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.swing.JOptionPane;

public class Client {
	private String HOST = "azx.ddns.net";
	private int PORT = 7979;
	private Socket sock;
	private PrintWriter out;
	private BufferedReader in;
	private boolean conectado = false;
	private Interfaz gui;
	private String separador = "-1-2-3-";
	private String separador2 = "-4-5-6-";
	private String nickname = "DEFAULT";
	
	public Client(String h, int p, Interfaz g){
		HOST = h;
		PORT = p;
		gui = g;
		
		
		gui.btnConnect.setEnabled(false);
		nickname = JOptionPane.showInputDialog(gui.frm, "Selecciona un nickname, para poder identificarte");
		if(nickname == null){
			nickname = "DEFALUT";
		}
		gui.lblNick.setText(nickname);
		gui.printLog("Conectando al servidor ["+HOST+"] en el puerto ["+String.valueOf(PORT)+"]\n");
		gui.btnClose.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(conectado){
					try {
						if(sock.isConnected()){
							sock.close();
							if(sock.isClosed()){
								gui.printLog("Desconectado del servidor ["+HOST+"] ["+String.valueOf(PORT)+"]\n");
								conectado = false;
								gui.btnClose.setEnabled(false);
								gui.btnConnect.setEnabled(true);
								gui.lblClientStatus.setForeground(Color.RED);
								gui.lblClientStatus.setText("No conectado");
								gui.printClients("Clientes conectados:");
							}
						}
					} catch (IOException e1) {
						gui.printLog("Error al desconectar del servidor\n");
						e1.printStackTrace();
					}
				}
			}
		});
		gui.btnSendAll.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(conectado) sendMsgAll(sock, gui.inMsg.getText().toString());
			}
		});
		gui.btnSendTo.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(conectado) sendMsg(sock, gui.inMsg.getText().toString(), Integer.parseInt(gui.inSendTo.getText().toString()));
			}
		});
		
		new Thread(new Runnable(){
			@Override
			public void run(){
				sock = new Socket();
				try {
					sock.connect(new InetSocketAddress(HOST, PORT), 7000);
				} catch (IOException e) {
					conectado = false;
					gui.btnConnect.setEnabled(true);
					gui.lblClientStatus.setForeground(Color.RED);
					gui.lblClientStatus.setText("No conectado [Error]");
					gui.printLog("Error 001 [Timeout] al conectar con el servidor ["+HOST+"] en el puerto ["+String.valueOf(PORT)+"]\n");
				}
				if(sock.isConnected()){
					gui.printLog("Conectado a ["+HOST+"] en el puerto ["+String.valueOf(PORT)+"]\n");
					conectado = true;
					gui.btnClose.setEnabled(true);
					gui.lblClientStatus.setForeground(Color.BLUE);
					gui.lblClientStatus.setText("Conectado [" + HOST + ":" + String.valueOf(PORT) + "]");
					conectado();
				}else{
					conectado = false;
					gui.lblClientStatus.setForeground(Color.RED);
					gui.lblClientStatus.setText("No conectado [Error]");
					gui.printLog("Error 002 [No conectado] al conectar con el servidor ["+HOST+"] en el puerto ["+String.valueOf(PORT)+"]\n");
				}
			}
		}).start();
	}
	
	private void conectado(){
		new Thread(new Runnable(){
			@Override
			public void run() {
				recibirMensajes();
			}
		}).start();
	}
	
	private void sendMsg(Socket clientSocket, String msg, int userId){
		if(!clientSocket.isClosed()){
			try {
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
				gui.printLog("Enviando a "+clientSocket.getInetAddress().toString()+" ["+String.valueOf(userId)+"] -> "+msg+"\n");
				out.println("sendto"+separador+msg+separador+String.valueOf(userId));
				//0:tipo de process; 1:mensaje; 2:clientId;
			} catch (IOException e1) {
				gui.printLog("Error 003 [Desconocido] al enviar mensaje\n");
				e1.printStackTrace();
			}
		}else{
			gui.printLog("Error 001 [No conectado] al enviar mensaje\n");
		}
	}
	
	private void sendMsgAll(Socket clientSocket, String msg){
		if(!clientSocket.isClosed()){
			try {
				PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
				gui.printLog("Enviando a todos -> "+msg+"\n");
				out.println("sendall"+separador+msg);
			} catch (IOException e1) {
				gui.printLog("Error 003 [Desconocido] al enviar mensaje\n");
				e1.printStackTrace();
			}
		}else{
			gui.printLog("Error 001 [No conectado] al enviar mensaje\n");
		}
	}
	
	private void recibirMensajes(){
		try {
			String data;
			
			BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
			out.println("nicknameSetup"+separador+nickname);
			
			while(conectado){
				data = in.readLine();
				
				if(data == null){
					sock.close();
					gui.btnClose.setEnabled(false);
					gui.btnConnect.setEnabled(true);
					gui.printLog("[Servidor desconectado]\n");
					break;
				}else{
					if(data.split(separador).length > 1){
						String[] dataAux = data.split(separador);
						
						//0:tipo de process; 1:cliId; 2:nickname; 3:mensaje;
						
						if(dataAux[0].equals("clientslist")){
							String[] manageConnections = dataAux[1].split(separador2);
							String manageConnectionsFinal = "";
							
							for(int i=0;i < manageConnections.length;i++){
								manageConnectionsFinal += manageConnections[i]+"\n";
							}
							gui.printClients(manageConnectionsFinal);
						}else if(dataAux[0].equals("chatrecv")){
							gui.printLog("[MSG De ["+dataAux[2]+"] ["+dataAux[1]+"] -> "+dataAux[3]+"\n");
						}
					}else{						
						gui.printLog("[MSG Del servidor] -> "+data+"\n");
					}
				}
			}
		} catch (IOException e) {
			if(!sock.isConnected()){
				gui.printLog("[Servidor desconectado]\n");
				try {
					sock.close();
					gui.btnClose.setEnabled(false);
					gui.btnConnect.setEnabled(true);
				} catch (IOException e1) {
					System.out.println("Error al recibir mensaje, puede que el servidor ya no este conectado.");
				}
			}
			System.out.println("Error al recibir mensaje, puede que el servidor ya no este conectado.");
		}
	}
}
