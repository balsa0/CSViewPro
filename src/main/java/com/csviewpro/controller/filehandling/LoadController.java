package com.csviewpro.controller.filehandling;

import com.csviewpro.controller.ApplicationUiStateController;
import com.csviewpro.controller.NotificationsController;
import com.csviewpro.controller.TableGridController;
import com.csviewpro.domain.ApplicationPreferences;
import com.csviewpro.service.CsvParserService;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

/**
 * Created by Balsa on 2016. 10. 19..
 */
@Controller
public class LoadController {

	// logger
	private final static Logger log = Logger.getLogger(LoadController.class);

	@Autowired
	private ApplicationContext context;

	@Autowired
	private FileHistoryController fileHistoryController;

	@Autowired
	private CsvParserService csvParserService;

	@Autowired
	private NotificationsController notificationsController;

	@Autowired
	private ApplicationUiStateController uiStateController;

	@Autowired
	private TableGridController tableGridController;

	// preferences
	private Preferences loadPreferences;

	@PostConstruct
	private void init(){
		loadPreferences = Preferences.userNodeForPackage(LoadController.class);
	}

	public void openFileChooserAction(){
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open CSV File");
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("Pontlisták","*.csv","*.txt"),
				new FileChooser.ExtensionFilter("CSV Pontlisták","*.csv"),
				new FileChooser.ExtensionFilter("TXT Pontlisták","*.txt"),
				new FileChooser.ExtensionFilter("Minden fájl","*.*")
		);

		// get last directory
		String lastDir = loadPreferences.get(ApplicationPreferences.APP_LOADFILE_LAST_DIR,"");

		// set as initial directory
		if(lastDir != ""){
			File lastDirRef = new File(lastDir);
			if(lastDirRef.exists() && lastDirRef.isDirectory()){
				fileChooser.setInitialDirectory(lastDirRef);
			}
		}

		File chosenFile = fileChooser.showOpenDialog(context.getBean(Stage.class));

		// return if no file has been opened
		if(chosenFile == null)
			return;

		// add opened file to the list of recent files
		fileHistoryController.addRecentFile(chosenFile);

		// save last directory preference
		try {
			// get parent directory path of opened file
			lastDir = chosenFile.getParentFile().getCanonicalPath();
			// update last directory setting
			loadPreferences.put(ApplicationPreferences.APP_LOADFILE_LAST_DIR, lastDir);
		}catch (IOException e){/* ignore exceptions */}

		// open the file
		openFileAction(chosenFile);
	}

	public void openFileAction(File file){

		// show alert if file not exists or not a file
		if(!file.exists() || !file.isFile()){
			// show alert
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Fájlművelet sikertelen");
			alert.setHeaderText(null);
			alert.setContentText("A megnyitni kívánt fájl nem létezik: "+file.getAbsolutePath());
			alert.show();
			// stop execution
			return;
		}

		// start parsing and opening the file
		try {
			// show loading notification
			notificationsController.showLoaderNotification(file.getName());

			// start parsing on a different thread
			Task<Void> loaderTask = new Task<Void>(){

				@Override
				protected Void call() throws Exception {
					// service call
					csvParserService.loadFile(file);

					return null;
				}
			};

			new Thread(loaderTask).start();

			loaderTask.setOnSucceeded(event -> {
				// set main layout state to numeric view
				uiStateController.activateFileOpenState(file);
				// hide loader bar
				notificationsController.hide();
				// rebuild table grid
				tableGridController.rebuildTable();
			});

		}catch (Exception e){

			// log the error message
			log.error("There was an error during loading file: "+file.getAbsolutePath(),e);

			// show alert
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Nem sikerült megnyitni a fájlt");
			alert.setHeaderText(null);
			alert.setContentText("Hiba lépett fel a fájl megnyitása közben: "+file.getAbsolutePath()+"" +
					"\r\n"+e.getLocalizedMessage());
			alert.show();

		}/*finally {
			// hide modal
			//notificationsController.hide();

		}*/


	}

}