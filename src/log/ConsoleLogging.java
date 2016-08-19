package log;

public class ConsoleLogging implements Logable{
	private static ConsoleLogging instance = null;

	public void log(String mes) {
		System.out.println(mes);
	}
	
	public static ConsoleLogging getInstace() {
		if (instance == null) {
			instance = new ConsoleLogging();
		}
		return instance;
	}
	
	
}
