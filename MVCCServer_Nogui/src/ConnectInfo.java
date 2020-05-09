import java.awt.EventQueue;
import java.util.Scanner;

public class ConnectInfo extends Thread {
	int port;
	public ConnectInfo() {
		// TODO Auto-generated constructor stub
		
	}
	public int getPort() {
		return port;
	}
	public void run() {
		System.out.println("\n\r\n\r\n\r-----------Initializing completed.-----------");
		Scanner sc=new Scanner(System.in);
		System.out.print("Please input the port number:");
		//ipaddr=sc.nextLine();
		port=sc.nextInt();
		while(port<0||port>65535)
		{
			System.out.println("Invalid Input!");
			System.out.print("Please input the port number:");
			port=sc.nextInt();
		}
		CommandThread.setContent("Server Start");
		TCPThread thread=new TCPThread(port);
		//thread.start();
		Main.threadpool.execute(thread);
			
		
	}
}
