package core.ga;

import java.awt.EventQueue;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.swing.SwingUtilities;

import core.dutytable.MIC;
import core.dutytable.Worker;
import core.gui.DutyTimeTable_GUI;
import core.gui.TableFrame;
import myutils.Display;
import myutils.StringUtils;
import myutils.Utils;

public class MIC_GA implements Runnable {
	public static int N_GEN = 1000;
	public static int N_POP = 100;
	public static float P_MUTATION = 0.02f;
	public static float A_MUTATION = 0.1f;
	public static float P_SURVIVE = 0.25f;

	public static final float SCORE_EMPTY = -500f;
	public static final float SCORE_HAS_LESSON = -1000f;
	public static final float SCORE_WANTED = 2f;
	public static final float SCORE_AVAILABLE = -0.5f;
	public static final float SCORE_CONTINUOUS = 5f;
	public static final float SCORE_HOUR_SD = 0f;

	protected int N_GENE;
	protected int L_GENE;
	/** [num of animal][num of gene] **/
	protected List<MIC_Life> lifes;

	private Worker[] workers;
	private MIC mic;
	private int maxWorkerNameLength;

	private Display display;
	private TableFrame tableFrame;

	public float avgFitness = Float.MIN_VALUE;
	public float sdFitness = Float.MAX_VALUE;
	private float lastAvgFitness;
	private float sumFitness;

	public Thread thread;
	private boolean shouldStop = false;
	public String mode;

	/** contrucstor **/
	public MIC_GA(MIC mic, Worker[] workers, Display display, TableFrame tableFrame) {
		N_GENE = mic.days.length;
		L_GENE = mic.days[0].timeslot.length;
		this.display = display;
		this.tableFrame = tableFrame;
		this.mic = mic;
		this.workers = workers;
		lifes = new ArrayList<MIC_Life>();
		for (int iLife = 0; iLife < N_POP; iLife++)
			lifes.add(new MIC_Life(N_GENE, L_GENE, this.mic, this.workers));
		mode = "";
	}

	/** static method **/

	/** instance method **/
	/** common method **/
	private void setRandom() {
		for (MIC_Life life : lifes)
			life.setRandom();
	}

	private void benchmark() {
		for (MIC_Life life : lifes)
			life.benchmark();
	}

	private void addNew(int i) {
		MIC_Life newLife = MIC_Life.cx(lifes.get(i), getNew());
		newLife.benchmark();
		lifes.add(newLife);
	}

	private void sort() {
		Collections.sort(lifes, Collections.reverseOrder());
		// Collections.sort(lifes);
	}

	private void calcStat() {
		lastAvgFitness = avgFitness;
		sumFitness = 0;
		for (MIC_Life life : lifes)
			sumFitness += life.fitness;
		avgFitness = sumFitness / lifes.size();
		sdFitness = 0;
		for (MIC_Life life : lifes)
			sdFitness += Math.pow(life.fitness - avgFitness, 2);
	}

	private void report(int iGEN) {		
		calcStat();
		saveToMic();
		/** display **/
		tableFrame.update(mic);		
	}

	private void report_old(int iGEN) {
		calcStat();
		/** prepare to display **/
		String msg;
		int width = maxWorkerNameLength + 5;
		display.clearBuffer();
		Calendar now = Calendar.getInstance();
		java.util.Date date = now.getTime();
		display.writeBuffer(date.toString());
		msg = String.format("\n %s%5s | %s%5s \n %s%5s | %s%5s \n %s%5s | %s%5s",
				"Population: ", lifes.size(), "Generation: ", iGEN, "Avg.: ", avgFitness,
				"SD.: ", sdFitness, "Best: ", lifes.get(0).fitness, "hourSD: ",
				lifes.get(0).hoursSd / 2);
		display.writeBuffer(msg + "\n\n");
		for (MIC.Day day : mic.days) {
			msg = StringUtils.center("Day-" + day.dayOfWeek, width);
			display.writeBuffer(msg + " | ");
		}
		for (int iTimeslot = 0; iTimeslot < mic.days[0].timeslot.length; iTimeslot++) {
			display.writeBuffer("\n");
			for (int iDay = 0; iDay < mic.days.length; iDay++) {
				int workerId = lifes.get(0).genes[iDay].codes[iTimeslot];
				if (workerId != -1)
					msg = mic.days[iDay].timeslot[iTimeslot].possibleWorkers
							.get(workerId).name;
				else
					msg = "";
				msg = StringUtils.center(msg, width);
				display.writeBuffer(msg + " | ");
			}
		}
		/** display **/
		display.checkUpdateBuffer();
	}

	private void saveToMic() {
		for (int iTimeslot = 0; iTimeslot < mic.days[0].timeslot.length; iTimeslot++) {			
			for (int iDay = 0; iDay < mic.days.length; iDay++) {
				int workerId = lifes.get(0).genes[iDay].codes[iTimeslot];
				if (workerId != -1) {
					workerId = mic.days[iDay].timeslot[iTimeslot].possibleWorkers
							.get(workerId).id;
					mic.days[iDay].timeslot[iTimeslot].worker = workers[workerId];
				} else {
					mic.days[iDay].timeslot[iTimeslot].worker = null;
				}
			}
		}
	}

	/** grow method **/
	private MIC_Life getNew() {
		MIC_Life newLife = new MIC_Life(N_GENE, L_GENE, this.mic, this.workers);
		newLife.setRandom();
		return newLife;
	}

	private void addSome() {
		int I = Utils.random.nextInt(lifes.size()) * 2;
		for (int i = 0; i < I; i++) {
			addNew(i);
		}
	}

	private void removeSome() {
		int I = Utils.random.nextInt(lifes.size()) - 2;
		for (int i = 0; i < I; i++)
			lifes.remove(lifes.size() - 1);
	}

	private void start_grow() {
		display.writeln("grow...");
		lifes.clear();
		lifes.add(getNew());
		lifes.add(getNew());
		maxWorkerNameLength = 0;
		for (Worker worker : workers)
			maxWorkerNameLength = Math.max(maxWorkerNameLength, worker.name.length());
		maxWorkerNameLength += 5;
		for (int iGEN = 0; (iGEN < N_GEN) && !shouldStop; iGEN++) {
			// addNew();
			addSome();
			// benchmark();
			sort();
			removeSome();
			report(iGEN + 1);
			/** check if the loop should end **/
			if ((avgFitness != lastAvgFitness) || (lifes.size() <= 16))
				N_GEN++;
			/** slow down for debug **/
			Utils.sleep(1000);
		}
		display.writeln("\nFinished!!!");
	}

	/** cx method **/
	private void start_cx() {
		display.writeln("cx...");
		setRandom();
		maxWorkerNameLength = 0;
		for (Worker worker : workers)
			maxWorkerNameLength = Math.max(maxWorkerNameLength, worker.name.length());
		maxWorkerNameLength += 5;
		for (int iGEN = 0; (iGEN < N_GEN) && !shouldStop; iGEN++) {
			benchmark();
			sort();
			refresh();
			sort();
			report(iGEN + 1);
			/** check if the loop should end **/
			if (avgFitness != lastAvgFitness)
				N_GEN++;
			cx();
			mutate();
			/** slow down for debug **/
			Utils.sleep(1000);
		}
		display.writeln("\nFinished!!!");
	}

	/** losers cx with random life who's better then it **/
	private void cx() {
		/** new child is iLife, parents are iLife and i **/
		List<MIC_Life> lifepool = new ArrayList<MIC_Life>();
		lifepool.add((MIC_Life) lifes.get(0).clone());
		for (int iLife = 1; iLife < N_POP * P_SURVIVE; iLife++) {
			lifepool.add(lifes.get(iLife));
		}
		lifes.clear();
		MIC_Life life1, life2;
		while (lifes.size() < N_POP) {
			life1 = lifepool.get(Utils.random.nextInt(lifepool.size()));
			life2 = new MIC_Life(life1.genes.length, life1.genes[0].codes.length, mic,
					workers);
			life2.setRandom();
			MIC_Life newLife = MIC_Life.cx(life1, life2);
			lifes.add(newLife);
		}
	}

	private void mutate() {
		for (int iLife = 1; iLife < N_POP; iLife++) {
			if (Utils.random.nextFloat() < P_MUTATION)
				lifes.get(iLife).mutate(A_MUTATION);
		}
	}

	private void refresh() {
		for (int i = 1; i < lifes.size(); i++)
			for (int j = i + 1; j < lifes.size(); j++)
				if (MIC_Life.equals(lifes.get(i), lifes.get(j))) {
					MIC_Life newLife = MIC_Life.cx(lifes.get(j), getNew());
					newLife.benchmark();
					lifes.set(j, newLife);
				}
	}

	public void start() {
		if (thread == null)
			thread = new Thread(this);
		thread.start();
	}

	public void start(String mode) {
		display.writeln("Generating...");
		tableFrame.reInit(mic);
		tableFrame.show();
		this.mode = mode;
		start();
	}

	@Override
	public void run() {
		do {
			switch (mode) {
			case "cx":
				mode="";
				start_cx();
				finish();
				break;
			case "grow":
				mode="";
				start_grow();
				finish();
				break;
			default:
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} while (true);
	}

	public void stop() {
		shouldStop = true;
		tableFrame.end();
	}

	private void finish() {
		saveToMic();
		tableFrame.hide();
		display.writeln("Finished");
	}
}
