package analisadorJMRT;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.semantria.Crawler;


public class AnalisadorLexico {
	
	//Método para remoção de todos os sinais de pontuação presentes nos textos (reviews)
	public static ArrayList<String> retPontuacao(ArrayList<String> review) {

		ArrayList<String> reviewSemPontuacao = new ArrayList<String>();
		String sinais[] = { "!", "\"", "#", "$", "%", "&", "'", "\\(", "\\)",
				"\\*", "\\+", ",", "-", "\\.", "/", ":", ";", "<", "=", ">",
				"\\?", "@", "\\[", "\\]", "^", "_", "`", "\\{", "|", "\\}", "~" };
		
		ArrayList<String> stopList = createList("stop_list_google.txt");
		
		for (int i = 0; i < review.size(); i++) {

			String reviewTemp = review.get(i);
			for (String r : sinais) {
				reviewTemp = reviewTemp.replaceAll(r, "");
			}
			
			for (String stopWord : stopList) {
				reviewTemp = reviewTemp.replaceAll(" " + stopWord, " ");
				
			}
			

			reviewSemPontuacao.add(reviewTemp);
		}

		return reviewSemPontuacao;

	}

	public static ArrayList<String> createList(String filename) {

		ArrayList<String> list = new ArrayList<String>();

		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));

			while (br.ready()) {
				list.add(br.readLine());

			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	public static int countWords(ArrayList<String> dictionary, String[] texto) {

		int count = 0;

		for (int j = 0; j < texto.length; j++) {

			if (dictionary.contains(texto[j])) {
				count++;
			}

		}

		return count;
	}

	public static void main(String[] args) {

		ArrayList<String> texto = new ArrayList<String>();
		
		Crawler crawler = new Crawler("http://www.imdb.com/title/tt2103281");
//		texto.add("Ultimately, \"Dawn of the Planet of the Apes\" is a very, very good film. I did have some complaints with a few characters and lines, but for the most part this film is extremely effective in continuing the return the apes to the silver screen. It takes a franchise that looked to be beyond saving and has turned it into what will likely become a box office contender whenever it hits theaters for the foreseeable future.");
//		texto.add("This film is not just the best film of the summer, it's probably so far the best movie of the year!");
		
		texto.add(crawler.getCleanedReviews().get(105));
		
		ArrayList<String> textoSemPontuacao = new ArrayList<String>();

		ArrayList<String> positive = createList("positive.txt");

		ArrayList<String> negative = createList("negative.txt");

		textoSemPontuacao = retPontuacao(texto);

		ArrayList<String[]> textos = new ArrayList<String[]>();

		for (int i = 0; i < textoSemPontuacao.size(); i++) {
			textos.add(textoSemPontuacao.get(i).split(" "));
		}

		for (int i = 0; i < texto.size(); i++) {
			System.out.println(textoSemPontuacao.get(i));
		}
		
		System.out.println(countWords(positive,textos.get(0)));
		System.out.println(countWords(negative,textos.get(0)));

		System.out.println(textos.get(0).length);

	}
}
