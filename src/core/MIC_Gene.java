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

	public void mutate(Day day) {
		int newCode;
		for (int iTimeslot = 0; iTimeslot < day.timeslot.length; iTimeslot++)
			if ((day.timeslot[iTimeslot].possibleWorkers.size() > 1)
					&& (Utils.random.nextFloat() < GA.A_MUTATION)) {
				// System.out.println("\ngene-mutate-really " +
				// Utils.random.nextInt());
				// System.out.println(day.timeslot[iTimeslot].possibleWorkers.size());
				do {
					newCode = Utils.random
							.nextInt(day.timeslot[iTimeslot].possibleWorkers.size());
				} while (newCode == codes[iTimeslot]);
				codes[iTimeslot] = newCode;
			}
	}
}
