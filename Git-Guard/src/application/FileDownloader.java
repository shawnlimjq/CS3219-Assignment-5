package application;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileDownloader {
	
	private String fileURL;
	
	public FileDownloader(String fileURL){
		this.fileURL = fileURL;
	}
	
	public void downloadFile() {
		try {
			// Download file and display : Taken from http://www.codejava.net/java-se/networking/use-httpurlconnection-to-download-file-from-an-http-url
		    URL url = new URL(fileURL);
		    HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		    int responseCode = httpConn.getResponseCode();
			 
			// always check HTTP response code first
			if (responseCode == HttpURLConnection.HTTP_OK) {
			    String fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1,
                        fileURL.length());
			 
				// opens input stream from the HTTP connection
				InputStream inputStream = httpConn.getInputStream();
				String saveFilePath = fileURL.replace("https://", "");
			 
				// opens an output stream to save into file
	            FileOutputStream outputStream = new FileOutputStream(saveFilePath);
		 
	            int bytesRead = -1;
	            byte[] buffer = new byte[1024];
	            while ((bytesRead = inputStream.read(buffer)) != -1) {
	                outputStream.write(buffer, 0, bytesRead);
	            }
		 
	            outputStream.close();
	            inputStream.close();
	 
	            System.out.println("File downloaded");
	            
			} else {
			    System.out.println("No file to download. Server replied HTTP code: " + responseCode);
			}
			httpConn.disconnect();
		} catch (Exception e){
			System.out.println("Failed to download");
		}
	}
}
