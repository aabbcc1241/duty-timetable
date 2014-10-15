package core.dutytable.mic;

import java.util.ArrayList;
import java.util.List;

import core.dutytable.worker.Worker;

public class Timeslot implements Cloneable {
	public Worker worker;
	public List<Worker> possibleWorkers;

	public Timeslot() {
		worker = null;
		possibleWorkers = new ArrayList<Worker>();
	}

	@Override
	protected Object clone() {
		Timeslot result = new Timeslot();
		result.worker = worker;
		result.possibleWorkers = possibleWorkers;
		return result;
	}
}
