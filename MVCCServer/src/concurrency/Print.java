package concurrency;
/*
 * Author: Xinnan SHEN
 * 
 * Date: 10/05/2020
 * 
 * Usage: Synchronized print in case of conflict
 */
public class Print {
	private Print() {
		
	}
	public static synchronized void println(String str) {
		System.out.println(str);
	}
}
