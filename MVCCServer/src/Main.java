import java.awt.EventQueue;
import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;



public class Main {

    static Integer transaction_id = 1;
    static volatile boolean flag = true;
    static ExecutorService threadpool;
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
    	threadpool=Executors.newCachedThreadPool();
    	
 		final long timeInterval = 15*60*1000;
 		Runnable gc = new Runnable() {
			@Override
			public void run() {
				while (Main.flag){

					try {
						Records.garbageClean();
						if(Main.flag)
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
		threadpool.execute(thread);
		
		Object lock=new Object();
		
		threadpool.execute(new Runnable() {
			public void run() {
				try {
					Welcome window = new Welcome();
					window.frame.setVisible(true);
					Backup backupthread=new Backup(window,lock);
					threadpool.execute(backupthread);
					//backupthread.start();
					ReadBackup rBackup=new ReadBackup(window,lock);
					//rBackup.start();
					threadpool.execute(rBackup);
					Runtime.getRuntime().addShutdownHook(new Thread() {
					    public void run() { 
					    	try{
					    		Main.flag=false;
					    		window.frame.setVisible(false);
					    		threadpool.shutdownNow();
					    		threadpool.awaitTermination(1, TimeUnit.SECONDS);
					    		//ThreadPoolExecutor executor = (ThreadPoolExecutor) threadpool;
					    		System.out.println("Server terminated.");
					    		}
					    	catch (Exception e) {
								// TODO: handle exception
					    		System.out.println("failed:"+e.getMessage());
							}
					    }
					 });
					
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		});
    	


    }


    


}
