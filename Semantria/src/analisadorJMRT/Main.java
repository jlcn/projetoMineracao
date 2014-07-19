package analisadorJMRT;

import java.awt.Color;
import java.awt.GradientPaint;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

import org.jfree.data.category.DefaultCategoryDataset;

import com.semantria.Crawler;

public class Main {

	public static void main(String[] args) {

		ArrayList<Integer> r_positivos = new ArrayList<Integer>();

		ArrayList<Integer> r_neutros = new ArrayList<Integer>();

		ArrayList<Integer> r_negativos = new ArrayList<Integer>();

		String filmes[] = { "http://www.imdb.com/title/tt2103281",
				"http://www.imdb.com/title/tt2109248",
				"http://www.imdb.com/title/tt1646971",
		"http://www.imdb.com/title/tt2294449" };

		for (int j = 0; j < filmes.length; j++) {

			Crawler crawler = new Crawler(filmes[j]);

			ArrayList<String> reviews = crawler.getCleanedReviews();

			// ArrayList<String> reviews = new ArrayList<String>();

			ArrayList<String> reviewsSemPontuacao = new ArrayList<String>();

			ArrayList<String> positive = AnalisadorLexico
					.createList("positive.txt");

			ArrayList<String> negative = AnalisadorLexico
					.createList("negative.txt");

			reviewsSemPontuacao = AnalisadorLexico.retPontuacao(reviews);

			ArrayList<String[]> reviewsTratadas = new ArrayList<String[]>();

			for (int i = 0; i < reviewsSemPontuacao.size(); i++) {
				reviewsTratadas.add(reviewsSemPontuacao.get(i).split(" "));
			}

			int positivos = 0;
			int negativos = 0;
			int neutro = 0;

			for (int i = 0; i < reviewsTratadas.size(); i++) {
				// System.out.println(reviewsSemPontuacao.get(i));
				// System.out.println("Positve words: " +
				// AnalisadorLexico.countWords(positive,reviewsTratadas.get(i)));
				// System.out.println("Negative words: " +
				// AnalisadorLexico.countWords(negative,reviewsTratadas.get(i)));
				// System.out.println("Total words: " +
				// reviewsTratadas.get(i).length);
				int palavrasPositivas = AnalisadorLexico.countWords(positive,
						reviewsTratadas.get(i));
				int palavrasNegativas = AnalisadorLexico.countWords(negative,
						reviewsTratadas.get(i));

				if (palavrasPositivas - palavrasNegativas == 0) {
					neutro++;
				} else if (palavrasPositivas > palavrasNegativas) {
					positivos++;
				} else {
					negativos++;
				}

			}

			r_positivos.add(positivos);
			r_negativos.add(negativos);
			r_neutros.add(neutro);

		}

		DefaultCategoryDataset ds = GerarGrafico.createDs(r_positivos,
				r_negativos, r_neutros);

		// cria o gráfico
		JFreeChart grafico = GerarGrafico.createChart(ds, "JMRT-Gráfico");

		try {
			OutputStream arquivo = new FileOutputStream("grafico.png");

			ChartUtilities.writeChartAsPNG(arquivo, grafico, 550, 400);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
