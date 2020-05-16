package gui;
/*
 * Author: Xinnan SHEN
 * Email: xinnan.shen@student.unimelb.edu.au
 * Date: 07/05/2020
 * 
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
			// TODO: handle exception
			//e.printStackTrace();
			//System.out.println(e.getMessage());
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