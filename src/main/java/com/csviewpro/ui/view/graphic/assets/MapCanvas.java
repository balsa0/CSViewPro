package com.csviewpro.ui.view.graphic.assets;

import com.csviewpro.ui.view.graphic.gestures.NodeGestures;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import org.springframework.stereotype.Component;

/**
 * Created by Balsa on 2016. 11. 20..
 */
@Component
public class MapCanvas extends Pane{

	DoubleProperty myScale = new SimpleDoubleProperty(1.0);

	public MapCanvas() {

		setPrefSize(600, 600);
		setStyle("-fx-background-color: red; -fx-border-color: blue;");

		// add scale transform
		scaleXProperty().bind(myScale);
		scaleYProperty().bind(myScale);

		NodeGestures nodeGestures = new NodeGestures( this);

		Label label1 = new Label("Draggable node 1");
		label1.setTranslateX(10);
		label1.setTranslateY(10);
		label1.addEventFilter( MouseEvent.MOUSE_PRESSED, nodeGestures.getOnMousePressedEventHandler());
		label1.addEventFilter( MouseEvent.MOUSE_DRAGGED, nodeGestures.getOnMouseDraggedEventHandler());

		Label label2 = new Label("Draggable node 2");
		label2.setTranslateX(100);
		label2.setTranslateY(100);
		label2.addEventFilter( MouseEvent.MOUSE_PRESSED, nodeGestures.getOnMousePressedEventHandler());
		label2.addEventFilter( MouseEvent.MOUSE_DRAGGED, nodeGestures.getOnMouseDraggedEventHandler());

		Label label3 = new Label("Draggable node 3");
		label3.setTranslateX(200);
		label3.setTranslateY(200);
		label3.addEventFilter( MouseEvent.MOUSE_PRESSED, nodeGestures.getOnMousePressedEventHandler());
		label3.addEventFilter( MouseEvent.MOUSE_DRAGGED, nodeGestures.getOnMouseDraggedEventHandler());

		Circle circle1 = new Circle( 300, 300, 50);
		circle1.setStroke(Color.ORANGE);
		circle1.setFill(Color.ORANGE.deriveColor(1, 1, 1, 0.5));
		circle1.addEventFilter( MouseEvent.MOUSE_PRESSED, nodeGestures.getOnMousePressedEventHandler());
		circle1.addEventFilter( MouseEvent.MOUSE_DRAGGED, nodeGestures.getOnMouseDraggedEventHandler());

		Rectangle rect1 = new Rectangle(100,100);
		rect1.setTranslateX(450);
		rect1.setTranslateY(450);
		rect1.setStroke(Color.BLUE);
		rect1.setFill(Color.BLUE.deriveColor(1, 1, 1, 0.5));
		rect1.addEventFilter( MouseEvent.MOUSE_PRESSED, nodeGestures.getOnMousePressedEventHandler());
		rect1.addEventFilter( MouseEvent.MOUSE_DRAGGED, nodeGestures.getOnMouseDraggedEventHandler());

		getChildren().addAll(label1, label2, label3, circle1, rect1);


		addGrid();
	}

	/**
	 * Add a grid to the canvas, send it to back
	 */
	public void addGrid() {

		double w = getBoundsInLocal().getWidth();
		double h = getBoundsInLocal().getHeight();

		// add grid
		Canvas grid = new Canvas(w, h);

		setLayoutX(0);
		setLayoutY(0);


		// don't catch mouse events
		grid.setMouseTransparent(true);

		GraphicsContext gc = grid.getGraphicsContext2D();

		gc.setStroke(Color.AQUA);
		gc.setLineWidth(1/getScale());

		// draw grid lines
		double offset = 50;
		for( double i=offset; i < w; i+=offset) {
			gc.strokeLine( i, 0, i, h);
			gc.strokeLine( 0, i, w, i);
		}

		getChildren().add(grid);
		grid.setScaleX(1);
		grid.setScaleY(1);

		grid.toBack();
	}

	public double getScale() {
		return myScale.get();
	}

	public void setScale( double scale) {
		myScale.set(scale);
	}

	public void setPivot( double x, double y) {
		setTranslateX(getTranslateX()-x);
		setTranslateY(getTranslateY()-y);
	}


}
