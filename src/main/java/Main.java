package main.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	public static Stage primaryStage;

	@Override
	public void start (Stage stage) throws Exception {
		primaryStage = stage;
		final int windowWidth = 800;
		final int windowHeight = 400;

		Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
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
