package gui;
/*
 * Author: Xinnan SHEN
 * 
 * Date: 07/05/2020
 * 
 * Usage: Show time on gui
 */
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JLabel;

public class Time implements Runnable{
	private JLabel label;
	public Time(JLabel label) {
		this.label=label;
	}
	public void run() {
		try {
			while(Server.getflag()) {
				SimpleDateFormat sdf=new SimpleDateFormat();
				sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
				label.setText(sdf.format(new Date()));
				if(Server.getflag())
					Thread.sleep(1000);
			}
		} catch (Exception e) {
			
			return;
		}
	}
	public JLabel getLabel() {
		return label;
	}
	public void setLabel(JLabel label) {
		this.label = label;
	}
}
