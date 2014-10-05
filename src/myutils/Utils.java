package myutils;

import java.util.Random;

public class Utils {
	public static Random random = new Random(System.currentTimeMillis());

	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}
}
