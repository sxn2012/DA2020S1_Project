package backup;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import org.json.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import concurrency.Person;
import concurrency.Print;
import concurrency.Transaction;
import transmission.CommandThread;
import transmission.ConnectInfo;
import transmission.Server;
/*
 * Author: Xinnan SHEN
 * Email: xinnan.shen@student.unimelb.edu.au
 * Date: 07/05/2020
 * 
 */
public class ReadBackup extends Thread {
	//public static boolean lock=false;
	//private Welcome window;
	private Object lock;
	public ReadBackup(Object lock) {
		// TODO Auto-generated constructor stub
		//this.window=window;
		this.lock=lock;
	}
	public void run() {
		//lock=true;
		String current_dir=System.getProperty("user.dir");
		String fileSeperator=File.separator;
		String mydir=current_dir+fileSeperator+"data";
		File path=new File(mydir);
		if(!path.exists())
		{
			//lock=false;
			synchronized (lock) {
				lock.notifyAll();
				ConnectInfo ci=new ConnectInfo();
				//ci.start();
				Server.getThreadpool().execute(ci);
			}
			
			return;
		}
		String filepath=mydir+fileSeperator+"MVCCdata.json";
		File backupfile=new File(filepath);
		if(!backupfile.exists())
		{
			//lock=false;
			//notifyAll();
			synchronized (lock) {
				lock.notifyAll();
				ConnectInfo ci=new ConnectInfo();
				//ci.start();
				Server.getThreadpool().execute(ci);
			}
			return;
		}
		synchronized (lock) {
			Print.println("Backup Reading...");
			try {
				Thread.sleep(500);
				InputStream is = new FileInputStream(filepath);
				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
				String line=reader.readLine();
				//JSONObject jsonObject = new JSONObject(line);
				Map<String, Object> map = new Gson().fromJson(
					    line, new TypeToken<HashMap<String, Object>>() {}.getType()
					);
				//System.out.println(map);
				for(String key:map.keySet()) {
					Map<String, Object> submap=(Map<String, Object>) map.get(key);
					Integer pid=((Double) submap.get("ID")).intValue();
				    String name=(String) submap.get("Name");
				    Integer created_tid=((Double) submap.get("CreatedTid")).intValue();
				    Integer expired_tid=((Double) submap.get("ExpiredTid")).intValue();
				    long lastWrite_timestamp=((Double) submap.get("LastWrite")).longValue();
				    long lastRead_timestamp=((Double) submap.get("LastRead")).longValue();
				    Person person=new Person(pid, name,created_tid,expired_tid,lastWrite_timestamp,lastRead_timestamp);
				    //System.out.println(person.getstr());
				    Transaction t=CommandThread.newTransaction();
				    String status=t.add(person);
				    if(status.contains("Failure:"))
				    	throw new Exception(status);
				    status=t.commit();
				    if(status.contains("Failure:"))
				    	throw new Exception(status);
				}
				reader.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//System.out.println(e.getMessage());
				//JOptionPane.showMessageDialog(window.frame, "There might be some errors in the initial process ("+e.getMessage()+").","Error", JOptionPane.ERROR_MESSAGE);
			}
			finally {
				//lock=false;
				lock.notifyAll();
				Print.println("Backup has been read.");
				ConnectInfo ci=new ConnectInfo();
				//ci.start();
				Server.getThreadpool().execute(ci);
			}
		}
		
		
	}
	public Object getLock() {
		return lock;
	}
	public void setLock(Object lock) {
		this.lock = lock;
	}
}
