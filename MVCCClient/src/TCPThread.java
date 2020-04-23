/*
 * Author: Xinnan SHEN
 * Email: xinnan.shen@student.unimelb.edu.au
 * Date: 23/04/2020
 * 
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;



public class TCPThread extends Thread{
	private String ip;
	private int port;
	
	public TCPThread(String ip,int port) {
		// TODO Auto-generated constructor stub
		this.ip=ip;
		this.port=port;
		
	}
	
	public void run()
	{
		try {
			Scanner s=new Scanner(System.in);
			Socket client=new Socket(ip,port);
			DataInputStream is=new DataInputStream(client.getInputStream());
			DataOutputStream os=new DataOutputStream(client.getOutputStream());
			System.out.print("$");
			while(true){
				String command=s.nextLine();
				os.writeUTF(command);
				String str=is.readUTF();
				System.out.println("\t>>>>"+str);
				if(command.equals("exit"))
				{
					is.close();
					os.close();
					client.close();
					break;
				}
				System.out.print("$");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
	}
}
