package com.csviewpro.controller.actioncontroller;

import com.csviewpro.controller.view.StatusBarController;
import com.csviewpro.domain.conversion.DoubleConverter;
import com.csviewpro.domain.conversion.TypeConverter;
import com.csviewpro.domain.model.RowData;
import com.csviewpro.service.WorkspaceDataService;
import com.csviewpro.ui.menu.MainMenuBar;
import com.csviewpro.ui.view.common.AnalysisChartView;
import com.csviewpro.ui.view.numeric.NumericView;
import com.csviewpro.ui.view.numeric.assets.TableGrid;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.LinkedList;
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
	private WorkspaceDataService workspaceDataService;

	@Autowired
	private MainMenuBar mainMenuBar;

	private ObservableList<RowData> selectedPoints = null;
	private ObservableList<TablePosition> selectedCells = null;

	@PostConstruct
	private void setup(){

		// setup selection action for numeric view
		tableGrid.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, newSelection) -> {
			// if selection is null
			if(newSelection == null)
				return;
			// selected points
			ObservableList<RowData> newPoints = tableGrid.getSelectionModel().getSelectedItems();

			// selected cells
			ObservableList<TablePosition> newCells = tableGrid.getSelectionModel().getSelectedCells();

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

	private void pointSelectAction(ObservableList<RowData> newPoints){

		if(newPoints == null)
			return;

		if(newPoints.size() > 1)
			statusBarController.setStatusText(""+ newPoints.size()+" pont kijelölve.");

		// update selected ones
		selectedPoints = newPoints;
	}

	private void cellSelectAction(ObservableList<RowData> newPoints, ObservableList<TablePosition> newCells){

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

	public ObservableList<RowData> getSelectedPoints() {
		return selectedPoints;
	}

	public ObservableList<TablePosition> getSelectedCells() {
		return selectedCells;
	}

	public Table<Integer,Integer,ObservableValue> getCellValues(){

		// results
		Table<Integer,Integer,ObservableValue> result = HashBasedTable.create();

		getSelectedCells().forEach(tablePosition -> {
			Integer rowNum = tablePosition.getRow();
			Integer colNum = tablePosition.getColumn();

			// skip for edit column
			if(!colNum.equals(tableGrid.getColumns().size() -1))
				result.put(
						rowNum,
						colNum,
						workspaceDataService
								.getActiveDataSet().getPoints()
								.get(rowNum).get(colNum)
				);
		});

		return result;
	}

	public void unSelectAction(){

		// remove all cells
		selectedCells = FXCollections.emptyObservableList();

		// remove all points
		selectedPoints = FXCollections.emptyObservableList();

		// set main menu bar state
		mainMenuBar.activateSelectionState(selectedPoints);

		// remove selection from table
		tableGrid.getSelectionModel().clearSelection();
	}

	/**
	 * Select a single row.
	 * @param rowData the row data of row to select.
	 */
	public void selectRowAction(RowData rowData){
		// do the selection in the table view, callback will do the rest
		tableGrid.getSelectionModel().select(rowData);
	}

	public void selectRowAction(List<RowData> rows){
		// do the selection in the table view, callback will do the rest
		rows.forEach(rowData -> {
			tableGrid.getSelectionModel().select(rowData);
		});
	}


	public void selectCellAction(Integer rowNum, Integer columnNum){
		tableGrid.getSelectionModel().select(
				rowNum,
				(TableColumn) tableGrid.getColumns().get(columnNum)
		);
	}


}
