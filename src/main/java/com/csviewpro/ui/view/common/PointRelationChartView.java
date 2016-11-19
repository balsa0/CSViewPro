package com.csviewpro.ui.view.common;

import com.csviewpro.controller.actioncontroller.RowActionsController;
import com.csviewpro.controller.actioncontroller.SelectionController;
import com.csviewpro.controller.util.ImageUtil;
import com.csviewpro.domain.conversion.DoubleConverter;
import com.csviewpro.domain.conversion.TypeConverter;
import com.csviewpro.domain.model.RowData;
import com.csviewpro.domain.model.enumeration.ColumnRole;
import com.csviewpro.domain.model.enumeration.GeodeticSystem;
import com.csviewpro.service.WorkspaceDataService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Balsa on 2016. 11. 18..
 */
@Component
public class PointRelationChartView {

	@Autowired
	private SelectionController selectionController;

	@Autowired
	private RowActionsController rowActionsController;

	@Autowired
	private ImageUtil imageUtil;

	@Autowired
	private WorkspaceDataService workspaceDataService;

	// chart window
	private Stage stage = new Stage();

	// main layout
	private BorderPane borderPane = new BorderPane();

	// main scene
	private Scene scene = new Scene(borderPane);

	private Tooltip cursorTooltip = new Tooltip("0.00");

	// decimal format
	private DecimalFormat decimalFormat = new DecimalFormat("#.##");

	// converter
	private TypeConverter doubleConverter = new DoubleConverter();


	// active listener map
	Map<ObservableValue, ChangeListener> activeValueChangeListeners = new HashMap<>();

	@PostConstruct
	private void init(){
		stage.setScene(scene);
		stage.setTitle("Pontok elhelyezkedése");
		stage.getIcons().add(
				imageUtil.getResourceIconImage("actions/chart_scatter_sm.png").getImage()
		);

		// tooltip
		cursorTooltip.setConsumeAutoHidingEvents(true);
		cursorTooltip.setAutoHide(true);

		stage.setOnHiding(event -> {
			// clear change listeners
			clearChangeListeners();
		});

	}

	public void showAndUpdate(){

		// fist clear the active value change listeners
		clearChangeListeners();

		// get selected values
		ObservableList<RowData> selectedValues = selectionController.getSelectedPoints();

		// extend selections to rows
		selectionController.selectRowAction(selectedValues);

		// get geodetic system
		GeodeticSystem geodeticSystem = workspaceDataService
				.getActiveDataSet()
				.getHeaderDescriptor()
				.getGeodeticSystem();

		// set title
		stage.setTitle("Pontok elheyezkedése (" + selectedValues.size() + " pont)");

		// create series from sets
		// map <column index, series>
		Map<String, XYChart.Series<Number, Number>> chartSeries = new HashMap<>();

		// get point name index
		Integer nameColumnIndex = workspaceDataService
				.getActiveDataSet()
				.getHeaderDescriptor()
				.getColumIndexForRole(ColumnRole.POINTNAME);

		// get point code index
		Integer codeColumnIndex = workspaceDataService
				.getActiveDataSet()
				.getHeaderDescriptor()
				.getColumIndexForRole(ColumnRole.POINTCODE);

		// get point x coordinate index
		Integer xColumnIndex = workspaceDataService
				.getActiveDataSet()
				.getHeaderDescriptor()
				.getColumIndexForRole(ColumnRole.XCOORDINATE);

		// get point y coordinate index
		Integer yColumnIndex = workspaceDataService
				.getActiveDataSet()
				.getHeaderDescriptor()
				.getColumIndexForRole(ColumnRole.YCOORDINATE);

		// do the conversion for every cell
		for(RowData rowData : selectedValues){

			String pointCode = String.valueOf(rowData.get(codeColumnIndex).getValue());
			String pointName = String.valueOf(rowData.get(nameColumnIndex).getValue());

			if(pointCode == null || "".equals(pointCode.trim()))
				pointCode = "nincs kód";

			// create or get series
			XYChart.Series series;
			if(chartSeries.containsKey(pointCode)){
				series = chartSeries.get(pointCode);
			}else{
				// new series
				series = new XYChart.Series();
				series.setName(pointCode);
				series.setData(FXCollections.observableArrayList());

				// put the series to the map
				chartSeries.put(pointCode, series);
			}

			// get coordinates
			Double xValue = (Double) rowData.get(xColumnIndex).getValue();
			Double yValue = (Double) rowData.get(yColumnIndex).getValue();

			// create XY data for chart
			XYChart.Data data;
			if(geodeticSystem.equals(GeodeticSystem.EOV)){
				data = new XYChart.Data(yValue, xValue);
			}else{
				data = new XYChart.Data(xValue, yValue);
			}

			// add the point to the data
			data.setExtraValue(rowData);

			// create node for hover events
			data.setNode(new HoveredThresholdNode(pointName));

			// set click action for node
			data.getNode().setOnMouseClicked(event -> {

				// un-select if no modifier keys are selected
				if(!event.isShiftDown())
					selectionController.unSelectAction();

				selectionController.selectRowAction((RowData) data.getExtraValue());

				// edit if control is not down
				if(!event.isShiftDown())
					rowActionsController.editRowAction();

			});

			// add data to series
			series.getData().add(data);

			// add change listener to data
//			rowData.getValue().addListener((observable, oldValue, newValue) -> {
//				data.setYValue(newValue);
//			});

		}

		//defining the axes
		final NumberAxis xAxis = new NumberAxis();
		xAxis.setAutoRanging(true);
		xAxis.setForceZeroInRange(false);
		xAxis.setLabel("X koordináta");

		final NumberAxis yAxis = new NumberAxis();
		yAxis.setAutoRanging(true);
		yAxis.setForceZeroInRange(false);
		yAxis.setLabel("Y koordináta");

		// creating chart
		final ScatterChart<Number,Number> scatterChart;

		if(geodeticSystem.equals(GeodeticSystem.EOV)){
			scatterChart = new ScatterChart<>(
					yAxis,
					xAxis,
					FXCollections.observableArrayList(chartSeries.values())
			);
		}else{
			scatterChart = new ScatterChart<>(
					xAxis,
					yAxis,
					FXCollections.observableArrayList(chartSeries.values())
			);
		}

		// set up tooltip for mouse
		scatterChart.setOnMouseMoved(event -> {
			// get cursor value
			Double cursorXValue = (Double) xAxis.getValueForDisplay(event.getX() - 15);
			Double cursorYValue = (Double) yAxis.getValueForDisplay(event.getY() - 15);

			// set tooltip text
			cursorTooltip.setText(
					geodeticSystem.name() + " ( Y:" + decimalFormat.format(cursorYValue)
							+ ", X:" + decimalFormat.format(cursorXValue) + " )");

			// show or hide tooltip
			if (cursorYValue > yAxis.getLowerBound() && cursorYValue < yAxis.getUpperBound()){
				cursorTooltip.show(scatterChart, event.getScreenX() + 15, event.getScreenY() + 15);
			}else
				cursorTooltip.hide();
		});

		// set chart as center item
		borderPane.setCenter(scatterChart);

		// show window
		stage.show();
	}

	private void clearChangeListeners(){
		// detach listeners from value objects
		activeValueChangeListeners
				.entrySet()
				.forEach(entry -> {
					// remove listener from observableValue
					entry.getKey().removeListener(entry.getValue());
				});

		// clear listener map
		activeValueChangeListeners.clear();
	}


	/** a node which displays a value on hover, but is otherwise empty */
	class HoveredThresholdNode extends StackPane {

		HoveredThresholdNode(String name) {
			setPrefSize(8, 8);

			final Label label = new Label(name);
			label.setTextFill(Color.BLACK);
			label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
			label.getStyleClass().addAll("default-color0", "chart-line-symbol", "chart-series-line");

			setOnMouseEntered(mouseEvent -> {
				getChildren().setAll(label);
				setCursor(Cursor.HAND);
				toFront();
			});

			setOnMouseExited(mouseEvent -> {
				getChildren().clear();
				setCursor(Cursor.CROSSHAIR);
			});

		}

	}

	public void close(){
		stage.close();
	}

}
