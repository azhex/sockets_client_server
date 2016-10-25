package AzxServ;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

import javax.imageio.ImageIO;

public class Server {
	private ServerSocket serverSocket;
	private Socket[] clientsSocket;
	private String[] nicknames;
	private int maxClients;
	private Interfaz gui;
	private boolean conectado = false;
	private String separador = "-1-2-3-";
	private String separador2 = "-4-5-6-";
	
	public Server(int port, int mC, Interfaz g) {
		try{
			gui = g;
			maxClients = mC;
			
			gui.btn2.setEnabled(false);
			gui.btn3.setEnabled(true);
			
			clientsSocket = new Socket[maxClients];
			nicknames = new String[maxClients];
			
			new Thread(new Runnable(){
				public void run(){
					gui.btn1.addActionListener(new ActionListener(){
						@Override
						public void actionPerformed(ActionEvent e) {
							if(conectado){
								String msg = gui.inMsg.getText();
								
								sendMsgAll(clientsSocket, msg, true);
							}
						}
					});
					gui.btn3.addActionListener(new ActionListener(){
						@Override
						public void actionPerformed(ActionEvent e) {
							for(int i=0;i<clientsSocket.length;i++){
								try {
									if(clientsSocket[i] != null){
										clientsSocket[i].close();
										clientsSocket[i] = null;
									}
									serverSocket.close();
									conectado = false;
									gui.printClients("Clientes:\n");
									gui.txtLog.setText("Log:\n");;
									gui.lblServStatus.setForeground(Color.RED);
									gui.lblServStatus.setText("No conectado");
									gui.btn2.setEnabled(true);
									gui.btn3.setEnabled(false);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
							}
						}
					});
					gui.btnSendTo.addActionListener(new ActionListener(){
						@Override
						public void actionPerformed(ActionEvent e){
							if(conectado){
								sendMsg(clientsSocket[(int) Integer.parseInt(gui.inSendTo.getText().toString())], gui.inMsg.getText(), gui.inSendTo.getText().toString(), true);
							}
						}
					});
					gui.btnCloseTo.addActionListener(new ActionListener(){
						@Override
						public void actionPerformed(ActionEvent arg0) {
							if(conectado){
								try {
									clientsSocket[(int) Integer.parseInt(gui.inCloseTo.getText())].close();
									clientsSocket[(int) Integer.parseInt(gui.inCloseTo.getText())] = null;
									gui.printLog("[Cliente "+(int) Integer.parseInt(gui.inCloseTo.getText())+" desconectado]\n");
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					});
				}
			}).start();
			
			System.out.println("Servidor iniciado puerto: "+port);
			gui.printLog("Servidor iniciado en el puerto:"+String.valueOf(port)+"\n");
			
			serverSocket = new ServerSocket(port);
			conectado = true;
			
			gui.lblServStatus.setForeground(Color.BLUE);
			gui.lblServStatus.setText("Esperando conexiones [" + String.valueOf(port) + "]");
			
			new Thread(new Runnable(){
				public void run(){
					try {
						waitConnection();
					} catch (IOException e) {
						System.out.println("Socket cerrado, cancelando conexiones entrantes");
					}
				}
			}).start();
			
			new Thread(new Runnable(){
				public void run(){
					try {
						manageConnections();
					} catch (IOException | InterruptedException e) {
						e.printStackTrace();
					}
				}
			}).start();
			
			new Thread(new Runnable(){
				@Override
				public void run() {
					while(conectado){
						for(int i=0;i < maxClients; i++){
							if(clientsSocket[i] != null && clientsSocket[i].isConnected()){
								sendClientList(clientsSocket[i], String.valueOf(i));
							}
						}
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}).start();
			
		} catch (IOException e1) {
			gui.lblServStatus.setForeground(Color.RED);
			gui.lblServStatus.setText("Error [" + String.valueOf(port) + "]");
			conectado = false;
		}
	}
	
	private void waitConnection() throws IOException{
		while(conectado){
			for(int i=0;i < maxClients; i++){
				if(clientsSocket[i] == null || clientsSocket[i].isInputShutdown()){
					final int clientN = i;
					clientsSocket[i] = serverSocket.accept();
					if(clientsSocket[i].isConnected()){
						nicknames[i] = "";
						System.out.println("Cliente conectado -> "+i);
						gui.printLog("Cliente conectado -> "+String.valueOf(i)+" ["+clientsSocket[i].getInetAddress().toString()+":"+String.valueOf(clientsSocket[i].getPort())+"]\n");
						PrintWriter out = new PrintWriter(clientsSocket[i].getOutputStream(), true);
						new Thread(new Runnable(){
							public void run(){
								recibirMensajes(clientN);
							}
						}).start();
						System.out.println("[Mensaje de bienvenida a "+i+"]");
						out.println("[Bienvenido al servidor de azhex, eres el cliente -> "+i+"]");
						
						break;
					}
				}else{
					
				}
			}
		}
	}
	
	private void sendClientList(Socket cli, String id){
		String connMangData = "";
		
		connMangData += "clientslist"+separador;
		connMangData += new Date()+separador2;
		connMangData += "------------Connection Manager-------------"+separador2;
		for(int i=0;i < maxClients;i++){
			if(clientsSocket[i] == null){
				connMangData += String.valueOf(i)+" -> null"+separador2;
			}else if(clientsSocket[i].isInputShutdown() || clientsSocket[i].isClosed() || clientsSocket[i].isOutputShutdown()){
				connMangData += String.valueOf(i)+" -> Desconectado"+separador2;
				clientsSocket[i] = null;
			}else{
				connMangData += String.valueOf(i)+" -> ["+nicknames[i]+"] ["+clientsSocket[i].getInetAddress().toString()+"] [OK]"+separador2;
			}
		}
		connMangData += "-------------------------------------------"+separador2;
		sendMsg(cli, connMangData, id, false);
	}
	
	private void manageConnections() throws IOException, InterruptedException{
		while(conectado){
			String connMangData = "";
			
			connMangData += new Date()+"\n";
			connMangData += "------------Connection Manager------------\n";
			for(int i=0;i < maxClients;i++){
				if(clientsSocket[i] == null){
					connMangData += String.valueOf(i)+" -> null\n";
					nicknames[i] = "";
				}else if(clientsSocket[i].isInputShutdown() || clientsSocket[i].isClosed() || clientsSocket[i].isOutputShutdown()){
					connMangData += String.valueOf(i)+" -> Desconectado\n";
					clientsSocket[i] = null;
					nicknames[i] = "";
				}else{
					connMangData += String.valueOf(i)+" -> ["+nicknames[i]+"] ["+clientsSocket[i].getInetAddress().toString()+"] [OK]\n";
				}
			}
			connMangData += "------------------------------------------\n";
			gui.printClients(connMangData);
			
			Thread.sleep(1000);
		}
	}
	
	private void recibirMensajes(int i){
		while(true){
			if(clientsSocket[i] != null && !clientsSocket[i].isInputShutdown() && !clientsSocket[i].isClosed()){
				try {
					String data;
					BufferedReader in = new BufferedReader(new InputStreamReader(clientsSocket[i].getInputStream()));
					
					data = in.readLine();
					
					if(data == null){
						clientsSocket[i].close();
						clientsSocket[i] = null;
						gui.printLog("[Cliente "+i+" desconectado]\n");
						break;
					}else{
						String[] dataAux = data.split(separador);
						
						/*
						 * DataAux -> Datos extra que deben ser procesados
						 * Chat -> DataAux[0]=tipo de procesamiento, dataAux[1]:mensaje, dataAux[2]: clientId
						 */
						
						if(dataAux.length > 1){
							if(dataAux.length == 2){
								if(dataAux[0].equals("nicknameSetup")){
									nicknames[i] = dataAux[1];
								}else if(dataAux[0].equals("sendall")){
									gui.printLog("[MSG Chat de: ["+i+"] ["+nicknames[i]+"] enviando a todos] -> "+dataAux[1]+"\n");
									sendMsgAll(clientsSocket, "chatrecv"+separador+String.valueOf(i)+separador+nicknames[i]+separador+dataAux[1], false);
								}
							}else if(dataAux.length == 3){
								if(dataAux[0].equals("sendto")){
									gui.printLog("[MSG Chat de: ["+i+"] ["+nicknames[i]+"] enviando a ["+dataAux[2]+"] ["+nicknames[Integer.parseInt(dataAux[2])]+"] de ["+String.valueOf(i)+"]] -> "+dataAux[1]+"\n");
									sendMsg(clientsSocket[Integer.parseInt(dataAux[2].toString())], "chatrecv"+separador+String.valueOf(i)+separador+nicknames[Integer.parseInt(dataAux[2])]+separador+dataAux[1], dataAux[2].toString(), false);
								}
							}else{
								gui.printLog("[MSG Chat de: "+i+" -> error en la configuracion de los datos recibidos] -> "+data+"\n");
							}
						}else{
							gui.printLog("[MSG De: "+i+"] -> "+data+"\n");
						}
					}
				} catch (IOException e1) {
					System.out.println("Connection reset error en recv");
				}
			}else{
				break;
			}
		}
	}
	
	private void sendMsgAll(Socket[] clientsSocket, String msg, boolean display){
		for(int i = 0;i < clientsSocket.length;i++){
			if(clientsSocket[i] != null){
				try {
					PrintWriter out = new PrintWriter(clientsSocket[i].getOutputStream(), true);
					if(display) gui.printLog("Enviando a todos ["+String.valueOf(i)+"] -> "+msg+"\n");
					out.println(msg);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	private void sendMsg(Socket clientSocket, String msg, String id, boolean display){
		if(clientSocket != null){
			try {
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
				if(display) gui.printLog("Enviando a "+clientSocket.getInetAddress()+" ["+id+"] -> "+msg+"\n");
				out.println(msg);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
