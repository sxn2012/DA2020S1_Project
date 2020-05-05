import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

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
				//long id=new Date().getTime();
				CommandThread dealthread=new CommandThread(client,window);
				dealthread.start();
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
