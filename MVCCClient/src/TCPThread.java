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

import javax.swing.JFrame;
import javax.swing.JOptionPane;



public class TCPThread extends Thread{
	Socket client;
	String command;
	JFrame frame;
	public TCPThread(Socket client) {
		// TODO Auto-generated constructor stub
		this.client=client;
		this.command="";
		this.frame=null;
	}
	public void Setcommand(String command) {
		this.command=command;
	}
	public void Setframe(JFrame frame) {
		this.frame=frame;
	}
	public void run()
	{
		try {
			//Scanner s=new Scanner(System.in);
			DataInputStream is=new DataInputStream(client.getInputStream());
			DataOutputStream os=new DataOutputStream(client.getOutputStream());
			//System.out.print("$");
			//String command=s.nextLine();
			os.writeUTF(command);
			String str=is.readUTF();
			//System.out.println("\t>>>>"+str);
			if(str.contains("Failure"))
				JOptionPane.showMessageDialog(frame, str,"Error", JOptionPane.ERROR_MESSAGE); 
			else
			JOptionPane.showMessageDialog(frame, str,"information", JOptionPane.INFORMATION_MESSAGE); 
			if(command.equals("exit"))
			{
				is.close();
				os.close();
				client.close();
				
			}
			//System.out.print("$");
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
	}
}
