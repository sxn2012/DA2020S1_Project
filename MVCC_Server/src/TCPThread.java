import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPThread extends Thread{
	private int port;
	public static int transaction_id=0;
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
				CommandThread dealthread=new CommandThread(client);
				dealthread.start();
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
