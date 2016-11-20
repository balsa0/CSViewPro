package com.csviewpro.ui.view.graphic;

import com.csviewpro.ui.view.graphic.assets.MapCanvas;
import com.csviewpro.ui.view.graphic.assets.MapViewAssembly;
import com.csviewpro.ui.view.graphic.gestures.GraphicViewGestures;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import org.controlsfx.control.MasterDetailPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by Balsa on 2016. 11. 20..
 */
// http://stackoverflow.com/questions/29506156/javafx-8-zooming-relative-to-mouse-pointer
@Component
public class GraphicView extends MasterDetailPane {

	@Autowired
	private MapViewAssembly mapViewAssembly;

	@Autowired
	private MapCanvas mapCanvas;

	@PostConstruct
	private void init(){

		// master node
		this.setMasterNode(mapViewAssembly);
		this.setShowDetailNode(false);

		// view gestures
		GraphicViewGestures graphicViewGestures = new GraphicViewGestures(mapCanvas);
		this.addEventFilter( MouseEvent.MOUSE_PRESSED, graphicViewGestures.getOnMousePressedEventHandler());
		this.addEventFilter( MouseEvent.MOUSE_DRAGGED, graphicViewGestures.getOnMouseDraggedEventHandler());
		this.addEventFilter( ScrollEvent.ANY, graphicViewGestures.getOnScrollEventHandler());

	}



}
