package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GitGuard extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			MainPage page = MainPage.getInstance();
			Scene scene = new Scene(page);
			primaryStage.setTitle("Git-Guard");
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
