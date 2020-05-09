import java.awt.EventQueue;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/*
 * Author: Xinnan SHEN
 * Email: xinnan.shen@student.unimelb.edu.au
 * Date: 23/04/2020
 * 
 */

public class Main {

	static ExecutorService threadpool;
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		//System.out.println("Login first");
		threadpool=Executors.newCachedThreadPool();
		
		threadpool.execute(new Runnable() {
			public void run() {
				try {
					Welcome window = new Welcome();
					window.frame.setVisible(true);
					Runtime.getRuntime().addShutdownHook(new Thread() {
					    public void run() { 
					    	try{
					    		window.frame.setVisible(false);
					    		threadpool.shutdownNow();
					    		threadpool.awaitTermination(1, TimeUnit.SECONDS);
					    		//ThreadPoolExecutor executor = (ThreadPoolExecutor) threadpool;
					    		Print.println("Client terminated.");
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
		
		
		//thread.start();
		
		}
		
	

}
