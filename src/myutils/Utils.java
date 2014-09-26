package myutils;

import java.util.Random;

public class Utils {
	public static Random random = new Random(System.currentTimeMillis());

	public static void println(int i) {
		while (i-->0)
			System.out.println();
	}
}
