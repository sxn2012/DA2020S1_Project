/*
 * Author: Xinnan SHEN
 * Email: xinnan.shen@student.unimelb.edu.au
 * Date: 23/04/2020
 * 
 */

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Login first");
		TCPThread thread=new TCPThread("127.0.0.1", 7777);
		thread.start();
		}
		
	

}
