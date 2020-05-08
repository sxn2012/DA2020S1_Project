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

public class TCPThread extends Thread{
	private int port;
	public static int transaction_id=0;
	//private GUI window;
	
	public TCPThread(int port) {
		// TODO Auto-generated constructor stub
		this.port=port;
		
		
	}
	public void run() {
		ServerSocket server;
		try {
			server=new ServerSocket(port);
			
			while(true)
			{
				Socket client=server.accept();
				long id=new Date().getTime();
				//System.out.println(id);
				CommandThread dealthread=new CommandThread(client,id);
				dealthread.start();
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			//JOptionPane.showMessageDialog(window.frame, "Connection Failed! ("+e.getMessage()+").","Error", JOptionPane.ERROR_MESSAGE);

		}
	}
}
