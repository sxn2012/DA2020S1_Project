package transmission;
/*
 * Author: Xinnan SHEN
 * Email: xinnan.shen@student.unimelb.edu.au
 * Date: 07/05/2020
 * 
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import concurrency.Print;
import gui.GUI;
import gui.Server;

public class TCPThread extends Thread{
	private int port;
	private static Long transaction_id;
	private GUI window;
	
	public TCPThread(int port,GUI window) {
		// TODO Auto-generated constructor stub
		this.port=port;
		this.window=window;
		
	}
	public void run() {
		ServerSocket server;
		try {
			server=new ServerSocket(port);
			while(Server.getflag())
			{
				Socket client=server.accept();
				long id=new Date().getTime();
				//System.out.println(id);
				CommandThread dealthread=new CommandThread(server,client,window,id);
				//dealthread.start();
				Server.getthreadpool().execute(dealthread);
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
			//JOptionPane.showMessageDialog(window.frame, "There might be some problem in the connection: "+e.getMessage(),"Error", JOptionPane.ERROR_MESSAGE);
			Print.println("There might be some problems in the connection: "+e.getMessage());
			System.exit(1);
		}
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public GUI getWindow() {
		return window;
	}
	public void setWindow(GUI window) {
		this.window = window;
	}
	public static Long getTransaction_id() {
		long t = new Date().getTime();
//		return TCPThread.transaction_id++;
		return Long.valueOf(t);
	}
	public static void setTransaction_id(Long transaction_id) {
		TCPThread.transaction_id = transaction_id;
	}
}
