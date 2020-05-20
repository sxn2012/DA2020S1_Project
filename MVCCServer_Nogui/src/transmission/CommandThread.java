package transmission;
/*
 * Author: Xinnan SHEN, Xiguang Li
 * 
 * Date: 23/04/2020
 * 
 * Usage: Deal with Commands from Client
 */
import java.awt.Frame;
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

public class CommandThread extends Thread{
	private ServerSocket server;
	private Socket client;
	private boolean login;
	private Transaction t;
	//GUI window;
	private long idl;
	private DataInputStream is;
	private DataOutputStream os;
	public CommandThread(ServerSocket server,Socket client,long id) {
		
		this.server=server;
		this.client=client;
		login=false;
		
		this.idl=id;
		Runtime.getRuntime().addShutdownHook(new Thread() {
		    public void run() { 
		    	try{
		    		
		    		if(is!=null) is.close();
		    		if(os!=null) os.close();
		    		if(CommandThread.this.client!=null) CommandThread.this.client.close();
		    		if(CommandThread.this.server!=null) CommandThread.this.server.close();
		    		Print.println("IOStream and Connection closed.");
		    		}
		    	catch (Exception e) {
					
		    		Print.println("IOStream/Connection closing failed: "+e.getMessage());
				}
		    }
		 });
	}
	public static synchronized void setContent(String str) {
		SimpleDateFormat sdf=new SimpleDateFormat();
		sdf.applyPattern("dd-MM-yyyy HH:mm:ss");
		Print.println(sdf.format(new Date())+"\t"+str);
		
	}
	public static synchronized Transaction newTransaction(){

		Long t =  TCPThread.getTransaction_id();

		Records.instance().getActive().add(t);
		return new Transaction(t);


    }
	public void run()
	{
		try
		{
			TimeoutThread timeoutThread=new TimeoutThread();
			timeoutThread.renewcount();

			Server.getThreadpool().execute(timeoutThread);
			DetectTimeout dTimeout=new DetectTimeout(timeoutThread,this);

			Server.getThreadpool().execute(dTimeout);
			while(Server.isFlag()) {
				if(client==null) break;
				is=new DataInputStream(client.getInputStream());
				os=new DataOutputStream(client.getOutputStream());
				String command=is.readUTF();//receive command from clients
				//detect client timeout
				if(timeoutThread.getcount()>15 * 60) {
					login=false;
					// tell client to leave
					os.writeUTF("Failure: Operation Timeout");
					os.flush();
					is.close();
					os.close();
					client.close();
					break;
				}
				timeoutThread.renewcount();
				if(command.equals("exit"))//client wants to exit
				{
					//client is still logged in
					if(login) {
						//force client to log out
						t.rollback();//rollback uncommitted changes
						t=null;
						login=false;
					}
					//let client to exit the system
					os.writeUTF("Bye");
					is.close();
					os.close();
					client.close();
					setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has exited.");
					break;
				}
				else if (command.equals("login"))//client wants to login
				{
					//client has not logged in
					if(!login)
					{
						//let client log into the system
						t=newTransaction();
						login=true;
						os.writeUTF("Login Success");
						setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has successfully logged in.");
					}
					else {//client has already logged in
						//do nothing
						os.writeUTF("Failure: Already Logged in. No need to re-login");
						setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has not successfully logged in.");
					}
				}
				else if(command.equals("logout"))//client wants to logout
				{
					if(login)//client has logged in
					{
						//let client log out of system
						t.rollback();//rollback uncommitted changes
						t=null;
						login=false;
						os.writeUTF("Logout Success");
						setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has successfully logged out.");
					}
					else {//client has not logged in
						//do nothing
						os.writeUTF("Failure: Not Logged in");
						setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has not successfully logged out.");
					}
				}
				else {
					//deal with specific commands
					String[]strarr=command.split(" ");
					String str=strarr[0];
					if(str.equals("add"))//insert data
					{
						if(!login) {//client has not logged in
							//must be logged in to operate
							os.writeUTF("Failure: Not Logged in");
							setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has not successfully inserted data.");
						}
						else if(strarr.length<3) {//parameters are not valid
							//do nothing
							os.writeUTF("Failure: Invalid Operation");
							setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has not successfully inserted data.");
						}
						else{
							//test id and name
							String id=strarr[1];
							String name="";
							for(int i=2;i<strarr.length;i++)
							{
								name=name+strarr[i];
								if(i+1<strarr.length)
									name=name+" ";
							}
							Pattern pattern =Pattern.compile("[0-9]*");
							if(!pattern.matcher(id).matches()) {//id and name are not valid
								//do nothing
								os.writeUTF("Failure: Invalid Operation");
								setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has not successfully inserted data.");
							}
							else {
								//insert data
								String status=t.add(new Person(Integer.parseInt(id), name));
								os.writeUTF(status);//send the result
								if(!status.contains("Failure:"))//success
									setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has successfully inserted data ("+id+" ,"+name+").");
								else//failure
									setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has not successfully inserted data.");
							}
						}
						
						
					}
					else if(str.equals("delete"))//delete data
					{
						if(!login) {//client has not logged in
							//must be logged in to operate
							os.writeUTF("Failure: Not Logged in");
							setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has not successfully deleted data.");
						}
						else if(strarr.length!=2) {//parameters are not valid
							//do nothing
							os.writeUTF("Failure: Invalid Operation");
							setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has not successfully deleted data.");
						}
						else{
							//test id
							String id=strarr[1];
							Pattern pattern =Pattern.compile("[0-9]*");
							if(!pattern.matcher(id).matches()) {//id is not valid
								//do nothing
								os.writeUTF("Failure: Invalid Operation");
								setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has not successfully deleted data.");
							}
							else {
								//delete data
								String status=t.delete(Integer.parseInt(id));
								os.writeUTF(status);//send the result
								if(!status.contains("Failure:"))//success
									setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has successfully deleted data No."+id+".");
								else//failure
									setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has not successfully deleted data.");
							}
						}
					}

					else if(str.equals("select"))//select data
					{
						if(!login) {//client has not logged in
							//must be logged in to operate
							os.writeUTF("Failure: Not Logged in");
							setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has not successfully selected data.");
						}
						else if(strarr.length!=2) {//parameters are not valid
							//do nothing
							os.writeUTF("Failure: Invalid Operation");
							setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has not successfully selected data.");
						}
						else{
							//test id
							String id=strarr[1];
							Pattern pattern =Pattern.compile("[0-9]*");
							if(!pattern.matcher(id).matches()) {//id is not valid
								os.writeUTF("Failure: Invalid Operation");
								setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has not successfully selected data.");
							}
							else {
								//select data
								String status=t.select(Integer.parseInt(id));
								os.writeUTF(status);//send the result
								if(!status.contains("Failure:"))//success
									setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has successfully selected data No."+id+".");
								else//failure
									setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has not successfully selected data.");
							}
						}
					}

					else if(str.equals("update"))//update data
					{
						if(!login) {//client has not logged in
							//must be logged in to operate
							os.writeUTF("Failure: Not Logged in");
							setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has not successfully updated data.");
						}
						else if(strarr.length<3) {//parameters are not valid
							//do nothing
							os.writeUTF("Failure: Invalid Operation");
							setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has not successfully updated data.");
						}
						else{
							//test id and name
							String id=strarr[1];
							String name="";
							for(int i=2;i<strarr.length;i++)
							{
								name=name+strarr[i];
								if(i+1<strarr.length)
									name=name+" ";
							}
							Pattern pattern =Pattern.compile("[0-9]*");
							if(!pattern.matcher(id).matches()) {//id and name are not valid
								//do nothing
								os.writeUTF("Failure: Invalid Operation");
								setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has not successfully updated data.");
							}
							else {
								//update data
								String status=t.update(Integer.parseInt(id), name);
								os.writeUTF(status);//send result
								if(!status.contains("Failure")) {//success
									setContent(this.idl + " --- " + client.getInetAddress().getHostAddress() + " has successfully updated data No." + id + " as: " + name + ".");
								}else {//failure
									if (status.contains("rollback")) {
										this.t = newTransaction();
									}
									setContent(this.idl + " --- " + client.getInetAddress().getHostAddress() + " has not successfully updated data.");

								}
							}
						}
					}
					else if(str.equals("view"))//view data
					{
						if(!login) {//client has not logged in
							//must be logged in to operate
							os.writeUTF("Failure: Not Logged in");
							setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has not successfully viewed data.");
						}
						else if(strarr.length!=1) {//parameters are not valid
							//do nothing
							os.writeUTF("Failure: Invalid Operation");
							setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has not successfully viewed data.");
						}
						else {
							//view data
							String status=t.view();
							os.writeUTF(status);//send results
							if(!status.contains("Failure:"))//success
								setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has successfully viewed data.");
							else//failure
								setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has not successfully viewed data.");
						}
					}
					else if(str.equals("commit"))//commit commands
					{
						if(!login) {//client has not logged in
							//must be logged in to operate
							os.writeUTF("Failure: Not Logged in");
							setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has not successfully committed.");
						}
						else if(strarr.length!=1) {//parameters are not valid
							//do nothing
							os.writeUTF("Failure: Invalid Operation");
							setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has not successfully committed.");
						}
						else {
							//commit commands
							String status=t.commit();
							os.writeUTF(status);//send results
							if(!status.contains("Failure:"))//success
								setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has successfully committed.");
							else//failure
								setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has not successfully committed.");
							this.t = newTransaction();//generate a new transaction
							
						}
					}
					else if(str.equals("rollback"))//rollback commands
					{
						if(!login) {//client has not logged in
							//must be logged in to operate
							os.writeUTF("Failure: Not Logged in");
							setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has not successfully rolled back.");
						}
						else if(strarr.length!=1) {//parameters are not valid
							//do nothing
							os.writeUTF("Failure: Invalid Operation");
							setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has not successfully rolled back.");
						}
						else 
							{
								//rollback commands
								String status=t.rollback();
								os.writeUTF(status);//send results
								if(!status.contains("Failure:"))//success
									setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has successfully rolled back.");
								else//failure
									setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has not successfully rolled back.");
								this.t = newTransaction();//generate a new transaction
							}
						
					}
					else {//operations that cannot be recognized
						//do nothing
						os.writeUTF("Failure: Invalid Operation");
						setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" has made an invalid operation.");
					}
				}
				os.flush();


			}
		}
		catch (Exception e) {
			
			if(t!=null)
			{
				this.t.rollback();
				Records.instance().getActive().remove(this.t.getTid());
			}
			
            if(e.getMessage()!=null&&!e.getMessage().equals(""))
				setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" might have some problems ("+e.getMessage()+").");
			else 
				setContent(this.idl+" --- "+client.getInetAddress().getHostAddress()+" might have some problems ("+e.getClass()+").");
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
