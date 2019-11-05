import java.util.*;

public class Chromosome {
	public double[] genes;
	public double fitness;
	public Vector<Point> calcP;
	public Chromosome(int chromosomeLength) {
		this.genes = new double[chromosomeLength];
		this.fitness = 0;
		calcP =new Vector<Point>();
	}



	public void generateGenes() {
		for (int i = 0; i < genes.length; i++) {
			double value = (Math.random()*21)-10;
			if(value>10) {
				genes[i]=10; 
			}
			else if(value<-10) {
				genes[i]=-10;
			}
			else
				genes[i]=value; 
		}
	}
	public void calcY(Vector<Point> actualP) {
		
		for(int i=0;i<actualP.size();i++) {
			double x=actualP.get(i).x;
			double c=1;
			double y=0;
			for (int j=0;j<genes.length;j++) {
				y+=c*genes[j];
				c*=x;
			}
			Point p=new Point();
			p.x=x;
			p.y=y;
			calcP.add(p);
			//System.out.println(x+" "+y);
		}
	}
	public void calcFitness(Vector<Point> actualP) {
		calcY(actualP);
		double c=0;
		for(int i=0;i<actualP.size();i++) {
			double s=calcP.get(i).y-actualP.get(i).y;
			c+=s*s;
		}
		fitness= c/actualP.size();
		
		//System.out.println(fitness);
	}
}
