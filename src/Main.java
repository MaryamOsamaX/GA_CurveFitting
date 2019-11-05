import java.io.*;
import java.util.*;

public class Main {
	static void do_algo() {
		BufferedReader reader;
		int max_generation = 1000;
		String out = "";

		try {
			reader = new BufferedReader(new FileReader("src/input-2.txt"));
			int C = Integer.parseInt(reader.readLine());/// num of test cases
		//	System.out.println("c= " + C);

			for (int i = 0; i < C; i++) {

				Vector<Point> points = new Vector<Point>();

				String line = reader.readLine();
				String[] arr = line.split(" ");
				int N = Integer.parseInt(arr[0]); /// num of Points
				//System.out.println("N= " + N);
				int D = Integer.parseInt(arr[1]);/// requested degree
				//System.out.println("D= " + D);

				for (int j = 0; j < N; j++) {
					line = reader.readLine();
					arr = line.split(" ");
					points.add(new Point(Double.parseDouble(arr[0]), Double.parseDouble(arr[1])));
					// System.out.println("point"+ j +"= "+Double.parseDouble(arr[0])+"
					// "+Double.parseDouble(arr[1]));
				}

				CurveFittingGA cr = new CurveFittingGA(1000, D, points);
				// call functions
				for (int g = 0; g < max_generation; g++) {
					cr.calcPopFitness();
					cr.updateSolution();
					Vector<Integer> selected = cr.selection();
					Vector<Integer> crossed = cr.crossOver(selected);
					cr.mutation(crossed, g, max_generation);

				}

				cr.updateSolution();

				String ss = "";

				for (int m = 0; m < cr.sol.genes.length; m++) {
					ss += cr.sol.genes[m] + " ";
				}

				out += "Case " + (i + 1) + " : \n" + ss + "\n" + cr.bestV +"\n";
				

			}
			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter("src/output.txt"));
			writer.write(out);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Finish");
	}
	public static void main(String[] args) {
		do_algo();
	}

}