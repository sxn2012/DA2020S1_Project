import javax.swing.JButton;

public class BtnTextChange extends Thread {
	Object isconnected;
	JButton btnConfirm;
	public BtnTextChange(Object isconnected,JButton btnConfirm) {
	// TODO Auto-generated constructor stub
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
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
	}
}
