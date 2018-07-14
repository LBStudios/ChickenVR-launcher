package main.java;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class MainMenuController {

	@FXML
	protected void runVR (ActionEvent event) {
		// STUFF
	}

	@FXML
	protected void runNonVR (ActionEvent event) {
		// STUFF
	}

	@FXML
	protected void quit (ActionEvent event) {
		Platform.exit();
	}
}
