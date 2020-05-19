package gui;
/*
 * Author: Xiguang Li, Xinnan SHEN
 * 
 * Date: 07/05/2020
 * 
 */
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;

import concurrency.Client;
import transmission.ConnectThread;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.regex.Pattern;
//This file is to show a welcome frame
//needs to input the server ip and port to connect
public class Welcome {

	private JFrame frame;
	private JTextField textIP;
	private JTextField textPort;

	
	/**
	 * Create the application.
	 */
	public Welcome() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("MVCC Client");
		frame.setBounds(100, 100, 400, 250);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);
		
		textIP = new JTextField();
		textIP.setText("127.0.0.1");
		textIP.setBounds(137, 42, 187, 22);
		frame.getContentPane().add(textIP);
		textIP.setColumns(10);
		
		textPort = new JTextField();
		textPort.setText("7777");
		textPort.setBounds(137, 104, 187, 22);
		frame.getContentPane().add(textPort);
		textPort.setColumns(10);
		
		JLabel lblServerIp = new JLabel("Server IP:");
		lblServerIp.setFont(new Font("Dialog", Font.BOLD, 15));
		lblServerIp.setBounds(12, 42, 107, 18);
		frame.getContentPane().add(lblServerIp);
		
		JLabel lblServerPort = new JLabel("Server PORT:");
		lblServerPort.setFont(new Font("Dialog", Font.BOLD, 15));
		lblServerPort.setBounds(12, 104, 107, 18);
		frame.getContentPane().add(lblServerPort);
		//confirm button
		JButton btnConfirm = new JButton("Confirm");
		btnConfirm.addMouseListener(new MouseAdapter() {
			

			@Override
			public void mouseReleased(MouseEvent e) {
				super.mouseReleased(e);
				if(btnConfirm.isEnabled()) {
					//connect to server
					Object isconnected=new Object();
					BtnTextChange bc=new BtnTextChange(isconnected, btnConfirm);
					Client.getThreadpool().execute(bc);
					ConnectThread ct=new ConnectThread(isconnected, frame,textIP,textPort);
					Client.getThreadpool().execute(ct);
					textIP.setEnabled(false);
					textPort.setEnabled(false);
				}
			}
		});
		btnConfirm.setBounds(119, 159, 105, 28);
		frame.getContentPane().add(btnConfirm);
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	public JTextField getTextIP() {
		return textIP;
	}

	public void setTextIP(JTextField textIP) {
		this.textIP = textIP;
	}

	public JTextField getTextPort() {
		return textPort;
	}

	public void setTextPort(JTextField textPort) {
		this.textPort = textPort;
	}
}
