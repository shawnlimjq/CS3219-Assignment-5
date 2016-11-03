package application;
	
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import javafx.util.Duration;


public class MainPage extends AnchorPane {
	
	private static final String MAIN_PAGE_FXML_URL = "MainPage.fxml";
	private static ObservableList<PieChart.Data> list = FXCollections.observableList(new ArrayList<PieChart.Data>());
	private static ObservableList<String> contributors  = FXCollections.observableList(new ArrayList<String>());
	
	private static final String CONTRIBUTIONS = "contributions";
	private static final String LOGIN = "login";
	private static final String CREATED_AT = "created_at";
	private static final String COMMIT = "commit";
	private static final String COMMITTER = "committer";
	private static final String AUTHOR = "author";
	private static final String DATE = "date";
	private static final String WEEKS = "weeks";
	private static final String SOURCE = "source";
	private static final String MESSAGE = "message";
	private static final String NAME = "name";
	private static final String ADDITION = "a";
	private static final String DELETION = "d";
	
	TranslateTransition openPanel;
	TranslateTransition closePanel;
	private static final int TRANSITION_TIME = 350;
	private static final int STARTPOSITION = 0;
	private boolean checkError;
	private JSONObject jsonObj;
	
	// Can store at a storage
	private HashMap<String, HashMap<String, Integer>> committerCommits;
	private HashMap<String, HashSet<String>> directoryListing;
	
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
	private TextField notificationHours;
	@FXML
	private TextField notificationDays;
	@FXML
	private TextArea notifyEmail;
	@FXML
	private TabPane mainTabPane;
	@FXML
	private Tab tabA;
	@FXML
	private Tab tabB;
	@FXML
	private Tab tabC;
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
	private ScatterChart<String, Integer> contributorScatter;
	@FXML
	private ChoiceBox<String> contributorChoice;
	@FXML
	private DatePicker startDate;
	@FXML
	private Button addBtn;
	@FXML
	private Button addNoti;
	
	private static MainPage instance = null;

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
		if (checkError==false){
			errorLabel.setVisible(true);
		} else{
			errorLabel.setVisible(false);
		}
		
	}
	
	public void initialise() {
		initializeHiddenPanel();
		contributorChart.setAnimated(false);
		Platform.runLater( () -> this.requestFocus() );
		data = new Data();
		
		githubRepoInput.setOnKeyPressed(new EventHandler<KeyEvent>() {

			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.ENTER)) {
					// Testing link - https://github.com/ymymym/MaterialDateTimePicker
					mainParser = new Parser(githubRepoInput.getText());
					checkError = mainParser.parseURL();
					checkError();
					data.checkIn(mainParser.getUrl(), new Date());
				
					if(checkError==true){
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
            	populateHisto();
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
	            		data.add(mainParser.getUrl(), emailAdds);
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
	
	private void populateScatter(){
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
	}
	
	
	private void initTabA(){
		// Tab A
		ContriParser contriParser = new ContriParser(githubRepoInput.getText());
		checkError = contriParser.parseURL();
		checkError();
		
		if(checkError){
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
		}
	}
	
	private void initTabB(){
		// Tab B
		if(checkError){
			JSONObject sourceObj = (JSONObject) jsonObj.get(SOURCE);
			if(sourceObj!=null){
				disableDate((String) sourceObj.get(CREATED_AT));
			} else{
				disableDate((String) jsonObj.get(CREATED_AT));
			}
			contributorChoice.setItems(contributors);
		}
	}
	
	// Keep going in this method if file or directory clicked
	private void updateFileChooser(String initPath){
		FileParser fileParser = new FileParser(mainParser.getUrl(), initPath);
		checkError = fileParser.parseURL();
		checkError();
		
		if(checkError) {
			// Update UI with parser's JSONArray
			JSONArray jsonArr = fileParser.getJSONArr();
			
			//Use this to add data to directory listing
			for(int i =0 ; i< jsonArr.size(); i++){
				// Show first lvl objects
				// TODO
				JSONObject innerJsonObj = (JSONObject) jsonArr.get(i);
				String [] path = ((String) innerJsonObj.get("path")).split("/");
				if(innerJsonObj.get("type").equals("file")){
					// Show as file. Show commit when clicked
					String fileName = path[path.length-1];
					
				} else if (innerJsonObj.get("type").equals("dir")){
					// Show as directory
					String dirName = path[path.length - 1];
				}
			}
		}
	}
	
	// Call this after a file is clicked
	private void displayCommits(String filePath){
		CommitParser commitParser = new CommitParser(mainParser.getUrl(), "", "", filePath);
		checkError = commitParser.parseURL();
		checkError();
		
		if(checkError) {
			// Update UI with parser's JSONArray
			JSONArray jsonArr = commitParser.getJSONArr();
			
			//Use this to add data to directory listing
			for(int i = 0 ; i< jsonArr.size(); i++){
				JSONObject commitObj = (JSONObject) jsonArr.get(i);
				commitObj = (JSONObject) commitObj.get(COMMIT);
				JSONObject committerObj = (JSONObject) commitObj.get(COMMITTER);
				
				String msg = (String) commitObj.get(MESSAGE);
				String date = (String) committerObj.get(DATE);
				String name = (String) committerObj.get(NAME);
				// Show MSG , date , committer 
				// TODO : update UI with this 3 data
			}
		}
	}
	
	// Call this if select code chunk is clicked
	private void displayContent(String fileURL){
		FileDownloader fileDownloader = new FileDownloader(fileURL);
		fileDownloader.downloadFile();
		
		FileReader fileReader = new FileReader(fileURL);
		fileReader.readFile();
		
		ArrayList<String> content = fileReader.getContent();
		
		// TODO : print the content. They are in line
		for(int i =0 ; i < content.size(); i++){
			
		}
	}
	
	private void updateTabD(){
		// Tab D
		StatsParser statsParser = new StatsParser(mainParser.getUrl());
		checkError = statsParser.parseURL();
		checkError();
		
		if(checkError){
			// Update UI with parser's JSONArray
			JSONArray jsonArr = statsParser.getJSONArr();
			// TODO : update accordingly
			list.clear();
			piechartLine.setData(list);
			
			//Use this to add data to piechart. FOR each author
			for(int i =0 ; i< jsonArr.size(); i++){
	
				JSONObject innerJsonObj = (JSONObject) jsonArr.get(i);
				JSONArray weeksArr = (JSONArray) innerJsonObj.get(WEEKS);
				int addition = 0;
				int deletion = 0;
				for(int z = 0 ; z < weeksArr.size(); z++){
					JSONObject weekObj = (JSONObject) weeksArr.get(z);
					addition += (int) (weekObj.get(ADDITION));
					deletion += (int) (weekObj.get(DELETION));
				}
				
				JSONObject authorObj = (JSONObject) innerJsonObj.get(AUTHOR);
				
				// TODO : shawn uncomment this and replace the list Tab D
				list.add(new PieChart.Data((String) authorObj.get(LOGIN), addition - deletion));
			}
		}
	}
	
	
	public void updateTabB(){
		committerCommits = new HashMap<String, HashMap<String, Integer>>();
		DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
		String formattedDate = startDate.getValue().format(formatter) + "T00:00:00Z";
		String committerName = contributorChoice.getValue();
		CommitParser commitParser = new CommitParser(mainParser.getUrl(), committerName, formattedDate, "");
		checkError = commitParser.parseURL();
		checkError();

		JSONArray jsonArr = commitParser.getJSONArr();
		
		// Date is key, Value is number of commits for the committer
		HashMap<String, Integer> hm = new HashMap<String, Integer>();
		// Do something with the jsonArr, commit per day maybe. jsonArr.get(0) stands for the latest commit
		for(int i= 0 ; i < jsonArr.size(); i++) {
			// Commit syntax EG 
			/*
			"commit": {
		      "committer": {
		        "name": "Wouter Dullaert",
		        "email": "wouter.dullaert@gmail.com",
		        "date": "2016-03-17T20:21:14Z"
		      },
		      "committer": {
		        "name": "Wouter Dullaert",
		        "email": "wouter.dullaert@gmail.com",
		        "date": "2016-03-17T20:21:14Z"
		      },
		      "message": "Increased version number to v2.3.0",
		      "tree": {
		        "sha": "166d33193dabe1f9d61062bbb5329092b82e026f",
		        "url": "https://api.github.com/repos/ymymym/MaterialDateTimePicker/git/trees/166d33193dabe1f9d61062bbb5329092b82e026f"
		      },
		      "url": "https://api.github.com/repos/ymymym/MaterialDateTimePicker/git/commits/d857cf3110f4796980da1e11a1887c0aa6e652b8",
		      "comment_count": 0
		    }
		    */
			JSONObject commitObj = (JSONObject) jsonArr.get(i);
			commitObj = (JSONObject) commitObj.get(COMMIT);
			JSONObject committerObj = (JSONObject) commitObj.get(COMMITTER);
			String date = (String) committerObj.get(DATE);
		
			date = date.substring(0, date.indexOf("T"));
			
			int noOfCommits = 0;
			if(hm.get(date) != null){
				noOfCommits = hm.get(date);
			}
			
			noOfCommits += 1;
			hm.put(date, noOfCommits);
		}
		
		committerCommits.put(committerName, hm);
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
}
