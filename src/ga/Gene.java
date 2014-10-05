package ga;

import java.util.Arrays;

import myutils.Utils;

public class Gene {
	public boolean[] codes;

	public Gene(int length) {
		codes = new boolean[length];
	}

	/** static method **/
	public static Gene cx(Gene gene1, Gene gene2) {
		Gene newGene = new Gene(gene1.codes.length);
		for (int iCode = 0; iCode < newGene.codes.length; iCode++)
			newGene.codes[iCode] = (Utils.random.nextBoolean()) ? gene1.codes[iCode]
					: gene2.codes[iCode];
		return newGene;
	}
	public static boolean equals(Gene gene1, Gene gene2) {
		return Arrays.equals(gene1.codes, gene2.codes);
	}

	/** instance method **/
	public void setRandom() {
		for (int i = 0; i < codes.length; i++)
			codes[i] = Utils.random.nextBoolean();
	}

	public void mutate() {
		for (int i = 0; i < codes.length; i++)
			if (Utils.random.nextFloat() < GA.A_MUTATION)
				codes[i] = !codes[i];
	}

}
