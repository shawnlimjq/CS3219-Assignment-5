package application;

public class CommitParser extends Parser {
	
	private String author;
	private String since;
	private String path;
	
	// PARAM : author	string	GitHub login or email address by which to filter by commit author.
	// PARAM : since	string	Only commits after this date will be returned. This is a timestamp in ISO 8601 format: YYYY-MM-DDTHH:MM:SSZ.
	public CommitParser(String url, String author, String since, String path) {
		super(url);
		this.author = author;
		this.since = since;
		this.path = path;
		convertToAPIURL();
	}

	private void convertToAPIURL(){
		// From : https://github.com/{username}/{repo name}
        // To : https://api.github.com/repos/{username}/{repo name}/commits
		setUrl(getUrl()  + "/commits?author=" + author + "&since=" + since + "&path=" + path);
	}
}
