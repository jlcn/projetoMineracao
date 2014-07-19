package analisadorJMRT;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class AnalisadorLexico {

	// Método para remoção de todos os sinais de pontuação presentes nos textos
	// (reviews)
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
			br.close();
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

}
