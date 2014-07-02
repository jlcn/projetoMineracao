package com.semantria;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {

	private Document doc;
	private Elements reviews;
	private String html_base;
	private String html_page;

	public Crawler (String initial_page) {

		//"http://www.imdb.com/title/tt1937390/"

		this.reviews = new Elements();
		this.html_base = initial_page;
		this.html_page = html_base+"reviews?start=0";
		this.crawl();
	}
	
	public Elements getReviews() {
		
		return this.reviews;
	}

	private void crawl() {
		
		do{

			try {
			
				// need http protocol
				this.doc = Jsoup.connect(this.html_page).get();

				// get all reviews
				Elements temp = doc.select("div#tn15content > p");
				this.reviews.addAll(temp.subList(0, Math.min(10, temp.size()-1)));
				Elements links_next = doc.select("a[href] > img[alt=[Next]]");

				//checks if there's more pages
				if (!links_next.isEmpty()) {

					this.html_page = this.html_base + links_next.get(0).parent().attr("href");
				} else {
				
					this.html_page = null;
				}
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