package analisadorJMRT;

import java.awt.Color;
import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

public class GerarGrafico {

	public static JFreeChart createChart(final CategoryDataset dataset, String titulo) {

		// create the chart...
		final JFreeChart chart = ChartFactory.createBarChart(titulo, // chart
				// title
				"Filmes", // domain axis label
				"Reviews", // range axis label
				dataset, // data
				PlotOrientation.VERTICAL, // orientation
				true, // include legend
				true, // tooltips?
				false // URLs?
				);

		// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

		// set the background color for the chart...
		chart.setBackgroundPaint(Color.white);

		// get a reference to the plot for further customisation...
		final CategoryPlot plot = chart.getCategoryPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);

		// set the range axis to display integers only...
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		// disable bar outlines...
		final BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setDrawBarOutline(false);

		renderer.setSeriesPaint(0, Color.blue);
		renderer.setSeriesPaint(1, Color.green);
		renderer.setSeriesPaint(2, Color.red);

		final CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions
				.createUpRotationLabelPositions(Math.PI / 6.0));
		// OPTIONAL CUSTOMISATION COMPLETED.

		return chart;

	}

	public static DefaultCategoryDataset createDs(
			ArrayList<Integer> r_positivos, ArrayList<Integer> r_negativos,
			ArrayList<Integer> r_neutros) {
		final String series1 = "Positivo";
		final String series2 = "Neutro";
		final String series3 = "Negativo";

		final String category1 = "Filme 1";
		final String category2 = "Filme 2";
		final String category3 = "Filme 3";
		final String category4 = "Filme 4";

		DefaultCategoryDataset ds = new DefaultCategoryDataset();
		ds.addValue(r_positivos.get(0), series1, category1);
		ds.addValue(r_positivos.get(1), series1, category2);
		ds.addValue(r_positivos.get(2), series1, category3);
		ds.addValue(r_positivos.get(3), series1, category4);

		ds.addValue(r_neutros.get(0), series2, category1);
		ds.addValue(r_neutros.get(1), series2, category2);
		ds.addValue(r_neutros.get(2), series2, category3);
		ds.addValue(r_neutros.get(3), series2, category4);

		ds.addValue(r_negativos.get(0), series3, category1);
		ds.addValue(r_negativos.get(1), series3, category2);
		ds.addValue(r_negativos.get(2), series3, category3);
		ds.addValue(r_negativos.get(3), series3, category4);

		return ds;
	}

}
