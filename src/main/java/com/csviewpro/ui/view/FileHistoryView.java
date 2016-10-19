package com.csviewpro.ui.view;

import com.csviewpro.controller.filehandling.FileHistoryController;
import com.csviewpro.controller.util.ImageUtil;
import com.csviewpro.ui.toolbar.FileHistoryToolbar;
import java.lang.String;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * Created by Balsa on 2016. 10. 19..
 */
@Component
public class FileHistoryView extends BorderPane {

	@Autowired
	FileHistoryController fileHistoryController;

	@Autowired
	FileHistoryToolbar fileHistoryToolbar;

	@Autowired
	ImageUtil imageUtil;


	// internal treeview
	private TreeView treeView = new TreeView();

	@PostConstruct
	private void init(){
		populate();
		updateContent();
	}

	private void populate(){
		// add toolbar
		setTop(fileHistoryToolbar);
		// set treeview as central element
		setCenter(treeView);
	}

	private void updateContent(){
		// root node
		TreeItem<String> rootnode = new TreeItem<>(
				"Ismert fájlok",
				imageUtil.getResourceIconImage("actions/tree_sm.png")
		);
		rootnode.setExpanded(true);

		// set rootnode as root
		treeView.setRoot(rootnode);

		// favourites
		TreeItem<String> favouritesNode = new TreeItem<>(
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

			}catch (IOException e){

			}
		});

		// history
		TreeItem<String> historyNode = new TreeItem<>(
				"Előzmények",
				imageUtil.getResourceIconImage("actions/clock_sm.png")
		);
		historyNode.setExpanded(true);

		// add nodes to root node
		rootnode.getChildren().addAll(
				favouritesNode,
				historyNode
		);
	}

}
