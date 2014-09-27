package core;

public class Worker {
	int id;
	String name;
	Day[] days;

	class Day {
		int dayOfWeek;
		Timeslot[] timeslot = new Timeslot[17];

		class Timeslot {
			public int status;

			/*
			 * 0 for lessons 1 for wanted 2 for available 10 for duty-wanted 20
			 * for duty-available
			 */

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

	public Worker(int id) {
		this(id, "no_name");
	}

	public Worker(int id, String name) {
		this.id = id;
		this.name = name;
		days = new Day[5];
		for (int dayOfWeek = 1; dayOfWeek <= days.length; dayOfWeek++) {
			days[dayOfWeek - 1] = new Day(dayOfWeek);
		}
	}
}
