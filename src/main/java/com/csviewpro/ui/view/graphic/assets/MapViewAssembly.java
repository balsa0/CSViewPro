package com.csviewpro.ui.view.graphic.assets;

import javafx.scene.Cursor;
import javafx.scene.layout.Pane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by Balsa on 2016. 11. 20..
 */
@Component
public class MapViewAssembly extends Pane{

	@Autowired
	private MapCanvas mapCanvas;


	@PostConstruct
	private void init(){

		// css class
		this.setId("mapViewMain");

		// set cursor
		this.setCursor(Cursor.CROSSHAIR);

		// add nodes
		getChildren().addAll(mapCanvas);

	}

}
