package ga;

import myutils.Utils;

public class GA {
	public static int NPOP = 32;
	public static float PMUTATION = 0.1f;
	public static float PCX = 0.25f;

	protected int NGEN;
	protected int NGENE;
	protected int LGENE;
	protected Life[] lifes;// [num of animal][num of gene]

	/* contrucstor */
	public GA(int nGEN, int nGENE, int lGENE) {
		super();
		NGEN = nGEN;
		NGENE = nGENE;
		LGENE = lGENE;
		lifes = new Life[NPOP];
		for (Life life : lifes) {
			life = new Life(NGENE, LGENE);
		}
	}

	/* static method */
	public static Life cx(Life life1, Life life2) {
		Life result = (Life) life1.clone();
		life1.cx(life2);
		return result;
	}

	/* instance method */
	protected void setRandom() {
		for (Life life : lifes)
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
		for (Life life : lifes)
			life.benchmark();
	}

	// losers cx with random life who's better then it
	protected void cx() {
		// new child is iLife, parents are iLife and i
		int i, j;
		for (int iLife = 0; iLife < lifes.length; iLife++) {
			if (iLife > lifes.length * PCX) {
				i = Utils.random.nextInt(iLife);
				lifes[iLife] = cx(lifes[iLife], lifes[i]);
			}
		}
	}

	protected void mutation() {
		for (Life life : lifes)
			if (Utils.random.nextFloat() < PMUTATION)
				life.mutate();
	}

}
