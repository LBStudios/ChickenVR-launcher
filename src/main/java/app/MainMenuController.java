package app;

import java.net.URL;
import java.io.*;
import java.util.Map;

import com.esotericsoftware.yamlbeans.YamlReader;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.io.FileUtils;

public class MainMenuController {
	// links to download the game (images for now)
	public final String VRDownloadURL = "https://www.dropbox.com/s/172lnm5hrc0gsgi/Chicken%20VR.zip?dl=1"; //https://github.com/LBStudios/ChickenVR/releases/download/1.0/ChickenVR-Release_1.0.zip
	public final String nonVRDownloadURL = "https://i.imgur.com/645NvpE.png";

	// these are used to check the latest version of the application
	private final String VRInfoURL = "https://raw.githubusercontent.com/LBStudios/ChickenVR/master/ProjectSettings/ProjectSettings.asset";
	private final String nonVRInfoURL = "";

	// the paths to the executables are dependent on the OS
	private static File applicationRoot;
	private static File VRExecutable;
	private static File nonVRExecutable;
	private static File versionsFile;

	private boolean VRUpdated;
	private boolean nonVRUpdated;

	// get the buttons so that we can edit the text
	@FXML
	Button runVRButton;
	@FXML
	Button runNonVRButton;

	/**
	 * This function is executed once, when the application has loaded. It gets the operating system, sets some of the
	 * directories and file paths, and then updates the buttons to display the installation status.
	 */
	@FXML
	protected void initialize () {
		String clientOS = System.getProperty("os.name"); // get the operating system
		System.out.println("Currently running on " + clientOS);

		// set the application paths for VR and non VR
		if (SystemUtils.IS_OS_WINDOWS) {
			applicationRoot = new File("%PROGRAMFILES%\\Chicken VR"); // Windows
			VRExecutable = new File("%PROGRAMFILES%\\Chicken VR\\Chicken VR\\Chicken VR.exe");
			nonVRExecutable = new File("%PROGRAMFILES%\\Chicken VR\\Chicken Non VR\\Chicken Non VR.exe");
			versionsFile = new File("%PROGRAMFILES%\\Chicken VR\\versions.yaml");

		} /*else if (SystemUtils.IS_OS_MAC) {
			applicationRoot = new File("/Applications/Chicken VR"); // MacOS or Mac OS X
			VRExecutable = new File("/Applications/Chicken VR/Chicken VR/Chicken VR.app");
			nonVRExecutable = new File("/Applications/Chicken VR/Chicken Non VR.app");
			versionsFile = new File("/Applications/Chicken VR/versions.yaml");

		}*/ else {
			Alert incompatibleOS = new Alert(Alert.AlertType.ERROR); // create an error alert

			incompatibleOS.setTitle("Incompatible OS");
			incompatibleOS.setHeaderText("Chicken VR is Not Supported");
			// insert the OS name into the message
			incompatibleOS.setContentText(String.format("Chicken VR is not compatible with %s. Windows is currently the only supported operating system.", clientOS));
			incompatibleOS.showAndWait(); // wait until they say ok

			Platform.exit(); // quit the application
		}

		// check for the Chicken VR folder in the applications directory
		if (!applicationRoot.exists()) {
			System.out.println("Creating Chicken VR application folder");
			applicationRoot.mkdir(); // create the directory
		}

		updateButtons();
	}

	/**
	 * Updates the "play" buttons to display the installation status. The buttons will either say "Installed,"
	 * "Updating...," or "Ready to Install."
	 */
	private void updateButtons () {
		// disable the non VR button because it doesn't exist yet
		runNonVRButton.setDisable(true);

		String VR = "Chicken VR";
		String nonVR = "Chicken Non VR";

		// check if the VR and non VR applications are installed and update the buttons
		String installed = "\nInstalled";
		String update = "\nUpdating...";
		String notInstalled = "\nReady to Install";

		// make sure each version exists, otherwise say it can be installed

		if (VRExecutable.isFile()) {
			// if updated to latest version
			if (atLatestVersion(true, VRInfoURL)) {
				runVRButton.setText(VR + installed);
				VRUpdated = true;

			} else {
				// not fully updated
				runVRButton.setText(VR + update);
				// run the updater automatically update(VRExecutable);
			}
		} else {
			runVRButton.setText(VR + notInstalled);
		}

		if (nonVRExecutable.isFile()) {
			// if updated to latest version
			if (atLatestVersion(false, nonVRInfoURL)) {
				runNonVRButton.setText(nonVR + installed);

			} else {
				runNonVRButton.setText(nonVR + update);
				// update(nonVRExecutable);
			}
		} else {
			runNonVRButton.setText(nonVR + notInstalled);
		}
	}

	/**
	 * Checks if the VR and non VR applications are updated to the latest versions.
	 *
	 * @param VR true if we should use the VR version of the game and false for the non VR version
	 * @param infoURL the URL to access the ProjectSettings.asset file
	 * @return true if the application is updated, false if it needs to be updated
	 */
	private boolean atLatestVersion (boolean VR, String infoURL) {
		/* Version file should be YAML:
		 *
		 * VR: 1.0
		 * nonVR: 3.2
		 * */

		// VR will be true for VR and false for non VR
		// the info URL is the link to ProjectSettings.asset

		String version; // for accessing the local versions file
		if (VR) version = "VR";
		else version = "nonVR";

		String localVersion; // this is read from the computer
		String latestVersion = "Network Error"; // latest version number from GitHub

		try {
			YamlReader reader = new YamlReader(new FileReader(versionsFile)); // create the YAML reader
			localVersion = (String) ((Map) reader.read()).get(version);

			// create a buffer to read each line of the project settings file
			BufferedReader input = new BufferedReader(
				new InputStreamReader(new URL(infoURL).openStream())
			);

			String inputLine;
			// keep going until we find a line with "bundleVersion" which is the version of the app
			while ((inputLine = input.readLine()) != null) {
				if (inputLine.trim().startsWith("bundleVersion")) {
					// read the YAML
					reader = new YamlReader(inputLine.trim()); // only read the current line
					latestVersion = (String)
						((Map) reader.read()).get("bundleVersion"); // get a string of the latest version

					break; // don't check any other lines
				}
			}

			return localVersion.equals(latestVersion); // true if latest, false if update available

		} catch (IOException e) { return false; } // the file doesn't exist or there is no key for the game
	}

	/**
	 * Runs the application if installed and updated. Updates if installed and not updated. Installs if not installed.
	 *
	 * @param executable the executable as a File
	 * @param downloadURL where to download the app
	 */
	private void run (File executable, String downloadURL) {

		if (executable.exists()) {
			// run even if it's not fully updated
			try {
				// verify that we're on Windows
				if (SystemUtils.IS_OS_WINDOWS) {
					Runtime.getRuntime().exec(
						"start " + executable.getName(), // command (on Windows)
						new String[]{applicationRoot.getAbsolutePath()}, // environment variables
						executable.getParentFile()
					);
				} /*if (SystemUtils.IS_OS_MAC) {
					Runtime.getRuntime().exec(
						"open " + executable.getName(),
						new String[]{},            // environment variables
						executable.getParentFile() // working directory
					);
				}*/

			} catch (IOException e) {
				e.printStackTrace(); // there was something very problematic
			}
		} else {
			// install the application if it doesn't exist

			try {
				// create a temporary directory and copy the zip file
				File tempFile = File.createTempFile("ChickenVR", ".tmp");
				tempFile.deleteOnExit(); // delete the temporary file when the application is closed
				FileUtils.copyURLToFile(new URL(downloadURL), tempFile); // download the zip

				// extract the zip file to the applications folder
				ZipFile zipFile = new ZipFile(tempFile.getAbsolutePath());
				zipFile.extractAll(applicationRoot.getAbsolutePath());

				// update the buttons
				updateButtons();

			} catch (IOException | ZipException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Called when the run VR button is pressed.
	 */
	@FXML
	protected void runVR (ActionEvent event) {
		run(VRExecutable, VRDownloadURL);
	}

	/**
	 * Called when the run non VR button is pressed.
	 */
	@FXML
	protected void runNonVR (ActionEvent event) {
		run(nonVRExecutable, nonVRDownloadURL);
	}

	@FXML
	protected void quit (ActionEvent event) {
		Platform.exit();
	}
}
