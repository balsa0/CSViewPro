package com.csviewpro.controller.actioncontroller;

import com.csviewpro.controller.util.ApplicationUiStateController;
import com.csviewpro.domain.model.RowData;
import com.csviewpro.service.WorkspaceDataService;
import com.csviewpro.ui.view.common.PointEditorSheet;
import com.csviewpro.ui.view.numeric.NumericView;
import com.csviewpro.ui.view.numeric.assets.TableGrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import static com.csviewpro.controller.util.ApplicationUiStateController.UiState;

/**
 * Created by Balsa on 2016. 11. 18..
 */
@Controller
public class EditRowController {

	@Autowired
	private ApplicationUiStateController applicationUiStateController;

	@Autowired
	private WorkspaceDataService workspaceDataService;

	@Autowired
	private TableGrid tableGrid;

	@Autowired
	private NumericView numericView;

	private PointEditorSheet activeEditor;

	/**
	 * Action for editing a specified row.
	 * @param row the row to edit.
	 */
	public void editRowAction(RowData row){

		// if active state is numeric state
		if(UiState.STATE_FILE_OPEN.equals(applicationUiStateController.getActiveState())){
			// create property editor for the row
			activeEditor = new PointEditorSheet(
					row, workspaceDataService.getActiveDataSet().getHeaderDescriptor()
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
