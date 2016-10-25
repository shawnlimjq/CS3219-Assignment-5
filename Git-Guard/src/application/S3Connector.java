package application;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

public class S3Connector {

	private static final String SUFFIX = "/";
	private static final String accessKey = "AKIAJICGFKMYAER4HNEQ";
	private static final String secretAccessKey = "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";
	private String bucketName;
	private String folderName;
	private AWSCredentials credentials;
	private AmazonS3 s3client;
	
	public S3Connector(String bucketName, String folderName) {
		init(bucketName, folderName);
	}
	
	private void init(String bucketName, String folderName) {
		this.bucketName = bucketName;
		this.folderName = folderName;
		credentials = new BasicAWSCredentials(accessKey, secretAccessKey);
		s3client = new AmazonS3Client(credentials);
	}
	
	public void downloadFile(String fileName) {
		try {
			String pathName = folderName + SUFFIX + fileName;
			
			if (s3client.doesObjectExist(bucketName, pathName)) {
				S3Object object = s3client.getObject(new GetObjectRequest(bucketName, pathName));
			    InputStream stream = object.getObjectContent();
			    BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(fileName));
			    
			    int bytesRead;
			    byte[] content = new byte[1024];
			    while ((bytesRead = stream.read(content)) != -1) {
			      outputStream.write(content, 0, bytesRead);
			    }
			    
			    // close resource even during exception
			    stream.close();
			    outputStream.close();
			    
			} else {
				System.out.println("No existing data, creating new data file..");
			}
		    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  catch (AmazonS3Exception e) {
			e.printStackTrace();
		}
	}
	
	public void uploadFile(String fileName) {
		String pathName = folderName + SUFFIX + fileName;
		s3client.putObject(new PutObjectRequest(bucketName, pathName, 
				new File(fileName))
				.withCannedAcl(CannedAccessControlList.PublicRead));
	}
	
}
