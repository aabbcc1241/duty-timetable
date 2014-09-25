package core;


public class MIC {
	int weekNum;
	Day[] days;

	class Day {
		int dayOfWeek;
		Timeslot[] timeslot = new Timeslot[17];

		class Timeslot {
			public Worker worker;
		}

		public Day(int dayOfWeek) {
			this.dayOfWeek = dayOfWeek;
			for (int iTimeslot = 0; iTimeslot < timeslot.length; iTimeslot++) {
				timeslot[iTimeslot] = new Timeslot();
			}
		}
	}

	public MIC() {
		days = new Day[5];
		for (int dayOfWeek = 1; dayOfWeek <= 5; dayOfWeek++) {
			days[dayOfWeek - 1] = new Day(dayOfWeek);
		}
	}
}
