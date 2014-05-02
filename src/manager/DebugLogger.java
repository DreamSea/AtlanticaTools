package manager;

import java.time.LocalDate;
import java.time.LocalTime;

public class DebugLogger {
	public static void log(String s, boolean debug)
	{
		if (debug)
		{
			System.out.println("["+LocalDate.now()+" "+LocalTime.now().toString()+"] "+s);
		}
	}
}
