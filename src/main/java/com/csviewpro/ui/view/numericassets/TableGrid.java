package com.csviewpro.ui.view.numericassets;

import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by Balsa on 2016. 10. 15..
 */
@Component
public class TableGrid extends TableView{

	/// TODO enable drag mouse selection:
	/// https://community.oracle.com/thread/2621389

	@PostConstruct
	public void init(){
		setupProperties();
	}

	public void setupProperties(){
		setEditable(true);

		getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		getSelectionModel().setCellSelectionEnabled(true);

	}

}
