package edu.iastate.cs311.hw5;

public class WikiCSCrawler {

	public static void main(String[] args){
		
		
		
		try{
			new WikiCrawler("/wiki/I.html", 1000, "WikiCS.txt").crawl();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		
		
	}
}
