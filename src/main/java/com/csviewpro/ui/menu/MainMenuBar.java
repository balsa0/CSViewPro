package com.csviewpro.ui.menu;

import com.csviewpro.controller.ApplicationUiStateController;
import com.csviewpro.controller.filehandling.LoadController;
import com.csviewpro.controller.util.ImageUtil;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by Balsa on 2016. 10. 15..
 */
@Component
public class MainMenuBar extends MenuBar {

	@Autowired
	LoadController loadController;

	@Autowired
	ApplicationContext applicationContext;

	@Autowired
	ImageUtil imageUtil;

	// file
	private Menu fileMenu = new Menu("Fájl");
	private MenuItem fileMenu_open = new MenuItem("Megnyitás...");
	private MenuItem fileMenu_close = new MenuItem("Fájl bezárása");
	private MenuItem fileMenu_exit = new MenuItem("Kilépés");

	// view
	private Menu viewMenu = new Menu("Nézet");
	private MenuItem viewMenu_numerical = new MenuItem("Táblázat nézet");
	private MenuItem viewMenu_graphical = new MenuItem("Térkép nézet");

	// help
	private Menu helpMenu = new Menu("Súgó");
	private MenuItem helpMenu_about = new MenuItem("Névjegy és Licenszek");

	@PostConstruct
	public void init(){
		populate();
		setupFileMenu();
		setupViewMenu();
		setupHelpMenu();
	}

	private void populate(){
		getMenus().addAll(
				fileMenu,
				viewMenu,
				helpMenu
		);
	}

	private void setupFileMenu(){
		// open menu item
		fileMenu_open.setGraphic(imageUtil.getResourceIconImage("actions/open_sm.png"));
		fileMenu_open.setOnAction(event -> {
			loadController.openFileChooserAction();
		});
		fileMenu_open.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));

		// close file item
		fileMenu_close.setDisable(true);
		fileMenu_close.setGraphic(imageUtil.getResourceIconImage("actions/close_sm.png"));
		fileMenu_close.setOnAction(event -> {
			loadController.closeFileAction();
		});

		// exit menu item
		fileMenu_exit.setGraphic(imageUtil.getResourceIconImage("actions/exit_sm.png"));
		fileMenu_exit.setOnAction(event -> {
			applicationContext.getBean(Stage.class).close();
		});

		// populate file menu
		fileMenu.getItems().addAll(
				fileMenu_open,
				new SeparatorMenuItem(),
				fileMenu_close,
				new SeparatorMenuItem(),
				fileMenu_exit
		);
	}

	private void setupViewMenu(){
		// view menu
		viewMenu.setDisable(true);
		// numerical view
		viewMenu_numerical.setGraphic(imageUtil.getResourceIconImage("actions/numeric_sm.png"));
		// graphical
		viewMenu_graphical.setGraphic(imageUtil.getResourceIconImage("actions/map_sm.png"));

		// add items
		viewMenu.getItems().addAll(
				viewMenu_numerical,
				viewMenu_graphical
		);
	}

	private void setupHelpMenu(){
		// about menu
		helpMenu_about.setGraphic(imageUtil.getResourceIconImage("actions/about_sm.png"));

		// populate help menu
		helpMenu.getItems().addAll(
				helpMenu_about
		);
	}

	/**
	 * This method adjusts menu items according to the current ui state.
	 * @param uiState
	 */
	public void activateMenuState(ApplicationUiStateController.UiState uiState){

		// file history
		switch (uiState){
			case STATE_FILEHISTORY:
				// disable close file menu
				fileMenu_close.setDisable(true);
				// disable view menu
				viewMenu.setDisable(true);

				break;
			case STATE_FILE_OPEN:
				// enable close file menu
				fileMenu_close.setDisable(false);
				// enable view menu
				viewMenu.setDisable(false);

				break;

			default:
				return;
		}
	}

}
