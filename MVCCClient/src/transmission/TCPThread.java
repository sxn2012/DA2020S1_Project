package transmission;
/*
 * Author: Xinnan SHEN
 * 
 * Date: 23/04/2020
 * 
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import concurrency.Print;
import gui.GUI;





public class TCPThread extends Thread{
	private Socket client;
	private String command;
	private DataInputStream is;
	private DataOutputStream os;
	//JFrame frame;
	private GUI window;
	private JFrame welcomeframe;
	public TCPThread(Socket client,GUI window) {
		// TODO Auto-generated constructor stub
		this.client=client;
		this.command="";
		this.window=window;
		//close is,os,socket when client shuts down
		Runtime.getRuntime().addShutdownHook(new Thread() {
		    public void run() { 
		    	try{
		    		if(is!=null) is.close();
		    		if(os!=null) os.close();
		    		if(TCPThread.this.client!=null) TCPThread.this.client.close();
		    		//System.out.println("IOStream and Connection closed.");
		    		}
		    	catch (Exception e) {
					// TODO: handle exception
		    		Print.println("IOStream/Connection closing failed: "+e.getMessage());
				}
		    }
		 });
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
			is=new DataInputStream(client.getInputStream());
			os=new DataOutputStream(client.getOutputStream());
			//System.out.print("$");
			//String command=s.nextLine();
			if(command.equals("exit")||command.equals("logout"))
				flag=true;
			os.writeUTF(command);//send instructions to server
			os.flush();
			String str=is.readUTF();//receive feedback from server
			//deal with feedbacks received
			if(str.equals("Failure: Operation Timeout"))
			{
				is.close();
				os.close();
				client.close();
				window.getFrame().setVisible(false);
				welcomeframe.setVisible(true);
			}
			//System.out.println("\t>>>>"+str);
			if(str.contains("Failure:"))
				JOptionPane.showMessageDialog(window.getFrame(), str,"Error", JOptionPane.ERROR_MESSAGE); 
			else
			{
				JOptionPane.showMessageDialog(window.getFrame(), str,"information", JOptionPane.INFORMATION_MESSAGE); 
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
				window.getFrame().setVisible(false);
				welcomeframe.setVisible(true);
				//System.exit(0);
			}
			//System.out.print("$");
			
			
		} catch (Exception e) {
			// TODO: handle exception
			JOptionPane.showMessageDialog(window.getFrame(), "There might be some errors in the connection","Error", JOptionPane.ERROR_MESSAGE);
			//e.printStackTrace();
			if(flag)
			{
				//System.exit(1);
				window.getFrame().setVisible(false);
				welcomeframe.setVisible(true);
			}
		}
		
		
	}
	public Socket getClient() {
		return client;
	}
	public void setClient(Socket client) {
		this.client = client;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public DataInputStream getIs() {
		return is;
	}
	public void setIs(DataInputStream is) {
		this.is = is;
	}
	public DataOutputStream getOs() {
		return os;
	}
	public void setOs(DataOutputStream os) {
		this.os = os;
	}
	public GUI getWindow() {
		return window;
	}
	public void setWindow(GUI window) {
		this.window = window;
	}
	public JFrame getWelcomeframe() {
		return welcomeframe;
	}
	public void setWelcomeframe(JFrame welcomeframe) {
		this.welcomeframe = welcomeframe;
	}
}
