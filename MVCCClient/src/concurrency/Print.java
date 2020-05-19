package concurrency;
/*
 * Author: Xinnan SHEN
 * 
 * Date: 09/05/2020
 * 
 */
public class Print {
	private Print() {
		
	}
	public static synchronized void println(String str) {
		System.out.println(str);
	}
}
