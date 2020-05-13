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
 * Author: Xinnan SHEN
 * Email: xinnan.shen@student.unimelb.edu.au
 * Date: 07/05/2020
 * 
 */


public class Server {

	private static Integer transaction_id = 1;
	private static volatile boolean flag = true;
	private static ExecutorService threadpool;

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
						//System.out.println(e.getMessage());
					}
				}
			}
		};

		Thread thread = new Thread(gc);
		//thread.start();
		threadpool.execute(thread);
		
		Object lock=new Object();
		Backup backupthread=new Backup(lock);
		threadpool.execute(backupthread);
		//backupthread.start();
		ReadBackup rBackup=new ReadBackup(lock);
		threadpool.execute(rBackup);
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
		    public void run() { 
		    	try{
		    		Server.flag=false;
		    		threadpool.shutdownNow();
		    		threadpool.awaitTermination(1, TimeUnit.SECONDS);
		    		//ThreadPoolExecutor executor = (ThreadPoolExecutor) threadpool;
		    		Print.println("Server terminated.");
		    		}
		    	catch (Exception e) {
					// TODO: handle exception
		    		Print.println("failed:"+e.getMessage());
				}
		    }
		 });
		//rBackup.start();
		/*EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Welcome window = new Welcome();
					window.frame.setVisible(true);
					Backup backupthread=new Backup(window,lock);
					backupthread.start();
					ReadBackup rBackup=new ReadBackup(window,lock);
					rBackup.start();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
    	*/


    }

	public static Integer getTransaction_id() {
		return transaction_id;
	}

	public static void setTransaction_id(Integer transaction_id) {
		Server.transaction_id = transaction_id;
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
