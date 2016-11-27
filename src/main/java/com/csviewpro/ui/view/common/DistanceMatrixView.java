package com.csviewpro.ui.view.common;

import com.csviewpro.controller.actioncontroller.SelectionController;
import com.csviewpro.controller.util.ImageUtil;
import com.csviewpro.domain.model.RowData;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by Balsa on 2016. 11. 26..
 */
@Component
public class DistanceMatrixView{

	@Autowired
	private SelectionController selectionController;

	@Autowired
	private ImageUtil imageUtil;

	// chart window
	private Stage stage = new Stage();

	// main layout
	private BorderPane borderPane = new BorderPane();

	// main scene
	private Scene scene = new Scene(borderPane);

	@PostConstruct
	private void init(){
		stage.setScene(scene);
		stage.setTitle("Grafikus elemzés");
		stage.getIcons().add(
				imageUtil.getResourceIconImage("actions/length_sm.png").getImage()
		);
	}

	public void showAndUpdate(){

		// get selected values
		ObservableList<RowData> selectedValues = selectionController.getSelectedPoints();






	}
}
