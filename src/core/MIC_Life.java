package core;

import core.MIC.Day;
import ga.Gene;
import ga.Life;

public class MIC_Life implements Cloneable, Comparable<MIC_Life> {
	private MIC mic;
	private Worker[] workers;

	/** represent days **/
	public MIC_Gene[] genes;
	public float fitness;

	/*
	 * public MIC_Life(final int NGENE, final int LGENE) { super(NGENE, LGENE);
	 * genes = new MIC_Gene[NGENE]; for (int iGENE = 0; iGENE < NGENE; iGENE++)
	 * genes[iGENE] = new MIC_Gene(LGENE); }
	 */

	public MIC_Life(int NGENE, int LGENE, MIC mic, Worker[] workers) {
		genes = new MIC_Gene[NGENE];
		for (int iGENE = 0; iGENE < NGENE; iGENE++)
			genes[iGENE] = new MIC_Gene(LGENE);
		this.mic = mic;
		this.workers = workers;
	}

	/** implementing **/
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
	public int compareTo(MIC_Life o) {
		return Float.compare(this.fitness, o.fitness);
	}

	/** static method **/
	public static MIC_Life cx(MIC_Life life1, MIC_Life life2) {
		MIC_Life newLife = (MIC_Life) life1.clone();
		for (int iGene = 0; iGene < newLife.genes.length; iGene++) {
			newLife.genes[iGene] = MIC_Gene.cx(life1.genes[iGene], life2.genes[iGene]);
		}
		return newLife;
	}

	public void setRandom() {
		for (int iDay = 0; iDay < mic.days.length; iDay++)
			genes[iDay].setRandom(mic.days[iDay].timeslot);
	}

	public void benchmark() {
		fitness = 0;
		int workerId, workerIdLast;
		for (int iDay = 0; iDay < genes.length; iDay++)
			for (int iTimeslot = 0; iTimeslot < genes.length; iTimeslot++) {
				/*
				 * System.out.println(); System.out.print("iDay\t\t");
				 * System.out.println(iDay); System.out.print("iTimeslot\t\t");
				 * System.out.println(iTimeslot); System.out.print("genes[" +
				 * iDay + "].codes[" + iTimeslot + "]\t\t");
				 * System.out.println(genes[iDay].codes[iTimeslot]);
				 * System.out.print("mic.days["+iDay+"].timeslot["+iTimeslot+
				 * "].possibleWorkers.size()\t\t");
				 * System.out.println(mic.days[iDay
				 * ].timeslot[iTimeslot].possibleWorkers.size());
				 */
				workerId = mic.days[iDay].timeslot[iTimeslot].possibleWorkers
						.get(genes[iDay].codes[iTimeslot]).id;
				switch (workers[workerId].days[iDay].timeslot[iTimeslot].status) {
				/** check valid **/
				case 0:
					fitness += MIC_GA.SCORE_HAS_LESSON;
					break;
				/** check wanted and available bonus **/
				case 1:
					fitness += MIC_GA.SCORE_WANTED;
					break;
				case 10:
					fitness += MIC_GA.SCORE_WANTED;
					break;
				case 2:
					fitness += MIC_GA.SCORE_AVAILABLE;
					break;
				case 20:
					fitness += MIC_GA.SCORE_AVAILABLE;
					break;
				default:
					break;
				}
				/** check continuous bonus **/
				if (iTimeslot > 0) {
					workerIdLast = genes[iDay].codes[iTimeslot - 1];
					if (workerId == workerIdLast)
						fitness += MIC_GA.SCORE_CONTINUOUS;
				}
			}
	}

	public void mutate() {
		for (int iDay = 0; iDay < mic.days.length; iDay++)
			genes[iDay].mutate(mic.days[iDay]);
	}
}
