package core;

import ga.GA;
import myutils.Utils;

public class MIC_Gene {
	public int[] codes;

	/*
	 * int []represent time-slots int is worker id
	 */

	public MIC_Gene(int length) {
		codes = new int[length];
	}

	public void setRandom() {
		for (int i = 0; i < codes.length; i++)
			codes[i] = Utils.random.nextInt(DutyTimeTable.WORKER_AMOUNT);
	}

	public void cx(MIC_Gene gene2) {
		for (int i = 0; i < codes.length; i++)
			if (Utils.random.nextBoolean())
				codes[i] = gene2.codes[i];
	}

	public void mutate() {
		for (int i = 0; i < codes.length; i++)
			if (Utils.random.nextFloat() < GA.A_MUTATION) {
				int newCode;
				do {
					newCode = Utils.random.nextInt(DutyTimeTable.WORKER_AMOUNT);
				} while (newCode == codes[i]);
				codes[i] = newCode;
			}
	}
}
