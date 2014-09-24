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
}
