package com.csviewpro.ui.menu;

import com.csviewpro.controller.loader.LoadController;
import com.csviewpro.controller.util.ImageUtil;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by Balsa on 2016. 10. 15..
 */
@Component
public class MainMenuBar extends MenuBar {

	@Autowired
	LoadController loadController;

	@Autowired
	ImageUtil imageUtil;

	private Menu fileMenu = new Menu("Fájl");
	private MenuItem fileMenu_open = new MenuItem("Megnyitás...");

	@PostConstruct
	public void init(){
		populate();
		setupFileMenu();
	}

	public void populate(){
		getMenus().add(
				fileMenu
		);
	}

	public void setupFileMenu(){
		fileMenu_open.setGraphic(imageUtil.getResourceIconImage("actions/open_sm.png"));
		fileMenu_open.setOnAction(event -> {
			loadController.openFileAction();
		});


		// populate file menu
		fileMenu.getItems().addAll(
				fileMenu_open
		);
	}

}
