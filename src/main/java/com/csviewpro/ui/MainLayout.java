package com.csviewpro.ui;

import com.csviewpro.ui.menu.MainMenuBar;
import com.csviewpro.ui.view.FileHistoryView;
import com.csviewpro.ui.view.NumericView;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import org.controlsfx.control.StatusBar;
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
		setBottom(new StatusBar());
	}

}
