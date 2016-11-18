package com.csviewpro.ui.view.numeric.assets;

import com.csviewpro.controller.util.ImageUtil;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by Balsa on 2016. 10. 15..
 */
@Component
public class TableGrid extends TableView{

	/// TODO enable drag mouse selection:
	/// https://community.oracle.com/thread/2621389

	private ContextMenu singleSelectionContextMenu;
	private ContextMenu multiSelectionContextMenu;

	@PostConstruct
	private void init(){
		setupProperties();
		setupContextMenus();
	}

	private void setupProperties(){
		setEditable(true);

		getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		getSelectionModel().setCellSelectionEnabled(true);


	}

	private void setupContextMenus(){
		// context menu
		singleSelectionContextMenu = new ContextMenu();
		singleSelectionContextMenu.setConsumeAutoHidingEvents(true);

		// multiple points are selected
		multiSelectionContextMenu = new ContextMenu();
		multiSelectionContextMenu.setConsumeAutoHidingEvents(true);

	}

	public ContextMenu getSingleSelectionContextMenu() {
		return singleSelectionContextMenu;
	}

	public ContextMenu getMultiSelectionContextMenu() {
		return multiSelectionContextMenu;
	}
}
