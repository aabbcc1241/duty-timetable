package ga;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import myutils.Utils;

public class GA {
	public static int N_POP = 32;
	public static float P_MUTATION = 0.1f;
	public static float A_MUTATION = 0.1f;
	public static float P_CX = 0.25f;

	protected int N_GEN;
	protected int N_GENE;
	protected int L_GENE;
	/** [num of animal][num of gene] **/
	protected List<Life> lifes;

	/** contrucstor **/
	public GA(int nGEN, int nGENE, int lGENE) {
		super();
		N_GEN = nGEN;
		N_GENE = nGENE;
		L_GENE = lGENE;
		lifes = new ArrayList<Life>();
		for (int iLife = 0; iLife < N_POP; iLife++)
			lifes.add(new Life(N_GENE, L_GENE));
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
		for (int iGEN = 0; iGEN < N_GEN; iGEN++) {
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
		for (int iLife = 0; iLife < N_POP; iLife++) {
			if (iLife > N_POP * P_CX) {
				i = Utils.random.nextInt(iLife);
				lifes.set(iLife, cx(lifes.get(iLife),lifes.get(i)));
			}
		}
	}

	protected void mutation() {
		for (Life life : lifes)
			if (Utils.random.nextFloat() < P_MUTATION)
				life.mutate();
	}

}
