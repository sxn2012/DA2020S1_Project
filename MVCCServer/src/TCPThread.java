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
			while(Main.flag)
			{
				Socket client=server.accept();
				long id=new Date().getTime();
				//System.out.println(id);
				CommandThread dealthread=new CommandThread(server,client,window,id);
				//dealthread.start();
				Main.threadpool.execute(dealthread);
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
}
