package ga;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import myutils.Utils;

public class GA {
	public static int N_POP = 32;
	public static float P_MUTATION = 0.1f;
	public static float A_MUTATION = 0.1f;
	public static float P_SURVIVE = 0.75f;

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
		/** Top P_SURVIVE (x100%) can marry **/
		List<Life> newLifes = new ArrayList<Life>(lifes);
		for (int iLife = 0; iLife < N_POP; iLife++) {
			if ((float) iLife / N_POP < P_SURVIVE) {
				newLifes.add(lifes.get(iLife));
			}
		}
		while (newLifes.size() < lifes.size()) {
			Life newLife = cx(newLifes.get(Utils.random.nextInt(newLifes.size())),
					newLifes.get(Utils.random.nextInt(newLifes.size())));
			newLifes.add(newLife);
		}
		lifes = newLifes;
	}

	protected void mutation() {
		for (Life life : lifes)
			if (Utils.random.nextFloat() < P_MUTATION)
				life.mutate();
	}

}
