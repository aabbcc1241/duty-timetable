package ga;

import myutils.Utils;

public class Gene {
	public boolean[] codes;

	public Gene(int length) {
		codes = new boolean[length];
	}

	public void setRandom() {
		for (int i = 0; i < codes.length; i++)
			codes[i] = Utils.random.nextBoolean();
	}

	public void cx(Gene gene2) {
		for (int i = 0; i < codes.length; i++)
			if(Utils.random.nextBoolean())
				codes[i]=gene2.codes[i];
	}

	public void mutate() {
		for (int i = 0; i < codes.length; i++)
			if(Utils.random.nextFloat()<GA.AMUTATION)
				codes[i]=!codes[i];
	}
}
