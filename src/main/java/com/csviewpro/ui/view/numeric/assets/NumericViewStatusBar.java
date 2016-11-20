package com.csviewpro.ui.view.numeric.assets;

import com.csviewpro.controller.util.ImageUtil;
import com.csviewpro.domain.model.enumeration.GeodeticSystem;
import com.csviewpro.service.WorkspaceDataService;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import org.controlsfx.control.StatusBar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by Balsa on 2016. 11. 17..
 */
@Component
public class NumericViewStatusBar extends StatusBar{

	@Autowired
	private ImageUtil imageUtil;

	@Autowired
	private WorkspaceDataService workspaceDataService;

	private Label geodeticSystemLabel = new Label();

	private Label pointCountLabel = new Label();

	@PostConstruct
	public void init(){
		// set up geodetic system indicator
		geodeticSystemLabel.setGraphic(
				imageUtil.getResourceIconImage("actions/geodeticsystem_sm.png")
		);
		geodeticSystemLabel.setPadding(new Insets(2,4,1,2));

		// set up geodetic system indicator
		pointCountLabel.setGraphic(
				imageUtil.getResourceIconImage("actions/marker_sm.png")
		);
		pointCountLabel.setPadding(new Insets(2,8,1,2));

		// add it to the status bar
		getRightItems().addAll(
				new Separator(Orientation.VERTICAL),
				pointCountLabel,
				new Separator(Orientation.VERTICAL),
				geodeticSystemLabel
		);
	}

	public void updateIcons(){

		// get active geodetic system
		GeodeticSystem geodeticSystem = workspaceDataService.getActiveDataSet().getHeaderDescriptor().getGeodeticSystem();

		if(geodeticSystem == null){
			geodeticSystemLabel.setText("Ismeretlen vetületi rendszer");
		}else{
			geodeticSystemLabel.setText(geodeticSystem.name());
		}

		// ge number of points loaded
		Integer pointCount = workspaceDataService.getActiveDataSet().getPoints().size();

		pointCountLabel.setText("" + pointCount + " pont");



	}


}
