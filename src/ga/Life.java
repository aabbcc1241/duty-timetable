package ga;

public class Life implements Cloneable {
	public Gene[] genes;
	public float fitness;

	public Life(final int NGENE, final int LGENE) {
		genes = new Gene[NGENE];
		for (int iGENE = 0; iGENE < NGENE; iGENE++)
			genes[iGENE] = new Gene(LGENE);
	}

	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			Object result = new Life(genes.length, genes[0].codes.length);
			return result;
		}
	}

	public void setRandom() {
		for (Gene gene : genes)
			gene.setRandom();
	}

	public void benchmark() {
		fitness = 0;
		for (int iGene = 0; iGene < genes.length; iGene++)
			for (int iCode = 0; iCode < genes.length; iCode++)
				if (genes[iGene].codes[iCode])
					fitness++;
	}

	public void cx(Life life2) {
		// new child is this , parents are this and life2
		for (int i = 0; i < genes.length; i++)
			genes[i].cx(life2.genes[i]);
	}

	public void mutate() {
		for (Gene gene : genes)
			gene.mutate();
	}
}
