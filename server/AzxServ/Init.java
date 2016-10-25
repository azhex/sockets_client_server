package AzxServ;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import AzxServ.Interfaz;
import AzxServ.Server;

public class Init {
	public static Server serv;

	public static void main(String[] args) {
		Interfaz gui = new Interfaz("Sockets server");
		gui.btn3.setEnabled(false);
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
