package AzxCli;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JOptionPane;

public class Init {
	private static Client cli;
	
	public static void main(String[] args) {
		Interfaz gui = new Interfaz("Sockets client");
		gui.btnClose.setEnabled(false);
		gui.btnClose.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(cli.conectado  && cli != null){
					try {
						if(cli.sock.isConnected()){
							cli.sock.close();
							if(cli.sock.isClosed()){
								gui.printLog("Desconectado del servidor ["+cli.HOST+"] ["+String.valueOf(cli.PORT)+"]\n");
								cli.conectado = false;
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
				if(cli.conectado && cli != null) cli.sendMsgAll(cli.sock, gui.inMsg.getText().toString());
			}
		});
		gui.btnSendTo.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(cli.conectado && cli != null) cli.sendMsg(cli.sock, gui.inMsg.getText().toString(), Integer.parseInt(gui.inSendTo.getText().toString()));
			}
		});
		gui.btnConnect.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					cli = null;
					System.gc();
					cli = new Client(gui.inHost.getText().toString(), Integer.parseInt(gui.inPort.getText().toString()), gui);
				}catch(Exception err){
					gui.lblClientStatus.setForeground(Color.RED);
					gui.lblClientStatus.setText("Error");
					JOptionPane.showMessageDialog(null, "Error al conectar al servidor, asegurate de que el puerto y host introducidos son correctos, si no contacta con el desarrollador: azhex99@gmail.com", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		gui.btnExit.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
	}
}
