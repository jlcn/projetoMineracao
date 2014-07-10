package com.semantria;

import com.semantria.mapping.Document;
import com.semantria.mapping.output.DocAnalyticData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class TestApp
{
	private static final int TIMEOUT_BEFORE_GETTING_RESPONSE = 5000; //in millisec

	public static void main(String args[])
	{

		// Uncomment to set the output stream to a file instead of the console
//		File file = new File("output.txt");  
//		FileOutputStream fis;
//		try {
//			fis = new FileOutputStream(file);
//			PrintStream out = new PrintStream(fis);  
//			System.setOut(out);
//		} catch (FileNotFoundException e1) {
//			e1.printStackTrace();
//		}
		
		Scanner in = new Scanner(System.in);

		// Use correct Semantria API credentias here
		String key = "4ea25558-cf1a-48ec-92b0-53d273714b12";
		String secret = "6f1eb449-3065-478a-a2f5-59ea5e0dceac";

		if( args != null && args.length == 2 )
		{
			key = args[0];
			secret = args[1];
		}

		// Initial texts for processing
		List<String> initialTexts = new ArrayList<String>();
		
		System.out.print("Digite aqui a url da IMDb desejada para a analise (ex: http://www.imdb.com/title/tt2568204/):\n>");
		String url = in.nextLine();
		if(url.equals("")) url = "http://www.imdb.com/title/tt2568204/";
		
		// Adding the documents gotten from the crawler
		Crawler crawler = new Crawler(url);
		initialTexts = crawler.getCleanedReviews();

		System.out.println("Processando...");

		Session session = Session.createSession(key, secret, true);
		// session.setCallbackHandler(new CallbackHandler());
		for(String text : initialTexts)
		{
			String uid = UUID.randomUUID().toString();
			// Creates a sample document which need to be processed on Semantria
			// Queues document for processing on Semantria service
			if( session.queueDocument( new Document( uid, text )) == 202)
			{
				// System.out.println("\"" + uid + "\" document queued succsessfully.");
			}
		}
		// System.out.println();
		try
		{
			// As Semantria isn't real-time solution you need to wait some time before getting of the processed results
			// In real application here can be implemented two separate jobs, one for queuing of source data another one for retreiving
			// Wait ten seconds while Semantria process queued document
			Thread.sleep(TIMEOUT_BEFORE_GETTING_RESPONSE);
			List<DocAnalyticData> processed = new ArrayList<DocAnalyticData>();

			while(processed.size() < initialTexts.size())
			{
				// Requests processed results from Semantria service
				List<DocAnalyticData> temp = session.getProcessedDocuments();
				processed.addAll(temp);
				if(processed.size() >= initialTexts.size()) break;
				// System.out.println("Retrieving your processed results...");
				Thread.currentThread().sleep(TIMEOUT_BEFORE_GETTING_RESPONSE);
			}
			
			// Output helpers
			boolean[] polarities = new boolean[processed.size()];
			int matches = 0;
			String polarity;
			int score;
			
			for (int i = 0; i < polarities.length; i++) {
				polarity = processed.get(i).getSentimentPolarity();
				score = crawler.getClassificacaoUsuarios().get(i).intValue();
				polarities[i] = (polarity.equals("negative") && score <= 4)
						|| (polarity.equals("neutral") && score >= 5 && score <= 7)
						|| (polarity.equals("positive") && score >= 8);
				if(polarities[i]) matches++;
			}
			
			System.out.println(matches+" das "+polarities.length+" comparacoes entre polaridades foram semelhantes.\n-----Fim de execucao-----");
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());
		}
	}
}
