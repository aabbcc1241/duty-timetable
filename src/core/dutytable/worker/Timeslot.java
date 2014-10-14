package core.dutytable.worker;

public class Timeslot {
	public int status;

	/*
	 * 0 for lessons 1 for wanted 2 for available 10 for duty-wanted 20 for
	 * duty-available
	 */

	public Timeslot() {
		status = 0;
	}
}
