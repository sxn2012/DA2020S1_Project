/*
 * Author: Xinnan SHEN
 * Email: xinnan.shen@student.unimelb.edu.au
 * Date: 05/05/2020
 * 
 */
import java.awt.EventQueue;
import java.awt.TextArea;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JFormattedTextField;
import javax.swing.JTextPane;



import javax.swing.JLabel;
import javax.swing.JScrollPane;

public class GUI {

	JFrame frame;
	JTextArea textArea;
	
	

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}
	
	public void setContent(String str) {
		SimpleDateFormat sdf=new SimpleDateFormat();
		sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
		if(textArea.getText().equals(""))
			textArea.setText(sdf.format(new Date())+"\t"+str);
		else
			textArea.setText(textArea.getText()+"\n\r"+sdf.format(new Date())+"\t"+str);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 500, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		//textArea.setBounds(12, 24, 476, 238);
		//frame.getContentPane().add(textArea);
		
		JScrollPane scrollPane=new JScrollPane(textArea);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);  
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);  
		scrollPane.setBounds(12, 24, 476, 238);
		frame.getContentPane().add(scrollPane);
		
		JLabel lblSystemOutput = new JLabel("System Output:");
		lblSystemOutput.setBounds(12, 0, 125, 18);
		frame.getContentPane().add(lblSystemOutput);
		
		JLabel lblTimeLabel = new JLabel();
		lblTimeLabel.setBounds(285, 0, 142, 18);
		frame.getContentPane().add(lblTimeLabel);
		new Thread(new Time(lblTimeLabel)).start();
		
	}
}
