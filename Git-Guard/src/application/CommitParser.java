package application;

public class CommitParser extends Parser {
	
	private String url;
	
	public CommitParser(String url) {
		super(url);
	}

	private void convertToAPIURL(){
		// From : https://github.com/{username}/{repo name}
        // To : https://api.github.com/repos/{username}/{repo name}/contributors
		
		// Remove front https
		String urlWithoutHttps = url.replace("https://", "");
		// Get /{username}/{repo name}
		String names = urlWithoutHttps.substring(urlWithoutHttps.indexOf("/"));
		url = "https://api.github.com/repos" + names  + "/commits";
	}
}
