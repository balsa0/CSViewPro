package com.csviewpro.controller;

import com.csviewpro.ApplicationInitializer;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.File;

/**
 * This class controls UI state of application.
 */
@Controller
public class ApplicationUiStateController {

	/**
	 * This enum represents the different main states of application UI.
	 */
	public enum UiState{
		STATE_FILEHISTORY,
		STATE_FILE_OPEN
	}

	@Autowired
	private MainLayoutController mainLayoutController;

	@Autowired
	private ApplicationContext applicationContext;

	// set default state as filehistory state
	private UiState activeState = UiState.STATE_FILEHISTORY;

	/**
	 * Returns actual state enum of the UI.
	 * @return actual state as {@link UiState}.
	 */
	public UiState getActiveState() {
		return activeState;
	}

	/**
	 * Set actual app state property directly.
	 * @param activeState the new state of the application.
	 */
	private void setActiveState(UiState activeState) {
		this.activeState = activeState;
	}

	/**
	 * Activate file history view state of the application ui.
	 */
	public void activateFileHistoryState(){
		// activate layout
		mainLayoutController.activateFileHistoryView();
		// remove window subtitle
		setAppSubTitleText(null);
		// set state
		setActiveState(UiState.STATE_FILEHISTORY);
	}

	/**
	 * Activate 'file open' state of the application ui.
	 * @param openedFile the file opened. Filename will be extracted.
	 */
	public void activateFileOpenState(File openedFile){
		// null check
		if(openedFile == null)
			throw new RuntimeException("Can not activate 'file open' state of appliaction with openedFile param being null.");

		// activate state
		activateFileOpenState(openedFile.getName());
	}

	/**
	 * Activate 'file open' state of the application ui.
	 * @param fileName the name of the opened file.
	 */
	public void activateFileOpenState(String fileName){
		// activate numeric layout
		mainLayoutController.activateNumericView();
		// add window subtitle
		setAppSubTitleText(fileName);
		// set state
		setActiveState(UiState.STATE_FILE_OPEN);
	}



	/**
	 * This method sets or un-sets the application window subtitle.
	 * @param subTitle the subtitle to display. Set to null or empty to remove subtitle of the application.
	 */
	private void setAppSubTitleText(String subTitle){
		// get primary stage from app context
		Stage primaryStage = applicationContext.getBean(Stage.class);

		if(subTitle == null || "".equals(subTitle))
			// remove subtitle
			primaryStage.setTitle(ApplicationInitializer.APPLICATION_NAME);
		else
			// display given subtitle
			primaryStage.setTitle(ApplicationInitializer.APPLICATION_NAME + " - " + subTitle);
	}

}
