package com.csviewpro.ui.view;

import com.csviewpro.ui.toolbar.MainToolBar;
import com.csviewpro.ui.view.numericassets.TableGrid;
import javafx.scene.layout.BorderPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by Balsa on 2016. 10. 15..
 */
@Component
public class NumericView extends BorderPane{

	@Autowired
	MainToolBar mainToolBar;

	@Autowired
	TableGrid tableGrid;

	@PostConstruct
	private void init(){
		populate();
	}

	private void populate(){

		setTop(mainToolBar);
		setCenter(tableGrid);

	}


}
