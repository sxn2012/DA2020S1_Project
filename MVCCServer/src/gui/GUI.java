package gui;
/*
 * Author: Xinnan SHEN
 * 
 * Date: 05/05/2020
 * 
 */
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JFormattedTextField;
import javax.swing.JTextPane;



import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
//Main GUI of the system
//shows the status of system
public class GUI {

	private JFrame frame;
	private JTextArea textArea;
	private int num_logs;
	private JFrame welcomeframe;
	
	/**
	 * Create the application.
	 */
	public GUI(JFrame welcomeframe) {
		
		initialize();
		num_logs=0;
		this.welcomeframe=welcomeframe;
		
	}
	//add content into the GUI
	public synchronized void setContent(String str) {
		SimpleDateFormat sdf=new SimpleDateFormat();
		sdf.applyPattern("dd-MM-yyyy HH:mm:ss");
		if(textArea.getText().equals(""))
			textArea.setText(sdf.format(new Date())+"\t"+str);
		else
			textArea.setText(textArea.getText()+"\n\r"+sdf.format(new Date())+"\t"+str);
		if(num_logs>=500)
		{
			textArea.setText("");
			num_logs=0;
			setContent("Screen has been cleared.");
		}
		textArea.paintImmediately(textArea.getBounds());
		frame.setExtendedState(Frame.NORMAL);
		num_logs++;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("MVCC Server");
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int response=JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION);
				if(response==JOptionPane.NO_OPTION)
					return;
				else if(response==JOptionPane.YES_OPTION)
				{
					super.windowClosing(e);
					frame.setVisible(false);
					System.exit(0);
					
					
				}
				
			}
		});
		frame.setBounds(100, 100, 600, 600);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		
		
		JScrollPane scrollPane=new JScrollPane(textArea);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);  
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);  
		scrollPane.setBounds(12, 24, 563, 527);
		frame.getContentPane().add(scrollPane);
		
		JLabel lblSystemOutput = new JLabel("System Output:");
		lblSystemOutput.setBounds(12, 0, 125, 18);
		frame.getContentPane().add(lblSystemOutput);
		
		JLabel lblTimeLabel = new JLabel();
		lblTimeLabel.setBounds(285, 0, 142, 18);
		frame.getContentPane().add(lblTimeLabel);
		Server.getthreadpool().execute(new Thread(new Time(lblTimeLabel)));
		
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	public JTextArea getTextArea() {
		return textArea;
	}

	public void setTextArea(JTextArea textArea) {
		this.textArea = textArea;
	}

	public int getNum_logs() {
		return num_logs;
	}

	public void setNum_logs(int num_logs) {
		this.num_logs = num_logs;
	}

	public JFrame getWelcomeframe() {
		return welcomeframe;
	}

	public void setWelcomeframe(JFrame welcomeframe) {
		this.welcomeframe = welcomeframe;
	}
}
