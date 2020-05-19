package gui;
import java.awt.EventQueue;
import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.concurrent.Executor;
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
 * Date: 09/05/2020
 * 
 */

public class Server {


    private static volatile boolean flag = true;
    private static ExecutorService threadpool;
    public static boolean getflag() {
    	return flag;
    }
    public static void setflag(boolean f) {
    	flag=f;
    }
    public static ExecutorService getthreadpool() {
		return threadpool;
	}
    public static void setthreadpool(ExecutorService pool) {
		threadpool=pool;
	}
    public static void main(String[] args) throws Exception{
    	
    	Server.setthreadpool(Executors.newCachedThreadPool());
    	
 		final long timeInterval = 15*60*1000;
 		Runnable gc = new Runnable() {
			@Override
			public void run() {
				while (Server.getflag()){

					try {
						Records.garbageClean();
						if(Server.getflag())
							Thread.sleep(timeInterval);

					}catch (Exception e){
						
						return;
					}
				}
			}
		};

		Thread thread = new Thread(gc);
		Server.getthreadpool().execute(thread);
		
		Object lock=new Object();
		
		Server.getthreadpool().execute(new Runnable() {
			public void run() {
				try {
					Welcome window = new Welcome();
					window.getFrame().setVisible(true);
					Backup backupthread=new Backup(window,lock);
					Server.getthreadpool().execute(backupthread);
					
					ReadBackup rBackup=new ReadBackup(window,lock);
					
					Server.getthreadpool().execute(rBackup);
					Runtime.getRuntime().addShutdownHook(new Thread() {
					    public void run() { 
					    	try{
					    		Server.setflag(false);
					    		window.getFrame().setVisible(false);
					    		Server.getthreadpool().shutdownNow();
					    		Server.getthreadpool().awaitTermination(1, TimeUnit.SECONDS);
					    		
					    		Print.println("Server terminated.");
					    		}
					    	catch (Exception e) {
								
					    		Print.println("failed:"+e.getMessage());
							}
					    }
					 });
					
				} catch (Exception e) {
					Print.println("Error: "+e.getMessage());
				}
			}
		});
    	


    }


    


}
