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
    	/*
        Transaction one = newTransaction();
        one.add(new Person(1,"name1"));
        one.add(new Person(2,"name2"));
//        one.commit();

//        Transaction three = newTransaction();
        Transaction two = newTransaction();



        System.out.println("Before:");
        //System.out.println("transaction one:"+one.fetch());
        //System.out.println("transaction two:"+two.fetch());
        System.out.print("transaction one:");
        for (Person p:one.fetch())
        {
        	System.out.print(p.getstr()+"\t");
        }
        System.out.println();
        System.out.print("transaction two:");
        for (Person p:two.fetch())
        {
        	System.out.print(p.getstr()+"\t");
        }
        System.out.println();
//        three.update(1,"name3");

//       one.rollback();
        one.commit();
        System.out.println("After:");
        //System.out.println("transaction one:"+one.fetch());
        //System.out.println("transaction two:"+two.fetch());
        System.out.print("transaction one:");
        for (Person p:one.fetch())
        {
        	System.out.print(p.getstr()+"\t");
        }
        System.out.println();
        System.out.print("transaction two:");
        for (Person p:two.fetch())
        {
        	System.out.print(p.getstr()+"\t");
        }
        System.out.println();
		*/
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
						//e.printStackTrace();
						//System.out.println(e.getMessage());
						return;
					}
				}
			}
		};

		Thread thread = new Thread(gc);
		//thread.start();
		Server.getthreadpool().execute(thread);
		
		Object lock=new Object();
		
		Server.getthreadpool().execute(new Runnable() {
			public void run() {
				try {
					Welcome window = new Welcome();
					window.getFrame().setVisible(true);
					Backup backupthread=new Backup(window,lock);
					Server.getthreadpool().execute(backupthread);
					//backupthread.start();
					ReadBackup rBackup=new ReadBackup(window,lock);
					//rBackup.start();
					Server.getthreadpool().execute(rBackup);
					Runtime.getRuntime().addShutdownHook(new Thread() {
					    public void run() { 
					    	try{
					    		Server.setflag(false);
					    		window.getFrame().setVisible(false);
					    		Server.getthreadpool().shutdownNow();
					    		Server.getthreadpool().awaitTermination(1, TimeUnit.SECONDS);
					    		//ThreadPoolExecutor executor = (ThreadPoolExecutor) threadpool;
					    		Print.println("Server terminated.");
					    		}
					    	catch (Exception e) {
								// TODO: handle exception
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
