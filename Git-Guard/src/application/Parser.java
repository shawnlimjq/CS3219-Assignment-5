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
		jsonArr = new JSONArray();
	}
	
	private void convertToAPIURL(){
		// From : https://github.com/{username}/{repo name}
        // To : https://api.github.com/repos/{username}/{repo name}/contributors
		
		// Remove front https
		String urlWithoutHttps = url.replace("https://", "");
		// Get /{username}/{repo name}
		String names = urlWithoutHttps.substring(urlWithoutHttps.indexOf("/"));
		url = "https://api.github.com/repos" + names  + "/contributors";
	}
	
	public void parseURL() {
		try{
			
			convertToAPIURL();
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
