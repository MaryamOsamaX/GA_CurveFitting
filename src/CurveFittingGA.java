import java.util.Random;
import java.util.Vector;

public class CurveFittingGA {
	Vector<Chromosome> pop;
	Vector<Point> actualP;
	int popSize,d;
	double bestV;
	Chromosome sol;

	CurveFittingGA(int popS, int d, Vector<Point> actualP1) {
		
		sol = new Chromosome(d+1);
		for(int i=0;i<d+1;i++) { sol.genes[i]=0; }
		bestV=0;
		pop = new Vector<Chromosome>();
		actualP = new Vector<Point>();
		actualP=actualP1;
		popSize = popS;
		this.d = d;

		for (int i = 0; i < popS; i++) {
			pop.add(new Chromosome(d+1));
			pop.get(i).generateGenes();
		}
	}

	public Vector<Integer> selection() {

		Vector<Integer> selected = new Vector<Integer>();
		Vector<Integer> rouletteWheel = new Vector<Integer>();
		rouletteWheel.add(0);
		int totalFitness = 0;
		/// initialize the roulette wheel
		for (int i = 0; i < pop.size(); i++) {
			totalFitness += pop.get(i).fitness;
			rouletteWheel.add(totalFitness);
		}

		Random random = new Random();
		for (int x = 0; x < pop.size() / 2; x++) {
			if (totalFitness != 0) {
				int random1 = random.nextInt(totalFitness), random2 = random.nextInt(totalFitness);
				Vector<Integer> s = new Vector<Integer>();

				for (int i = 0; i < rouletteWheel.size() - 1; i++) {
					if (s.size() == 2)
						break;
					if (random1 >= rouletteWheel.get(i) && random1 < rouletteWheel.get(i + 1)) {
						s.add(i);
					}
					if (random2 >= rouletteWheel.get(i) && random2 < rouletteWheel.get(i + 1)) {
						s.add(i);
					}
				}

				selected.add(s.get(0));
				selected.add(s.get(1));
			}

		}
		return selected;

	}

	public Vector<Integer> crossOver(Vector<Integer> selected) {

		Random random = new Random();
		for (int k = 0; k < selected.size() - 1; k += 2) {
			double r = random.nextDouble();
			if (r <= 0.7) {
				int begin = random.nextInt(d);
				for (int i = begin; i < d+1; i++) {
					double temp = pop.get(selected.get(k)).genes[i];
					pop.get(selected.get(k)).genes[i] = pop.get(selected.get(k + 1)).genes[i];
					pop.get(selected.get(k + 1)).genes[i] = temp;
				}

			}
		}
		return selected;
	}

	public void mutation(Vector<Integer> crossed) {
		Random random = new Random();
		for (int i = 0; i < crossed.size(); i++) {
			for (int x = 0; x < pop.get(crossed.get(i)).genes.length; x++) {
				double r = random.nextDouble();
				if (r <= 0.01) {
					if (pop.get(crossed.get(i)).genes[x] == 1)
						pop.get(crossed.get(i)).genes[x] = 0;
					else
						pop.get(crossed.get(i)).genes[x] = 1;
				}
			}
		}
	}

	public void calcPopFitness() {
		for (int i = 0; i < pop.size(); i++) {
			pop.get(i).calcFitness(actualP);
		}
	}

	public void updateSolution() {
		for (int i = 0; i < pop.size(); i++) {
			if (bestV < pop.get(i).fitness) {
				bestV = pop.get(i).fitness;
				for(int j=0;j<sol.genes.length;j++) {
					sol.genes[j] = pop.get(i).genes[j];
				}
				
				//System.out.println("best value = "+bestV+" -> "+Arrays.toString(sol.genes));
			}
		}
	}
}
