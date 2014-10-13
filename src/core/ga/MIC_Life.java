package core.ga;

import core.dutytable.MIC;
import core.dutytable.Worker;

public class MIC_Life implements Cloneable, Comparable<MIC_Life> {
	private MIC mic;
	private Worker[] workers;

	/** represent days **/
	public MIC_Gene[] genes;
	public float fitness;
	public int[] hours;
	public int hoursSd;
	public float hoursAvg;

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
		hours = new int[workers.length];
	}

	/** implementing **/
	@Override
	public Object clone() {
		MIC_Life newLife = new MIC_Life(genes.length, genes[0].codes.length, mic, workers);
		newLife.genes = new MIC_Gene[this.genes.length];
		for (int iGene = 0; iGene < this.genes.length; iGene++)
			try {
				newLife.genes[iGene] = (MIC_Gene) this.genes[iGene].clone();
			} catch (CloneNotSupportedException e) {
				//display.writeln(e.toString);
				//display.writeln("Cannot clone the gene");				
				e.printStackTrace();
			}
		return newLife;
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

	public static boolean equals(MIC_Life life1, MIC_Life life2) {
		boolean isSame = life1.genes.length == life2.genes.length;
		int i = 0;
		while (isSame && (i < life1.genes.length)) {
			isSame &= MIC_Gene.equals(life1.genes[i], life2.genes[i]);
			i++;
		}
		return isSame;
	}

	/** instance method **/
	public void setRandom() {
		for (int iDay = 0; iDay < mic.days.length; iDay++)
			genes[iDay].setRandom(mic.days[iDay].timeslot);
	}

	public void benchmark() {
		fitness = 0;
		for (int i = 0; i < hours.length; i++)
			hours[i] = 0;
		int workerId, workerIdLast, index;
		for (int iDay = 0; iDay < genes.length; iDay++)
			for (int iTimeslot = 0; iTimeslot < genes.length; iTimeslot++) {
				if ((index = genes[iDay].codes[iTimeslot]) == -1)
					continue;
				workerId = mic.days[iDay].timeslot[iTimeslot].possibleWorkers.get(index).id;
				hours[workerId]++;
				switch (workers[workerId].days[iDay].timeslot[iTimeslot].status) {
				/** check valid **/
				case -1:
					fitness += MIC_GA.SCORE_EMPTY;
					break;
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
					index = genes[iDay].codes[iTimeslot - 1];
					workerIdLast = mic.days[iDay].timeslot[iTimeslot-1].possibleWorkers.get(index).id;
					if (workerId == workerIdLast)
						fitness += MIC_GA.SCORE_CONTINUOUS;
				}
			}
		/** check avg hours (0.5hr) **/
		int sum = 0;
		for (int i = 0; i < hours.length; i++)
			sum += hours[i];
		hoursAvg = sum / (float) hours.length;
		hoursSd = 0;
		for (int i = 0; i < hours.length; i++)
			hoursSd += Math.pow(hours[i] - hoursAvg, 2);
		fitness += hoursSd * MIC_GA.SCORE_HOUR_SD;
	}

	public void mutate(float A_MUTATION) {
		for (int iDay = 0; iDay < mic.days.length; iDay++)
			genes[iDay].mutate(mic.days[iDay], A_MUTATION);
	}
}
