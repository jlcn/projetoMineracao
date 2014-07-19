package analisadorJMRT;

import java.util.ArrayList;

import com.semantria.Crawler;

public class Main {
	
	public static void main(String[] args) {
		
		Crawler crawler = new Crawler("http://www.imdb.com/title/tt2103281");
		
		ArrayList<String> reviews = crawler.getCleanedReviews();		
		
		ArrayList<String> reviewsSemPontuacao = new ArrayList<String>();

		ArrayList<String> positive = AnalisadorLexico.createList("positive.txt");

		ArrayList<String> negative = AnalisadorLexico.createList("negative.txt");

		reviewsSemPontuacao = AnalisadorLexico.retPontuacao(reviews);

		ArrayList<String[]> reviewsTratadas = new ArrayList<String[]>();

		for (int i = 0; i < reviewsSemPontuacao.size(); i++) {
			reviewsTratadas.add(reviewsSemPontuacao.get(i).split(" "));
		}

		for (int i = 0; i < reviewsTratadas.size(); i++) {
			System.out.println(reviewsSemPontuacao.get(i));
			System.out.println("Positve words: " + AnalisadorLexico.countWords(positive,reviewsTratadas.get(i)));
			System.out.println("Negative words: " + AnalisadorLexico.countWords(negative,reviewsTratadas.get(i)));
			System.out.println("Total words: " + reviewsTratadas.get(i).length);

		}
	}

}
