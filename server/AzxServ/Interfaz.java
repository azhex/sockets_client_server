package AzxServ;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.BevelBorder;
import java.awt.Insets;
import java.awt.Point;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.EtchedBorder;

public class Interfaz {
	public JButton btn1, btn2, btn3, btnSendTo, btnCloseTo;
	public JTextField inMsg, inPort, inMaxClients, inCloseTo, inSendTo;
	public JTextArea txtLog, txtClientes;
	public JScrollPane scllLog, scllClients;
	public JLabel lblServStatus;
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public Interfaz(String vTitle){
		JFrame frm = new JFrame(vTitle);
		frm.getContentPane().setBackground(new Color(204, 204, 102));
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frm.setIconImage (new ImageIcon(getClass().getResource("res/ic_launcher.png")).getImage());
		frm.getContentPane().setLayout(null);
		
		JLabel lblSocketsServer = new JLabel("Sockets server");
		lblSocketsServer.setToolTipText("http://azx.ddns.net:8080");
		lblSocketsServer.setFont(new Font("Yu Gothic UI Light", Font.BOLD, 25));
		lblSocketsServer.setBounds(10, 11, 192, 27);
		frm.getContentPane().add(lblSocketsServer);
		
		scllLog = new JScrollPane();
		scllLog.setViewportBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		scllLog.setBackground(Color.WHITE);
		scllLog.setBounds(10, 49, 735, 353);
		frm.getContentPane().add(scllLog);
		
		txtLog = new JTextArea();
		txtLog.setMargin(new Insets(7, 7, 7, 7));
		txtLog.setForeground(Color.DARK_GRAY);
		txtLog.setBackground(Color.WHITE);
		txtLog.setLineWrap(true);
		scllLog.setViewportView(txtLog);
		txtLog.setText("Log:\r\n");
		txtLog.setEditable(false);
		txtLog.setFont(new Font("SansSerif", Font.BOLD, 12));
		
		scllClients = new JScrollPane();
		scllClients.setViewportBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		scllClients.setBackground(Color.WHITE);
		scllClients.setBounds(755, 49, 254, 353);
		frm.getContentPane().add(scllClients);
		
		txtClientes = new JTextArea();
		txtClientes.setMargin(new Insets(7, 7, 7, 7));
		txtClientes.setForeground(Color.DARK_GRAY);
		txtClientes.setBackground(Color.WHITE);
		txtClientes.setLineWrap(true);
		scllClients.setViewportView(txtClientes);
		txtClientes.setEditable(false);
		txtClientes.setFont(new Font("SansSerif", Font.BOLD, 12));
		txtClientes.setText("Clientes:");
		
		JLabel lblByGabrielRevilla = new JLabel("By Gabriel Revilla Horcas");
		lblByGabrielRevilla.setFont(new Font("DejaVu Sans", Font.ITALIC, 12));
		lblByGabrielRevilla.setBounds(206, 22, 197, 14);
		frm.getContentPane().add(lblByGabrielRevilla);
		
		JPanel panel = new JPanel();
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel.setBounds(10, 413, 514, 106);
		frm.getContentPane().add(panel);
		panel.setLayout(null);
		
		inMsg = new JTextField();
		inMsg.setBounds(96, 42, 408, 20);
		panel.add(inMsg);
		inMsg.setToolTipText("Enviar mensaje");
		inMsg.setColumns(10);
		
		inSendTo = new JTextField();
		inSendTo.setBounds(149, 73, 46, 20);
		panel.add(inSendTo);
		inSendTo.setColumns(10);
		
		btnSendTo = new JButton("Enviar a ->");
		btnSendTo.setBounds(10, 72, 129, 23);
		panel.add(btnSendTo);
		btnSendTo.setBackground(Color.LIGHT_GRAY);
		
		JLabel lblEnviarMensaje = new JLabel("Enviar mensaje");
		lblEnviarMensaje.setFont(new Font("DejaVu Sans", Font.BOLD | Font.ITALIC, 12));
		lblEnviarMensaje.setBounds(10, 11, 568, 14);
		panel.add(lblEnviarMensaje);
		
		btn1 = new JButton("Enviar a todos");
		btn1.setBounds(205, 72, 149, 23);
		panel.add(btn1);
		btn1.setBackground(Color.LIGHT_GRAY);
		
		JLabel lblMensaje = new JLabel("Mensaje:");
		lblMensaje.setBounds(10, 45, 76, 14);
		panel.add(lblMensaje);
		
		JButton btn4 = new JButton("Limpiar log");
		btn4.setBounds(364, 72, 140, 23);
		panel.add(btn4);
		btn4.setBackground(Color.LIGHT_GRAY);
		btn4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtLog.setText("Log:\n");
			}
		});
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel_1.setBounds(534, 413, 475, 106);
		frm.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		inCloseTo = new JTextField();
		inCloseTo.setBounds(249, 73, 71, 20);
		panel_1.add(inCloseTo);
		inCloseTo.setColumns(10);
		
		btnCloseTo = new JButton("Desconectar cliente ->");
		btnCloseTo.setBounds(10, 72, 229, 23);
		panel_1.add(btnCloseTo);
		btnCloseTo.setBackground(Color.LIGHT_GRAY);
		
		btn2 = new JButton("Iniciar server");
		btn2.setBounds(330, 43, 135, 23);
		panel_1.add(btn2);
		btn2.setBackground(new Color(0, 204, 102));
		
		btn3 = new JButton("Cerrar");
		btn3.setBounds(330, 72, 135, 23);
		panel_1.add(btn3);
		btn3.setBackground(new Color(255, 0, 0));
		
		JLabel lblControlDeClientes = new JLabel("Control de clientes y servidor");
		lblControlDeClientes.setFont(new Font("DejaVu Sans", Font.BOLD | Font.ITALIC, 12));
		lblControlDeClientes.setBounds(10, 11, 196, 14);
		panel_1.add(lblControlDeClientes);
		
		inMaxClients = new JTextField();
		inMaxClients.setBounds(122, 42, 45, 20);
		panel_1.add(inMaxClients);
		inMaxClients.setColumns(10);
		
		JLabel lblNMaxClientes = new JLabel("N\u00BA Max Clientes:");
		lblNMaxClientes.setBounds(10, 47, 102, 14);
		panel_1.add(lblNMaxClientes);
		
		inPort = new JTextField();
		inPort.setBounds(249, 44, 71, 20);
		panel_1.add(inPort);
		inPort.setColumns(10);
		
		JLabel lblPuerto = new JLabel("Puerto:");
		lblPuerto.setBounds(177, 47, 62, 14);
		panel_1.add(lblPuerto);
		
		JLabel lblNewLabel = new JLabel("Estado:");
		lblNewLabel.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 12));
		lblNewLabel.setBounds(216, 11, 55, 14);
		panel_1.add(lblNewLabel);
		
		lblServStatus = new JLabel("No conectado");
		lblServStatus.setForeground(Color.RED);
		lblServStatus.setBounds(281, 12, 184, 14);
		panel_1.add(lblServStatus);
		
		frm.setSize(1026, 556);
		frm.setResizable(false);
		frm.setLocationRelativeTo(null);
		
		frm.setVisible(true);
	}
	
	public void printLog(String txt){
		this.txtLog.setText(this.txtLog.getText()+new Date()+" | "+txt);
		Dimension tamanhoTextArea = txtLog.getSize();
		Point p = new Point(
		   0,
		   tamanhoTextArea.height);
		scllLog.getViewport().setViewPosition(p);
	}
	
	public void printClients(String txt){
		this.txtClientes.setText(txt);
	}
}
