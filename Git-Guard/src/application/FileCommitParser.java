package application;

public class FileCommitParser extends Parser {
	
	private String SHA;

	// PARAM : SHA	string	Commit Unique Identifier
	public FileCommitParser(String url, String SHA) {
		super(url);
		this.SHA = SHA;
		convertToAPIURL();
	}

	private void convertToAPIURL(){
		// From : https://github.com/{username}/{repo name}
        // To : https://api.github.com/repos/{username}/{repo name}/commits/{SHA}
		setUrl(getUrl()  + "/commits/" + SHA + MainPage.KEY);
	}
}
