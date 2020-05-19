package transmission;
import java.awt.EventQueue;
import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import backup.Backup;
import backup.ReadBackup;
import concurrency.Print;
import concurrency.Records;
/*
 * Author: Xiguang Li, Xinnan SHEN
 * 
 * Date: 07/05/2020
 * 
 */


public class Server {

	private static volatile boolean flag = true;
	private static ExecutorService threadpool;

    public static void main(String[] args) throws Exception{
    	
    	System.out.println("Initializing the system...");
    	threadpool=Executors.newCachedThreadPool();
 		final long timeInterval = 15*60*1000;
 		Runnable gc = new Runnable() {
			@Override
			public void run() {
				while (Server.flag){

					try {
						Records.garbageClean();
						if(Server.flag)
							Thread.sleep(timeInterval);

					}catch (Exception e){
						
					}
				}
			}
		};

		Thread thread = new Thread(gc);
		
		threadpool.execute(thread);
		
		Object lock=new Object();
		Backup backupthread=new Backup(lock);
		threadpool.execute(backupthread);
		
		ReadBackup rBackup=new ReadBackup(lock);
		threadpool.execute(rBackup);
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
		    public void run() { 
		    	try{
		    		Server.flag=false;
		    		threadpool.shutdownNow();
		    		threadpool.awaitTermination(1, TimeUnit.SECONDS);
		    		Print.println("Server terminated.");
		    		}
		    	catch (Exception e) {
					
		    		Print.println("failed:"+e.getMessage());
				}
		    }
		 });
		


    }



	public static boolean isFlag() {
		return flag;
	}

	public static void setFlag(boolean flag) {
		Server.flag = flag;
	}

	public static ExecutorService getThreadpool() {
		return threadpool;
	}

	public static void setThreadpool(ExecutorService threadpool) {
		Server.threadpool = threadpool;
	}


    


}
