package core;

import ga.GA;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import myutils.Display;
import myutils.StringUtils;
import myutils.Utils;

public class MIC_GA extends GA {
	public static int N_GEN = 1000;
	public static int N_POP = 100;
	public static float P_MUTATION = 0.99f;
	public static float A_MUTATION = 0.5f;
	public static float P_SURVIVE = 0.25f;

	public static final float SCORE_HAS_LESSON = -1000f;
	public static final float SCORE_WANTED = 1f;
	public static final float SCORE_AVAILABLE = -0.5f;
	public static final float SCORE_CONTINUOUS = 0.5f;

	protected int N_GENE;
	protected int L_GENE;
	/** [num of animal][num of gene] **/
	protected List<MIC_Life> lifes;

	private Worker[] workers;
	private MIC mic;
	private int maxWorkerNameLength;

	private Display display;

	/** contrucstor **/
	public MIC_GA(MIC mic, Worker[] workers, Display display) {
		super(N_GEN, mic.days.length, mic.days[0].timeslot.length, display, false);
		this.mic = mic;
		this.workers = workers;
		lifes = new ArrayList<MIC_Life>();
		for (int iLife = 0; iLife < N_POP; iLife++)
			lifes.add(new MIC_Life(N_GENE, L_GENE, this.mic, this.workers));
	}

	/** static method **/

	/** instance method **/
	protected void setRandom() {
		for (MIC_Life life : lifes)
			life.setRandom();
	}

	@Override
	public void start() {
		PrintStream oriPrintStream = System.out;
		System.setOut(new PrintStream(display));
		setRandom();
		maxWorkerNameLength = 0;
		for (Worker worker : workers)
			maxWorkerNameLength = Math.max(maxWorkerNameLength, worker.name.length());
		maxWorkerNameLength += 5;
		for (int iGEN = 0; iGEN < N_GEN; iGEN++) {
			benchmark();
			sort();
			report(iGEN + 1);
			/*
			 * if (avgFitness == lastAvgFitness) // break; ; if
			 * (lifes.get(0).fitness > 0) //break; ; else
			 */
			N_GEN++;
			cx();
			mutation();
		}
		System.setOut(oriPrintStream);
	}

	@Override
	protected void benchmark() {
		for (MIC_Life life : lifes)
			life.benchmark();
	}

	@Override
	/** losers cx with random life who's better then it **/
	protected void cx() {
		/** new child is iLife, parents are iLife and i **/
		List<MIC_Life> newLifes = new ArrayList<MIC_Life>();
		for (int iLife = 0; iLife < N_POP; iLife++) {
			if (iLife < N_POP * P_SURVIVE) {
				// if(Utils.random.nextInt(iLife+1)==0){
				newLifes.add((MIC_Life) lifes.get(iLife).clone());
			}
		}
		while (newLifes.size() < lifes.size()) {
			MIC_Life newLife = MIC_Life.cx(
					newLifes.get(Utils.random.nextInt(newLifes.size())),
					newLifes.get(Utils.random.nextInt(newLifes.size())));
			newLifes.add(newLife);
		}
		lifes = newLifes;
	}

	@Override
	protected void mutation() {
		for (MIC_Life life : lifes)
			if (Utils.random.nextFloat() < P_MUTATION)
				life.mutate();
	}

	@Override
	public void report(int iGEN) {
		calcStat();
		/** prepare to display **/
		String msg;
		int width = maxWorkerNameLength + 5;
		display.clearBuffer();
		Calendar now = Calendar.getInstance();
		java.util.Date date = now.getTime();
		display.writeBuffer(date.toString());
		msg = String.format("\n%s%5s | %s%5s | %s%5s | %s%5s", "Generation: ", iGEN,
				"Best: ", lifes.get(0).fitness, "Avg.: ", avgFitness, "SD.: ", sdFitness);

		display.writeBuffer(msg + "\n");
		for (MIC.Day day : mic.days) {
			msg = StringUtils.center("Day-" + day.dayOfWeek, width);
			display.writeBuffer(msg + " | ");
		}
		for (int iTimeslot = 0; iTimeslot < mic.days[0].timeslot.length; iTimeslot++) {
			display.writeBuffer("\n");
			for (int iDay = 0; iDay < lifes.get(0).genes.length; iDay++) {
				int workerId = lifes.get(0).genes[iDay].codes[iTimeslot];
				msg = StringUtils.center(workers[workerId].name, width);
				display.writeBuffer(msg + " | ");
			}
		}

		/** display **/
		display.checkUpdateBuffer();
	}

}
