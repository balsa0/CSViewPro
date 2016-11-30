package com.csviewpro.ui.toolbar;

import com.csviewpro.controller.action.LoadController;
import com.csviewpro.controller.util.ImageUtil;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by Balsa on 2016. 10. 15..
 */
@Component
public class MainToolBar extends ToolBar {

	@Autowired
	LoadController loadController;

	@Autowired
	ImageUtil imageUtil;

	Button mapViewButton = new Button("Térkép");

	@PostConstruct
	private void init(){
		// set padding
		this.setPadding(new Insets(4));
		// setup
		setupButtons();
		populate();
	}

	private void setupButtons(){
		mapViewButton.setGraphic(imageUtil.getResourceIconImage("actions/map_md.png", 24));
		mapViewButton.setOnAction(event -> {
			loadController.openFileChooserAction();
		});
	}

	private void populate(){
		getItems().addAll(
				mapViewButton
		);

	}




}
