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
	//JFrame frame;
	GUI window;
	JFrame welcomeframe;
	public TCPThread(Socket client,GUI window) {
		// TODO Auto-generated constructor stub
		this.client=client;
		this.command="";
		this.window=window;
	}
	public void Setcommand(String command) {
		this.command=command;
	}
	
	/*public void Setframe(JFrame frame) {
		this.frame=frame;
	}*/
	
	public void SetWelcomeframe(JFrame welcomeframe) {
		this.welcomeframe=welcomeframe;
	}
	public void run()
	{
		boolean flag=false;
		try {
			//Scanner s=new Scanner(System.in);
			DataInputStream is=new DataInputStream(client.getInputStream());
			DataOutputStream os=new DataOutputStream(client.getOutputStream());
			//System.out.print("$");
			//String command=s.nextLine();
			if(command.equals("exit")||command.equals("logout"))
				flag=true;
			os.writeUTF(command);
			String str=is.readUTF();
			if(str.equals("Failure: Operation Timeout"))
			{
				is.close();
				os.close();
				client.close();
				window.frame.setVisible(false);
				welcomeframe.setVisible(true);
			}
			//System.out.println("\t>>>>"+str);
			if(str.contains("Failure:"))
				JOptionPane.showMessageDialog(window.frame, str,"Error", JOptionPane.ERROR_MESSAGE); 
			else
			{
				JOptionPane.showMessageDialog(window.frame, str,"information", JOptionPane.INFORMATION_MESSAGE); 
				if(command.equals("logout"))
				{
					window.SetDisableBtn();
				}
				else if(command.equals("login"))
				{
					window.SetEnableBtn();
				}
			}
			if(str.equals("Bye"))
			{
				is.close();
				os.close();
				client.close();
				window.frame.setVisible(false);
				welcomeframe.setVisible(true);
				//System.exit(0);
			}
			//System.out.print("$");
			
			
		} catch (Exception e) {
			// TODO: handle exception
			JOptionPane.showMessageDialog(window.frame, "There might be some errors in the connection ("+e.getMessage()+").","Error", JOptionPane.ERROR_MESSAGE);
			if(flag)
			{
				//System.exit(1);
				window.frame.setVisible(false);
				welcomeframe.setVisible(true);
			}
		}
		
		
	}
}
