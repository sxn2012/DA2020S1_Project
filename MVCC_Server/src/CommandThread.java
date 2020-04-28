/*
 * Author: Xinnan SHEN
 * Email: xinnan.shen@student.unimelb.edu.au
 * Date: 23/04/2020
 * 
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.regex.Pattern;

public class CommandThread extends Thread{
	Socket client;
	boolean login;
	Transaction t;
	public CommandThread(Socket client) {
		// TODO Auto-generated constructor stub
		this.client=client;
		login=false;
	}
	public static Transaction newTransaction(){

        TCPThread.transaction_id++;
        Records.instance().active.add(TCPThread.transaction_id);

        return new Transaction(TCPThread.transaction_id);
    }
	public void run()
	{
		try
		{
			while(true) {		
				DataInputStream is=new DataInputStream(client.getInputStream());
				DataOutputStream os=new DataOutputStream(client.getOutputStream());
				String command=is.readUTF();
				System.out.println("command:'"+command+"' received");
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
					break;
				}
				else if (command.equals("login"))
				{
					if(!login)
					{
						t=newTransaction();
						login=true;
						os.writeUTF("Login Success");
					}
					else {
						os.writeUTF("Failure: Already Logged in. No need to re-login");
					}
				}
				else if(command.equals("logout"))
				{
					if(login)
					{
						t=null;
						login=false;
						os.writeUTF("Logout Success");
					}
					else {
						os.writeUTF("Failure: Not Logged in");
					}
				}
				else {
					String[]strarr=command.split(" ");
					String str=strarr[0];
					if(str.equals("add"))
					{
						if(!login) os.writeUTF("Failure: Not Logged in");
						else if(strarr.length!=3) os.writeUTF("Failure: Invalid Input");
						else{
							String id=strarr[1];
							String name=strarr[2];
							Pattern pattern =Pattern.compile("[0-9]*");
							if(!pattern.matcher(id).matches()) os.writeUTF("Failure: Invalid Input");
							else {
								os.writeUTF(t.add(new Person(Integer.parseInt(id), name)));
							}
						}
						
						
					}
					else if(str.equals("delete"))
					{
						if(!login) os.writeUTF("Failure: Not Logged in");
						else if(strarr.length!=2) os.writeUTF("Failure: Invalid Input");
						else{
							String id=strarr[1];
							Pattern pattern =Pattern.compile("[0-9]*");
							if(!pattern.matcher(id).matches()) os.writeUTF("Failure: Invalid Input");
							else {
								os.writeUTF(t.delete(Integer.parseInt(id)));
							}
						}
					}
					
					else if(str.equals("update"))
					{
						if(!login) os.writeUTF("Failure: Not Logged in");
						else if(strarr.length!=3) os.writeUTF("Failure: Invalid Input");
						else{
							String id=strarr[1];
							String name=strarr[2];
							Pattern pattern =Pattern.compile("[0-9]*");
							if(!pattern.matcher(id).matches()) os.writeUTF("Failure: Invalid Input");
							else {
								os.writeUTF(t.update(Integer.parseInt(id), name));
							}
						}
					}
					else if(str.equals("view"))
					{
						if(!login) os.writeUTF("Failure: Not Logged in");
						else if(strarr.length!=1) os.writeUTF("Failure: Invalid Input");
						else os.writeUTF(t.view());
					}
					else if(str.equals("commit"))
					{
						if(!login) os.writeUTF("Failure: Not Logged in");
						else if(strarr.length!=1) os.writeUTF("Failure: Invalid Input");
						else os.writeUTF(t.commit());this.t = newTransaction();
					}
					else if(str.equals("rollback"))
					{
						if(!login) os.writeUTF("Failure: Not Logged in");
						else if(strarr.length!=1) os.writeUTF("Failure: Invalid Input");
						else os.writeUTF(t.rollback());
					}
					else if(str.equals("help"))
					{
						os.writeUTF("'login' : log into system\n\r\t    'logout': log out of system\n\r\t    'add': add data into database (usage: add id name)\n\r\t    'update': update data  (usage: update id name)\n\r\t    'delete': delete data (usage: delete id)\n\r\t   'view': view data in database\n\r\t   'commit': commit changes\n\r\t   'rollback': undo changes\n\r\t   'exit': exit the system\n\r\t    ");
					}
					else {
						os.writeUTF("Failure: Invalid Input\n\r\t    For instructions, type 'help'");
					}
				}
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
