package com.csviewpro.ui.toolbar;

import com.csviewpro.controller.filehandling.LoadController;
import com.csviewpro.controller.util.ImageUtil;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by Balsa on 2016. 10. 15..
 */
@Component
public class FileHistoryToolbar extends ToolBar {

	@Autowired
	LoadController loadController;

	@Autowired
	ImageUtil imageUtil;

	Button openButton = new Button();

	@PostConstruct
	private void init(){
		// set padding
		this.setPadding(new Insets(4));
		// setup
		setupButtons();
		populate();
	}

	private void setupButtons(){
		openButton.setGraphic(imageUtil.getResourceIconImage("actions/open_md.png", 24));
		openButton.setOnAction(event -> loadController.openFileChooserAction());
		openButton.setText("Megnyitás...");
		openButton.setTooltip(new Tooltip("Megnyitás..."));
	}

	private void populate(){
		getItems().addAll(
				openButton
		);
	}




}
