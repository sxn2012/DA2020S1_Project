
public class Print {
	private Print() {
		// TODO Auto-generated constructor stub
	}
	public static synchronized void println(String str) {
		System.out.println(str);
	}
	public static synchronized void print(String str) {
		System.out.print(str);
	}
}
