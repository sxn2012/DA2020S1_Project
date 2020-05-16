package transmission;
/*
 * Author: Xinnan SHEN
 * Email: xinnan.shen@student.unimelb.edu.au
 * Date: 23/04/2020
 * 
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import concurrency.Person;
import concurrency.Print;
import concurrency.Records;
import concurrency.Transaction;
import gui.GUI;
import gui.Server;

public class CommandThread extends Thread{

	private ServerSocket server;
	private Socket client;
	private boolean login;
	private Transaction t;
	private GUI window;
	private long idl;
	private DataInputStream is;
	private DataOutputStream os;
	  
	public CommandThread(ServerSocket server,Socket client,GUI window,long id) {
		// TODO Auto-generated constructor stub
		this.server=server;
		this.client=client;
		login=false;
		this.window=window;
		this.idl=id;
		Runtime.getRuntime().addShutdownHook(new Thread() {
		    public void run() { 
		    	try{
		    		
		    		if(is!=null) is.close();
		    		if(os!=null) os.close();
		    		if(CommandThread.this.client!=null) CommandThread.this.client.close();
		    		if(CommandThread.this.server!=null) CommandThread.this.server.close();
		    		//System.out.println("IOStream and Connection closed.");
		    		}
		    	catch (Exception e) {
					// TODO: handle exception
		    		Print.println("IOStream/Connection closing failed: "+e.getMessage());
				}
		    }
		 });
	}
	public static synchronized Transaction newTransaction(){

		Long t =  TCPThread.getTransaction_id();
//        TCPThread.setTransaction_id(t);
//        Integer tid = Integer.valueOf(t);
        Records.instance().getActive().add(t);
        return new Transaction(t);
    }
	public void run()
	{
		try
		{
			TimeoutThread timeoutThread=new TimeoutThread();
			timeoutThread.renewcount();
			Server.getthreadpool().execute(timeoutThread);
			//timeoutThread.start();
			DetectTimeout dTimeout=new DetectTimeout(timeoutThread,this);
			//dTimeout.start();
			Server.getthreadpool().execute(dTimeout);
			while(Server.getflag()) {
				if(client==null) break;
				is=new DataInputStream(client.getInputStream());
				os=new DataOutputStream(client.getOutputStream());
				String command=is.readUTF();//receive data from client
				
				if(timeoutThread.getcount()>15*60) {
					login=false;
					//window.setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has timed out.");
					is.close();
					os.close();
					client.close();
					break;
				}
				timeoutThread.renewcount();
				//window.setContent("command:'"+command+"' from "+client.getInetAddress().getHostAddress()+" received on port "+client.getLocalPort());
				//System.out.println("command:'"+command+"' from "+client.getInetAddress().getHostAddress()+" received on port "+client.getLocalPort());
				//deal with instructions from clients
				if(command.equals("exit"))
				{
					if(login) {
						t=null;
						login=false;
					}
					os.writeUTF("Bye");
					is.close();
					os.close();
					client.close();
					window.setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has exited.");
					break;
				}
				else if (command.equals("login"))
				{
					if(!login)
					{
						t=newTransaction();
						login=true;
						os.writeUTF("Login Success");
						window.setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has successfully logged in.");
					}
					else {
						os.writeUTF("Failure: Already Logged in. No need to re-login");
						window.setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has not successfully logged in.");
					}
				}
				else if(command.equals("logout"))
				{
					if(login)
					{
						t.rollback();//rollback uncommitted changes
						t=null;
						login=false;
						os.writeUTF("Logout Success");
						window.setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has successfully logged out.");
					}
					else {
						os.writeUTF("Failure: Not Logged in");
						window.setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has not successfully logged out.");
					}
				}
				else {
					String[]strarr=command.split(" ");
					String str=strarr[0];
					if(str.equals("add"))
					{
						if(!login) {
							os.writeUTF("Failure: Not Logged in");
							window.setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has not successfully inserted data.");
						}
						else if(strarr.length<3) {
							os.writeUTF("Failure: Invalid Operation");
							window.setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has not successfully inserted data.");
						}
						else{
							String id=strarr[1];
							String name="";
							for(int i=2;i<strarr.length;i++)
							{
								name=name+strarr[i];
								if(i+1<strarr.length)
									name=name+" ";
							}
							Pattern pattern =Pattern.compile("[0-9]*");
							if(!pattern.matcher(id).matches()) {
								os.writeUTF("Failure: Invalid Operation");
								window.setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has not successfully inserted data.");
							}
							else {
								String status=t.add(new Person(Integer.parseInt(id), name));
								os.writeUTF(status);
								if(!status.contains("Failure:"))
									window.setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has successfully inserted data ("+id+" ,"+name+").");
								else
									window.setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has not successfully inserted data.");
							}
						}
						
						
					}
					else if(str.equals("delete"))
					{
						if(!login) {
							os.writeUTF("Failure: Not Logged in");
							window.setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has not successfully deleted data.");
						}
						else if(strarr.length!=2) {
							os.writeUTF("Failure: Invalid Operation");
							window.setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has not successfully deleted data.");
						}
						else{
							String id=strarr[1];
							Pattern pattern =Pattern.compile("[0-9]*");
							if(!pattern.matcher(id).matches()) {
								os.writeUTF("Failure: Invalid Operation");
								window.setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has not successfully deleted data.");
							}
							else {
								String status=t.delete(Integer.parseInt(id));
								os.writeUTF(status);
								if(!status.contains("Failure:"))
									window.setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has successfully deleted data No."+id+".");
								else
									window.setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has not successfully deleted data.");
							}
						}
					}

					else if(str.equals("select"))
					{
						if(!login) {
							os.writeUTF("Failure: Not Logged in");
							window.setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has not successfully selected data.");
						}
						else if(strarr.length!=2) {
							os.writeUTF("Failure: Invalid Operation");
							window.setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has not successfully selected data.");
						}
						else{
							String id=strarr[1];
							Pattern pattern =Pattern.compile("[0-9]*");
							if(!pattern.matcher(id).matches()) {
								os.writeUTF("Failure: Invalid Operation");
								window.setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has not successfully selected data.");
							}
							else {
								String status=t.select(Integer.parseInt(id));
								os.writeUTF(status);
								if(!status.contains("Failure:"))
									window.setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has successfully selected data No."+id+".");
								else
									window.setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has not successfully selected data.");
							}
						}
					}

					else if(str.equals("update"))
					{
						if(!login) {
							os.writeUTF("Failure: Not Logged in");
							window.setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has not successfully updated data.");
						}
						else if(strarr.length<3) {
							os.writeUTF("Failure: Invalid Operation");
							window.setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has not successfully updated data.");
						}
						else{
							String id=strarr[1];
							String name="";
							for(int i=2;i<strarr.length;i++)
							{
								name=name+strarr[i];
								if(i+1<strarr.length)
									name=name+" ";
							}
							Pattern pattern =Pattern.compile("[0-9]*");
							if(!pattern.matcher(id).matches()) {
								os.writeUTF("Failure: Invalid Operation");
								window.setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has not successfully updated data.");
							}
							else {
								String status=t.update(Integer.parseInt(id), name);
								os.writeUTF(status);
								if(!status.contains("Failure"))
									window.setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has successfully updated data No."+id+" as: "+name+".");
								else

									window.setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has not successfully updated data.");
							}
						}
					}
					else if(str.equals("view"))
					{
						if(!login) {
							os.writeUTF("Failure: Not Logged in");
							window.setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has not successfully viewed data.");
						}
						else if(strarr.length!=1) {
							os.writeUTF("Failure: Invalid Operation");
							window.setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has not successfully viewed data.");
						}
						else {
							String status=t.view();
							os.writeUTF(status);
							if(!status.contains("Failure:"))
								window.setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has successfully viewed data.");
							else
								if(status.contains("rollback")){
									this.t = newTransaction();
								}
								window.setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has not successfully viewed data.");
						}
					}
					else if(str.equals("commit"))
					{
						if(!login) {
							os.writeUTF("Failure: Not Logged in");
							window.setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has not successfully committed.");
						}
						else if(strarr.length!=1) {
							os.writeUTF("Failure: Invalid Operation");
							window.setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has not successfully committed.");
						}
						else {
							String status=t.commit();
							os.writeUTF(status);
							if(!status.contains("Failure:"))
								window.setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has successfully committed.");
							else
								window.setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has not successfully committed.");
							this.t = newTransaction();
							
						}
					}
					else if(str.equals("rollback"))
					{
						if(!login) {
							os.writeUTF("Failure: Not Logged in");
							window.setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has not successfully rolled back.");
						}
						else if(strarr.length!=1) {
							os.writeUTF("Failure: Invalid Operation");
							window.setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has not successfully rolled back.");
						}
						else 
							{
								String status=t.rollback();
								os.writeUTF(status);
								if(!status.contains("Failure:"))
									window.setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has successfully rolled back.");
								else
									window.setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has not successfully rolled back.");
								this.t = newTransaction();
							}
						
					}
					else {
						os.writeUTF("Failure: Invalid Operation");
						window.setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has made an invalid operation.");
					}
				}
				
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			//JOptionPane.showMessageDialog(window.frame, e.getClass().toString(),"Error", JOptionPane.ERROR_MESSAGE);
			//System.exit(1);
			if(t!=null)
			{
				this.t.rollback();
				Records.instance().getActive().remove(this.t.getTid());
			}
			
            window.setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" might have some problems ("+e.getMessage()+").");

		}
	}
	public ServerSocket getServer() {
		return server;
	}
	public void setServer(ServerSocket server) {
		this.server = server;
	}
	public Socket getClient() {
		return client;
	}
	public void setClient(Socket client) {
		this.client = client;
	}
	public boolean isLogin() {
		return login;
	}
	public void setLogin(boolean login) {
		this.login = login;
	}
	public Transaction getT() {
		return t;
	}
	public void setT(Transaction t) {
		this.t = t;
	}
	public GUI getWindow() {
		return window;
	}
	public void setWindow(GUI window) {
		this.window = window;
	}
	public long getIdl() {
		return idl;
	}
	public void setIdl(long idl) {
		this.idl = idl;
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
	
	
}
