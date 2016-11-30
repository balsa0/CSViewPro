package com.csviewpro.controller.action;

import com.csviewpro.controller.util.ApplicationUiStateController;
import com.csviewpro.domain.model.RowData;
import com.csviewpro.service.WorkspaceDataService;
import com.csviewpro.ui.view.common.PointEditorSheet;
import com.csviewpro.ui.view.numeric.NumericView;
import com.csviewpro.ui.view.numeric.assets.TableGrid;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Calendar;
import java.util.Date;

import static com.csviewpro.controller.util.ApplicationUiStateController.UiState;

/**
 * Created by Balsa on 2016. 11. 18..
 */
@Controller
public class RowActionsController {

	@Autowired
	private ApplicationUiStateController applicationUiStateController;

	@Autowired
	private WorkspaceDataService workspaceDataService;

	@Autowired
	private TableGrid tableGrid;

	@Autowired
	private NumericView numericView;

	@Autowired
	private SelectionController selectionController;

	private PointEditorSheet activeEditor;

	private Date lastDeleteAction = new Date(0);

	public void editRowAction(){
		// return if nothing is selected
		if(selectionController.getSelectedPoints() == null ||
				selectionController.getSelectedPoints().isEmpty())
			return;
		// edit the row
		editRowAction(selectionController.getSelectedPoints().get(0));
	}

	/**
	 * Action for editing a specified row.
	 * @param row the row to edit.
	 */
	public void editRowAction(RowData row){

		// if active state is numeric state
		if(UiState.STATE_FILE_OPEN_NUMERIC.equals(applicationUiStateController.getActiveState())){
			// create property editor for the row
			activeEditor = new PointEditorSheet(
					row, workspaceDataService.getActiveDataSet().getDataSetMetaData()
			);

			// highlight the row
			tableGrid.getSelectionModel().clearSelection();
			tableGrid.getSelectionModel().select(row);

			// set property sheet
			numericView.setRight(activeEditor);

			// set focus on editor
			activeEditor.requestFocus();
		}

	}

	public void deleteRowAction(){

		// return if nothing is selected
		if(selectionController.getSelectedPoints() == null ||
				selectionController.getSelectedPoints().isEmpty())
			return;

		// delete the row
		for (RowData rowData : selectionController.getSelectedPoints()){
			deleteRowAction(rowData);
		}
	}

	/**
	 * Action for removing a point.
	 * @param row the row to remove.
	 */
	public void deleteRowAction(RowData row){

		// show question before deleting
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Törlés megerősítése");
		alert.setHeaderText(null);
		alert.setContentText("Biztosan törli a pontot?");

		// check if the last delete action was less than 30s ago.
		Calendar cal = Calendar.getInstance();
		cal.setTime(lastDeleteAction);
		cal.add(Calendar.SECOND, 30);
		boolean skipConfirmation = new Date().before(cal.getTime());

		// do not show confirmation
		if(!skipConfirmation)
			alert.showAndWait();

		if(skipConfirmation || alert.getResult() == ButtonType.OK){
			// hide editor
			hideEditorAction();

			// remove row
			workspaceDataService.getActiveDataSet().getPoints().remove(row);

			// update last action
			lastDeleteAction = new Date();
		}

	}

	/**
	 * Action for hiding editor.
	 */
	public void hideEditorAction(){
		numericView.setRight(null);
	}

	public PointEditorSheet getActiveEditor() {
		return activeEditor;
	}

	public void setActiveEditor(PointEditorSheet activeEditor) {
		this.activeEditor = activeEditor;
	}
}
