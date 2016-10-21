package application;

import java.net.URL;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

public class Parser {
	
	private String url;
	private JSONArray jsonArr;
		
	public Parser(String url){
		this.url = url;
	}
	
	public void parseURL() {
		try{
		    URL buildUrl = new URL(url);

		    JSONParser parser = new JSONParser();
		    // read from the URL
		    Scanner scan = new Scanner(buildUrl.openStream());
		    String str = new String();
		    while (scan.hasNext())
		        str += scan.nextLine();
		    scan.close();
		 
	        jsonArr = (JSONArray) parser.parse(str);
				
		} catch (Exception e){
			System.out.println("Unable to parse URL!");
		}
	}
	
	public JSONArray getJSONArr(){
		return jsonArr;
	}
	
	public void setUrl(String url){
		this.url = url;
	}
	
	public String getUrl(){
		return url;
	}
}
