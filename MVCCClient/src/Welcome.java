/*
 * Author: Xinnan SHEN
 * Email: xinnan.shen@student.unimelb.edu.au
 * Date: 07/05/2020
 * 
 */
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.Socket;

public class Welcome {

	JFrame frame;
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
		
		JButton btnConfirm = new JButton("Confirm");
		btnConfirm.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Socket client=new Socket(textIP.getText().trim(), Integer.parseInt(textPort.getText().trim()));
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							try {
								GUI window = new GUI(client,frame);
								window.frame.setVisible(true);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
					frame.setVisible(false);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(frame, "Connection Failed! ("+e1.getMessage()+").","Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnConfirm.setBounds(119, 159, 105, 28);
		frame.getContentPane().add(btnConfirm);
	}
}
