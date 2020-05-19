package backup;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import concurrency.Person;
import concurrency.Print;
import concurrency.Transaction;
import transmission.CommandThread;
import transmission.Server;
/*
 * Author: Xinnan SHEN
 * 
 * Date: 07/05/2020
 * 
 */
public class Backup extends Thread {
	private String current_dir;
	private String fileSeperator;
	private Object lock;

	public Backup(Object lock) {
		
		this.current_dir=System.getProperty("user.dir");
		this.fileSeperator=File.separator;
		this.lock=lock;
		
	}
	
	
	
	
	public void run() {
		//back up data in the server
		String mydir=current_dir+fileSeperator+"data";
		File path=new File(mydir);
		//generate a new file to store the data, if not exists
		if(!path.exists())
			path.mkdir();
		//reading and writing file must be synchronized
		synchronized (lock) 
		{
			try {
				Print.println("Backup Writer waiting...");
				lock.wait(500);
				Print.println("Backup Writer Continue running");
				
				String filepath=mydir+fileSeperator+"MVCCdata.json";
				Print.println("The backup is stored in:"+filepath);
				while(Server.isFlag())
				{
					
					
					//generate a new transaction and view the data
					Transaction t=CommandThread.newTransaction();
					
					int i=0;
					JSONObject json=new JSONObject();
			    	for (Person p:t.fetch())
			        {
			    		//view data and transform into JSON format
			    		
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
			    	//write JSON string into file
			    	
			    	FileWriter fw=new FileWriter(filepath);
			    	fw.write(json.toString());
			    	fw.close();
			    	if(Server.isFlag())
			    		Thread.sleep(10000);
			    	//backup data every 10 seconds
			    
				}
			}
			catch (Exception e) 
			{
				
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
		
		
		
}

