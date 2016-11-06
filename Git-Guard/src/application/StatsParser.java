package application;


public class StatsParser extends Parser {
	
	public StatsParser(String url) {
		super(url);
		convertToAPIURL();
	}

	private void convertToAPIURL(){
		// From : https://github.com/{username}/{repo name}
        // To : https://api.github.com/repos/{username}/{repo name}/stats/contributors
		setUrl(getUrl() + "/stats/contributors" + MainPage.KEY);
	}
}