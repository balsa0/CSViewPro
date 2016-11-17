package com.csviewpro.ui.view.common;

import com.csviewpro.controller.filehandling.FileHistoryController;
import com.csviewpro.controller.filehandling.LoadController;
import com.csviewpro.controller.util.ImageUtil;
import com.csviewpro.ui.toolbar.FileHistoryToolbar;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.BorderPane;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

/**
 * Created by Balsa on 2016. 10. 19..
 */
@Component
public class FileHistoryView extends BorderPane {

	// logger
	private final static Logger log = Logger.getLogger(FileHistoryView.class);

	@Autowired
	FileHistoryController fileHistoryController;

	@Autowired
	LoadController loadController;

	@Autowired
	FileHistoryToolbar fileHistoryToolbar;

	@Autowired
	ImageUtil imageUtil;

	// specific nodes
	private TreeItem<String> rootNode;
	private TreeItem<String> favouritesNode;
	private TreeItem<String> recentsNode;

	// context menus
	private ContextMenu favouriteContextMenu;
	private ContextMenu recentContextMenu;

	// internal tree view
	private TreeView treeView = new TreeView();

	@PostConstruct
	private void init(){
		populate();
		setupContextMenus();
		updateContent();
	}

	private void populate(){
		// add toolbar
		setTop(fileHistoryToolbar);
		// set tree view as central element
		setCenter(treeView);
	}

	public void setupContextMenus(){
		// favourites menu
		favouriteContextMenu = new ContextMenu();
		favouriteContextMenu.setConsumeAutoHidingEvents(true);

		MenuItem removeFav = new MenuItem(
				"Eltávolítás a kedvencekből",
				imageUtil.getResourceIconImage("actions/cancel_sm.png")
		);
		removeFav.setOnAction(event -> {
			// get selected item
			String file = (String) (((TreeItem)treeView.getSelectionModel().getSelectedItem()).getValue());
			// remove item from favourites
			fileHistoryController.removeFavourite(file);

		});

		// populate favourites context menu
		favouriteContextMenu.getItems().addAll(
				removeFav
		);

		// recents menu
		recentContextMenu = new ContextMenu();
		recentContextMenu.setConsumeAutoHidingEvents(true);

		MenuItem addFav = new MenuItem(
				"Hozzáadás kedvencekhez",
				imageUtil.getResourceIconImage("actions/star_plus_sm.png")
		);
		addFav.setOnAction(event -> {
			// get selected item
			String filePath = (String) (((TreeItem)treeView.getSelectionModel().getSelectedItem()).getValue());
			// add item to favs
			fileHistoryController.addFavourite(filePath);
		});

		recentContextMenu.getItems().addAll(
			addFav
		);

	}

	public void updateContent(){
		// root node
		rootNode = new TreeItem<>(
				"Ismert fájlok",
				imageUtil.getResourceIconImage("actions/tree_sm.png")
		);
		rootNode.setExpanded(true);

		// set rootnode as root
		treeView.setRoot(rootNode);

		// favourites
		favouritesNode = new TreeItem<>(
				"Kedvencek",
				imageUtil.getResourceIconImage("actions/star_sm.png")
		);
		favouritesNode.setExpanded(true);

		// fill favorites
		fileHistoryController.getFavourites().forEach(file -> {
			try{

				// create new node
				TreeItem<String> node = new TreeItem<>(
						file.getCanonicalPath(),
						file.exists() && file.isFile() ?
								imageUtil.getResourceIconImage("actions/csv_sm.png") :
								imageUtil.getResourceIconImage("actions/csv_missing_sm.png")
				);

				// add node to parent
				favouritesNode.getChildren().add(node);

			}catch (IOException e){/* ignoring exceptions */}
		});

		// recent files
		recentsNode = new TreeItem<>(
				"Előzmények",
				imageUtil.getResourceIconImage("actions/clock_sm.png")
		);
		recentsNode.setExpanded(true);

		// fill recent files
		fileHistoryController.getRecentFiles().forEach(file -> {
			try{

				// create new node
				TreeItem<String> node = new TreeItem<>(
						file.getCanonicalPath(),
						file.exists() && file.isFile() ?
								imageUtil.getResourceIconImage("actions/csv_sm.png") :
								imageUtil.getResourceIconImage("actions/csv_missing_sm.png")
				);

				// add node to parent
				recentsNode.getChildren().add(node);

			}catch (IOException e){/* ignoring exceptions */}
		});

		// set node click actions
		treeView.setOnMouseClicked(event -> {

			// close context menus
			favouriteContextMenu.hide();
			recentContextMenu.hide();

			// only on double click
			if(event.getClickCount() == 2){
				// open the selected file
				openSelectedFile();
			}
		});

		// set context menu event
		treeView.setOnContextMenuRequested(event -> {
			openContextMenu(event);
		});

		// add nodes to root node
		rootNode.getChildren().addAll(
				favouritesNode,
				recentsNode
		);
	}

	/**
	 * This method triggers opening of the selected item in the file history view.
	 */
	private void openSelectedFile(){

		// get selected item
		TreeItem selected = (TreeItem)treeView.getSelectionModel().getSelectedItem();

		// check for special nodes and skip if selected
		if(selected == rootNode || selected == recentsNode || selected == favouritesNode)
			return;

		// load the file
		loadController.openFileAction(
				new File((String)selected.getValue())
		);
	}

	private void openContextMenu(ContextMenuEvent event){
		// get selected item
		TreeItem selected = (TreeItem)treeView.getSelectionModel().getSelectedItem();

		// check for special nodes and skip if selected
		if(selected == rootNode || selected == recentsNode || selected == favouritesNode)
			return;

		// if selected a recent item
		if(selected.getParent() == recentsNode){
			recentContextMenu.show(treeView, event.getScreenX(), event.getScreenY());
		}

		// if selected a faved item
		if(selected.getParent() == favouritesNode){
			favouriteContextMenu.show(treeView, event.getScreenX(), event.getScreenY());
		}
	}

}
