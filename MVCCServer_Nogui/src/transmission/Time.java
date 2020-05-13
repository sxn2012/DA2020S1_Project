package transmission;
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
			while(Server.isFlag()) {
				SimpleDateFormat sdf=new SimpleDateFormat();
				sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
				label.setText(sdf.format(new Date()));
				if(Server.isFlag())
					Thread.sleep(1000);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
