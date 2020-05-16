package gui;
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

import concurrency.Client;
import transmission.TCPThread;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.Socket;
import java.util.regex.Pattern;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
//Main frame of client
//have buttons on it
//press button means sending instructions to server
public class GUI {

	private JFrame frame;
	private TCPThread thread;
	private Socket client;
	private JFrame welcomeframe;
	private JButton btnLogin;
	private JButton btnLogout;
	private JButton btnInsert;
	private JButton btnSelect;
	private JButton btnUpdate;
	private JButton btnDelete;
	private JButton btnView;
	private JButton btnCommit;
	private JButton btnRollback;
	/**
	 * Create the application.
	 */
	public GUI(Socket client,JFrame welcomeframe) {
		this.client=client;
		this.welcomeframe=welcomeframe;
		initialize();
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("MVCC Client");
		GUI temp=this;
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int response=JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION);
				if(response==JOptionPane.NO_OPTION)
					return;
				else if(response==JOptionPane.YES_OPTION)
				{
					if(btnLogin.isEnabled())
					{
						super.windowClosing(e);
						TCPThread thread=new TCPThread(client,temp);
						thread.SetWelcomeframe(welcomeframe);
						//thread.Setframe(frame);
						thread.Setcommand("exit");
						//thread.start();
						Client.getThreadpool().execute(thread);
					}
					else {
						JOptionPane.showMessageDialog(frame, "Please Log out of the system first!","Warning", JOptionPane.WARNING_MESSAGE); 
					}
				}
				
			}
		});
		frame.setBounds(100, 100, 500, 300);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);
		
		
		btnLogin = new JButton("Login");
		btnLogin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(btnLogin.isEnabled())
				{
					TCPThread thread=new TCPThread(client,temp);
					thread.SetWelcomeframe(welcomeframe);
					//thread.Setframe(frame);
					thread.Setcommand("login");
					//thread.start();
					Client.getThreadpool().execute(thread);
				}
			}
		});
		btnLogin.setBounds(30, 42, 105, 28);
		frame.getContentPane().add(btnLogin);
		
		btnLogout = new JButton("Logout");
		btnLogout.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(btnLogout.isEnabled())
				{
					TCPThread thread=new TCPThread(client,temp);
					thread.SetWelcomeframe(welcomeframe);
					//thread.Setframe(frame);
					thread.Setcommand("logout");
					//thread.start();
					Client.getThreadpool().execute(thread);
				}
				
			}
		});
		btnLogout.setBounds(30, 116, 105, 28);
		frame.getContentPane().add(btnLogout);
		
		btnInsert = new JButton("Insert");
		btnInsert.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(btnInsert.isEnabled())
				{
					String insert_id=JOptionPane.showInputDialog("Please input the id you want to insert");
					String insert_name=JOptionPane.showInputDialog("Please input the name you want to insert");
					Pattern pattern =Pattern.compile("[0-9]*");
					if(insert_id==null||insert_name==null||insert_id.equals("")||insert_name.equals("")||!pattern.matcher(insert_id).matches())
					{
						JOptionPane.showMessageDialog(frame, "Input is not valid","Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					String insert = insert_id+" "+insert_name;
					//JOptionPane.showInputDialog("Please input a value you want to insert (id name)");
					TCPThread thread=new TCPThread(client,temp);
					thread.SetWelcomeframe(welcomeframe);
					//thread.Setframe(frame);
					thread.Setcommand("add "+insert);
					//thread.start();
					Client.getThreadpool().execute(thread);
				}
				
			}
		});
		btnInsert.setBounds(193, 42, 105, 28);
		frame.getContentPane().add(btnInsert);
		
		btnSelect = new JButton("Select");
		btnSelect.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(btnSelect.isEnabled())
				{
					String select = JOptionPane.showInputDialog("Please input the id you want to select");
					Pattern pattern =Pattern.compile("[0-9]*");
					if(select==null||select.equals("")||!pattern.matcher(select).matches())
					{
						JOptionPane.showMessageDialog(frame, "ID is not valid","Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					TCPThread thread=new TCPThread(client,temp);
					thread.SetWelcomeframe(welcomeframe);
					//thread.Setframe(frame);
					thread.Setcommand("select "+select);
					//thread.start();
					Client.getThreadpool().execute(thread);
				}
				
			}
		});
		btnSelect.setBounds(193, 116, 105, 28);
		frame.getContentPane().add(btnSelect);
		
		btnUpdate = new JButton("Update");
		btnUpdate.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(btnUpdate.isEnabled())
				{
					String update_id=JOptionPane.showInputDialog("Please input the id you want to update");
					String update_name=JOptionPane.showInputDialog("Please input the name you want to update");
					Pattern pattern =Pattern.compile("[0-9]*");
					if(update_id==null||update_name==null||update_id.equals("")||update_name.equals("")||!pattern.matcher(update_id).matches())
					{
						JOptionPane.showMessageDialog(frame, "Input is not valid","Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					String update = update_id+" "+update_name;
					//JOptionPane.showInputDialog("Please input a value you want to update (id name)");
					TCPThread thread=new TCPThread(client,temp);
					thread.SetWelcomeframe(welcomeframe);
					//thread.Setframe(frame);
					thread.Setcommand("update "+update);
					//thread.start();
					Client.getThreadpool().execute(thread);
				}
				
			}
		});
		btnUpdate.setBounds(333, 42, 105, 28);
		frame.getContentPane().add(btnUpdate);
		
		btnDelete = new JButton("Delete");
		btnDelete.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(btnDelete.isEnabled())
				{
					String delete = JOptionPane.showInputDialog("Please input the id you want to delete");
					Pattern pattern =Pattern.compile("[0-9]*");
					if(delete==null||delete.equals("")||!pattern.matcher(delete).matches())
					{
						JOptionPane.showMessageDialog(frame, "ID is not valid","Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					TCPThread thread=new TCPThread(client,temp);
					thread.SetWelcomeframe(welcomeframe);
					//thread.Setframe(frame);
					thread.Setcommand("delete "+delete);
					//thread.start();
					Client.getThreadpool().execute(thread);
				}
				
			}
		});
		btnDelete.setBounds(333, 116, 105, 28);
		frame.getContentPane().add(btnDelete);
		
		btnView = new JButton("View");
		btnView.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(btnView.isEnabled())
				{
					TCPThread thread=new TCPThread(client,temp);
					thread.SetWelcomeframe(welcomeframe);
					//thread.Setframe(frame);
					thread.Setcommand("view");
					//thread.start();
					Client.getThreadpool().execute(thread);
				}
				
			}
		});
		btnView.setBounds(30, 189, 105, 28);
		frame.getContentPane().add(btnView);
		
		btnCommit = new JButton("Commit");
		btnCommit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(btnCommit.isEnabled())
				{
					TCPThread thread=new TCPThread(client,temp);
					thread.SetWelcomeframe(welcomeframe);
					//thread.Setframe(frame);
					thread.Setcommand("commit");
					//thread.start();
					Client.getThreadpool().execute(thread);
				}
				
			}
		});
		btnCommit.setBounds(193, 189, 105, 28);
		frame.getContentPane().add(btnCommit);
		
		btnRollback = new JButton("Rollback");
		btnRollback.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(btnRollback.isEnabled())
				{
					TCPThread thread=new TCPThread(client,temp);
					thread.SetWelcomeframe(welcomeframe);
					//thread.Setframe(frame);
					thread.Setcommand("rollback");
					//thread.start();
					Client.getThreadpool().execute(thread);
				}
				
			}
		});
		btnRollback.setBounds(333, 189, 105, 28);
		frame.getContentPane().add(btnRollback);
		this.SetDisableBtn();
	}
	
	public void SetDisableBtn() 
	{
		btnLogin.setEnabled(true);
		btnLogout.setEnabled(false);
		btnInsert.setEnabled(false);
		btnSelect.setEnabled(false);
		btnUpdate.setEnabled(false);
		btnDelete.setEnabled(false);
		btnView.setEnabled(false);
		btnCommit.setEnabled(false);
		btnRollback.setEnabled(false);
	}
	
	public void SetEnableBtn() 
	{
		btnLogin.setEnabled(false);
		btnLogout.setEnabled(true);
		btnInsert.setEnabled(true);
		btnSelect.setEnabled(true);
		btnUpdate.setEnabled(true);
		btnDelete.setEnabled(true);
		btnView.setEnabled(true);
		btnCommit.setEnabled(true);
		btnRollback.setEnabled(true);
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	public TCPThread getThread() {
		return thread;
	}

	public void setThread(TCPThread thread) {
		this.thread = thread;
	}

	public Socket getClient() {
		return client;
	}

	public void setClient(Socket client) {
		this.client = client;
	}

	public JFrame getWelcomeframe() {
		return welcomeframe;
	}

	public void setWelcomeframe(JFrame welcomeframe) {
		this.welcomeframe = welcomeframe;
	}

	public JButton getBtnLogin() {
		return btnLogin;
	}

	public void setBtnLogin(JButton btnLogin) {
		this.btnLogin = btnLogin;
	}

	public JButton getBtnLogout() {
		return btnLogout;
	}

	public void setBtnLogout(JButton btnLogout) {
		this.btnLogout = btnLogout;
	}

	public JButton getBtnInsert() {
		return btnInsert;
	}

	public void setBtnInsert(JButton btnInsert) {
		this.btnInsert = btnInsert;
	}

	public JButton getBtnSelect() {
		return btnSelect;
	}

	public void setBtnSelect(JButton btnSelect) {
		this.btnSelect = btnSelect;
	}

	public JButton getBtnUpdate() {
		return btnUpdate;
	}

	public void setBtnUpdate(JButton btnUpdate) {
		this.btnUpdate = btnUpdate;
	}

	public JButton getBtnDelete() {
		return btnDelete;
	}

	public void setBtnDelete(JButton btnDelete) {
		this.btnDelete = btnDelete;
	}

	public JButton getBtnView() {
		return btnView;
	}

	public void setBtnView(JButton btnView) {
		this.btnView = btnView;
	}

	public JButton getBtnCommit() {
		return btnCommit;
	}

	public void setBtnCommit(JButton btnCommit) {
		this.btnCommit = btnCommit;
	}

	public JButton getBtnRollback() {
		return btnRollback;
	}

	public void setBtnRollback(JButton btnRollback) {
		this.btnRollback = btnRollback;
	}
}
