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

public class TCPThread extends Thread{
	private int port;
	private static int transaction_id=0;
	//private GUI window;
	
	public TCPThread(int port) {
		// TODO Auto-generated constructor stub
		this.port=port;
		
		
	}
	public void run() {
		ServerSocket server;
		try {
			server=new ServerSocket(port);
			
			while(Server.isFlag())
			{
				Socket client=server.accept();
				long id=new Date().getTime();
				//System.out.println(id);
				CommandThread dealthread=new CommandThread(server,client,id);
				//dealthread.start();
				Server.getThreadpool().execute(dealthread);
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			Print.println("There might be some problem in the connection: "+e.getMessage());
			//JOptionPane.showMessageDialog(window.frame, "Connection Failed! ("+e.getMessage()+").","Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public static int getTransaction_id() {
		return transaction_id;
	}
	public static void setTransaction_id(int transaction_id) {
		TCPThread.transaction_id = transaction_id;
	}
}
