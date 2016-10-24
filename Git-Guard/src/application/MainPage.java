package application;
	
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

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
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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
	private static final String AUTHOR = "author";
	private static final String DATE = "date";
	
	TranslateTransition openPanel;
	TranslateTransition closePanel;
	private static final int TRANSITION_TIME = 350;
	private static final int STARTPOSITION = 0;
	private boolean checkError;
	private JSONObject jsonObj;
	private HashMap<String, HashMap<String, Integer>> authorCommits;
	private String url = "";
	
	@FXML
	private Label gitGuardLabel;
	@FXML
	private Label errorLabel;
	@FXML
	private TextField githubRepoInput;
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
	private BarChart contributorChart;
	@FXML
	private ChoiceBox<String> contributorChoice;
	@FXML
	private DatePicker startDate;
	@FXML
	private Button addBtn;
	
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
		Platform.runLater( () -> this.requestFocus() );
		githubRepoInput.setOnKeyPressed(new EventHandler<KeyEvent>() {

			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.ENTER)) {
					// Testing link - https://github.com/ymymym/MaterialDateTimePicker
					Parser mainParser = new Parser(githubRepoInput.getText());
					checkError = mainParser.parseURL();
					checkError();
					
					jsonObj= mainParser.getJSONObj();
				
					
					mainTabPane.visibleProperty().set(true);
					mainTabPane.disableProperty().set(false);
					initTabA();
				}
			}
		});
		
		tabA.setOnSelectionChanged(new EventHandler<Event>() {
            @Override
            public void handle(Event t) {
            	// Condition ensures init ran only once
                if (tabA.isSelected() && url != githubRepoInput.getText()) {
                	initTabA();
                	url = githubRepoInput.getText();
                }
            }
        });
		
		tabB.setOnSelectionChanged(new EventHandler<Event>() {
            @Override
            public void handle(Event t) {
        		// Condition ensures init ran only once
                if (tabB.isSelected() && url != githubRepoInput.getText()) {
                	initTabB();
                	url = githubRepoInput.getText();
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
            	ObservableList<XYChart.Series<String, Number>> list1 = FXCollections.observableArrayList();
            	//list1.add(authorCommits.get(contributorChoice.getValue()).);
            	contributorChart.setData(list1);
            }
        });
	}
	
	private void initTabA(){
		// Tab A
		ContriParser contriParser = new ContriParser(githubRepoInput.getText());
		checkError = contriParser.parseURL();
		checkError();
		
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
		
		url = githubRepoInput.getText();
	}
	
	private void initTabB(){
		// Tab B
		disableDate((String) jsonObj.get(CREATED_AT));
		contributorChoice.setItems(contributors);
	}
	
	public void updateTabB(){
		DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
		String formattedDate = startDate.getValue().format(formatter) + "T00:00:00Z";
		String authorName = contributorChoice.getValue();
		CommitParser commitParser = new CommitParser(githubRepoInput.getText(), authorName, formattedDate);
		checkError = commitParser.parseURL();
		checkError();
		
		JSONArray jsonArr = commitParser.getJSONArr();
		
		// Date is key, Value is number of commits for the author
		HashMap<String, Integer> hm = new HashMap<String, Integer>();
		
		// Do something with the jsonArr, commit per day maybe. jsonArr.get(0) stands for the latest commit
		for(int i= 0 ; i < jsonArr.size(); i++) {
			// Commit syntax EG 
			/*
			"commit": {
		      "author": {
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
			JSONObject authorObj = (JSONObject) commitObj.get(AUTHOR);
			String date = (String) authorObj.get(DATE);
			date = date.substring(0, date.indexOf("T"));
			
			int noOfCommits = 0;
			if(hm.get(date) != null){
				noOfCommits = hm.get(date);
			}
			
			noOfCommits += 1;
			hm.put(date, noOfCommits);
		}
		
		authorCommits.put(authorName, hm);
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
