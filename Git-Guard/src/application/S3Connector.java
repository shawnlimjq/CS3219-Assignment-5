package application;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

public class S3Connector {

	private static final String accessKey = "";
	private static final String secretAccessKey = "";
	private static final String bucketName = "";
	private static final String folderName = "";
	private static final String fileName = "";
	private AWSCredentials credentials;
	private AmazonS3 s3client;
	
	public S3Connector() {
		init();
	}
	
	private void init() {

	}
	
	public void downloadFile() {
		
	}
	
	public void uploadFile() {
		
	}
	
	
	
	
	
}
