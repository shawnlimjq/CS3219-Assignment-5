package application;

import java.net.URL;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Parser {
	
	private String url;
	private JSONArray jsonArr;
	private JSONObject jsonObj;
	private String oldUrl;
	
	public Parser(String url){
		this.url = url;
		this.oldUrl = url;
		jsonArr = new JSONArray();
		jsonObj = new JSONObject();
		convertToAPIURL();
	}
	
	private void convertToAPIURL(){
		// From : https://github.com/{username}/{repo name}
        // To : https://api.github.com/repos/{username}/{repo name}
		
		// Remove front https
		String urlWithoutHttps = getUrl().replace("https://", "");
		// Get /{username}/{repo name}
		String names = urlWithoutHttps.substring(urlWithoutHttps.indexOf("/"));
		this.url = "https://api.github.com/repos" + names;
	}
	
	public boolean parseURL() {
		try{
			
		    URL buildUrl = new URL(url);

		    JSONParser parser = new JSONParser();
		    // read from the URL
		    Scanner scan = new Scanner(buildUrl.openStream());
		    String str = new String();
		    while (scan.hasNext())
		        str += scan.nextLine();
		    scan.close();
		    if(this instanceof ContriParser || this instanceof CommitParser || this instanceof FileParser || this instanceof StatsParser){
		    	jsonArr = (JSONArray) parser.parse(str);
		    } else {
		    	jsonObj = (JSONObject) parser.parse(str);
		    }
	        return true;
				
		} catch (Exception e){
			System.out.println("Unable to parse URL! Repo might be private or it doesn't exist!" + e.toString());
			return false;
		}
	}
	
	public JSONArray getJSONArr(){
		return jsonArr;
	}
	
	public JSONObject getJSONObj(){
		return jsonObj;
	}
	
	public void setUrl(String url){
		this.url = url;
	}
	
	public String getUrl(){
		return url;
	}
	
	public String getOldUrl(){
		return oldUrl;
	}
}
