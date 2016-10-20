package application;
	
import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;


public class MainPage extends AnchorPane {
	
	private static final String MAIN_PAGE_FXML_URL = "MainPage.fxml";
	private static ObservableList<PieChart.Data> list = FXCollections.observableList(new ArrayList<PieChart.Data>());
	
	@FXML
	private Label gitGuardLabel;
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
					//commands to show json stuff here
					piechartA.setData(list);
					//Use this to add data to piechart
					list.add(new PieChart.Data("a", 10));
					mainTabPane.visibleProperty().set(true);
				}
			}
		});
	}
	
}
