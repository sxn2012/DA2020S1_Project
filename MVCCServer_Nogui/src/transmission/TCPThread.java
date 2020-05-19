package transmission;
/*
 * Author: Xinnan SHEN, Xiguang Li
 * 
 * Date: 07/05/2020
 * 
 * Usage: Deal with communication between client and server
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
	private static Long transaction_id;
	
	public TCPThread(int port) {
		
		this.port=port;
		
		
	}
	public void run() {
		ServerSocket server;
		try {
			server=new ServerSocket(port);//bind port
			
			while(Server.isFlag())
			{
				Socket client=server.accept();//accept connection from client
				long id=new Date().getTime();
				
				CommandThread dealthread=new CommandThread(server,client,id);
				
				Server.getThreadpool().execute(dealthread);
			}
		}
		catch (Exception e) {
			
			Print.println("There might be some problem in the connection: "+e.getMessage());
			System.exit(1);
		}
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public static Long getTransaction_id() {
		long t = new Date().getTime();

		return Long.valueOf(t);

	}
	public static void setTransaction_id(Long transaction_id) {
		TCPThread.transaction_id = transaction_id;
	}
}
