package ga;

public class Life {
	public Gene[] genes;

	public Life(final int NGENE, final int LGENE) {
		genes = new Gene[NGENE];
		for (int iGENE = 0; iGENE < NGENE; iGENE++)
			genes[iGENE] = new Gene(LGENE);
	}

	public void setRandom() {
		for (Gene gene : genes)
			gene.setRandom();
	}
}
