package application;

public class FileParser extends Parser {
	
	private String path;
	
	public FileParser(String url, String path) {
		super(url);
		this.path = path;
		convertToAPIURL();
	}

	private void convertToAPIURL(){
		// From : https://github.com/{username}/{repo name}
        // To : https://api.github.com/repos/{username}/{repo name}/contents
		setUrl(getUrl()  + "/contents" + path);
	}
}
