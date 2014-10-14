package core.dutytable.mic;

import java.util.ArrayList;
import java.util.List;

import core.dutytable.worker.Worker;

public class Timeslot {
	public Worker worker;
	public List<Worker> possibleWorkers;

	public Timeslot() {
		worker = null;
		possibleWorkers = new ArrayList<Worker>();
	}
}
