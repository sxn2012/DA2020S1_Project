package transmission;
import java.awt.EventQueue;
import java.util.Scanner;

import concurrency.Print;
/*
 * Author: Huidu Lu, Xinnan SHEN
 * 
 * Date: 07/05/2020
 * 
 */
public class ConnectInfo extends Thread {
	private int port;
	public ConnectInfo() {
		
	}
	public int getPort() {
		return port;
	}
	public void run() {
		Print.println("\n\r\n\r\n\r-----------Initializing completed.-----------");
		Scanner sc=new Scanner(System.in);
		Print.print("Please input the port number:");
		//input port number
		String portInput = sc.nextLine();
		
		// check port input validity
		while(!portInput.trim().matches("\\d+") || Integer.parseInt(portInput.trim()) < 0
				||Integer.parseInt(portInput.trim()) > 65535)
		{
			Print.println("Invalid Input!");
			Print.print("Please input a valid port number:");
			portInput = sc.nextLine();
		}
		//start server
		int port = Integer.parseInt(portInput.trim());
		CommandThread.setContent("Server Start");
		TCPThread thread=new TCPThread(port);
		
		Server.getThreadpool().execute(thread);
			
		
	}
	public void setPort(int port) {
		this.port = port;
	}
}
