package core.dutytable.mic;

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
	protected Object clone() throws CloneNotSupportedException {
		MIC result = new MIC();

		return result;
	}

	/** instance methods **/
	private void clearPossibleWorkers() {
		for (Day day : days)
			for (Timeslot timeslot : day.timeslot)
				timeslot.possibleWorkers.clear();
	}

	public void findPossibleWorkers(Worker[] workers) {
		int status;
		clearPossibleWorkers();
		for (int iWorker = 0; iWorker < workers.length; iWorker++)
			for (int iDay = 0; iDay < workers[iWorker].days.length; iDay++)
				for (int iTimeslot = 0; iTimeslot < workers[iWorker].days[iDay].timeslot.length; iTimeslot++) {
					status = workers[iWorker].days[iDay].timeslot[iTimeslot].status;
					if ((status == 1) || (status == 10) || (status == 2) || (status == 20))
						days[iDay].timeslot[iTimeslot].possibleWorkers.add(workers[iWorker]);
				}
	}

}
