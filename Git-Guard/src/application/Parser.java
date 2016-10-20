package application;

import java.net.URL;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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
		 
	        JSONArray array = (JSONArray) parser.parse(str);
				
	        /*
			 System.out.println("The 2nd element of array");
			 System.out.println(array.get(1));
			 System.out.println();
			
			 JSONObject obj2 = (JSONObject)array.get(1);
			 System.out.println("Field \"1\"");
			 System.out.println(obj2.get("1"));    
			
			 s = "{}";
			 obj = parser.parse(s);
			 System.out.println(obj);
			
			 s = "[5,]";
			 obj = parser.parse(s);
			 System.out.println(obj);
			
			 s = "[5,,2]";
			 obj = parser.parse(s);
			 System.out.println(obj);
		      */                  
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
