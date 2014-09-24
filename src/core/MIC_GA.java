package core;

import ga.GA;

public class MIC_GA extends GA {
	private static final int NGEN = 50;
	private final int NGENE;
	private final int LGENE;

	private Worker[] workers;
	private MIC mic;

	public MIC_GA(MIC mic, Worker[] workers) {
		super(NGEN, mic.days.length, mic.days[0].timeslot.length);
		this.mic = mic;
		this.workers = workers;
		NGENE = mic.days.length;
		LGENE = mic.days[0].timeslot.length;
	}

	public void start() {
	}

}
