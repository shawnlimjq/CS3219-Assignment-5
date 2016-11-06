package application;

public class CommitParser extends Parser {
	
	private String author;
	private String since;
	private String path;
	private String SHA;
	
	// PARAM : author	string	GitHub login or email address by which to filter by commit author.
	// PARAM : since	string	Only commits after this date will be returned. This is a timestamp in ISO 8601 format: YYYY-MM-DDTHH:MM:SSZ.
	public CommitParser(String url, String author, String since, String path) {
		super(url);
		this.author = author;
		this.since = since;
		this.path = path;
		this.SHA = "";
		convertToAPIURL();
	}

	private void convertToAPIURL(){
		// From : https://github.com/{username}/{repo name}
        // To : https://api.github.com/repos/{username}/{repo name}/commits
		String tempAuthor = "";
		String tempSince = "";
		String tempPath = "";
		if(!author.equals("")){
			tempAuthor = "&author=" + author;
		}
		
		if(!since.equals("")){
			tempSince = "&since=" + since;
		}
		
		if(!path.equals("")){
			tempPath = "&path=" + path;
		}
		System.out.println(getUrl()  + "/commits?" + tempAuthor + tempSince + tempPath + "&" +MainPage.KEY.substring(1));
		setUrl(getUrl()  + "/commits?" + tempAuthor + tempSince + tempPath + "&" +MainPage.KEY.substring(1));
	}
	
	public void setSHA(String SHA){
		this.SHA = SHA;
		if(!SHA.equals(""))
		setUrl(getUrl()  + "&sha=" + SHA);
	}
}
