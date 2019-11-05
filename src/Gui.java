import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Gui extends JFrame {

	private static JPanel contentPane;
	static int i = 0;
	static Vector<ChartPanel> list;

	/**
	 * A demonstration application showing an XY series containing a null value.
	 *
	 * @param title the frame title.
	 */
	public Gui(final String title) {
		super(title);
		// getContentPane().setBounds(0, 0, 1500, 901);
		// contentPane.setLayout(null);
		list = new Vector<ChartPanel>();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 1520, 950);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(0, 0, 1500, 850);
		contentPane.add(panel_1);
		// panel_1.setLayout(null);

		JPanel panel2 = new JPanel();
		panel2.setBackground(Color.GRAY);
		panel2.setBounds(0, 851, 1500, 50);
		contentPane.add(panel2);

		JButton btnNext = new JButton("Next");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JPanel p = (JPanel) contentPane.getComponent(0);
				//System.out.println("Next "+i);
				if (i != list.size()) {
					if(i!=0) {
						//p.remove(list.get(i-1));
						p.removeAll();
					}
					//p.add(list.get(i));
					p.add(list.get(i));
					p.validate();
					i++;
				}

			}
		});
		btnNext.setBounds(1133, 13, 97, 25);
		panel2.add(btnNext);
		panel2.setLayout(null);
		/**************************************************/

		

	}


	static void do_() {
		BufferedReader reader;
		int max_generation = 1000;
		String out = "";

		try {
			reader = new BufferedReader(new FileReader("src/input-2.txt"));
			int C = Integer.parseInt(reader.readLine());/// num of test cases
			// System.out.println("c= " + C);

			for (int i = 0; i < C; i++) {

				Vector<Point> points = new Vector<Point>();
				Vector<Point> bpoints = new Vector<Point>();

				String line = reader.readLine();
				String[] arr = line.split(" ");
				int N = Integer.parseInt(arr[0]); /// num of Points
				// System.out.println("N= " + N);
				int D = Integer.parseInt(arr[1]);/// requested degree
				// System.out.println("D= " + D);

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
				bpoints = cr.bestP;
				for (int m = 0; m < cr.sol.genes.length; m++) {
					ss += cr.sol.genes[m] + " ";
				}

				out += "Case " + (i + 1) + " : \n" + ss + "\n" + cr.bestV + "\n";
				final XYSeries series = new XYSeries("Base Data");
				final XYSeries series2 = new XYSeries("Calc Data");
				for (int f = 0; f < points.size(); f++) {
					series.add(points.get(f).x, points.get(f).y);
					series2.add(bpoints.get(f).x, bpoints.get(f).y);
				}

				final XYSeriesCollection data = new XYSeriesCollection();
				data.addSeries(series);
				data.addSeries(series2);

				// final XYSeriesCollection data2 = new XYSeriesCollection(series);
				final JFreeChart chart = ChartFactory.createXYLineChart("Case " + (i + 1), "X", "Y", data,
						PlotOrientation.VERTICAL, true, true, false);
				chart.getXYPlot().setRenderer(new XYSplineRenderer());

				final ChartPanel chartPanel = new ChartPanel(chart);
				chartPanel.setPreferredSize(new java.awt.Dimension(1500, 850));
				// setContentPane(chartPanel);
				list.add(chartPanel);

				// panel_1.add(chartPanel, BorderLayout.CENTER);
				// contentPane.add(chartPanel, BorderLayout.NORTH);
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

	/**
	 * Starting point for the demonstration application.
	 *
	 * @param args ignored.
	 */
	public static void main(final String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui demo = new Gui("Curve Fitting");
					JPanel p = (JPanel) contentPane.getComponent(0);
					demo.setVisible(true);
					do_();
					//p.add(list.get(i));

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}
}