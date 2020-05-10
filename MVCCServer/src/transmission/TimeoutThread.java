package transmission;
import gui.Server;

/*
 * Author: Xinnan SHEN
 * Email: xinnan.shen@student.unimelb.edu.au
 * Date: 07/05/2020
 * 
 */
public class TimeoutThread extends Thread {
	private int count;
	public TimeoutThread() {
		// TODO Auto-generated constructor stub
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
				// TODO Auto-generated catch block
				//e.printStackTrace();
				//System.out.println(e.getMessage());
				return;
			}
		}
	}
}
