package ga;

public class GA {
	public static int NPOP = 32;
	public static float PMUTATION = 0.1f;
	public static float PCX = 0.25f;

	public int NGEN;
	public int NGENE;
	public int LGENE;
	public Life[] lifes;// [num of animal][num of gene]

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

	public void setRandom() {
		for (Life life : lifes)
			life.setRandom();
	}

	public void start() {
		setRandom();
	}

}
