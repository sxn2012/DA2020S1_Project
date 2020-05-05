import java.awt.EventQueue;
import java.lang.reflect.Array;
import java.util.HashSet;

public class Main {

    static Integer transaction_id = 1;


    public static void main(String[] args) {
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
    	EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
					window.setContent("Server Start");
			    	TCPThread thread=new TCPThread(7777,window);
			    	thread.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
    	


    }


    


}
