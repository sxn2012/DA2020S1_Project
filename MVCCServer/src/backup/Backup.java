package backup;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.*;

import concurrency.Person;
import concurrency.Print;
import concurrency.Transaction;
import gui.Server;
import gui.Welcome;
import transmission.CommandThread;
/*
 * Author: Xinnan SHEN
 * Email: xinnan.shen@student.unimelb.edu.au
 * Date: 10/05/2020
 * 
 */
public class Backup extends Thread {
	private String current_dir;
	private String fileSeperator;
	private Object lock;
	private Welcome window;
	public Backup(Welcome window,Object lock) {
		// TODO Auto-generated constructor stub
		this.current_dir=System.getProperty("user.dir");
		this.fileSeperator=File.separator;
		this.lock=lock;
		this.window=window;
	}
	
	
	
	
	public void run() {
		String mydir=current_dir+fileSeperator+"data";
		File path=new File(mydir);
		if(!path.exists())
			path.mkdir();
		synchronized (lock) 
		{
			try {
				Print.println("Backup waiting...");
				lock.wait();
				Print.println("Backup Continue running");
				window.getBtnConfirm().setText("Confirm");
				window.getBtnConfirm().setEnabled(true);
				while(Server.getflag())
				{
					String filepath=mydir+fileSeperator+"MVCCdata.json";
					/*while(ReadBackup.lock) {
						Thread.sleep(5000);
					}
					*/
					Transaction t=CommandThread.newTransaction();
					int i=0;
					JSONObject json=new JSONObject();
			    	for (Person p:t.fetch())
			        {
			    		
			    		//json.put("RecordNum", i);
			    		Map <String,Object> map=new HashMap<String, Object>();
			        	map.put("ID",p.getpid());
			    		map.put("Name",p.getname());
			    		map.put("CreatedTid",p.getcreated_tid());
			    		map.put("ExpiredTid",p.getexpired_tid());
			    		map.put("LastRead",p.getLastRead_timestamp());
			    		map.put("LastWrite",p.getLastWrite_timestamp());
			    		json.put("Record-"+i, map);
			    		i++;
			        }
			    	FileWriter fw=new FileWriter(filepath);
			    	fw.write(json.toString());
			    	fw.close();
			    	if(Server.getflag())
			    		Thread.sleep(10000);
			    	//if(output.equals("")) 
				}
			}
			catch (Exception e) 
			{
					// TODO Auto-generated catch block
					//e.printStackTrace();
					//System.out.println(e.getMessage());
					return;
			}
		}
		
	}




	public String getCurrent_dir() {
		return current_dir;
	}




	public void setCurrent_dir(String current_dir) {
		this.current_dir = current_dir;
	}




	public String getFileSeperator() {
		return fileSeperator;
	}




	public void setFileSeperator(String fileSeperator) {
		this.fileSeperator = fileSeperator;
	}




	public Object getLock() {
		return lock;
	}




	public void setLock(Object lock) {
		this.lock = lock;
	}




	public Welcome getWindow() {
		return window;
	}




	public void setWindow(Welcome window) {
		this.window = window;
	}
		
		
		
}

