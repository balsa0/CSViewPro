package com.csviewpro.ui;

import com.csviewpro.ui.menu.MainMenuBar;
import com.csviewpro.ui.view.common.FileHistoryView;
import javafx.scene.layout.BorderPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by Balsa on 2016. 10. 15..
 */
@Component
public class MainLayout extends BorderPane{

	@Autowired
	MainMenuBar mainMenuBar;

	@Autowired
	FileHistoryView fileHistoryView;

	@PostConstruct
	private void init(){
		populate();
	}

	/**
	 * Add elements to the layout
	 */
	private void populate(){
		// add elements
		setTop(mainMenuBar);
		setCenter(fileHistoryView);
	}

}
