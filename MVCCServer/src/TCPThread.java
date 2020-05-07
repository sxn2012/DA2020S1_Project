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
			
			while(true)
			{
				Socket client=server.accept();
				long id=new Date().getTime();
				//System.out.println(id);
				CommandThread dealthread=new CommandThread(client,window,id);
				dealthread.start();
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			JOptionPane.showMessageDialog(window.frame, "Connection Failed! ("+e.getMessage()+").","Error", JOptionPane.ERROR_MESSAGE);

		}
	}
}
