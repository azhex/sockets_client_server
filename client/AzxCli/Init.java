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
