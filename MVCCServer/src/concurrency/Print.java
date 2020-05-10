package concurrency;
/*
 * Author: Xinnan SHEN
 * Email: xinnan.shen@student.unimelb.edu.au
 * Date: 10/05/2020
 * 
 */
public class Print {
	private Print() {
		// TODO Auto-generated constructor stub
	}
	public static synchronized void println(String str) {
		System.out.println(str);
	}
}
