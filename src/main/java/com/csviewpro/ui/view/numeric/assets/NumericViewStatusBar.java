package com.csviewpro.ui.view.numeric.assets;

import com.csviewpro.controller.util.ImageUtil;
import com.csviewpro.domain.model.enumeration.GeodeticSystem;
import com.csviewpro.service.WorkspaceDataService;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
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

	@PostConstruct
	public void init(){
		geodeticSystemLabel.setGraphic(
				imageUtil.getResourceIconImage("actions/geodeticsystem_sm.png")
		);
		geodeticSystemLabel.setPadding(new Insets(2,4,1,2));
		getRightItems().add(geodeticSystemLabel);
	}

	public void updateGeodeticSystem(){

		// get active geodetic system
		GeodeticSystem geodeticSystem = workspaceDataService.getActiveDataSet().getHeaderDescriptor().getGeodeticSystem();

		if(geodeticSystem == null){
			geodeticSystemLabel.setText("Ismeretlen vetületi rendszer");
		}else{
			geodeticSystemLabel.setText(geodeticSystem.name());
		}

	}


}
