package core.dutytable.mic;

import java.util.ArrayList;

import core.dutytable.worker.Worker;

public class MIC implements Cloneable {
	public Day[] days;

	/** constructor **/
	public MIC() {
		days = new Day[5];
		for (int dayOfWeek = 1; dayOfWeek <= 5; dayOfWeek++) {
			days[dayOfWeek - 1] = new Day(dayOfWeek);
		}
	}

	/** implement **/
	@Override
	public Object clone() {
		MIC result = new MIC();
		for (int dayOfWeek = 1; dayOfWeek <= 5; dayOfWeek++) {
			result.days[dayOfWeek - 1] = (Day) days[dayOfWeek - 1].clone();
		}
		return result;
	}

	/** instance methods **/
	private void clearPossibleWorkers() {
		for (Day day : days)
			for (Timeslot timeslot : day.timeslots)
				// timeslot.possibleWorkers.clear();
				timeslot.possibleWorkers = new ArrayList<Worker>();
	}

	public void findPossibleWorkers(Worker[] workers) {
		int status;
		clearPossibleWorkers();
		for (int iWorker = 0; iWorker < workers.length; iWorker++)
			for (int iDay = 0; iDay < workers[iWorker].days.length; iDay++)
				for (int iTimeslot = 0; iTimeslot < workers[iWorker].days[iDay].timeslot.length; iTimeslot++) {
					status = workers[iWorker].days[iDay].timeslot[iTimeslot].status;
					if ((status == 1) || (status == 10) || (status == 2)
							|| (status == 20))
						days[iDay].timeslots[iTimeslot].possibleWorkers
								.add(workers[iWorker]);
				}
	}

}
