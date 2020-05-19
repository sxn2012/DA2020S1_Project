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
import gui.Welcome;
import transmission.CommandThread;
/*
 * Author: Xinnan SHEN
 * 
 * Date: 10/05/2020
 * 
 */
public class ReadBackup extends Thread {
	//public static boolean lock=false;
	private Welcome window;
	private Object lock;
	public ReadBackup(Welcome window,Object lock) {
		
		this.window=window;
		this.lock=lock;
	}
	public void run() {
		//read backup file and load data into memory
		
		String current_dir=System.getProperty("user.dir");
		String fileSeperator=File.separator;
		String mydir=current_dir+fileSeperator+"data";
		File path=new File(mydir);
		//if path not exists or file not exists, no need to load
		if(!path.exists())
		{
			
			synchronized (lock) {
				lock.notifyAll();
				window.getBtnConfirm().setText("Confirm");
				window.getBtnConfirm().setEnabled(true);
			}
			
			return;
		}
		String filepath=mydir+fileSeperator+"MVCCdata.json";
		File backupfile=new File(filepath);
		if(!backupfile.exists())
		{
			
			synchronized (lock) {
				lock.notifyAll();
				window.getBtnConfirm().setText("Confirm");
				window.getBtnConfirm().setEnabled(true);
			}
			return;
		}
		//if file exists, load data in the synchronized way
		synchronized (lock) {
			Print.println("Read Starting running...");
			try {
				//read data from file
				
				InputStream is = new FileInputStream(filepath);
				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
				String line=reader.readLine();
				//transfer string from JSON format
				
				Map<String, Object> map = new Gson().fromJson(
					    line, new TypeToken<HashMap<String, Object>>() {}.getType()
					);
				//store data into memory
				for(String key:map.keySet()) {
					Map<String, Object> submap=(Map<String, Object>) map.get(key);
					Integer pid=((Double) submap.get("ID")).intValue();
				    String name=(String) submap.get("Name");
				    Long created_tid=((Double) submap.get("CreatedTid")).longValue();
				    Long expired_tid=((Double) submap.get("ExpiredTid")).longValue();
				    long lastWrite_timestamp=((Double) submap.get("LastWrite")).longValue();
				    long lastRead_timestamp=((Double) submap.get("LastRead")).longValue();
				    Person person=new Person(pid, name,created_tid,expired_tid,lastWrite_timestamp,lastRead_timestamp);
				   
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
				
				
				JOptionPane.showMessageDialog(window.getFrame(), "There might be some errors in the initial process ("+e.getMessage()+").","Error", JOptionPane.ERROR_MESSAGE);
			}
			finally {
				
				lock.notifyAll();
				Print.println("Read Running Completed");
				
			}
		}
		
		
	}
	public Welcome getWindow() {
		return window;
	}
	public void setWindow(Welcome window) {
		this.window = window;
	}
	public Object getLock() {
		return lock;
	}
	public void setLock(Object lock) {
		this.lock = lock;
	}
}
