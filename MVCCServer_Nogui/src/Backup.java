import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.*;

public class Backup extends Thread {
	private String current_dir;
	private String fileSeperator;
	private Object lock;
	//private Welcome window;
	public Backup(Object lock) {
		// TODO Auto-generated constructor stub
		this.current_dir=System.getProperty("user.dir");
		this.fileSeperator=File.separator;
		this.lock=lock;
		
	}
	
	
	
	
	public void run() {
		String mydir=current_dir+fileSeperator+"data";
		File path=new File(mydir);
		if(!path.exists())
			path.mkdir();
		synchronized (lock) 
		{
			try {
				System.out.println("Backup Writer waiting...");
				lock.wait();
				System.out.println("Backup Writer Continue running");
				//window.btnConfirm.setText("Confirm");
				//window.btnConfirm.setEnabled(true);
				String filepath=mydir+fileSeperator+"MVCCdata.json";
				System.out.println("The backup is stored in:"+filepath);
				while(Main.flag)
				{
					
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
			    	if(Main.flag)
			    		Thread.sleep(10000);
			    	//if(output.equals("")) 
				}
			}
			catch (Exception e) 
			{
					// TODO Auto-generated catch block
					//System.out.println(e.getMessage());
					return;
			}
		}
		
	}
		
		
		
}

