package application;

public class ContriParser extends Parser {
	
	public ContriParser(String url) {
		super(url);
		convertToAPIURL();
	}

	private void convertToAPIURL(){
		// From : https://github.com/{username}/{repo name}
        // To : https://api.github.com/repos/{username}/{repo name}/contributors
		setUrl(getUrl() + "/contributors");
	}
}
