package gui;
/*
 * Author: Xinnan SHEN
 * Email: xinnan.shen@student.unimelb.edu.au
 * Date: 07/05/2020
 * 
 */
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;

import transmission.TCPThread;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.Socket;
import java.util.regex.Pattern;
//This file is to show a welcome frame
//needs to input a port to bind
public class Welcome {

	private JFrame frame;
	private JTextField textPort;
	private JButton btnConfirm;
	
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
		frame = new JFrame("MVCC Server");
		frame.setBounds(100, 100, 400, 250);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);
		
		textPort = new JTextField();
		textPort.setText("7777");
		textPort.setBounds(91, 54, 237, 22);
		frame.getContentPane().add(textPort);
		textPort.setColumns(10);
		
		JLabel lblServerPort = new JLabel("PORT:");
		lblServerPort.setFont(new Font("Dialog", Font.BOLD, 15));
		lblServerPort.setBounds(12, 54, 87, 18);
		frame.getContentPane().add(lblServerPort);
		//confirm button
		btnConfirm = new JButton("Waiting...");
		btnConfirm.setEnabled(false);
		btnConfirm.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//bind the port
				if(btnConfirm.isEnabled())
				{
					Pattern pattern=Pattern.compile("[0-9]*");
					if(textPort==null||textPort.getText().trim().equals("")||!pattern.matcher(textPort.getText().trim()).matches()||Integer.parseInt(textPort.getText().trim())>65535)
					{
						JOptionPane.showMessageDialog(frame, "Input Invalid!","Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					Server.getthreadpool().execute(new Runnable() {
					public void run() {
						try {
							GUI window = new GUI(frame);
							window.getFrame().setVisible(true);
							window.setContent("Server Start");
					    	TCPThread thread=new TCPThread(Integer.parseInt(textPort.getText().trim()),window);
					    	//thread.start();
					    	Server.getthreadpool().execute(thread);
					    	frame.setVisible(false);
						} catch (Exception e) {
							JOptionPane.showMessageDialog(frame, "Server Starting Failure ("+e.getMessage()+").","Error", JOptionPane.ERROR_MESSAGE);
							//e.printStackTrace();
						}
					}
				});
				}
				
			}
		});
		btnConfirm.setBounds(146, 123, 105, 28);
		frame.getContentPane().add(btnConfirm);
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	public JTextField getTextPort() {
		return textPort;
	}

	public void setTextPort(JTextField textPort) {
		this.textPort = textPort;
	}

	public JButton getBtnConfirm() {
		return btnConfirm;
	}

	public void setBtnConfirm(JButton btnConfirm) {
		this.btnConfirm = btnConfirm;
	}
}
