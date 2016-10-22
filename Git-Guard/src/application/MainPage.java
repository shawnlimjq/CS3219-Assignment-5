package application;
	
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.PieChart;
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
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;


public class MainPage extends AnchorPane {
	
	private static final String MAIN_PAGE_FXML_URL = "MainPage.fxml";
	private static ObservableList<PieChart.Data> list = FXCollections.observableList(new ArrayList<PieChart.Data>());
	private static ObservableList<String> contributors  = FXCollections.observableList(new ArrayList<String>());
	private static final String CONTRIBUTIONS = "contributions";
	private static final String LOGIN = "login";
	private static final String CREATED_AT = "created_at";
	
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
	
	public void initialise() {
		Platform.runLater( () -> this.requestFocus() );
		githubRepoInput.setOnKeyPressed(new EventHandler<KeyEvent>() {

			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.ENTER)) {
					
					// Testing link - https://github.com/ymymym/MaterialDateTimePicker
					Parser mainParser = new Parser(githubRepoInput.getText());
					boolean checkError = mainParser.parseURL();
					if (checkError==false){
						errorLabel.setVisible(true);
					} else{
						errorLabel.setVisible(false);
					}
					
					JSONObject jsonObj= mainParser.getJSONObj();
	
					disableDate((String) jsonObj.get(CREATED_AT));
				
					// Tab A
					ContriParser contriParser = new ContriParser(githubRepoInput.getText());
					checkError = contriParser.parseURL();
					
					if (checkError==false){
						errorLabel.setVisible(true);
					} else{
						errorLabel.setVisible(false);
					}
					
					// Update UI with parser's JSONArray
					JSONArray jsonArr = contriParser.getJSONArr();
					
					piechartA.setData(list);
					
					//Use this to add data to piechart
					for(int i =0 ; i< jsonArr.size(); i++){

						JSONObject innerJsonObj = (JSONObject) jsonArr.get(i);
						list.add(new PieChart.Data((String) innerJsonObj.get(LOGIN), (Long) innerJsonObj.get(CONTRIBUTIONS)));
						contributors.add((String) innerJsonObj.get(LOGIN));
						
					}
					
					
					// Tab B
					contributorChoice.setItems(contributors);
					
					DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
					String formattedDate = startDate.getValue().format(formatter) + "T00:00:00Z";
					
					CommitParser commitParser = new CommitParser(githubRepoInput.getText(), contributorChoice.getValue(), formattedDate);
					checkError = commitParser.parseURL();
					
					if (checkError==false){
						errorLabel.setVisible(true);
					} else{
						errorLabel.setVisible(false);
					}
					
					jsonArr = commitParser.getJSONArr();
					
					// Do something with the jsonArr, commit per day maybe. jsonArr.get(0) stands for the latest commit
					
					mainTabPane.visibleProperty().set(true);
				}
			}
		});
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
}
