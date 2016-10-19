package com.csviewpro.controller.loader;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

/**
 * Created by Balsa on 2016. 10. 19..
 */
@Controller
public class LoadController {

	@Autowired
	ApplicationContext context;

	public void openFileAction(){
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open CSV File");
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("Pontlisták","*.csv","*.txt"),
				new FileChooser.ExtensionFilter("CSV Pontlisták","*.csv"),
				new FileChooser.ExtensionFilter("TXT Pontlisták","*.txt"),
				new FileChooser.ExtensionFilter("Minden fájl","*.*")
		);
		fileChooser.showOpenDialog(context.getBean(Stage.class));
	}

}
