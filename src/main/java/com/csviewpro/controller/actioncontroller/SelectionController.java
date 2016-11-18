package com.csviewpro.controller.actioncontroller;

import com.csviewpro.controller.view.StatusBarController;
import com.csviewpro.domain.model.RowData;
import com.csviewpro.ui.menu.MainMenuBar;
import com.csviewpro.ui.view.numeric.NumericView;
import com.csviewpro.ui.view.numeric.assets.TableGrid;
import javafx.collections.FXCollections;
import javafx.scene.control.TablePosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by Balsa on 2016. 11. 17..
 */
@Component
public class SelectionController {

	@Autowired
	private TableGrid tableGrid;

	@Autowired
	private NumericView numericView;

	@Autowired
	private StatusBarController statusBarController;

	@Autowired
	private MainMenuBar mainMenuBar;

	private List<RowData> selectedPoints = null;
	private List<TablePosition> selectedCells = null;

	@PostConstruct
	private void setup(){

		// setup selection action for numeric view
		tableGrid.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, newSelection) -> {
			// if selection is null
			if(newSelection == null)
				return;
			// selected points
			List<RowData> newPoints = tableGrid.getSelectionModel().getSelectedItems();

			// selected cells
			List<TablePosition> newCells = tableGrid.getSelectionModel().getSelectedCells();

			// numeric view action
			numericViewAction();

			// select action
			cellSelectAction(newPoints, newCells);

			// set proper context menu on selection
			tableGrid.setOnContextMenuRequested(event -> {
				if(newPoints.size() > 1){
					// show multi point selection
					tableGrid.getMultiSelectionContextMenu().show(tableGrid, event.getScreenX(), event.getScreenY());
				}else{
					// show single point selection
					tableGrid.getSingleSelectionContextMenu().show(tableGrid, event.getScreenX(), event.getScreenY());
				}
			});

			// hide context menus on selection
			tableGrid.getSingleSelectionContextMenu().hide();
			tableGrid.getMultiSelectionContextMenu().hide();

		});

	}

	private void pointSelectAction(List<RowData> newPoints){

		if(newPoints == null)
			return;

		if(newPoints.size() > 1)
			statusBarController.setStatusText(""+ newPoints.size()+" pont kijelölve.");

		// update selected ones
		selectedPoints = newPoints;
	}


	private void cellSelectAction(List<RowData> newPoints, List<TablePosition> newCells){

		// select points
		pointSelectAction(newPoints);

		// set main menu bar state
		mainMenuBar.activateSelectionState(newPoints);

		// if cells are null, do not continue
		if(newCells == null)
			return;

		// update status bar
		if(newCells.size() > 1)
			statusBarController.setStatusText(""+newCells.size()+" cella kijelölve " +
					"(" + newPoints.size() + " pont).");

		// update selected cells
		selectedCells = newCells;
	}

	private void numericViewAction(){
		// hide property editor
		numericView.setRight(null);
	}

	public List<RowData> getSelectedPoints() {
		return selectedPoints;
	}

	public List<TablePosition> getSelectedCells() {
		return selectedCells;
	}

	public void unselectAction(){

		// remove all cells
		selectedCells = FXCollections.emptyObservableList();

		// remove all points
		selectedPoints = FXCollections.emptyObservableList();

		// set main menu bar state
		mainMenuBar.activateSelectionState(selectedPoints);

		// remove selection from table
		tableGrid.getSelectionModel().clearSelection();
	}
}
