package com.csviewpro.ui.view.numericassets;

import javafx.scene.control.TableView;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by Balsa on 2016. 10. 15..
 */
@Component
public class TableGrid extends TableView{

	@PostConstruct
	public void init(){
		setupProperties();
	}

	public void setupProperties(){
		setEditable(true);
	}

}
