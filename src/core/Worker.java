package core;

public class Worker {
	int weekNum;
	Day[] days;

	class Day {
		int dayOfWeek;
		Timeslot[] timeslot = new Timeslot[17];

		class Timeslot {
			public int status;

			public Timeslot() {
				status = 0;
			}
		}

		public Day(int dayOfWeek) {
			this.dayOfWeek = dayOfWeek;
			for (int iTimeslot = 0; iTimeslot < timeslot.length; iTimeslot++) {
				timeslot[iTimeslot] = new Timeslot();
			}
		}
	}

	public Worker() {
		days = new Day[5];
		for (int dayOfWeek = 1; dayOfWeek <= days.length; dayOfWeek++) {
			days[dayOfWeek - 1] = new Day(dayOfWeek);
		}
	}
}
