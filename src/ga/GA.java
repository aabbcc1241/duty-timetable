package ga;

public class GA {
	public static int NPOP = 32;
	public static float PMUTATION = 0.1f;
	public static float PCX = 0.25f;

	private int NGEN;
	private int NGENE;
	private int LGENE;
	private Life[] lifes;// [num of animal][num of gene]

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

	private void setRandom() {
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

	private void benchmark() {

	}

	private void cx() {

	}

	private void mutation() {
	}

}
