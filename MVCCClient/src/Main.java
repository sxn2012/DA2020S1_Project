import java.awt.EventQueue;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/*
 * Author: Xinnan SHEN
 * Email: xinnan.shen@student.unimelb.edu.au
 * Date: 23/04/2020
 * 
 */

public class Main {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		//System.out.println("Login first");
		Socket client=new Socket("127.0.0.1", 7777);
		
		//thread.start();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI(client);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		}
		
	

}
