package core;

public class ID {
	private static int lastId = -1;

	public static int getId() {
		return ++lastId;
	}
}
