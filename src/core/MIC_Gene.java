package core;

import java.util.Arrays;

import core.MIC.Day;
import core.MIC.Day.Timeslot;
import myutils.Utils;

public class MIC_Gene implements Cloneable {

	/** represent workerId of timeslots **/
	public int[] codes; // indexing to int [] possibleWorkers

	public MIC_Gene(int length) {
		codes = new int[length];
		for (int iCode = 0; iCode < codes.length; iCode++)
			codes[iCode] = -1;
	}

	/** implementing **/
	@Override
	protected Object clone() throws CloneNotSupportedException {
		MIC_Gene newGene = new MIC_Gene(codes.length);
		for (int iCode = 0; iCode < this.codes.length; iCode++)
			newGene.codes[iCode] = this.codes[iCode];
		return newGene;
	}

	/** static method **/
	public static MIC_Gene cx(MIC_Gene gene1, MIC_Gene gene2) {
		MIC_Gene newGene = new MIC_Gene(gene1.codes.length);
		for (int iCode = 0; iCode < newGene.codes.length; iCode++)
			newGene.codes[iCode] = (Utils.random.nextBoolean()) ? gene1.codes[iCode] : gene2.codes[iCode];
		return newGene;
	}

	public static boolean equals(MIC_Gene gene1, MIC_Gene gene2) {
		return Arrays.equals(gene1.codes, gene2.codes);
	}

	/** instance method **/
	public void setRandom(Timeslot[] timeslots) {
		int size;
		for (int iTimeslot = 0; iTimeslot < timeslots.length; iTimeslot++) {
			size = timeslots[iTimeslot].possibleWorkers.size();
			codes[iTimeslot] = (size > 0) ? Utils.random.nextInt(size) : -1;
		}
	}

	public void mutate(Day day, float A_MUTATION) {
		int newCode;
		for (int iTimeslot = 0; iTimeslot < day.timeslot.length; iTimeslot++)
			if ((day.timeslot[iTimeslot].possibleWorkers.size() > 1)
					&& (Utils.random.nextFloat() < A_MUTATION)) {
				do {
					newCode = Utils.random.nextInt(day.timeslot[iTimeslot].possibleWorkers.size());
				} while (newCode == codes[iTimeslot]);
				codes[iTimeslot] = newCode;
			}
	}
}
