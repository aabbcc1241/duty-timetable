package core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import myutils.Utils;

public class MIC_GA {
	public static int NPOP = 32;
	public static float PMUTATION = 0.1f;
	public static float AMUTATION = 0.1f;
	public static float PCX = 0.25f;

	public static final float PUNISH_HAS_LESSON = -5f;
	public static final float BONUS_WANTED = 1f;
	public static final float BONUS_AVAILABLE = 0.5f;
	public static final float BONUS_CONTINUOUS = 0.25f;

	protected int NGEN;
	protected int NGENE;
	protected int LGENE;
	/** [num of animal][num of gene] **/
	protected List<MIC_Life> lifes;

	private Worker[] workers;
	private MIC mic;

	/** contrucstor **/
	public MIC_GA(MIC mic, Worker[] workers) {
		this(50, mic.days.length, mic.days[0].timeslot.length);
		this.mic = mic;
		this.workers = workers;

		for (int iLife = 0; iLife < NPOP; iLife++)
			lifes.add(new MIC_Life(NGENE, LGENE, this.mic, this.workers));
	}

	private MIC_GA(int nGEN, int nGENE, int lGENE) {
		super();
		NGEN = nGEN;
		NGENE = nGENE;
		LGENE = lGENE;
		lifes = new ArrayList<MIC_Life>();
	}

	/** static method **/
	public static MIC_Life cx(MIC_Life life1, MIC_Life life2) {
		MIC_Life result = (MIC_Life) life1.clone();
		life1.cx(life2);
		return result;
	}

	/** instance method **/
	protected void setRandom() {
		for (MIC_Life life : lifes)
			life.setRandom();
	}

	public void start() {
		setRandom();
		for (int iGEN = 0; iGEN < NGEN; iGEN++) {
			benchmark();
			sort();
			cx();
			mutation();
		}
	}

	protected void benchmark() {
		for (MIC_Life life : lifes)
			life.benchmark();
	}

	protected void sort() {
		Collections.sort(lifes);
	}

	/** losers cx with random life who's better then it **/
	protected void cx() {
		/** new child is iLife, parents are iLife and i **/
		int i;
		for (int iLife = 0; iLife < NPOP; iLife++) {
			if (iLife > NPOP * PCX) {
				i = Utils.random.nextInt(iLife);
				lifes.set(iLife, cx(lifes.get(iLife), lifes.get(i)));
			}
		}
	}

	protected void mutation() {
		for (MIC_Life life : lifes)
			if (Utils.random.nextFloat() < PMUTATION)
				life.mutate();
	}

}
