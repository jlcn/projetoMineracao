package analisadorJMRT;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {

	private Document doc;
	private Elements reviews;
	private ArrayList<Integer> classificacaoUsuarios;
	private String html_base;
	private String html_page;

	public Crawler (String initial_page) {

		//"http://www.imdb.com/title/tt1937390/"

		this.reviews = new Elements();
		this.classificacaoUsuarios = new ArrayList<Integer>();
		this.html_base = initial_page;
		this.html_page = html_base+"reviews?start=0";
		this.crawl();
	}

	public Elements getReviews() {
		
		return this.reviews;
	}
	
	public ArrayList<Integer> getClassificacaoUsuarios() {
		return classificacaoUsuarios;
	}

	private void crawl() {
		
		Elements temp;
		Elements links_next;
		
		do{

			try {
			
				// need http protocol
				this.doc = Jsoup.connect(this.html_page).get();

				// get all reviews along with their scores, if any
				temp = doc.select("div#tn15content > p, div#tn15content img[height=12]");
				temp.remove(temp.size()-1);
							
				// Separates reviews from their scores, discarding any review without a score
				for (int i = 0; i < temp.size(); i++) {
					if(temp.get(i).tag().getName().equals("img"))
					{
						this.classificacaoUsuarios.add(new Integer(Integer.parseInt(temp.get(i).attr("alt").split("/")[0])));
						this.reviews.add(temp.get(i+1));
						i++;
					}
				}
				
				//checks if there's more pages
				links_next = doc.select("a[href] > img[alt=[Next]]");
				if (!links_next.isEmpty())
					this.html_page = links_next.get(0).parent().attr("abs:href");
				else
					this.html_page = null;
			} catch (IOException e) {
			
				e.printStackTrace();
			}
		} while(this.html_page != null);	
	}
	
	public ArrayList<String> getCleanedReviews() {
		
		ArrayList<String> list = new ArrayList<String>();
		
		for (Element review : this.reviews) {
			list.add(review.text());
		}
		return list;
	}
}