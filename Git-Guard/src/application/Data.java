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

	public Data() {
		this.init();
	}

	private void init() {
		dataSet = new HashMap<String, ArrayList<String>>();
		properties = new Properties();
		file = new File("data.properties");
	}

	public void save() {
		properties.putAll(dataSet);
		try {
			FileOutputStream fileOut = new FileOutputStream(file);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(dataSet);
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

	/*public static void main(String[] args) {
		S3test s3test = new S3test();
		ArrayList<String> emailList = new ArrayList<String>();
		emailList.add("123");
		emailList.add("456");
		s3test.dataSet.put("abc", emailList);
		s3test.save();
		
		S3test s2test = new S3test();
		emailList.remove(0);
		emailList.add("789");
		s2test.load();
		s2test.add("abc", emailList);
		s2test.save();
		
		
		S3test s1test = new S3test();
		s1test.load();
		System.out.println(s1test.dataSet.get("abc").get(1));
		System.out.println(s1test.dataSet.get("abc").get(2));
	}*/

}
