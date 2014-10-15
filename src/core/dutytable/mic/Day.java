package core.dutytable.mic;

public class Day implements Cloneable {
	public int dayOfWeek;
	public Timeslot[] timeslots = new Timeslot[17];

	public Day(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
		for (int iTimeslot = 0; iTimeslot < timeslots.length; iTimeslot++) {
			timeslots[iTimeslot] = new Timeslot();
		}
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		Day result = new Day(dayOfWeek);
		for (int iTimeslot = 0; iTimeslot < timeslots.length; iTimeslot++) {
			result.timeslots[iTimeslot] = (Timeslot) timeslots[iTimeslot].clone();
		}
		return result;
	}
}
