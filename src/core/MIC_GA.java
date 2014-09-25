package core;

import ga.Life;
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
	protected MIC_Life[] lifes;

	private Worker[] workers;
	private MIC mic;

	/** contrucstor **/
	public MIC_GA(MIC mic, Worker[] workers) {
		this(50, mic.days.length, mic.days[0].timeslot.length);
		this.mic = mic;
		this.workers = workers;
		for (MIC_Life life : lifes) {
			life = new MIC_Life(NGENE, LGENE, mic, workers);
		}
	}

	private MIC_GA(int nGEN, int nGENE, int lGENE) {
		super();
		NGEN = nGEN;
		NGENE = nGENE;
		LGENE = lGENE;
		lifes = new MIC_Life[NPOP];
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
			cx();
			mutation();
		}
	}

	protected void benchmark() {
		for (MIC_Life life : lifes)
			life.benchmark();
	}

	/** losers cx with random life who's better then it **/
	protected void cx() {
		/** new child is iLife, parents are iLife and i **/
		int i, j;
		for (int iLife = 0; iLife < lifes.length; iLife++) {
			if (iLife > lifes.length * PCX) {
				i = Utils.random.nextInt(iLife);
				lifes[iLife] = cx(lifes[iLife], lifes[i]);
			}
		}
	}

	protected void mutation() {
		for (MIC_Life life : lifes)
			if (Utils.random.nextFloat() < PMUTATION)
				life.mutate();
	}

}
