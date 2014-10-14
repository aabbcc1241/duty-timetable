package core.gui;

import core.dutytable.mic.MIC;
import core.ga.MIC_Life;

public class InfoCarrier {
	public int popSize, iGen;
	public float avgFitness, sdFitness;
	public MIC_Life bestLife;
	public MIC mic;

	public InfoCarrier(int popSize, int iGen, float avgFitness, float sdFitness, MIC_Life bestLife, MIC mic) {
		this.popSize = popSize;
		this.iGen = iGen;
		this.avgFitness = avgFitness;
		this.sdFitness = sdFitness;
		this.bestLife = bestLife;
		this.mic = mic;
	}
}
