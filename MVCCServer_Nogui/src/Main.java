import java.awt.EventQueue;
import java.lang.reflect.Array;
import java.util.HashSet;



public class Main {

    static Integer transaction_id = 1;


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
 		final long timeInterval = 15*60*1000;
 		Runnable gc = new Runnable() {
			@Override
			public void run() {
				while (true){

					try {
						Records.garbageClean();
						Thread.sleep(timeInterval);

					}catch (InterruptedException e){
						e.printStackTrace();
					}
				}
			}
		};

		Thread thread = new Thread(gc);
		thread.start();
		
		Object lock=new Object();
		Backup backupthread=new Backup(lock);
		backupthread.start();
		ReadBackup rBackup=new ReadBackup(lock);
		rBackup.start();
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


    


}