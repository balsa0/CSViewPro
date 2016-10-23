package com.csviewpro.ui.view;

import com.csviewpro.controller.filehandling.FileHistoryController;
import com.csviewpro.controller.loader.LoadController;
import com.csviewpro.controller.util.ImageUtil;
import com.csviewpro.ui.toolbar.FileHistoryToolbar;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
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

	// internal tree view
	private TreeView treeView = new TreeView();

	@PostConstruct
	private void init(){
		populate();
		updateContent();
	}

	private void populate(){
		// add toolbar
		setTop(fileHistoryToolbar);
		// set tree view as central element
		setCenter(treeView);
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
			// only on double click
			if(event.getClickCount() == 2){
				// open the selected file
				openSelectedFile();
			}
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

}
