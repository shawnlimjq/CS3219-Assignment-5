package application;
	
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.Glow;
import javafx.scene.effect.Reflection;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.Pair;


public class MainPage extends AnchorPane {
	
	private static final String MAIN_PAGE_FXML_URL = "MainPage.fxml";
	private static ObservableList<PieChart.Data> list = FXCollections.observableList(new ArrayList<PieChart.Data>());
	private static ObservableList<PieChart.Data> dList = FXCollections.observableList(new ArrayList<PieChart.Data>());
	private static ObservableList<String> contributors  = FXCollections.observableList(new ArrayList<String>());
	
	private static final String CONTRIBUTIONS = "contributions";
	private static final String LOGIN = "login";
	private static final String CREATED_AT = "created_at";
	private static final String COMMIT = "commit";
	private static final String AUTHOR = "author";
	private static final String DATE = "date";
	private static final String WEEKS = "weeks";
	private static final String SOURCE = "source";
	private static final String MESSAGE = "message";
	private static final String NAME = "name";
	private static final String ADDITION = "a";
	private static final String DELETION = "d";
	private static final String SHA = "sha";
	private static final String FILES = "files";
	private static final String FILENAME = "filename";
	private static final String PATCH = "patch";
	
	TranslateTransition openPanel;
	TranslateTransition closePanel;
	private static final int TRANSITION_TIME = 350;
	private static final int STARTPOSITION = 0;
	private boolean noError;
	private JSONObject jsonObj;
	
	// Can store at a storage
	private HashMap<String, HashMap<String, Integer>> committerCommits;
	private ArrayList<String> commitSHAS;
	
	private Parser mainParser;
	private Data data;
	
	private boolean loadA = false;
	private boolean loadB = false;
	private boolean loadC = false;
	private boolean loadD = false;
	
	@FXML
	private Label gitGuardLabel;
	@FXML
	private Label errorLabel;
	@FXML
	private Label notifyTitle;
	@FXML
	private TextField githubRepoInput;
	@FXML
	private ChoiceBox notificationHours;
	@FXML
	private ChoiceBox notificationMinutes;
	@FXML
	private TextArea notifyEmail;
	@FXML
	private TabPane mainTabPane;
	@FXML
	private TabPane tabCTabPane;
	@FXML
	private Tab tabA;
	@FXML
	private Tab tabB;
	@FXML
	private Tab tabC;
	@FXML
	private Tab tabCTabA;
	@FXML
	private Tab tabCTabB;
	@FXML
	private Tab tabCTabC;
	@FXML
	private Tab tabD;
	@FXML
	private Tab tabE;
	@FXML
	private AnchorPane tabAAP;
	@FXML
	private AnchorPane tabBAP;
	@FXML
	private AnchorPane tabCAP;
	@FXML
	private AnchorPane tabDAP;
	@FXML
	private AnchorPane tabEAP;
	@FXML
	private AnchorPane hiddenMenu;
	@FXML
	private ScrollPane tabASP;
	@FXML
	private ScrollPane tabBSP;
	@FXML
	private ScrollPane tabCSP;
	@FXML
	private ScrollPane tabDSP;
	@FXML
	private ScrollPane tabESP;
	@FXML
	private PieChart piechartA;
	@FXML
	private PieChart piechartLine;
	@FXML
	private BarChart<String, Integer> contributorChart;
	@FXML
	private BarChart<String, Integer> fileCommitHistory;
	@FXML
	private BarChart<String, Integer> lineCommitHistory;
	@FXML
	private ScatterChart<String, Number> contributorScatter;
	@FXML
	private ChoiceBox<String> contributorChoice;
	@FXML
	private ListView listViewFiles;
	@FXML
	private ListView listViewLines;
	@FXML
	private DatePicker startDate;
	@FXML
	private Button addBtn;
	@FXML
	private Button addNoti;
	
	private static MainPage instance = null;
	private int lineFrom;
	private int lineTo;
	
	public String transferURL="";
	
	//private String pieColors[];

	private MainPage() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(MAIN_PAGE_FXML_URL));
		loadFromFxml(fxmlLoader);
	}

	public static MainPage getInstance() throws IOException {
		if (instance == null) {
			instance = new MainPage();
		}
		return instance;
	}

	private void loadFromFxml(FXMLLoader fxmlLoader) throws IOException {
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		fxmlLoader.load();
		initialise();
	}

	private void checkError(){
		if (noError==false){
			errorLabel.setVisible(true);
		} else{
			errorLabel.setVisible(false);
		}
	}
	
	/*private void applyCustomPieChartColor(ObservableList<PieChart.Data> pieChartData) {
		  int i = 0;
		  for (PieChart.Data data : pieChartData) {
		    data.getNode().setStyle(
		      "-fx-pie-color: " + pieColors[i % pieColors.length] + ";"
		    );
		    i++;
		  }
		}
	
	private void initialisePieColors(){
		pieColors = new String[20];
		pieColors[0] = "#ffd700";
		pieColors[1] = "#ffa500";
		pieColors[2] = "#860061";
		pieColors[3] = "#adff2f";
		pieColors[4] = "#ff5700";
		pieColors[5] = "#ffd700";
		pieColors[6] = "#ffd700";
		pieColors[7] = "#ffd700";
		pieColors[8] = "#ffd700";
		pieColors[9] = "#ffd700";
		pieColors[10] = "#ffd700";
		pieColors[11] = "#ffd700";
		pieColors[12] = "#ffd700";
		pieColors[13] = "#ffd700";
		pieColors[14] = "#ffd700";
		pieColors[15] = "#ffd700";
		pieColors[16] = "#ffd700";
		pieColors[17] = "#ffd700";
		pieColors[18] = "#ffd700";
		pieColors[19] = "#ffd700";
	}
	*/
	public void initialise() {
		//initialisePieColors();
		initializeHiddenPanel();
		contributorChart.setAnimated(false);
		listViewLines.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		for(int i=0;i<24;i++){
			notificationHours.getItems().add(i);
		}
		for(int i=0;i<60;i++){
			notificationMinutes.getItems().add(i);
		}
		listViewLines.setOnKeyPressed(new EventHandler<KeyEvent>() {

            public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.ENTER)) {
					ObservableList<Integer> selectedItems = listViewLines.getSelectionModel().getSelectedIndices();
					lineFrom = 999999999;
					lineTo = -1;
					for (int i : selectedItems) {
						if(i<=lineFrom){
							lineFrom=i;
						}
						if(i>=lineTo){
							lineTo=i;
						}
					}
					displayLinesHistory(transferURL,lineFrom+1,lineTo+1);
				}
			}
        });
		
		Platform.runLater( () -> this.requestFocus() );
		data = new Data();
		
		
		githubRepoInput.setOnKeyPressed(new EventHandler<KeyEvent>() {

			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.ENTER)) {
					// Testing link - https://github.com/ymymym/MaterialDateTimePicker
					mainParser = new Parser(githubRepoInput.getText());
					noError = mainParser.parseURL();
					checkError();
					data.checkIn(mainParser.getOldUrl(), new Date());

					data.save();
					
					if(noError==true){
						
						loadA = false;
						loadB = false;

						jsonObj= mainParser.getJSONObj();
						mainTabPane.visibleProperty().set(true);
						mainTabPane.disableProperty().set(false);
						mainTabPane.getSelectionModel().select(tabA);
						initTabA();
						loadA = true;
						
					}else{
						mainTabPane.visibleProperty().set(false);
						mainTabPane.disableProperty().set(true);
					}
				}
			}
		});
		
		tabA.setOnSelectionChanged(new EventHandler<Event>() {
            @Override
            public void handle(Event t) {
            	// Condition ensures init ran only once
                if (tabA.isSelected() && !loadA) {
                	initTabA();
                	loadA = true;
                }
            }
        });
		
		tabB.setOnSelectionChanged(new EventHandler<Event>() {
            @Override
            public void handle(Event t) {
        		// Condition ensures init ran only once
                if (tabB.isSelected() && !loadB) {
                	initTabB();
                	loadB = true;
                }
            }
        });
		
		tabC.setOnSelectionChanged(new EventHandler<Event>() {
            @Override
            public void handle(Event t) {
        		// Condition ensures init ran only once
                if (tabC.isSelected() && !loadC) {
                	initTabC();
                	loadC = true;
                }
            }
        });
		
		tabD.setOnSelectionChanged(new EventHandler<Event>() {
            @Override
            public void handle(Event t) {
        		// Condition ensures init ran only once
                if (tabD.isSelected() && !loadD) {
                	updateTabD();
                	loadD = true;
                }
            }
        });
		
		addBtn.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent t) {
                // Update UI here
            	updateTabB();
            	// Use hashmap to populate histo
            	// populateHisto();
            	populateScatter();
            }
        });
		
		addNoti.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
            @Override
            public void handle(MouseEvent t) {
                // Wenhao, link to your s3 service here. Email address delimited by new line
            	String emails = notifyEmail.getText();
            	if(!emails.equals("")){
	            	ArrayList<String> emailAdds = new ArrayList<String>(Arrays.asList(emails.split("\n")));
	            	
	            	if(emailAdds.size()!= 0 && mainParser != null){
	            		// Do something with mainParser.getUrl() and the emailAdds. 
	            		// notificationDays.getText() and notificationHours.getTexT() as well
	            		data.add(mainParser.getOldUrl(), emailAdds);
	            		data.save();
	            	}
            	}
            }
		});
	}
	
	private void populateHisto(){
		XYChart.Series<String, Integer> series = new XYChart.Series<>();
    	contributorChart.getData().clear();
    	for (Map.Entry<String, HashMap<String, Integer>> committerEntry : committerCommits.entrySet()) {
    	    String committer = committerEntry.getKey();
    	    HashMap<String, Integer> dateCount = committerEntry.getValue();
    	    for(Map.Entry<String, Integer> dateEntry : dateCount.entrySet()){
    	    	String date = dateEntry.getKey();
    	    	int count = dateEntry.getValue();
    	    	series.getData().add(new XYChart.Data<>(date, count));
    	    }
    	}
    	contributorChart.getData().add(series);
	}
	
	/*private void populateScatter(){
		XYChart.Series<String, Integer> series = new XYChart.Series<>();
    	for (Map.Entry<String, HashMap<String, Integer>> committerEntry : committerCommits.entrySet()) {
    	    String committer = committerEntry.getKey();
    	    HashMap<String, Integer> dateCount = committerEntry.getValue();
    	    for(Map.Entry<String, Integer> dateEntry : dateCount.entrySet()){
    	    	String date = dateEntry.getKey();
    	    	int count = dateEntry.getValue();
    	    	series.getData().add(new XYChart.Data<>(date, count));
    	    }
    	}
    	contributorScatter.getData().add(series);
	}*/
	
	private Date addDay(Date date){
	    Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 1);
        return cal.getTime();
	}
	
	private void populateScatter(){
		
		Map<Date, Integer> m = new HashMap<Date, Integer>();
		Map<Date, Integer> m1 = new TreeMap();
		ObservableList<String> dateXAxisList = FXCollections.observableList(new ArrayList<String>());
		
		String sourceDate = startDate.getValue().toString();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date myDate;
		try {
			
			myDate = format.parse(sourceDate);
			Date currDate = new Date();
			
			while(!format.format(myDate).equals(format.format(currDate))){
				dateXAxisList.add(format.format(myDate));
				myDate = addDay(myDate);
			}
			
			dateXAxisList.add(format.format(myDate));
			
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//CategoryAxis dateXAxis = new CategoryAxis(dateXAxisList);
		
		//contributorScatter =  new ScatterChart<String,Number>(dateXAxis, new NumberAxis());
		
		Series<String, Number> series = new XYChart.Series<>();
    	for (Map.Entry<String, HashMap<String, Integer>> committerEntry : committerCommits.entrySet()) {
    	    String committer = committerEntry.getKey();
    	    HashMap<String, Integer> dateCount = committerEntry.getValue();
    	    
    	    /*for(Map.Entry<String, Integer> dateEntry : dateCount.entrySet()){
    	    	try {
					m.put(new java.sql.Date(format.parse(dateEntry.getKey()).getTime()), dateEntry.getValue());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    	    }*/
			for (int i = 0; i < dateXAxisList.size(); i++) {
				if (dateCount.containsKey(dateXAxisList.get(i))) {
					try {
						m.put(new java.sql.Date(format.parse(dateXAxisList.get(i)).getTime()),
								dateCount.get(dateXAxisList.get(i)));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					try {
						m.put(new java.sql.Date(format.parse(dateXAxisList.get(i)).getTime()), 0);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
    	    
    	    
    	    m1 = new TreeMap(m);
    	    for(Entry<Date, Integer> dateEntry : m1.entrySet()){
    	    	String date = dateEntry.getKey().toString();
    	    	System.out.println(date);
    	    	Number count = dateEntry.getValue();
    	    	System.out.println(count);
    	    	series.getData().add(new XYChart.Data<>(date, count));
    	    }
    	}
    	contributorScatter.getData().add(series);
	}
	
	
	private void initTabA(){
		// Tab A
		ContriParser contriParser = new ContriParser(githubRepoInput.getText());
		noError = contriParser.parseURL();
		checkError();
		
		if(noError){
			// Update UI with parser's JSONArray
			JSONArray jsonArr = contriParser.getJSONArr();
			list.clear();
			piechartA.setData(list);
			//Use this to add data to piechart
			for(int i =0 ; i< jsonArr.size(); i++){
	
				JSONObject innerJsonObj = (JSONObject) jsonArr.get(i);
				list.add(new PieChart.Data((String) innerJsonObj.get(LOGIN), (Long) innerJsonObj.get(CONTRIBUTIONS)));
				contributors.add((String) innerJsonObj.get(LOGIN));
				
			}
			list.forEach(data ->
             data.nameProperty().bind(
                     Bindings.concat(
                             data.getName(), "-", data.pieValueProperty(), " Commits"
                     )
             )
     );
			//applyCustomPieChartColor(list);
		}
	}
	
	private void initTabB(){
		// Tab B
		if(noError){
			JSONObject sourceObj = (JSONObject) jsonObj.get(SOURCE);
			if(sourceObj!=null){
				disableDate((String) sourceObj.get(CREATED_AT));
			} else{
				disableDate((String) jsonObj.get(CREATED_AT));
			}
			contributorChoice.setItems(contributors);
		}
	}
	
	private void initTabC(){
		// Tab C
		updateFileChooser("");
	}
	
	// Keep going in this method if file or directory clicked
	private void updateFileChooser(String initPath){
		FileParser fileParser = new FileParser(mainParser.getOldUrl(), initPath);
		noError = fileParser.parseURL();
		checkError();
		
		if(noError) {
			// Update UI with parser's JSONArray
			JSONArray jsonArr = fileParser.getJSONArr();
			listViewFiles.getItems().clear();
			//Use this to add data to directory listing
			if(initPath!=""){
				listViewFiles.getItems().add("Back");
			}
			for(int i =0 ; i< jsonArr.size(); i++){
				// Show first lvl objects
				// TODO
				JSONObject innerJsonObj = (JSONObject) jsonArr.get(i);
				String [] path = ((String) innerJsonObj.get("path")).split("/");
				if(innerJsonObj.get("type").equals("file")){
					// Show as file. Show commit when clicked
					String fileName = path[path.length-1];
					listViewFiles.getItems().add(fileName);
				} else if (innerJsonObj.get("type").equals("dir")){
					// Show as directory
					String dirName = path[path.length - 1];
					listViewFiles.getItems().add(dirName);
				}
			}
			listViewFiles.setOnMouseClicked(new EventHandler<MouseEvent>() {

		        @Override
		        public void handle(MouseEvent event) {
		        	if(listViewFiles.getItems().get(0)!="Back"){
		        		JSONObject innerJsonObj = (JSONObject) jsonArr.get(listViewFiles.getSelectionModel().getSelectedIndex());
		        		if(innerJsonObj.get("type").equals("file")){
							displayCommits(initPath+"/"+listViewFiles.getSelectionModel().getSelectedItem().toString());
						} else{
							updateFileChooser(initPath+"/"+listViewFiles.getSelectionModel().getSelectedItem().toString());
						}
		        	}else{
		        	
		        	if(listViewFiles.getSelectionModel().getSelectedItem().toString()!="Back"){
		        		JSONObject innerJsonObj = (JSONObject) jsonArr.get(listViewFiles.getSelectionModel().getSelectedIndex()-1);
		        		if(innerJsonObj.get("type").equals("file")){
		        			transferURL = initPath+"/"+listViewFiles.getSelectionModel().getSelectedItem().toString();
							displayCommits(transferURL);
							displayContent(transferURL);
						} else{
							updateFileChooser(initPath+"/"+listViewFiles.getSelectionModel().getSelectedItem().toString());
						}
		        	}else{
		        		int index=initPath.lastIndexOf('/');
		        		if(index!=0){
		        			updateFileChooser(initPath.substring(0,index));
		        		}else{
		        			updateFileChooser("");
		        		}
		        	}
		        	}
		        }
		    });
		}
	}
	
	// Call this after a file is clicked
	private void displayCommits(String filePath){
		commitSHAS = new ArrayList<String>();
		CommitParser commitParser = new CommitParser(mainParser.getOldUrl(), "", "", filePath);
		noError = commitParser.parseURL();
		checkError();
		XYChart.Series<String, Integer> series = new XYChart.Series<>();
		HashMap<String, Integer> commitCount = new HashMap<String, Integer>();
		fileCommitHistory.getData().clear();
		
		if(noError) {
			// Update UI with parser's JSONArray
			JSONArray jsonArr = commitParser.getJSONArr();

			for(int i = 0 ; i< jsonArr.size(); i++){
				JSONObject commitObj = (JSONObject) jsonArr.get(i);
				commitObj = (JSONObject) commitObj.get(COMMIT);
				JSONObject committerObj = (JSONObject) commitObj.get(AUTHOR);
				
				String msg = (String) commitObj.get(MESSAGE);
				String date = (String) committerObj.get(DATE);
				String name = (String) committerObj.get(NAME);
				String sha = (String) committerObj.get(SHA);
				commitSHAS.add(sha);
				// Show MSG , date , committer 
				// TODO : update UI with this 3 data
				if (!commitCount.containsKey(name)){
					commitCount.put(name, 0);
				}else{
					commitCount.put(name, commitCount.get(name)+1);
				}
			}
			for(Map.Entry<String, Integer> dateEntry : commitCount.entrySet()){
    	    	String name = dateEntry.getKey();
    	    	int count = dateEntry.getValue();
    	    	System.out.println(name+" " +count);
    	    	series.getData().add(new XYChart.Data<>(name, count));
    	    }
			fileCommitHistory.getData().add(series);
		}
	}
	
	// Call this if select code chunk is clicked
	private void displayContent(String fileURL){
		listViewLines.getItems().clear();
		FileDownloader fileDownloader = new FileDownloader(fileURL);
		fileDownloader.downloadFile();
		
		FileReader fileReader = new FileReader(fileURL);
		fileReader.readFile();
		
		ArrayList<String> content = fileReader.getContent();
		
		// TODO : print the content. They are in line
		for(int i =0 ; i < content.size(); i++){
			listViewLines.getItems().add(content.get(i));
		}
	}
	
	private void displayLinesHistory(String filePath, int from, int to) {
		// 0 is the latest commit
		for(int shaIndex =0 ; shaIndex < commitSHAS.size(); shaIndex++){
			// Commit SHA
			FileCommitParser fileCommitParser = new FileCommitParser(mainParser.getOldUrl(), commitSHAS.get(shaIndex));
			noError = fileCommitParser.parseURL();
			checkError();
			
			if(noError) {
				JSONObject jsonObj = fileCommitParser.getJSONObj();
				JSONArray jsonFiles = (JSONArray) jsonObj.get(FILES);
				for(int i = 0 ; i< jsonFiles.size(); i++){
					JSONObject jsonFile = (JSONObject) jsonFiles.get(i);
					String filename = (String) jsonFile.get(FILENAME);
					// TODO: Add Exception handling
					if(filename.equals(filePath.substring(filePath.lastIndexOf("/") + 1))){
						// Do something with the patch if it's the file
						// @@ -0,0 +1,19 @@
						String patch = (String) jsonFile.get(PATCH);
						String [] patches = patch.split("@@");
						String [] addDelete = patches[1].trim().split(" ");
						int startAdd = Integer.parseInt(addDelete[1].split(",")[0].substring(1)); 
						int startAddMax = startAdd + Integer.parseInt(addDelete[1].split(",")[1]) - 1;
						
						int startDel = Integer.parseInt(addDelete[0].split(",")[0].substring(1)); 
					    int startDelMax = startAdd + Integer.parseInt(addDelete[0].split(",")[1]) - 1;
						
						String [] lines = patch.split("\n");
						String header = lines[0];
						
						if(from <= startAddMax){
							for (int z = 1 ; z < lines.length ; z++){
								
								// Filter out lines more than to
								if(startAdd + z - 1 > to) {
									break;
								}
								
								if(lines[z].charAt(0) != '-' && startAdd + z - 1 >= from){
									// Populate right list view
								}
							}
						}
						
						if(from <= startDelMax){
							for (int z = 1 ; z < lines.length ; z++){
	
								// Filter out lines more than to
								if(startDel + z - 1 > to){
									break;
								}
								
								if(lines[z].charAt(0) != '+'  && startDel + z - 1 >= from){
									// Populate left list view
								}
							}
						}
					}
				}
			}
		}
	}
	
	private void updateTabD(){
		// Tab D
		CommitParser commitParser = new CommitParser(mainParser.getOldUrl(), "", "", "");
		noError = commitParser.parseURL();
		checkError();
		if(noError) {
			// Contributor is key, value is number of lines for the committer
			HashMap<String, Integer> contributorHm = new HashMap<String, Integer>();
			// File path is key, value is line, author
			HashMap<String, HashMap<String,String>> fileHm = new HashMap<String, HashMap<String,String>>(); 

			ArrayList<String> SHAs = new ArrayList<String>();
			ArrayList<String> contributors = new ArrayList<String>();
			// Do something with the jsonArr, commit per day maybe. jsonArr.get(0) stands for the latest commit
			String tempSHA = "";
			int z = 0;
			JSONArray jsonArr = commitParser.getJSONArr();
			System.out.println(jsonArr.size());
			do {
				
				jsonArr = commitParser.getJSONArr();
				
				for(int i = z ; i < jsonArr.size(); i++) {
					// Consider more than 30 commits
					JSONObject commitObj = (JSONObject) jsonArr.get(i);
					tempSHA = (String) commitObj.get(SHA);
					JSONObject authorObj = (JSONObject) ((JSONObject) commitObj.get(COMMIT)).get(AUTHOR);
					contributors.add((String) authorObj.get(NAME));
					SHAs.add(tempSHA);
				}
				
				if(jsonArr.size() == 30){
					commitParser.setSHA(tempSHA);
					noError = commitParser.parseURL();
					checkError();
					z = 1;
				}
			} while(jsonArr.size() == 30 && noError);
			
			for(int i = SHAs.size()-1 ; i > -1; i--){
				
				FileCommitParser fileCommitParser = new FileCommitParser(mainParser.getOldUrl(), SHAs.get(i));
				noError = fileCommitParser.parseURL();
				checkError();
				if(noError){
					
					JSONObject jsonFileObj = fileCommitParser.getJSONObj();
					JSONArray files = (JSONArray) jsonFileObj.get(FILES);
					
					for(int j = 0 ; j < files.size(); j++){
						
						JSONObject file = (JSONObject) files.get(j);
						String line = (String) file.get(PATCH);
						String filename = (String) file.get(FILENAME);
						String [] lines = line.split("\n");
						
						for(int k = 0 ; k < lines.length; k++){
							System.out.println(lines[k]);
							if(lines[k].charAt(0) == '-'){
								
								HashMap<String, String> temp = fileHm.get(filename);
								lines[k] = "+" + lines[k].substring(1);
								temp.remove(lines[k]);
								
								// This condition should run everytime
								if(contributorHm.containsKey(contributors.get(i))){
									 int count = contributorHm.get(contributors.get(i)) - 1;
									 contributorHm.put(contributors.get(i), count);
								}
								
							} else if (lines[k].charAt(0) == '+'){
								
								HashMap<String, String> temp = fileHm.get(filename);
								temp.put(lines[k], contributors.get(i));
								fileHm.put(filename, temp);
								int count = 1;
								if(contributorHm.containsKey(contributors.get(i))){
									 count = contributorHm.get(contributors.get(i)) + 1;
								}
								
								contributorHm.put(contributors.get(i), count);
							}
						}
					}
				}
			}
			
			for (Map.Entry<String, Integer> entry : contributorHm.entrySet()) {
			    String key = entry.getKey();
			    int value = entry.getValue();
			    dList.add(new PieChart.Data(key, value));
			}
			
			dList.forEach(data -> data.nameProperty().bind(Bindings.concat(data.getName(), "-", data.pieValueProperty(), " Lines")));
			
		}
	}
	
	
	public void updateTabB(){
		committerCommits = new HashMap<String, HashMap<String, Integer>>();
		DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
		String formattedDate = startDate.getValue().format(formatter) + "T00:00:00Z";
		String committerName = contributorChoice.getValue();
		CommitParser commitParser = new CommitParser(mainParser.getOldUrl(), committerName, formattedDate, "");
		noError = commitParser.parseURL();
		checkError();
		
		if(noError){
			// Date is key, Value is number of commits for the committer
			HashMap<String, Integer> hm = new HashMap<String, Integer>();
			// Do something with the jsonArr, commit per day maybe. jsonArr.get(0) stands for the latest commit
			String tempSHA = "";
			int z = 0;
			JSONArray jsonArr = commitParser.getJSONArr();
			
			do {
				
				jsonArr = commitParser.getJSONArr();
				
				for(int i = z ; i < jsonArr.size(); i++) {
					
					// Consider more than 250 commits
					JSONObject commitObj = (JSONObject) jsonArr.get(i);
					tempSHA = (String) commitObj.get(SHA);
					commitObj = (JSONObject) commitObj.get(COMMIT);
					JSONObject committerObj = (JSONObject) commitObj.get(AUTHOR);
					String date = (String) committerObj.get(DATE);
				
					date = date.substring(0, date.indexOf("T"));
					
					int noOfCommits = 0;
					if(hm.get(date) != null){
						noOfCommits = hm.get(date);
					}
					
					noOfCommits += 1;
					hm.put(date, noOfCommits);
				}
				
				if(jsonArr.size() == 30){
					commitParser.setSHA(tempSHA);
					noError = commitParser.parseURL();
					checkError();
					z = 1;
				}
			} while(jsonArr.size() == 30 && noError);
			
			committerCommits.put(committerName, hm);
		}
	}
	
	// To disable date before creation
	public void disableDate(String createDateStr){
		DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
		//convert String to LocalDate
		LocalDate createDate = LocalDate.parse(createDateStr.replace("Z", ""), formatter);
		 startDate.setValue(LocalDate.now());
	        final Callback<DatePicker, DateCell> dayCellFactory = 
	            new Callback<DatePicker, DateCell>() {
	                @Override
	                public DateCell call(final DatePicker datePicker) {
	                    return new DateCell() {
	                        @Override
	                        public void updateItem(LocalDate item, boolean empty) {
	                            super.updateItem(item, empty);
	                           
	                            if (item.isBefore(createDate)
	                                ) {
	                                    setDisable(true);
	                                    setStyle("-fx-background-color: #ffc0cb;");
	                            }   
	                    }
	                };
	            }
	        };
	        startDate.setDayCellFactory(dayCellFactory);
	}
	
	/*
	 * Setup hidden panel
	 */
	private void initializeHiddenPanel() {
		openPanel = new TranslateTransition(new Duration(TRANSITION_TIME), hiddenMenu);
		openPanel.setToX(STARTPOSITION);
		closePanel = new TranslateTransition(new Duration(TRANSITION_TIME), hiddenMenu);
	}
	
	/*
	 * Toggle hidden panel
	 */
	public void toggleHiddenPanel() {
		if (hiddenMenu.getTranslateX() != STARTPOSITION) {
			openPanel.play();
		} else {
			assert (hiddenMenu.getTranslateX() == STARTPOSITION);
			closePanel.setToX(+(hiddenMenu.getWidth()));
			closePanel.play();
		}
	}
	
	public String buildInput(String repo) {
		String input = "cs3219nus@gmail.com\n";
		
		ArrayList<String> emails = data.dataSet.get(mainParser.getOldUrl());
		for (int i = 0; i < emails.size(); i++) {
			input += emails.get(i);
		}
		
		input += "\n" + mainParser.getOldUrl() +"\n";
		
		input += data.lastCheckTime.get(mainParser.getOldUrl()).getTime();
		
		return input;
	}
}
