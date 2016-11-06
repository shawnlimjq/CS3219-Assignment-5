package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class FileDownloader {
	
	private String fileURL;
	private ArrayList<String> content;
	
	public FileDownloader(String fileURL){
		this.fileURL = fileURL;
		this.content = new ArrayList<String>();
	}
	
	public void downloadFile() {
		try {
			URL buildUrl = new URL(fileURL);
			// read from the URL
			Scanner scan = new Scanner(buildUrl.openStream());
			String str = new String();
			while (scan.hasNextLine()){
			    content.add(scan.nextLine());
			}
			scan.close();
			
		} catch (Exception e){
			System.out.println(e);
		}
	}
	
	public ArrayList<String> getContent(){
		return content;
	}
}
