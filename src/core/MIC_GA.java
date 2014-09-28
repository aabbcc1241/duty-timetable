package core;

import ga.Life;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import myutils.Display;
import myutils.StringUtils;
import myutils.Utils;

public class MIC_GA {
	public static int N_GEN = 1000;
	public static int N_POP = 100;
	public static float P_MUTATION = 0.02f;
	public static float A_MUTATION = 0.1f;
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

	protected Display display;
	public float avgFitness = Float.MIN_VALUE;
	public float sdFitness = Float.MAX_VALUE;
	protected float lastAvgFitness;
	protected float sumFitness;

	/** contrucstor **/
	public MIC_GA(MIC mic, Worker[] workers, Display display) {
		N_GENE = mic.days.length;
		L_GENE = mic.days[0].timeslot.length;
		this.display = display;
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
			/** check if the loop should end **/
			if (avgFitness == lastAvgFitness)
				;// break;
			else
				N_GEN++;
			cx();
			mutate();
			/** slow down for debug **/
			/*
			 * { try { Thread.sleep(1000); } catch (InterruptedException e) { //
			 * TODO Auto-generated catch block e.printStackTrace(); } }
			 */
		}
		System.out.println("\n\nFinished");
		System.setOut(oriPrintStream);
	}

	protected void benchmark() {
		for (MIC_Life life : lifes)
			life.benchmark();
	}

	protected void sort() {
		Collections.sort(lifes, Collections.reverseOrder());
	}

	/** losers cx with random life who's better then it **/
	protected void cx() {
		/** new child is iLife, parents are iLife and i **/
		List<MIC_Life> newLifes = new ArrayList<MIC_Life>();
		newLifes.add((MIC_Life) lifes.get(0).clone());
		for (int iLife = 1; iLife < N_POP; iLife++) {
			if (iLife < N_POP * P_SURVIVE) {
				newLifes.add((MIC_Life) lifes.get(iLife).clone());
			}
		}
		while (newLifes.size() < lifes.size()) {
			MIC_Life newLife = MIC_Life.cx(
			// newLifes.get(Utils.random.nextInt(newLifes.size())),
					lifes.get(Utils.random.nextInt(lifes.size())),
					// newLifes.get(Utils.random.nextInt(newLifes.size())));
					lifes.get(Utils.random.nextInt(lifes.size())));
			newLifes.add(newLife);
		}
		lifes.clear();
		for(MIC_Life life:newLifes)
			lifes.add((MIC_Life) life.clone());		
	}

	protected void mutate() {
		/*
		 * for (MIC_Life life : lifes) if (Utils.random.nextFloat() <
		 * P_MUTATION) life.mutate();
		 */
		for (int iLife = 1; iLife < N_POP; iLife++) {
			lifes.get(iLife).mutate();
		}
	}

	protected void calcStat() {
		lastAvgFitness = avgFitness;
		sumFitness = 0;
		for (MIC_Life life : lifes)
			sumFitness += life.fitness;
		avgFitness = sumFitness / N_POP;
		sdFitness = 0;
		for (MIC_Life life : lifes)
			sdFitness += Math.pow(life.fitness - avgFitness, 2);
	}

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
		// display.updateBuffer();
	}

}
