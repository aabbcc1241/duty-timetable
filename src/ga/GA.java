package ga;

public class GA {
	public static int NPOP = 32;
	public static float PMUTATION = 0.1f;
	public static float PCX = 0.25f;

	protected int NGEN;
	protected int NGENE;
	protected int LGENE;
	protected Life[] lifes;// [num of animal][num of gene]

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

	protected void cx() {

	}

	protected void mutation() {
		for (Life life : lifes)
			life.mutation();
	}

}
