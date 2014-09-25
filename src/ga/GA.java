package ga;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import myutils.Utils;

public class GA {
	public static int NPOP = 32;
	public static float PMUTATION = 0.1f;
	public static float AMUTATION = 0.1f;
	public static float PCX = 0.25f;

	protected int NGEN;
	protected int NGENE;
	protected int LGENE;
	/** [num of animal][num of gene] **/
	protected List<Life> lifes;

	/** contrucstor **/
	public GA(int nGEN, int nGENE, int lGENE) {
		super();
		NGEN = nGEN;
		NGENE = nGENE;
		LGENE = lGENE;
		lifes = new ArrayList<Life>();
		for (int iLife = 0; iLife < NPOP; iLife++)
			lifes.add(new Life(NGENE, LGENE));
	}

	/** static method **/
	public static Life cx(Life life1, Life life2) {
		Life result = (Life) life1.clone();
		life1.cx(life2);
		return result;
	}

	/** instance method **/
	protected void setRandom() {
		for (Life life : lifes)
			life.setRandom();
	}

	public void start() {
		setRandom();
		for (int iGEN = 0; iGEN < NGEN; iGEN++) {
			benchmark();
			sort();
			cx();
			mutation();
		}
	}

	protected void benchmark() {
		for (Life life : lifes)
			life.benchmark();
	}

	protected void sort() {
		Collections.sort(lifes);		
	}

	/** losers cx with random life who's better then it **/
	protected void cx() {
		/** new child is iLife, parents are iLife and i **/
		int i;
		for (int iLife = 0; iLife < NPOP; iLife++) {
			if (iLife > NPOP * PCX) {
				i = Utils.random.nextInt(iLife);
				lifes.set(iLife, cx(lifes.get(iLife),lifes.get(i)));
			}
		}
	}

	protected void mutation() {
		for (Life life : lifes)
			if (Utils.random.nextFloat() < PMUTATION)
				life.mutate();
	}

}
