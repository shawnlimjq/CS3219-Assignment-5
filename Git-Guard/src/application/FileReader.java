package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileReader {
	
	private String fileURL;
	private ArrayList<String> content;
	
	public FileReader(String fileURL){
		this.fileURL = fileURL;
	}
	
	public void readFile(){
		content.clear();
		Path file = Paths.get(fileURL.replace("https://", ""));
		try (InputStream in = Files.newInputStream(file);
		    BufferedReader reader =
		      new BufferedReader(new InputStreamReader(in))) {
		    String line = null;
		    while ((line = reader.readLine()) != null) {
		    	content.add(line);
		    }
		} catch (IOException x) {
		    System.err.println(x);
		}
	}
	
	public ArrayList<String> getContent(){
		return content;
	}
}
