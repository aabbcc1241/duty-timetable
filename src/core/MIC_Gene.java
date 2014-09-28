package core;

import core.MIC.Day;
import core.MIC.Day.Timeslot;
import ga.GA;
import myutils.Utils;

public class MIC_Gene {

	/** represent workerId of timeslots **/
	public int[] codes;

	public MIC_Gene(int length) {
		codes = new int[length];
	}

	/** static method **/
	public static MIC_Gene cx(MIC_Gene gene1, MIC_Gene gene2) {
		MIC_Gene newGene = new MIC_Gene(gene1.codes.length);
		for (int iCode = 0; iCode < newGene.codes.length; iCode++)
			newGene.codes[iCode] = (Utils.random.nextBoolean()) ? gene1.codes[iCode]
					: gene2.codes[iCode];
		return newGene;
	}

	public void setRandom(Timeslot[] timeslots) {
		for (int iTimeslot = 0; iTimeslot < timeslots.length; iTimeslot++)
			codes[iTimeslot] = Utils.random.nextInt(timeslots[iTimeslot].possibleWorkers
					.size());
	}

	public void cx(MIC_Gene gene2) {
		for (int i = 0; i < codes.length; i++)
			if (Utils.random.nextBoolean())
				codes[i] = gene2.codes[i];
	}

	public void mutate(Day day) {
		for (int iTimeslot = 0; iTimeslot < day.timeslot.length; iTimeslot++)
			if (Utils.random.nextFloat() < GA.A_MUTATION) {
				int newCode;
				do {
					newCode = Utils.random
							.nextInt(day.timeslot[iTimeslot].possibleWorkers.size());
				} while (newCode == codes[iTimeslot]);
				codes[iTimeslot] = newCode;
			}
	}
}
