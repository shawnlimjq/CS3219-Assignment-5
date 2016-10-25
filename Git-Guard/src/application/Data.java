package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Data {

	public Map<String, ArrayList<String>> dataSet;
	private Properties properties;
	private File file;
	private S3Connector s3client;

	public Data() {
		this.init();
	}

	private void init() {
		dataSet = new HashMap<String, ArrayList<String>>();
		properties = new Properties();
		file = new File("data.properties");
		s3client = new S3Connector("cs3219.ass5", "ass5_data");
	}

	public void save() {
		properties.putAll(dataSet);
		try {
			FileOutputStream fileOut = new FileOutputStream(file);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(dataSet);
			s3client.uploadFile(file.getName());
			out.flush();
			out.close();
			fileOut.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void load() {
		try {
			s3client.downloadFile(file.getName());
			FileInputStream fileIn = new FileInputStream(file);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			dataSet = (HashMap<String, ArrayList<String>>) in.readObject();
			in.close();
			fileIn.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void add(String repo, ArrayList<String> emails) {
		if (dataSet.containsKey(repo)) {
			ArrayList<String> existingMailList = dataSet.get(repo);
			for (int i = 0; i < emails.size(); i++) {
				if (!existingMailList.contains(emails.get(i))) {
					existingMailList.add(emails.get(i));
				}
			}
		} else {
			dataSet.put(repo, emails);
		}
	}
	
	public void add(String repo, String email) {
		ArrayList<String> mailList = new ArrayList<String>();
		mailList.add(email);
		this.add(repo, mailList);
	}
	
	public void delete(String repo) {
		dataSet.remove(repo);
	}
	
	public void reset(String repo) {
		ArrayList<String> mailList = new ArrayList<String>(); 
		this.delete(repo);
		this.add(repo, mailList);
	}

}