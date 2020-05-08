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

public class ReadBackup extends Thread {
	//public static boolean lock=false;
	private Welcome window;
	private Object lock;
	public ReadBackup(Welcome window,Object lock) {
		// TODO Auto-generated constructor stub
		this.window=window;
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
				window.btnConfirm.setText("Confirm");
				window.btnConfirm.setEnabled(true);
			}
			
			return;
		}
		String filepath=mydir+fileSeperator+"MVCCdata.json";
		File backupfile=new File(filepath);
		if(!backupfile.exists())
		{
			//lock=false;
			synchronized (lock) {
				lock.notifyAll();
				window.btnConfirm.setText("Confirm");
				window.btnConfirm.setEnabled(true);
			}
			return;
		}
		synchronized (lock) {
			System.out.println("Read Starting running...");
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
				//e.printStackTrace();
				JOptionPane.showMessageDialog(window.frame, "There might be some errors in the initial process ("+e.getMessage()+").","Error", JOptionPane.ERROR_MESSAGE);
			}
			finally {
				//lock=false;
				lock.notifyAll();
				System.out.println("Read Running Completed");
				
			}
		}
		
		
	}
}
