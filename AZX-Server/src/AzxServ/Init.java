package AzxServ;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JOptionPane;

import AzxServ.Interfaz;
import AzxServ.Server;

public class Init {
	public static Server serv;

	public static void main(String[] args) {
		Interfaz gui = new Interfaz("Sockets server");
		
		gui.btn3.setEnabled(false);
		gui.btn1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(serv.conectado && serv != null){
					String msg = gui.inMsg.getText();
					
					serv.sendMsgAll(serv.clientsSocket, msg, true);
				}
			}
		});
		gui.btn3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(serv != null){
					for(int i=0;i<serv.clientsSocket.length;i++){
						try {
							if(serv.clientsSocket[i] != null){
								serv.clientsSocket[i].close();
								serv.clientsSocket[i] = null;
							}
							serv.serverSocket.close();
							serv.conectado = false;
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
			}
		});
		gui.btnSendTo.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				if(serv.conectado  && serv != null){
					serv.sendMsg(serv.clientsSocket[(int) Integer.parseInt(gui.inSendTo.getText().toString())], gui.inMsg.getText(), gui.inSendTo.getText().toString(), true);
				}
			}
		});
		gui.btnCloseTo.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(serv.conectado  && serv != null){
					try {
						serv.clientsSocket[(int) Integer.parseInt(gui.inCloseTo.getText())].close();
						serv.clientsSocket[(int) Integer.parseInt(gui.inCloseTo.getText())] = null;
						gui.printLog("[Cliente "+(int) Integer.parseInt(gui.inCloseTo.getText())+" desconectado]\n");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		gui.btn2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					serv = null;
					System.gc();
					serv = new Server(Integer.parseInt(gui.inPort.getText()), Integer.parseInt(gui.inMaxClients.getText()), gui);
				}catch(Exception err){
					gui.lblServStatus.setForeground(Color.RED);
					gui.lblServStatus.setText("Error");
					JOptionPane.showMessageDialog(null, "Error al iniciar servidor, asegurate de que el puerto y los clientes máximos introducidos son correctos, si no contacta con el desarrollador: azhex99@gmail.com", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

}
