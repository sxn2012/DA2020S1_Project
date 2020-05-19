package gui;
/*
 * Author: Chaoxian Zhou, Xinnan SHEN
 * Date: 16/05/2020
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
	private JButton btnCrash;
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
		frame.setBounds(100, 100, 500, 350);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);
		
		//login button
		btnLogin = new JButton("Login");
		//press button to send login message to server
		btnLogin.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {
				super.mouseReleased(e);
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
		btnLogin.setBounds(30, 254, 105, 28);
		frame.getContentPane().add(btnLogin);
		//logout button
		btnLogout = new JButton("Logout");
		//send logout message to server
		btnLogout.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {
				super.mouseReleased(e);
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
		btnLogout.setBounds(193, 254, 105, 28);
		frame.getContentPane().add(btnLogout);
		//insert button
		btnInsert = new JButton("Insert");
		btnInsert.addMouseListener(new MouseAdapter() {


			@Override
			public void mouseReleased(MouseEvent e) {
				super.mouseReleased(e);
				if(btnInsert.isEnabled())
				{
					//input id and name
					String insert_id=JOptionPane.showInputDialog("Please input the id you want to insert,\r\nit should be a number(eg:1000)");
					String insert_name=JOptionPane.showInputDialog("Please input the name you want to insert");
					Pattern pattern =Pattern.compile("[0-9]*");
					//check input validity
					if(insert_id==null||insert_name==null||insert_id.equals("")||insert_name.equals("")||!pattern.matcher(insert_id).matches())
					{
						JOptionPane.showMessageDialog(frame, "Input is not valid","Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					String insert = insert_id+" "+insert_name;
					//JOptionPane.showInputDialog("Please input a value you want to insert (id name)");
					//send input message to server
					TCPThread thread=new TCPThread(client,temp);
					thread.SetWelcomeframe(welcomeframe);
					//thread.Setframe(frame);
					thread.Setcommand("add "+insert);
					//thread.start();
					Client.getThreadpool().execute(thread);
				}
			}
		});
		btnInsert.setBounds(193, 116, 105, 28);
		frame.getContentPane().add(btnInsert);
		//select button
		btnSelect = new JButton("Select");
		btnSelect.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {
				super.mouseReleased(e);
				if(btnSelect.isEnabled())
				{
					//input id
					String select = JOptionPane.showInputDialog("Please input the id you want to select,\r\nit should be a number(eg:1000)");
					Pattern pattern =Pattern.compile("[0-9]*");
					//check input validity
					if(select==null||select.equals("")||!pattern.matcher(select).matches())
					{
						JOptionPane.showMessageDialog(frame, "ID is not valid","Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					//send select message to server
					TCPThread thread=new TCPThread(client,temp);
					thread.SetWelcomeframe(welcomeframe);
					//thread.Setframe(frame);
					thread.Setcommand("select "+select);
					//thread.start();
					Client.getThreadpool().execute(thread);
				}
			}
		});
		btnSelect.setBounds(30, 116, 105, 28);
		frame.getContentPane().add(btnSelect);
		//update button
		btnUpdate = new JButton("Update");
		btnUpdate.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {
				super.mouseReleased(e);
				if(btnUpdate.isEnabled())
				{
					//input id and name
					String update_id=JOptionPane.showInputDialog("Please input the id you want to update,\r\nit should be a number(eg:1000)");
					String update_name=JOptionPane.showInputDialog("Please input the name you want to update");
					Pattern pattern =Pattern.compile("[0-9]*");
					//check input validity
					if(update_id==null||update_name==null||update_id.equals("")||update_name.equals("")||!pattern.matcher(update_id).matches())
					{
						JOptionPane.showMessageDialog(frame, "Input is not valid","Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					//send update message to server
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
		//delete button
		btnDelete = new JButton("Delete");
		btnDelete.addMouseListener(new MouseAdapter() {


			@Override
			public void mouseReleased(MouseEvent e) {
				super.mouseReleased(e);
				if(btnDelete.isEnabled())
				{
					//input id
					String delete = JOptionPane.showInputDialog("Please input the id you want to delete,\r\nit should be a number(eg:1000)");
					Pattern pattern =Pattern.compile("[0-9]*");
					//check input validity
					if(delete==null||delete.equals("")||!pattern.matcher(delete).matches())
					{
						JOptionPane.showMessageDialog(frame, "ID is not valid","Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					//send delete message to server
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
		//view button
		//send view message to server
		btnView = new JButton("View");
		btnView.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {
				super.mouseReleased(e);

				if(btnView.isEnabled())
				{
					TCPThread thread=new TCPThread(client,temp);
					thread.SetWelcomeframe(welcomeframe);
					//thread.Setframe(frame);
					thread.Setcommand("view");
					//thread.start();
					System.out.println("Start");
					Client.getThreadpool().execute(thread);
				}
			}
		});
		btnView.setBounds(30, 42, 105, 28);
		frame.getContentPane().add(btnView);
		//commit button
		//send commit message to server
		btnCommit = new JButton("Commit");
		btnCommit.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {
				super.mouseReleased(e);
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
		btnCommit.setBounds(30, 189, 105, 28);
		frame.getContentPane().add(btnCommit);
		//rollback button
		//send rollback message to server
		btnRollback = new JButton("Rollback");
		btnRollback.addMouseListener(new MouseAdapter() {


			@Override
			public void mouseReleased(MouseEvent e) {
				super.mouseReleased(e);
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
		
		btnCrash = new JButton("Crash");
		btnCrash.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				super.mouseReleased(e);
				System.exit(1);//abnormal exit

			}
		});
		btnCrash.setBounds(333, 254, 105, 28);
		frame.getContentPane().add(btnCrash);
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
		//btnCrash.setEnabled(false);
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
		//btnCrash.setEnabled(true);
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
	public JButton getBtnCrash() {
		return btnCrash;
	}

	public void setBtnCrash(JButton btnCrash) {
		this.btnCrash = btnCrash;
	}
}
