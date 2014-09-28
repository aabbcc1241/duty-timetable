package core;

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
	public static final float SCORE_CONTINUOUS = 2f;
	public static final float SCORE_HOUR_SD = -0.5f;

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

	public MIC_Life getNew() {
		MIC_Life newLife = new MIC_Life(N_GENE, L_GENE, this.mic, this.workers);
		newLife.setRandom();
		return newLife;
	}

	public void addNew() {
		MIC_Life newLife = MIC_Life.cx(lifes.get(0), getNew());
		newLife.benchmark();
		lifes.add(newLife);
	}

	public void addNew(int i) {
		MIC_Life newLife = MIC_Life.cx(lifes.get(i), getNew());
		newLife.benchmark();
		lifes.add(newLife);
	}

	public void addSome() {
		int I = Utils.random.nextInt(lifes.size()) * 2;
		for (int i = 0; i < I; i++) {
			addNew(i);
		}
	}

	public void removeSome() {
		int I = Utils.random.nextInt(lifes.size()) - 2;
		for (int i = 0; i < I; i++)
			lifes.remove(lifes.size() - 1);
	}

	public void grow() {
		PrintStream oriPrintStream = System.out;
		System.setOut(new PrintStream(display));
		lifes.clear();
		lifes.add(getNew());
		lifes.add(getNew());
		maxWorkerNameLength = 0;
		for (Worker worker : workers)
			maxWorkerNameLength = Math.max(maxWorkerNameLength, worker.name.length());
		maxWorkerNameLength += 5;
		for (int iGEN = 0; iGEN < N_GEN; iGEN++) {
			// addNew();
			addSome();
			// benchmark();
			sort();
			removeSome();
			report_grow(iGEN + 1);
			/** check if the loop should end **/
			if (avgFitness == lastAvgFitness)
				;// break;
			else
				N_GEN++;

			/** slow down for debug **/
			/*
			 * try { Thread.sleep(1000); } catch (InterruptedException e) {
			 * 
			 * e.printStackTrace(); }
			 */
		}
		System.out.println("\n\nFinished");
		System.setOut(oriPrintStream);
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
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
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
		// Collections.sort(lifes);
	}

	/** losers cx with random life who's better then it **/
	protected void cx() {
		/** new child is iLife, parents are iLife and i **/
		List<MIC_Life> lifepool = new ArrayList<MIC_Life>();
		lifepool.add((MIC_Life) lifes.get(0).clone());
		for (int iLife = 1; iLife < N_POP * P_SURVIVE; iLife++) {
			lifepool.add(lifes.get(iLife));
		}
		lifes.clear();
		MIC_Life life1, life2;
		while (lifes.size() < N_POP) {
			/*
			 * do { life1 = Utils.random.nextInt(lifepool.size()); life2 =
			 * Utils.random.nextInt(lifepool.size()); // System.out.println();
			 * // System.out.println(lifepool.size()); //
			 * System.out.println(life1); // System.out.println(life2); } while
			 * (MIC_Life.equals(lifepool.get(life1), lifepool.get(life2)));
			 */
			// System.out.println("!!");
			// } while (life1==life2);
			life1 = lifepool.get(Utils.random.nextInt(lifepool.size()));
			life2 = new MIC_Life(life1.genes.length, life1.genes[0].codes.length, mic,
					workers);
			life2.setRandom();
			// MIC_Life newLife = MIC_Life.cx(lifepool.get(life1),
			// lifepool.get(life2));
			MIC_Life newLife = MIC_Life.cx(life1, life2);
			// newLife.mutate(1);
			// System.out.println(MIC_Life.equals(newLife,
			// lifepool.get(life1)));
			lifes.add(newLife);
		}
	}

	protected void mutate() {
		for (int iLife = 1; iLife < N_POP; iLife++) {
			if (Utils.random.nextFloat() < P_MUTATION)
				lifes.get(iLife).mutate(A_MUTATION);
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

	public void report_grow(int iGEN) {
		// calcStat();
		/** prepare to display **/
		String msg;
		int width = maxWorkerNameLength + 5;
		display.clearBuffer();
		Calendar now = Calendar.getInstance();
		java.util.Date date = now.getTime();
		display.writeBuffer(date.toString());
		msg = String.format("\n%s%5s | %s%5s | %s%5s | %s%5s", "Generation: ", iGEN, "Best: ",
				lifes.get(0).fitness, "hourSD: ",lifes.get(0).hoursSd,"N_POP.: ", lifes.size());

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
