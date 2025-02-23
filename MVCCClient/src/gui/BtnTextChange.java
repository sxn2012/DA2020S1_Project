package gui;
import javax.swing.JButton;
//This file is used for dealing with gui showing when connecting to server
public class BtnTextChange extends Thread {
	private Object isconnected;
	private JButton btnConfirm;
	public BtnTextChange(Object isconnected,JButton btnConfirm) {
	
		this.isconnected=isconnected;
		this.btnConfirm=btnConfirm;
	}
	public void run() {
		synchronized (isconnected) {
		try {
			btnConfirm.setEnabled(false);
			btnConfirm.setText("Connecting");
			isconnected.wait();
			btnConfirm.setText("Confirm");
			btnConfirm.setEnabled(true);
			} catch (Exception e) {
				
			}
		}
	}
	public Object getIsconnected() {
		return isconnected;
	}
	public void setIsconnected(Object isconnected) {
		this.isconnected = isconnected;
	}
	public JButton getBtnConfirm() {
		return btnConfirm;
	}
	public void setBtnConfirm(JButton btnConfirm) {
		this.btnConfirm = btnConfirm;
	}
}
