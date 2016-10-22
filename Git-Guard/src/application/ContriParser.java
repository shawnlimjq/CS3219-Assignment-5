package application;

public class ContriParser extends Parser {
	
	private String url;
	
	public ContriParser(String url) {
		super(url);
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
}
