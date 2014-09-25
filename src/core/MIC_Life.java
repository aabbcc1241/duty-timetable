package core;

import ga.Gene;
import ga.Life;

public class MIC_Life extends Life {
	private MIC mic;
	private Worker[] workers;

	/** represent days **/
	public MIC_Gene[] genes;

	public MIC_Life(int NGENE, int LGENE, MIC mic, Worker[] workers) {
		super(NGENE, LGENE);
		this.mic = mic;
		this.workers = workers;
	}

	@Override
	public void benchmark() {
		fitness = 0;
		int  workerId,workerIdLast;
		for (int iDay = 0; iDay < genes.length; iDay++)
			for (int iTimeslot = 0; iTimeslot < genes.length; iTimeslot++) {

				 workerId = genes[iDay].codes[iTimeslot];
				switch (workers[workerId].days[iDay].timeslot[iTimeslot].status) {
				/** check valid **/
				case 0:
					fitness += MIC_GA.PUNISH_HAS_LESSON;
					break;
				/** check wanted and available bonus **/
				case 1:
					fitness += MIC_GA.BONUS_WANTED;
					break;
				case 2:
					fitness += MIC_GA.BONUS_AVAILABLE;
					break;
				default:
					break;
				}
				/** check continuous bonus **/
				if(iTimeslot>0){
					 workerIdLast = genes[iDay].codes[iTimeslot-1];
					 if(workerId==workerIdLast)
						 fitness+=MIC_GA.BONUS_CONTINUOUS;
				}
			}
		
	}
}
