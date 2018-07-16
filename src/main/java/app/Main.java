package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start (Stage stage) throws Exception {
		final int windowWidth = 800;
		final int windowHeight = 400;

		Parent root = FXMLLoader.load(getClass().getResource("/FXML/main.fxml"));
		root.getStylesheets().add(getClass().getResource("/css/stylesheet.css").toExternalForm());
		Scene scene = new Scene(root, windowWidth, windowHeight);

		stage.setWidth(windowWidth);
		stage.setHeight(windowHeight);

		stage.setTitle("Chicken VR Launcher");
		stage.setScene(scene);
		stage.show();
	}

	public static void main (String[] args) {
		launch(args);
	}
}
