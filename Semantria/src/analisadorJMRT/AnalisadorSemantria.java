package analisadorJMRT;

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

public class AnalisadorSemantria
{
	private static final int TIMEOUT_BEFORE_GETTING_RESPONSE = 5000; //in millisec
	private String movieUrl;
	private String[] categories;
	private String[] series;
	private int matches;
	private int[][] values;
	final private String key = "4ea25558-cf1a-48ec-92b0-53d273714b12";
	final private String secret = "6f1eb449-3065-478a-a2f5-59ea5e0dceac";
	private boolean[] polarities;
	private double[] imdbPolarities, semantriaPolarities;
	private Session session;
	
	public AnalisadorSemantria()
	{
		this.session = Session.createSession(key, secret, true);
		
		this.categories = new String[4];
		this.series = new String[3];
		this.values = new int[4][3];
	}

	public double[] getImdbPolarities() {
		return imdbPolarities;
	}

	public double[] getSemantriaPolarities() {
		return semantriaPolarities;
	}

	public void doThings(String movieUrl, Crawler crawler)
	{
		this.movieUrl = movieUrl;
		List<String> initialTexts = new ArrayList<String>();

		// Adding the documents gotten from the crawler
		//crawler = new Crawler(movieUrl);
		initialTexts = crawler.getCleanedReviews();

		System.out.println("Processando...");

		// session.setCallbackHandler(new CallbackHandler());
		for(String text : initialTexts)
		{
			String uid = UUID.randomUUID().toString();
			// Creates a sample document which need to be processed on Semantria
			// Queues document for processing on Semantria service
			if( session.queueDocument( new Document( uid, text )) == 202)
			{
				System.out.println("\"" + uid + "\" document queued succsessfully.");
			}
			else
			{
				System.out.println("\"" + uid + "\" document queued unsuccsessfully(ERROR).");
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
				System.out.println("Retrieving your processed results("+processed.size()+" from "+ initialTexts.size() +")...");
				Thread.currentThread().sleep(TIMEOUT_BEFORE_GETTING_RESPONSE);
			}

			// Output helpers
			polarities = new boolean[processed.size()];
			matches = 0;
			String polarity;
			int score;
			imdbPolarities = new double[3];
			semantriaPolarities = new double[3];

			for (int i = 0; i < polarities.length; i++) {
				//System.out.println(i);
				polarity = processed.get(i).getSentimentPolarity();
				score = crawler.getClassificacaoUsuarios().get(i).intValue();
				semantriaPolarities[(polarity.equals("negative") ? 0 : (polarity.equals("neutral") ? 1 : 2))]++;
				imdbPolarities[(score <= 4) ? 0 : ((score >= 8) ? 2 : 1 )]++;
				polarities[i] = (polarity.equals("negative") && score <= 4)
						|| (polarity.equals("neutral") && score >= 5 && score <= 7)
						|| (polarity.equals("positive") && score >= 8);
				if(polarities[i]) matches++;
			}
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

}
