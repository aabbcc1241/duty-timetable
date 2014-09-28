package ga;

public class Life implements Cloneable, Comparable<Life> {
	public Gene[] genes;
	public float fitness;

	public Life(final int NGENE, final int LGENE) {
		genes = new Gene[NGENE];
		for (int iGENE = 0; iGENE < NGENE; iGENE++)
			genes[iGENE] = new Gene(LGENE);
	}

	/**implementing**/
	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			Object result = new Life(genes.length, genes[0].codes.length);
			return result;
		}
	}

	@Override
	public int compareTo(Life o) {
		return Float.compare(this.fitness, o.fitness);
	}

	/** static method **/
	public static Life cx(Life life1, Life life2) {
		Life newLife = (Life) life1.clone();
		for (int iGene = 0; iGene < newLife.genes.length; iGene++) {
			newLife.genes[iGene] = Gene.cx(life1.genes[iGene], life2.genes[iGene]);
		}
		return newLife;
	}
	public static boolean equals(Life life1, Life life2) {
		boolean isSame = life1.genes.length == life2.genes.length;
		int i = 0;
		while (isSame && (i < life1.genes.length)) {
			isSame &= Gene.equals(life1.genes[i], life2.genes[i]);
			i++;
		}
		return isSame;
	}
	

	/** instance method **/
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

	public void mutate() {
		for (Gene gene : genes)
			gene.mutate();
	}
}
