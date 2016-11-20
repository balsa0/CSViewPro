package com.csviewpro.ui.menu;

import com.csviewpro.controller.actioncontroller.RowActionsController;
import com.csviewpro.controller.actioncontroller.SelectionController;
import com.csviewpro.controller.actioncontroller.ViewActionController;
import com.csviewpro.controller.util.ApplicationUiStateController;
import com.csviewpro.controller.actioncontroller.LoadController;
import com.csviewpro.controller.util.ImageUtil;
import com.csviewpro.domain.model.RowData;
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
import java.util.List;

/**
 * Created by Balsa on 2016. 10. 15..
 */
@Component
public class MainMenuBar extends MenuBar {

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private LoadController loadController;

	@Autowired
	private RowActionsController rowActionsController;

	@Autowired
	private SelectionController selectionController;

	@Autowired
	private ViewActionController viewActionController;


	@Autowired
	ImageUtil imageUtil;

	// file
	private Menu fileMenu = new Menu("Fájl");
	private MenuItem fileMenu_open = new MenuItem("Megnyitás...");
	private MenuItem fileMenu_close = new MenuItem("Fájl bezárása");
	private MenuItem fileMenu_exit = new MenuItem("Kilépés");

	// edit
	private Menu editMenu = new Menu("Szerkesztés");
	private MenuItem editMenu_editRow = new MenuItem("Kijelölt pont szerkesztése");
	private MenuItem editMenu_deleteRow = new MenuItem("Kijelölt pont törlése");
	private MenuItem editMenu_unselectAll = new MenuItem("Kijelölés megszűntetése");

	// view
	private Menu viewMenu = new Menu("Nézet");
	private MenuItem viewMenu_refresh = new MenuItem("Frissítés");
	private MenuItem viewMenu_reload = new MenuItem("Nézet újratöltése");
	private MenuItem viewMenu_numerical = new MenuItem("Táblázat nézet");
	private MenuItem viewMenu_graphical = new MenuItem("Térkép nézet");

	// tools
	private Menu toolsMenu = new Menu("Eszközök");
	private MenuItem toolsMenu_graphicalAnalysis = new MenuItem("Grafikus elemzés");
	private MenuItem toolsMenu_pointRelation = new MenuItem("Térbeli elhelyezkedés");

	// help
	private Menu helpMenu = new Menu("Súgó");
	private MenuItem helpMenu_cc = new MenuItem("Creative Commons elismervények");
	private MenuItem helpMenu_about = new MenuItem("Névjegy");

	@PostConstruct
	public void init(){
		populate();
		setupFileMenu();
		setupEditMenu();
		setupViewMenu();
		setupToolsMenu();
		setupHelpMenu();
	}

	private void populate(){
		getMenus().addAll(
				fileMenu,
				editMenu,
				viewMenu,
				toolsMenu,
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

	private void setupEditMenu(){
		// edit menu
		editMenu.setDisable(true);
		// edit row
		editMenu_editRow.setGraphic(imageUtil.getResourceIconImage("actions/edit_sm.png"));
		editMenu_editRow.setDisable(true);
		editMenu_editRow.setOnAction(event -> rowActionsController.editRowAction());

		editMenu_deleteRow.setGraphic(imageUtil.getResourceIconImage("actions/delete_sm.png"));
		editMenu_deleteRow.setDisable(true);
		editMenu_deleteRow.setOnAction(event -> rowActionsController.deleteRowAction());

		editMenu_unselectAll.setGraphic(imageUtil.getResourceIconImage("actions/clear_sm.png"));
		editMenu_unselectAll.setDisable(true);
		editMenu_unselectAll.setOnAction(event -> selectionController.unSelectAction());

		// add items
		editMenu.getItems().addAll(
				editMenu_editRow,
				editMenu_deleteRow,
				new SeparatorMenuItem(),
				editMenu_unselectAll
		);
	}

	private void setupViewMenu(){
		// view menu
		viewMenu.setDisable(true);
		// refresh view
		viewMenu_refresh.setGraphic(imageUtil.getResourceIconImage("actions/refresh_sm.png"));
		viewMenu_refresh.setOnAction(event -> viewActionController.refreshView());
		// reload view
		viewMenu_reload.setGraphic(imageUtil.getResourceIconImage("actions/reload_sm.png"));
		viewMenu_reload.setOnAction(event -> viewActionController.reloadView());
		// numerical view
		viewMenu_numerical.setGraphic(imageUtil.getResourceIconImage("actions/numeric_sm.png"));
		viewMenu_numerical.setOnAction(event -> {
			applicationContext.getBean(ApplicationUiStateController.class).switchToNumericViewState();
		});
		// graphical
		viewMenu_graphical.setGraphic(imageUtil.getResourceIconImage("actions/map_sm.png"));
		viewMenu_graphical.setOnAction(event -> {
			applicationContext.getBean(ApplicationUiStateController.class).switchToGraphicViewState();
		});

		// add items
		viewMenu.getItems().addAll(
				viewMenu_refresh,
				new SeparatorMenuItem(),
				viewMenu_numerical,
				viewMenu_graphical,
				new SeparatorMenuItem(),
				viewMenu_reload
		);
	}

	private void setupToolsMenu(){
		// tools menu
		toolsMenu.setDisable(true);

		// graphical analysis
		toolsMenu_graphicalAnalysis.setGraphic(imageUtil.getResourceIconImage("actions/chart_area_sm.png"));
		toolsMenu_graphicalAnalysis.setDisable(true);
		toolsMenu_graphicalAnalysis.setOnAction(event -> viewActionController.graphicalAnalysisAction());

		// relation analysis
		toolsMenu_pointRelation.setGraphic(imageUtil.getResourceIconImage("actions/chart_scatter_sm.png"));
		toolsMenu_pointRelation.setDisable(true);
		toolsMenu_pointRelation.setOnAction(event -> viewActionController.pointRelationAnalysis());

		toolsMenu.getItems().addAll(
				toolsMenu_graphicalAnalysis,
				toolsMenu_pointRelation
		);

	}

	private void setupHelpMenu(){
		// about menu
		helpMenu_about.setGraphic(imageUtil.getResourceIconImage("actions/info_sm.png"));
//		helpMenu.setOnAction(event -> new LicenseView().show());

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

		// activate state for the menu
		switch (uiState){
			case STATE_FILEHISTORY:
				// disable close file menu
				fileMenu_close.setDisable(true);
				// disable edit menu
				editMenu.setDisable(true);
				// disable view menu
				viewMenu.setDisable(true);
				// disable tools menu
				toolsMenu.setDisable(true);

				break;
			case STATE_FILE_OPEN_NUMERIC:
			case STATE_FILE_OPEN_GRAPHIC:
				// enable close file menu
				fileMenu_close.setDisable(false);
				// enable edit menu
				editMenu.setDisable(false);
				// enable view menu
				viewMenu.setDisable(false);
				// enable tools menu
				toolsMenu.setDisable(false);

				break;

			default:
				return;
		}

		// view specific stuff
		if(uiState == ApplicationUiStateController.UiState.STATE_FILE_OPEN_NUMERIC){
			// specific to numeric view
			viewMenu_graphical.setDisable(false);
			viewMenu_numerical.setDisable(true);
		}else if(uiState == ApplicationUiStateController.UiState.STATE_FILE_OPEN_GRAPHIC){
			// specific to graphical view
			viewMenu_graphical.setDisable(true);
			viewMenu_numerical.setDisable(false);
		}

	}

	public void activateSelectionState(List<RowData> selection){
		// if no selected rows
		if (selection == null || selection.size() == 0) {
			editMenu_editRow.setDisable(true);
			editMenu_deleteRow.setDisable(true);
			editMenu_unselectAll.setDisable(true);
			toolsMenu_graphicalAnalysis.setDisable(true);
			toolsMenu_pointRelation.setDisable(true);
		// if there are selected rows
		}else{
			editMenu_unselectAll.setDisable(false);
			editMenu_editRow.setDisable(true);
			editMenu_deleteRow.setDisable(true);
			toolsMenu_graphicalAnalysis.setDisable(false);
			toolsMenu_pointRelation.setDisable(false);
		}

		// if there are exactly 1 selected rows
		if(selection.size() == 1){
			editMenu_editRow.setDisable(false);
			editMenu_deleteRow.setDisable(false);
		}
	}

}
