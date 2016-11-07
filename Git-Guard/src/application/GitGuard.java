package application;

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class GitGuard extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			MainPage page = MainPage.getInstance();
			Scene scene = new Scene(page);
			final KeyCombination keyComb1 = new KeyCodeCombination(KeyCode.A,
                    KeyCombination.CONTROL_DOWN);
			scene.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler() {
				public void handle(KeyEvent event) {
					if (keyComb1.match(event)) {
						page.toggleHiddenPanel();
					}
				}

				@Override
				public void handle(Event event) {
					if (keyComb1.match((KeyEvent) event)) {
					page.toggleHiddenPanel();
					}
				}
			});
			primaryStage.setTitle("Git-Explorer");
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
