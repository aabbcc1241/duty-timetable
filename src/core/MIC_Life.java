package core;

import ga.Life;

public class MIC_Life extends Life {
	private MIC mic;
	private Worker[] workers;

	public MIC_Life(int NGENE, int LGENE, MIC mic, Worker[] workers) {
		super(NGENE, LGENE);
		this.mic = mic;
		this.workers = workers;
	}

	@Override
	public void benchmark() {
		fitness = 0;
		for (int iWorker = 0; iWorker < genes.length; iWorker++)
			for (int iCode = 0; iCode < genes.length; iCode++)				
				if (genes[iWorker].codes[iCode])
					fitness++;
	}

}
