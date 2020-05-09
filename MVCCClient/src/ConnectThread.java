import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class ConnectThread extends Thread {
	Object isconnected;
	JFrame frame;
	JTextField textIP;
	JTextField textPort;
	public ConnectThread(Object isconnected,JFrame frame,JTextField textIP,JTextField textPort) {
		// TODO Auto-generated constructor stub
		this.isconnected=isconnected;
		this.frame=frame;
		this.textIP=textIP;
		this.textPort=textPort;
	}
	public void run() {
		synchronized (isconnected) {
			
			Pattern pattern1=Pattern.compile("([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}");
			Pattern pattern2=Pattern.compile("[0-9]*");
			if(textIP==null||textPort==null||textIP.getText().trim().equals("")||textPort.getText().trim().equals("")||!pattern1.matcher(textIP.getText().trim()).matches()||!pattern2.matcher(textPort.getText().trim()).matches()||Integer.parseInt(textPort.getText().trim())>65535)
			{
				JOptionPane.showMessageDialog(frame, "Input Invalid!","Error", JOptionPane.ERROR_MESSAGE);
				isconnected.notifyAll();
				return;
			}
		try {	
				//Socket client=new Socket(textIP.getText().trim(), Integer.parseInt(textPort.getText().trim()));
				Socket client=new Socket();
				client.connect(new InetSocketAddress(textIP.getText().trim(), Integer.parseInt(textPort.getText().trim())), 5000);
				GUI window = new GUI(client,frame);
				window.frame.setVisible(true);
				frame.setVisible(false);
			} 
		catch (Exception e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(frame, "Connection Failed! ("+e.getMessage()+").","Error", JOptionPane.ERROR_MESSAGE);
			}
		finally {
			isconnected.notifyAll();
			}
		
		}
	}
}
