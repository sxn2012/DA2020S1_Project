/*
 * Author: Xinnan SHEN
 * Email: xinnan.shen@student.unimelb.edu.au
 * Date: 07/05/2020
 * 
 */
public class DetectTimeout extends Thread {
	TimeoutThread thread;
	CommandThread c;
	boolean istimeout;
	public DetectTimeout(TimeoutThread thread,CommandThread c) {
		// TODO Auto-generated constructor stub
		this.thread=thread;
		this.c=c;
		this.istimeout=false;
	}
	public void run() {
		while(true) {
			try
			{
				long count=thread.getcount();
				if(count>15*60&&!istimeout)
				{
					c.os.writeUTF("Failure: Operation Timeout");
					if(c.t!=null) {
						c.t.rollback();
						c.t=null;
					}
					c.setContent(c.idl+" --- "+c.client.getInetAddress().getHostAddress()+" has timed out.");
					istimeout=true;
				}
				Thread.sleep(100);
			}
			catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
}
