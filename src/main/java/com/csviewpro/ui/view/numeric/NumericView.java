package com.csviewpro.ui.view.numeric;

import com.csviewpro.ui.toolbar.MainToolBar;
import com.csviewpro.ui.view.numeric.assets.NumericViewStatusBar;
import com.csviewpro.ui.view.numeric.assets.TableGrid;
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
	private MainToolBar mainToolBar;

	@Autowired
	private TableGrid tableGrid;

	@Autowired
	private NumericViewStatusBar statusBar;

	@PostConstruct
	private void init(){
		populate();
	}

	private void populate(){

		setTop(mainToolBar);
		setCenter(tableGrid);
		setBottom(statusBar);

	}


}
