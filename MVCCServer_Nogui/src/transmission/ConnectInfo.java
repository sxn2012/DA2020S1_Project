package transmission;
import java.awt.EventQueue;
import java.util.Scanner;

import concurrency.Print;
/*
 * Author: Xinnan SHEN
 * Email: xinnan.shen@student.unimelb.edu.au
 * Date: 07/05/2020
 * 
 */
public class ConnectInfo extends Thread {
	private int port;
	public ConnectInfo() {
		// TODO Auto-generated constructor stub
		
	}
	public int getPort() {
		return port;
	}
	public void run() {
		Print.println("\n\r\n\r\n\r-----------Initializing completed.-----------");
		Scanner sc=new Scanner(System.in);
		Print.print("Please input the port number:");
		//ipaddr=sc.nextLine();
		port=sc.nextInt();
		while(port<0||port>65535)
		{
			Print.println("Invalid Input!");
			Print.print("Please input the port number:");
			port=sc.nextInt();
		}
		CommandThread.setContent("Server Start");
		TCPThread thread=new TCPThread(port);
		//thread.start();
		Server.getThreadpool().execute(thread);
			
		
	}
	public void setPort(int port) {
		this.port = port;
	}
}
