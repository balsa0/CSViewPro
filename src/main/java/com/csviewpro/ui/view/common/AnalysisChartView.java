package com.csviewpro.ui.view.common;

import com.csviewpro.controller.actioncontroller.RowActionsController;
import com.csviewpro.controller.actioncontroller.SelectionController;
import com.csviewpro.controller.util.ImageUtil;
import com.csviewpro.domain.conversion.DoubleConverter;
import com.csviewpro.domain.conversion.TypeConverter;
import com.csviewpro.domain.model.RowData;
import com.csviewpro.domain.model.enumeration.ColumnRole;
import com.csviewpro.service.WorkspaceDataService;
import com.google.common.collect.Table;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
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
public class AnalysisChartView{

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
		stage.setTitle("Grafikus elemzés");
		stage.getIcons().add(
				imageUtil.getResourceIconImage("actions/chart_area_sm.png").getImage()
		);

		// tooltip
		cursorTooltip.setConsumeAutoHidingEvents(true);
		cursorTooltip.setAutoHide(true);

		stage.setOnHiding(event -> {
			// clear change listeners
			clearChangeListeners();
		});

		stage.setOnCloseRequest(event -> {
			// clear change listeners
			clearChangeListeners();
		});

		scene.setOnMouseExited(event -> {
			cursorTooltip.hide();
		});

	}

	public void showAndUpdate(){

		// fist clear the active value change listeners
		clearChangeListeners();

		// get selected values
		Table<Integer, Integer, ObservableValue> selectedValues = selectionController.getCellValues();

		// set title
		stage.setTitle("Grafikus elemzés (" + selectedValues.size() + " elem)");

		// create series from sets
		// map <column index, series>
		Map<Integer, XYChart.Series<String, Number>> chartSeries = new HashMap<>();

		// get name column index
		Integer nameColumnIndex = workspaceDataService
				.getActiveDataSet()
				.getDataSetMetaData()
				.getColumIndexForRole(ColumnRole.POINTNAME);

		// do the conversion for every cell
		selectedValues.cellSet().forEach(cell -> {

			Integer rowIndex = cell.getRowKey();
			Integer columnIndex = cell.getColumnKey();

			// create or get series
			XYChart.Series series;
			if(chartSeries.containsKey(columnIndex)){
				series = chartSeries.get(columnIndex);
			}else{
				// new series
				series = new XYChart.Series();
				// get name of the series
				String columnName = workspaceDataService
						.getActiveDataSet()
						.getDataSetMetaData()
						.getDescriptorData()
						.get(columnIndex)
						.getName();

				// if column name is not set
				if(columnName == null || "".equals(columnName.trim())){
					columnName = "" + columnIndex + ". oszlop";
				}

				// column name
				series.setName(columnName);

				// put the series to the map
				chartSeries.put(columnIndex, series);
			}

			// get point from dataset
			RowData rowData = workspaceDataService
					.getActiveDataSet()
					.getPoints()
					// get the actual row from data set
					.get(rowIndex);

			// set point name
			String pointName = "" + chartSeries.size();

			// if name column is avaiable
			if(nameColumnIndex != null){
				pointName = rowData
						// get it's name
						.get(nameColumnIndex)
						.getValue()
						.toString();
			}

			// value of the cell
			Object value = cell.getValue().getValue();

			// create XY data for chart
			XYChart.Data data = new XYChart.Data(
					pointName,
					// convert types if necessary
					Number.class.isAssignableFrom(value.getClass())
							? value : doubleConverter.convert(value.toString())
			);

			// add the point to the data
			data.setExtraValue(rowData);

			// create node for hover events
			data.setNode(new HoveredThresholdNode(pointName));

			// set click action for node
			data.getNode().setOnMouseClicked(event -> {

				// un-select if no modifier keys are selected
				if(!event.isShiftDown())
					selectionController.unSelectAction();

				if(event.isControlDown()){
					selectionController.selectCellAction(rowIndex, columnIndex);
				}else{
					selectionController.selectRowAction((RowData) data.getExtraValue());
					// edit if control is not down
					if(!event.isShiftDown())
						rowActionsController.editRowAction();
				}

			});

			// add data to series
			series.getData().add(data);

			// add change listener to data
			cell.getValue().addListener((observable, oldValue, newValue) -> {
				data.setYValue(newValue);
			});

		});

		//defining the axes
		final CategoryAxis xAxis = new CategoryAxis();
		xAxis.setAutoRanging(true);

		final NumberAxis yAxis = new NumberAxis();
		yAxis.setAutoRanging(true);
		yAxis.setForceZeroInRange(false);

		// creating chart
		final AreaChart<String,Number> lineChart = new AreaChart<>(
				xAxis,
				yAxis,
				FXCollections.observableArrayList(chartSeries.values())
		);

		// set up tooltip for mouse
		lineChart.setOnMouseMoved(event -> {
			// get cursor value
			Double cursorYValue = (Double) yAxis.getValueForDisplay(event.getY() - 15);

			// set tooltip text
			cursorTooltip.setText(decimalFormat.format(cursorYValue));

			// show or hide tooltip
			if (cursorYValue > yAxis.getLowerBound() && cursorYValue < yAxis.getUpperBound()){
				cursorTooltip.show(lineChart, event.getScreenX() + 15, event.getScreenY() + 15);
			}else
				cursorTooltip.hide();
		});

		// set chart as center item
		borderPane.setCenter(lineChart);

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
