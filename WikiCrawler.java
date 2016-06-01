package edu.iastate.cs311.hw5;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
/**
 * A web crawler that crawls pages from Wikipedia and constructs a webgraph
 * @author Neh Batwara
 *
 */
public class WikiCrawler {
	
	
	
	private String seedUrl;
	private int reqCounter = 0;
	private int max;
	private String filename;
	public static final String BASE_URL = "http://www.cs.iastate.edu/~pavan";
	private static final ArrayList<String> DO_NOT_EXPLORE = new ArrayList<String>();
	static{
		DO_NOT_EXPLORE.add("#");
		DO_NOT_EXPLORE.add(":");
	}
	public WikiCrawler(String seedUrl, int max, String filename){
		this.seedUrl = seedUrl;
		this.max = max;
		this.filename = filename;
	}
	
	private static ArrayList<String> extractLinks(String doc){
		
		ArrayList<String> retList = new ArrayList<String>();
		if(doc == null){
			return retList;
		}
		int startIndex = doc.indexOf("<p>");
		if(startIndex == -1){
			return retList;
		
		}else{
			startIndex += 3;
		}
		
		int hRefIndex = -1;
		while((hRefIndex = doc.indexOf("href=\"", startIndex))!= -1){
			hRefIndex += 6;
			int linkEndIndex = doc.indexOf("\"", hRefIndex);
			startIndex = linkEndIndex;
			String link = doc.substring(hRefIndex, linkEndIndex).trim();
			boolean explore = true;
			for(String ignoreToken : DO_NOT_EXPLORE){
				if(link.contains(ignoreToken)){
					explore = false;
					break;
				}else{
					//do ntohing here. continue
				}
			}
			if(!explore){
				continue;
			}else{
				//do nothign here. go ahead.
			}
			if(link.contains("en.wikipedia.org")){
				int index = link.indexOf("en.wikipedia.org") +"en.wikipedia.org".length();
				retList.add(link.substring(index).trim());
			}else if (link.startsWith("/wiki")){
				retList.add(link.trim());
			}else{
				//System.err.println("Not adding : " +link);
			}
		}
		//System.out.println(retList);
		return retList;
	}
	/**
	 * Crawls the next page in the queue.  Collects new links until max limit reached; afterwards
	 * this will continue to add edges to the graph only between previously seen vertices.
	 */
	public void crawl(){
		Queue<String> queue = new LinkedList<String>();
		Set<String> visited = new HashSet<String>();
		queue.add(seedUrl);
		Set<Edge> lstEdges=  new LinkedHashSet<Edge>();
		while(!queue.isEmpty() && visited.size() < max){
			String urlString = queue.poll();
			//System.out.println("Reading : " +urlString);
			String textContent = getPageContent(urlString);
			//System.out.println(textContent);
			List<String> lst = extractLinks(textContent);
			if(lst == null || lst.isEmpty()){
				continue;
			}else{
				//do nothing here. 
			}
			visited.add(urlString);
			for(String targetLink : lst){
				if(!visited.contains(targetLink)){
					queue.add(targetLink);
				}
				if(!urlString.equals(targetLink)){
					lstEdges.add(new Edge(urlString, targetLink));
				}else{
					//do nothing
				}
			}
		}
		//Write to file
		try {
			Set<String> set = new HashSet<String>();
			FileOutputStream fos = new FileOutputStream(filename);
			PrintWriter pw = new PrintWriter(fos);
			pw.println(visited.size());
			for(Edge edge : lstEdges){
				if(visited.contains(edge.to)){
					pw.println(edge.from +" " +edge.to);
					set.add(edge.from);
				}
			}
			//System.out.println("SET SIZE : " +set.size());
			set.clear();
			for(String from : visited){
				set.add(from);
			}
			//System.out.println("SET SIZE : " +set.size());
			pw.close();
			fos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	 
	private String getPageContent(String urlString){
		// Wait for 3 seconds after every 100 requests
		if(reqCounter == 100){
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				//do nothing here
			}
		}
		try{
			URL url = new URL(BASE_URL +urlString);
		//	System.out.println("Reading " +url.toString());
			InputStream is = url.openStream();
			reqCounter++;
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			StringBuffer content = new StringBuffer();
			String line = null;
			while((line = br.readLine()) != null){
				//System.out.println("Line : " +line);
				content.append(line +"\n");
			}
			System.out.println("Content : " +content.toString());
			return content.toString();
		}catch(Exception exc){
			//exc.printStackTrace();
			System.out.println(exc.getMessage());
			return null;
		}
	}

}
