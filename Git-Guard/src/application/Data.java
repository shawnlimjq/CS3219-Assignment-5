package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.zip.CheckedInputStream;

import org.apache.http.impl.execchain.MainClientExec;

import com.amazonaws.services.budgets.model.TimeUnit;
import com.amazonaws.services.simpleworkflow.flow.worker.SynchronousActivityTaskPoller;

public class Data {

	public Map<String, ArrayList<String>> dataSet;
	public Map<String, Date> lastCheckTime;
	private Properties properties;
	private File dataFile, timeFile;
	private S3Connector s3client;

	public Data() {
		this.init();
	}

	private void init() {
		dataSet = new HashMap<String, ArrayList<String>>();
		lastCheckTime = new HashMap<String, Date>();
		properties = new Properties();
		dataFile = new File("data.properties");
		timeFile = new File("time.properties");
		s3client = new S3Connector("cs3219.ass5", "ass5_data");
	}

	public void save() {
		properties.putAll(dataSet);
		try {
			FileOutputStream dataFileOut = new FileOutputStream(dataFile);
			ObjectOutputStream dataOut = new ObjectOutputStream(dataFileOut);
			dataOut.writeObject(dataSet);
			
			FileOutputStream timeFileOut = new FileOutputStream(timeFile);
			ObjectOutputStream timeOut = new ObjectOutputStream(timeFileOut);
			timeOut.writeObject(dataSet);
			
			s3client.uploadFile(dataFile.getName());
			s3client.uploadFile(timeFile.getName());
			
			
			dataOut.flush();
			dataOut.close();
			
			timeOut.flush();
			timeOut.close();
			
			dataFileOut.close();
			timeFileOut.close();
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
			s3client.downloadFile(dataFile.getName());
			s3client.downloadFile(timeFile.getName());
			
			FileInputStream dataFileIn = new FileInputStream(dataFile);
			ObjectInputStream dataIn = new ObjectInputStream(dataFileIn);
			
			FileInputStream timeFileIn = new FileInputStream(timeFile);
			ObjectInputStream timeIn = new ObjectInputStream(timeFileIn);
			
			dataSet = (HashMap<String, ArrayList<String>>) dataIn.readObject();
			lastCheckTime = (HashMap<String, Date>) timeIn.readObject();
			
			dataIn.close();
			dataFileIn.close();
			
			timeIn.close();
			timeFileIn.close();
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

	public void checkIn(String repo, Date newTimestamp) {
		if (lastCheckTime.containsKey(repo)) {
			if (lastCheckTime.get(repo).after(newTimestamp)) {
				System.out.println("New timestamp cannot be before the current timestamp!");
			} else {
				lastCheckTime.put(repo, newTimestamp);
			}
		} else {
			lastCheckTime.put(repo, newTimestamp);
		}
	}
	
}