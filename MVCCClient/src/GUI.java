/*
 * Author: Xinnan SHEN
 * Email: xinnan.shen@student.unimelb.edu.au
 * Date: 05/05/2020
 * 
 */
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.Socket;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GUI {

	JFrame frame;
	TCPThread thread;
	Socket client;

	/**
	 * Create the application.
	 */
	public GUI(Socket client) {
		this.client=client;
		initialize();
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int response=JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION);
				if(response==JOptionPane.NO_OPTION)
					return;
				super.windowClosing(e);
				TCPThread thread=new TCPThread(client);
				thread.Setframe(frame);
				thread.Setcommand("exit");
				thread.start();
			}
		});
		frame.setBounds(100, 100, 500, 300);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				TCPThread thread=new TCPThread(client);
				thread.Setframe(frame);
				thread.Setcommand("login");
				thread.start();
			}
		});
		btnLogin.setBounds(30, 42, 105, 28);
		frame.getContentPane().add(btnLogin);
		
		JButton btnLogout = new JButton("Logout");
		btnLogout.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				TCPThread thread=new TCPThread(client);
				thread.Setframe(frame);
				thread.Setcommand("logout");
				thread.start();
			}
		});
		btnLogout.setBounds(30, 116, 105, 28);
		frame.getContentPane().add(btnLogout);
		
		JButton btnInsert = new JButton("Insert");
		btnInsert.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String insert = JOptionPane.showInputDialog("Please input a value you want to insert (id name)");
				TCPThread thread=new TCPThread(client);
				thread.Setframe(frame);
				thread.Setcommand("add "+insert);
				thread.start();
			}
		});
		btnInsert.setBounds(193, 42, 105, 28);
		frame.getContentPane().add(btnInsert);
		
		JButton btnSelect = new JButton("Select");
		btnSelect.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String select = JOptionPane.showInputDialog("Please input the id you want to select");
				TCPThread thread=new TCPThread(client);
				thread.Setframe(frame);
				thread.Setcommand("select "+select);
				thread.start();
			}
		});
		btnSelect.setBounds(193, 116, 105, 28);
		frame.getContentPane().add(btnSelect);
		
		JButton btnUpdate = new JButton("Update");
		btnUpdate.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String update = JOptionPane.showInputDialog("Please input a value you want to update (id name)");
				TCPThread thread=new TCPThread(client);
				thread.Setframe(frame);
				thread.Setcommand("update "+update);
				thread.start();
			}
		});
		btnUpdate.setBounds(333, 42, 105, 28);
		frame.getContentPane().add(btnUpdate);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String delete = JOptionPane.showInputDialog("Please input the id you want to delete");
				TCPThread thread=new TCPThread(client);
				thread.Setframe(frame);
				thread.Setcommand("delete "+delete);
				thread.start();
			}
		});
		btnDelete.setBounds(333, 116, 105, 28);
		frame.getContentPane().add(btnDelete);
		
		JButton btnView = new JButton("View");
		btnView.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				TCPThread thread=new TCPThread(client);
				thread.Setframe(frame);
				thread.Setcommand("view");
				thread.start();
			}
		});
		btnView.setBounds(30, 189, 105, 28);
		frame.getContentPane().add(btnView);
		
		JButton btnCommit = new JButton("Commit");
		btnCommit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				TCPThread thread=new TCPThread(client);
				thread.Setframe(frame);
				thread.Setcommand("commit");
				thread.start();
			}
		});
		btnCommit.setBounds(193, 189, 105, 28);
		frame.getContentPane().add(btnCommit);
		
		JButton btnRollback = new JButton("Rollback");
		btnRollback.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				TCPThread thread=new TCPThread(client);
				thread.Setframe(frame);
				thread.Setcommand("rollback");
				thread.start();
			}
		});
		btnRollback.setBounds(333, 189, 105, 28);
		frame.getContentPane().add(btnRollback);
	}
}
