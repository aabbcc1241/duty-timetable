package core.dutytable.worker;

public class Day {
	public int dayOfWeek;
	public Timeslot[] timeslot = new Timeslot[17];

	public Day(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
		for (int iTimeslot = 0; iTimeslot < timeslot.length; iTimeslot++) {
			timeslot[iTimeslot] = new Timeslot();
		}
	}
}