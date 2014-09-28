package core;

import java.util.ArrayList;
import java.util.List;

import core.MIC.Day.Timeslot;

public class MIC {
	Day[] days;

	class Day {
		int dayOfWeek;
		Timeslot[] timeslot = new Timeslot[17];

		class Timeslot {
			public Worker worker;
			public List<Worker> possibleWorkers;

			public Timeslot() {
				worker = null;
				possibleWorkers = new ArrayList<Worker>();
			}
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

	private void clearPossibleWorkers() {
		for (Day day : days)
			for (Timeslot timeslot : day.timeslot)
				timeslot.possibleWorkers.clear();
	}

	public void findPossibleWorkers(Worker[] workers) {
		clearPossibleWorkers();
		for (int iWorker = 0; iWorker < workers.length; iWorker++)
			for (int iDay = 0; iDay < workers[iWorker].days.length; iDay++)
				for (int iTimeslot = 0; iTimeslot < workers[iWorker].days[iDay].timeslot.length; iTimeslot++)
					if ((workers[iWorker].days[iDay].timeslot[iTimeslot].status == 1)
							||(workers[iWorker].days[iDay].timeslot[iTimeslot].status == 10)
							|| (workers[iWorker].days[iDay].timeslot[iTimeslot].status == 2)
							||(workers[iWorker].days[iDay].timeslot[iTimeslot].status == 20))
						days[iDay].timeslot[iTimeslot].possibleWorkers
								.add(workers[iWorker]);
	}

}
