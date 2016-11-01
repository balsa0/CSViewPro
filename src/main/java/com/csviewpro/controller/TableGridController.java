package com.csviewpro.controller;

import com.csviewpro.ui.view.numericassets.TableGrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Created by Balsa on 2016. 10. 31..
 */
@Controller
public class TableGridController {

	@Autowired
	private TableGrid tableGrid;

	public void rebuildTable(){

		// clear the table
		clearTable();



	}

	public void clearTable(){
		tableGrid.getItems().clear();
		tableGrid.getColumns().clear();
	}

}
