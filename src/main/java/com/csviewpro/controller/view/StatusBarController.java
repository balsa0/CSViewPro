package com.csviewpro.controller.view;

import com.csviewpro.controller.util.ApplicationUiStateController;
import com.csviewpro.ui.view.numeric.assets.NumericViewStatusBar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Created by Balsa on 2016. 11. 17..
 */
@Controller
public class StatusBarController {

	@Autowired
	private NumericViewStatusBar numericViewStatusBar;

	@Autowired
	private ApplicationUiStateController uiStateController;

	public void setStatusText(String text){
		switch (uiStateController.getActiveState()){
			case STATE_FILE_OPEN_NUMERIC:
				numericViewStatusBar.setText(text);
				break;
			default:
				return;
		}
	}

}
