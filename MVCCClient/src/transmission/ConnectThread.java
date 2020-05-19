package transmission;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import gui.GUI;

/*
 * This class is used for connecting to server
 * It is a thread which will be created upon clicking confirm button on Welcome frame
 * */

public class ConnectThread extends Thread {
	private Object isconnected;
	private JFrame frame;
	private JTextField textIP;
	private JTextField textPort;
	public ConnectThread(Object isconnected,JFrame frame,JTextField textIP,JTextField textPort) {
		// TODO Auto-generated constructor stub
		this.isconnected=isconnected;
		this.frame=frame;
		this.textIP=textIP;
		this.textPort=textPort;
	}
	public void run() {

		synchronized (isconnected) {
			//check input validity
			Pattern pattern1=Pattern.compile("([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}");
			Pattern pattern2=Pattern.compile("[0-9]*");
			if(textIP==null||textPort==null||textIP.getText().trim().equals("")||textPort.getText().trim().equals("")||!pattern1.matcher(textIP.getText().trim()).matches()||!pattern2.matcher(textPort.getText().trim()).matches()||Integer.parseInt(textPort.getText().trim())>65535)
			{
				JOptionPane.showMessageDialog(frame, "Input Invalid!","Error", JOptionPane.ERROR_MESSAGE);
				textIP.setEnabled(true);
				textPort.setEnabled(true);
				isconnected.notifyAll();
				return;
			}
		try {	
				//Socket client=new Socket(textIP.getText().trim(), Integer.parseInt(textPort.getText().trim()));
				//connect to server
				Socket client=new Socket();
				client.connect(new InetSocketAddress(textIP.getText().trim(), Integer.parseInt(textPort.getText().trim())), 5000);
				GUI window = new GUI(client,frame);
				//connect successfully
				window.getFrame().setVisible(true);
				frame.setVisible(false);
			} 
		catch (Exception e) {
				// TODO Auto-generated catch block
				//connection failed
				JOptionPane.showMessageDialog(frame, "Connection Failed! ("+e.getMessage()+").","Error", JOptionPane.ERROR_MESSAGE);
			}
		finally {
			textIP.setEnabled(true);
			textPort.setEnabled(true);
			isconnected.notifyAll();
			}
		
		}
	}
	public Object getIsconnected() {
		return isconnected;
	}
	public void setIsconnected(Object isconnected) {
		this.isconnected = isconnected;
	}
	public JFrame getFrame() {
		return frame;
	}
	public void setFrame(JFrame frame) {
		this.frame = frame;
	}
	public JTextField getTextIP() {
		return textIP;
	}
	public void setTextIP(JTextField textIP) {
		this.textIP = textIP;
	}
	public JTextField getTextPort() {
		return textPort;
	}
	public void setTextPort(JTextField textPort) {
		this.textPort = textPort;
	}
}
