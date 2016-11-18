package com.csviewpro.controller;

import com.csviewpro.domain.model.RowData;
import com.csviewpro.ui.view.numeric.NumericView;
import com.csviewpro.ui.view.numeric.assets.TableGrid;
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



}
