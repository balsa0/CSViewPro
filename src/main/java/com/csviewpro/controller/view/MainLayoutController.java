package com.csviewpro.controller.view;

import com.csviewpro.ui.MainLayout;
import com.csviewpro.ui.view.common.FileHistoryView;
import com.csviewpro.ui.view.numeric.NumericView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * This controller controls the state of main layout.
 */
@Controller
public class MainLayoutController {

	@Autowired
	private MainLayout mainLayout;

	@Autowired
	private FileHistoryView fileHistoryView;

	@Autowired
	private NumericView numericView;

	/**
	 * This method sets main layout state to numeric view.
	 */
	public void activateNumericView(){
		mainLayout.setCenter(numericView);
	}

	/**
	 * This method sets main layout state to file history view.
	 */
	public void activateFileHistoryView(){
		mainLayout.setCenter(fileHistoryView);
	}



}
