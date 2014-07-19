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

public class Main {

	public static void main(String[] args) {

		AnalisadorSemantria analisadorSemantria = new AnalisadorSemantria();
		
		ArrayList<Integer> r_positivos_semantria = new ArrayList<Integer>(),
						   r_neutros_semantria = new ArrayList<Integer>(),
						   r_negativos_semantria = new ArrayList<Integer>(),
						   r_positivos_estrelas = new ArrayList<Integer>(),
						   r_neutros_estrelas = new ArrayList<Integer>(),
						   r_negativos_estrelas = new ArrayList<Integer>();
		
		ArrayList<Integer> r_positivos = new ArrayList<Integer>();

		ArrayList<Integer> r_neutros = new ArrayList<Integer>();

		ArrayList<Integer> r_negativos = new ArrayList<Integer>();

		String filmes[] = { "http://www.imdb.com/title/tt2103281",
				"http://www.imdb.com/title/tt0109424",
				"http://www.imdb.com/title/tt1646971",
		"http://www.imdb.com/title/tt2294449" };
		
		//String[] filmes = {"http://www.imdb.com/title/tt3677466/"};
		
		for (int j = 0; j < filmes.length; j++) {

			Crawler crawler = new Crawler(filmes[j]);

			analisadorSemantria.doThings(filmes[j], crawler);
			
			r_positivos_semantria.add((int) analisadorSemantria.getSemantriaPolarities()[2]);
			
			r_neutros_semantria.add((int) analisadorSemantria.getSemantriaPolarities()[1]);

			r_negativos_semantria.add((int) analisadorSemantria.getSemantriaPolarities()[0]);
			
			r_positivos_estrelas.add((int) analisadorSemantria.getImdbPolarities()[2]);
			
			r_neutros_estrelas.add((int) analisadorSemantria.getImdbPolarities()[1]);

			r_negativos_estrelas.add((int) analisadorSemantria.getImdbPolarities()[0]);
			
			
			ArrayList<String> reviews = crawler.getCleanedReviews();

			// ArrayList<String> reviews = new ArrayList<String>();

			ArrayList<String> reviewsSemPontuacao = new ArrayList<String>();

			ArrayList<String> positive = AnalisadorLexico
					.createList("positive.txt");

			ArrayList<String> negative = AnalisadorLexico
					.createList("negative.txt");
			ArrayList<String> inversor= AnalisadorLexico.createList("inversors.txt");

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
				int palavrasPositivas = 0;
				int palavrasNegativas = 0;
				
				int [] analysis1 = AnalisadorLexico.countWords(inversor,positive,
						reviewsTratadas.get(i));
				int [] analysis2 =AnalisadorLexico.countWords(inversor,negative,
						reviewsTratadas.get(i));
				
				palavrasPositivas= analysis1[0]+analysis2[1];
				palavrasNegativas= analysis2[0]+analysis1[1];

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
				r_negativos, r_neutros),
				ds_semantria = GerarGrafico.createDs(r_positivos_semantria,
						r_negativos_semantria, r_neutros_semantria),
				ds_estrelas = GerarGrafico.createDs(r_positivos_estrelas,
						r_negativos_estrelas, r_neutros_estrelas);

		// cria o gráfico
		JFreeChart grafico = GerarGrafico.createChart(ds, "JMRT-Gráfico"),
				   grafico_semantria =  GerarGrafico.createChart(ds_semantria, "JMRT-Gráfico-Semantria"),
				   grafico_estrelas =  GerarGrafico.createChart(ds_estrelas, "JMRT-Gráfico-Estrelas");

		try {
			OutputStream arquivo = new FileOutputStream("grafico.png");

			ChartUtilities.writeChartAsPNG(arquivo, grafico, 550, 400);
			
			arquivo = new FileOutputStream("grafico-semantria.png");
			
			ChartUtilities.writeChartAsPNG(arquivo, grafico_semantria, 550, 400);
			
			arquivo = new FileOutputStream("grafico-estrelas.png");
			
			ChartUtilities.writeChartAsPNG(arquivo, grafico_estrelas, 550, 400);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
