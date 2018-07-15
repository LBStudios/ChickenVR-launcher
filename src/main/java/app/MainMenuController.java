package app;

import java.io.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import org.apache.commons.lang3.SystemUtils;

public class MainMenuController {
	// links to download the game (images for now)
	public final String VRDownloadURL = "https://i.imgur.com/JfBnOqu.png";
	public final String nonVRDownloadURL = "https://i.imgur.com/645NvpE.png";

	// the paths to the executables are dependent on the OS
	private static File VRExecutable;
	private static File nonVRExecutable;

	// get the buttons so that we can edit the text
	@FXML
	Button runVRButton;
	@FXML
	Button runNonVRButton;

	@FXML
	protected void initialize () {
		String clientOS = System.getProperty("os.name"); // get the operating system
		System.out.println("Currently running on " + clientOS);

		if (SystemUtils.IS_OS_WINDOWS) {
			VRExecutable = new File("%PROGRAMFILES%\\Chicken VR\\Chicken VR.exe"); // Windows
			nonVRExecutable = new File("%PROGRAMFILES%\\Chicken VR\\Chicken Non VR.exe");

		} else if (SystemUtils.IS_OS_MAC) {
			VRExecutable = new File("/Applications/Chicken VR/Chicken VR.app"); // MacOS or Mac OS X
			nonVRExecutable = new File("/Applications/Chicken VR/Chicken Non VR.app");

		} else {
			Alert incompatibleOS = new Alert(Alert.AlertType.ERROR); // create an error alert

			incompatibleOS.setTitle("Incompatible OS");
			incompatibleOS.setHeaderText("Chicken VR is Not Supported");
			// insert the OS name into the message
			incompatibleOS.setContentText(String.format("Chicken VR is not compatible with %s. Windows is currently the only supported operating system.", clientOS));
			incompatibleOS.showAndWait(); // wait until they say ok

			Platform.exit(); // quit the application
		}

		// check for the Chicken VR folder in the applications directory
		if (!VRExecutable.exists()) {
			System.out.println("Creating Chicken VR application folder");
			VRExecutable.getParentFile().mkdir(); // create the directory
		}

		/*
		* Version file should be written like this:
		*
		* (I have no idea)
		*
		* */

		// check if the VR and non VR applications are installed and update the buttons
		String installed = "\nInstalled";
		String update = "\nUpdating...";
		String notInstalled = "\nReady to Install";

		// make sure each version exists, otherwise say it can be installed

		if (VRExecutable.isFile()) {
			// if updated to latest version
			if (true) {
				runVRButton.setText(runVRButton.getText() + installed);
			} else {
				// not fully updated
				runVRButton.setText(runVRButton.getText() + update);
				// run the updater automatically update(VRExecutable);
			}
		} else {
			runVRButton.setText(runVRButton.getText() + notInstalled);
		}

		if (nonVRExecutable.isFile()) {
			// if updated to latest version
			if (true) {
				runNonVRButton.setText(runNonVRButton.getText() + installed);
			} else {
				runNonVRButton.setText(runNonVRButton.getText() + update);
				// update(nonVRExecutable);
			}
		} else {
			runNonVRButton.setText(runNonVRButton.getText() + notInstalled);
		}
	}

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
