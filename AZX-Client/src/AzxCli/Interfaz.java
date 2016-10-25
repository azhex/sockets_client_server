package AzxCli;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
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
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.BevelBorder;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.Insets;

public class Interfaz {
	public JButton btnSendAll, btnConnect, btnExit, btnSendTo, btnClose;
	public JTextField inMsg, inPort, inSendTo, inHost;
	public JTextArea txtLog, txtClientes;
	public JScrollPane scllLog;
	public JLabel lblNewLabel, lblPuerto, lblNewLabel_1, lblClientStatus, lblNick;
	public JFrame frm;
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public Interfaz(String vTitle){
		frm = new JFrame(vTitle);
		frm.getContentPane().setBackground(new Color(204, 204, 102));
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frm.setIconImage (new ImageIcon(getClass().getResource("res/ic_launcher.png")).getImage());
		frm.getContentPane().setLayout(null);
		
		JLabel lblSocketsClient = new JLabel("Sockets client");
		lblSocketsClient.setToolTipText("http://azhex.ddns.net:8080");
		lblSocketsClient.setFont(new Font("Yu Gothic UI Light", Font.BOLD, 25));
		lblSocketsClient.setBounds(10, 11, 186, 27);
		frm.getContentPane().add(lblSocketsClient);
		
		scllLog = new JScrollPane();
		scllLog.setViewportBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		scllLog.setBounds(10, 49, 722, 361);
		frm.getContentPane().add(scllLog);
		
		txtLog = new JTextArea();
		txtLog.setMargin(new Insets(7, 7, 7, 7));
		txtLog.setLineWrap(true);
		scllLog.setViewportView(txtLog);
		txtLog.setText("Log:\n");
		txtLog.setEditable(false);
		txtLog.setFont(new Font("SansSerif", Font.BOLD, 12));
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setViewportBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		scrollPane_1.setBounds(742, 49, 267, 361);
		frm.getContentPane().add(scrollPane_1);
		
		txtClientes = new JTextArea();
		txtClientes.setMargin(new Insets(7, 7, 7, 7));
		txtClientes.setLineWrap(true);
		scrollPane_1.setViewportView(txtClientes);
		txtClientes.setEditable(false);
		txtClientes.setFont(new Font("SansSerif", Font.BOLD, 12));
		txtClientes.setText("Clientes conectados:");
		
		JLabel lblByGabrielRevilla = new JLabel("By Gabriel Revilla Horcas");
		lblByGabrielRevilla.setFont(new Font("DejaVu Sans", Font.ITALIC, 12));
		lblByGabrielRevilla.setBounds(206, 22, 197, 14);
		frm.getContentPane().add(lblByGabrielRevilla);
		
		JPanel panel = new JPanel();
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel.setBounds(10, 421, 514, 106);
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
		lblEnviarMensaje.setBounds(10, 11, 494, 14);
		panel.add(lblEnviarMensaje);
		
		btnSendAll = new JButton("Enviar a todos");
		btnSendAll.setBounds(205, 72, 149, 23);
		panel.add(btnSendAll);
		btnSendAll.setBackground(Color.LIGHT_GRAY);
		
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
		panel_1.setBounds(534, 421, 475, 106);
		frm.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		btnClose = new JButton("Desconectar");
		btnClose.setBounds(10, 72, 144, 23);
		panel_1.add(btnClose);
		btnClose.setBackground(Color.RED);
		
		btnConnect = new JButton("Conectar");
		btnConnect.setBounds(164, 72, 134, 23);
		panel_1.add(btnConnect);
		btnConnect.setBackground(new Color(0, 204, 102));
		
		btnExit = new JButton("Cerrar");
		btnExit.setBounds(336, 72, 129, 23);
		panel_1.add(btnExit);
		btnExit.setBackground(Color.LIGHT_GRAY);
		
		JLabel lblControlDeClientes = new JLabel("Conexi\u00F3n y control del cliente");
		lblControlDeClientes.setFont(new Font("DejaVu Sans", Font.BOLD | Font.ITALIC, 12));
		lblControlDeClientes.setBounds(10, 11, 194, 14);
		panel_1.add(lblControlDeClientes);
		
		inPort = new JTextField();
		inPort.setBounds(390, 41, 75, 20);
		panel_1.add(inPort);
		inPort.setColumns(10);
		
		lblNewLabel = new JLabel("Host:");
		lblNewLabel.setBounds(10, 44, 61, 14);
		panel_1.add(lblNewLabel);
		
		inHost = new JTextField();
		inHost.setBounds(81, 41, 217, 20);
		panel_1.add(inHost);
		inHost.setColumns(10);
		
		lblPuerto = new JLabel("Puerto:");
		lblPuerto.setBounds(308, 44, 72, 14);
		panel_1.add(lblPuerto);
		
		lblNewLabel_1 = new JLabel("Estado:");
		lblNewLabel_1.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 12));
		lblNewLabel_1.setBounds(214, 11, 61, 14);
		panel_1.add(lblNewLabel_1);
		
		lblClientStatus = new JLabel("No conectado");
		lblClientStatus.setForeground(Color.RED);
		lblClientStatus.setBounds(285, 12, 180, 14);
		panel_1.add(lblClientStatus);
		
		JLabel lblNickname = new JLabel("Nickname:");
		lblNickname.setBounds(742, 23, 59, 14);
		frm.getContentPane().add(lblNickname);
		
		lblNick = new JLabel("DEFAULT");
		lblNick.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNick.setBounds(811, 23, 198, 14);
		frm.getContentPane().add(lblNick);
		
		frm.setSize(1026, 565);
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