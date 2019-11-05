import java.util.Random;
import java.util.Vector;
import java.lang.Math.*;


public class CurveFittingGA {
	Vector<Chromosome> pop;
	Vector<Point> actualP;
	int popSize, d;
	double bestV;
	Vector<Point> bestP;
	Chromosome sol;
	Double LP = -10.0, UP = 10.0;

	CurveFittingGA(int popS, int d, Vector<Point> actualP1) {

		sol = new Chromosome(d + 1);
		for (int i = 0; i < d + 1; i++) {
			sol.genes[i] = Double.MAX_VALUE;
		}
		bestV = Double.MAX_VALUE;
		pop = new Vector<Chromosome>();
		actualP = new Vector<Point>();
		bestP = new Vector<Point>();
		actualP = actualP1;
		popSize = popS;
		this.d = d;
		
		for (int i = 0; i < popS; i++) {
			pop.add(new Chromosome(d + 1));
			pop.get(i).generateGenes();
		}
	} 

	/// Random Double
	static double randomDouble(double min, double max) {
		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}
		Random r = new Random();
		return min + (max - min) * r.nextDouble();
	}

	public Vector<Integer> selection() {

		Vector<Integer> selected = new Vector<Integer>();
		Vector<Double> rouletteWheel = new Vector<Double>();
		rouletteWheel.add(0.0);
		Double totalFitness = 0.0;
		/// initialize the roulette wheel
		for (int i = 0; i < pop.size(); i++) {
			totalFitness += pop.get(i).fitness;
			rouletteWheel.add(totalFitness);
		}

		for (int x = 0; x < pop.size() / 2; x++) {
			if (totalFitness != 0) {
				Double random1 = randomDouble(0.0, totalFitness), random2 = randomDouble(0.0, totalFitness);
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
				for (int i = begin; i < d + 1; i++) {
					double temp = pop.get(selected.get(k)).genes[i];
					pop.get(selected.get(k)).genes[i] = pop.get(selected.get(k + 1)).genes[i];
					pop.get(selected.get(k + 1)).genes[i] = temp;
				}

			}
		}
		return selected;
	}

	public void mutation(Vector<Integer> crossed ,int t , int T) {
		Random random = new Random();
		Double b = 5.0, y = 0.0; // Dependency factor
		for (int i = 0; i < crossed.size(); i++) {
			for (int x = 0; x < pop.get(crossed.get(i)).genes.length; x++) {
				Double rYN = random.nextDouble();
				if(rYN<=0.01)
				{

				Double deltaL = pop.get(crossed.get(i)).genes[x] - LP,
					   deltaU = UP - pop.get(crossed.get(i)).genes[x];
				Double r1 = random.nextDouble(), r = random.nextDouble() ,c= random.nextDouble();
				if (r1 <= 0.5)
				     y = deltaL;
				else
				     y = deltaU;
				
				Double vOM= y * (1 - Math.pow(r , Math.pow((1-t/T),b) ));
				///Randomly
				
				if(c>=0.5)
					 pop.get(crossed.get(i)).genes[x]= (UP - pop.get(crossed.get(i)).genes[x])*vOM;
				else
					pop.get(crossed.get(i)).genes[x]= (LP + pop.get(crossed.get(i)).genes[x])*vOM;
					
				
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
			if (bestV > pop.get(i).fitness) {
				bestV = pop.get(i).fitness;
				for (int j = 0; j < sol.genes.length; j++) {
					sol.genes[j] = pop.get(i).genes[j];
				}
				bestP=pop.get(i).calcP;
				// System.out.println("best value = "+bestV+" -> "+Arrays.toString(sol.genes));
			}
		}
	}
}
