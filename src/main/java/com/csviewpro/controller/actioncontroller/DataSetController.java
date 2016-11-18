package com.csviewpro.controller.actioncontroller;

import com.csviewpro.domain.model.RowData;
import com.csviewpro.service.WorkspaceDataService;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Balsa on 2016. 11. 18..
 */
@Controller
public class DataSetController {

	@Autowired
	private WorkspaceDataService workspaceDataService;

	@Autowired
	private EditRowController editRowController;

	@Autowired
	private SelectionController selectionController;

	private Date lastDeleteAction = new Date(0);

	/**
	 * Action for removing a point.
	 * @param row the row to remove.
	 */
	public void deleteRowAction(RowData row){

		// show question before deleting
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Fájlművelet sikertelen");
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
			editRowController.hideEditorAction();

			// remove row
			workspaceDataService.getActiveDataSet().getPoints().remove(row);

			// update last action
			lastDeleteAction = new Date();
		}

	}


}
