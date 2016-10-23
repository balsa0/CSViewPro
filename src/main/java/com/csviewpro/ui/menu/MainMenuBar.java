package com.csviewpro.ui.menu;

import com.csviewpro.controller.loader.LoadController;
import com.csviewpro.controller.util.ImageUtil;
import javafx.application.Platform;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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
	ApplicationContext applicationContext;

	@Autowired
	ImageUtil imageUtil;

	private Menu fileMenu = new Menu("Fájl");
	private MenuItem fileMenu_open = new MenuItem("Megnyitás...");
	private MenuItem fileMenu_exit = new MenuItem("Kilépés");

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
		// open menu item
		fileMenu_open.setGraphic(imageUtil.getResourceIconImage("actions/open_sm.png"));
		fileMenu_open.setOnAction(event -> {
			loadController.openFileChooserAction();
		});
		fileMenu_open.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));

		// exit menu item
		fileMenu_exit.setOnAction(event -> {
			applicationContext.getBean(Stage.class).close();
		});

		// populate file menu
		fileMenu.getItems().addAll(
				fileMenu_open,
				new SeparatorMenuItem(),
				fileMenu_exit
		);
	}

}
