package transmission;
import gui.Server;

/*
 * Author: Xinnan SHEN
 * 
 * Date: 07/05/2020
 * 
 */

//define a counter to show how long the client has not responding
public class TimeoutThread extends Thread {
	private int count;
	public TimeoutThread() {
		
		count=0;
	}
	public void renewcount() {
		count=0;
	}
	public int getcount() {
		return count;
	}
	public void run() {
		while(Server.getflag()) {
			count++;
			try {
				if(Server.getflag())
					Thread.sleep(1000);
			} catch (Exception e) {
				
				return;
			}
		}
	}
}
