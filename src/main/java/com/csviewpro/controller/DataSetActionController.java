package com.csviewpro.controller;

import com.csviewpro.domain.model.RowData;
import com.csviewpro.service.WorkspaceDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Created by Balsa on 2016. 11. 18..
 */
@Controller
public class DataSetActionController {

	@Autowired
	private WorkspaceDataService workspaceDataService;

	@Autowired
	private PointEditorController pointEditorController;

	/**
	 * Action for removing a point.
	 * @param row the row to remove.
	 */
	public void deleteRowAction(RowData row){

		// hide editor
		pointEditorController.hideEditorAction();

		// remove row
		workspaceDataService.getActiveDataSet().getPoints().remove(row);
	}


}
