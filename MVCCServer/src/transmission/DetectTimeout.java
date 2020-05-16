package transmission;
import gui.Server;

/*
 * Author: Xinnan SHEN
 * Email: xinnan.shen@student.unimelb.edu.au
 * Date: 07/05/2020
 * 
 */

//detect whether the client has timed out
public class DetectTimeout extends Thread {
	private TimeoutThread thread;
	private CommandThread c;
	private boolean istimeout;
	public DetectTimeout(TimeoutThread thread,CommandThread c) {
		// TODO Auto-generated constructor stub
		this.thread=thread;
		this.c=c;
		this.istimeout=false;
	}
	public void run() {
		while(Server.getflag()) {
			try
			{
				long count=thread.getcount();
				if(count>15*60&&!istimeout)
				{
					c.getOs().writeUTF("Failure: Operation Timeout");
					if(c.getT()!=null) {
						c.getT().rollback();
						c.setT(null);
					}
					c.getWindow().setContent(c.getIdl()+" --- "+c.getClient().getInetAddress().getHostAddress()+" has timed out.");
					istimeout=true;
				}
				if(Server.getflag())
					Thread.sleep(100);
			}
			catch (Exception e) {
				// TODO: handle exception
				//e.printStackTrace();
				//System.out.println(e.getMessage());
				return;
			}
		}
	}
	public TimeoutThread getThread() {
		return thread;
	}
	public void setThread(TimeoutThread thread) {
		this.thread = thread;
	}
	public CommandThread getC() {
		return c;
	}
	public void setC(CommandThread c) {
		this.c = c;
	}
	public boolean isIstimeout() {
		return istimeout;
	}
	public void setIstimeout(boolean istimeout) {
		this.istimeout = istimeout;
	}
}