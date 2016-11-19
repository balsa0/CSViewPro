package com.csviewpro.ui.view.common;

import com.csviewpro.controller.actioncontroller.SelectionController;
import com.csviewpro.controller.util.ImageUtil;
import com.csviewpro.service.WorkspaceDataService;
import com.google.common.collect.Table;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Balsa on 2016. 11. 18..
 */
@Component
public class AnalysisChartView{

	@Autowired
	private SelectionController selectionController;

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

	@PostConstruct
	private void init(){
		stage.setScene(scene);
		stage.setTitle("Grafikus elemzés");
		stage.getIcons().add(
				imageUtil.getResourceIconImage("actions/chart_area_sm.png").getImage()
		);
	}

	public void showAndUpdate(){

		// get selected values
		Table<Integer, Integer, ObservableValue> selectedValues = selectionController.getCellValues();

		// convert values to sets
		Map<Integer, List<ObservableValue>> sets = new HashMap<>();

		// set title
		stage.setTitle("Grafikus elemzés (" + selectedValues.size() + " elem)");

		// do the conversion for every cell
		selectedValues.cellSet().forEach(cell -> {

			// create list if not created yet
			if(sets.get(cell.getColumnKey()) == null)
				sets.put(cell.getColumnKey(), new ArrayList<>());

			// add the value to the list
			sets.get(cell.getColumnKey()).add(cell.getValue());
		});

		// create series from sets
		ObservableList<XYChart.Series<Number, Number>> chartSeries = FXCollections.observableArrayList();

		sets.forEach((index, observableValues) -> {;

			// creating a new series
			XYChart.Series series = new XYChart.Series();

			// get name of the series
			String columnName = workspaceDataService
					.getActiveDataSet()
					.getHeaderDescriptor()
					.getDescriptorData()
					.get(index)
					.getName();

			// if column name is not set
			if(columnName == null || "".equals(columnName.trim())){
				columnName = "" + index + ". oszlop";
			}

			// set name of the series
			series.setName(columnName);

			// add values
			for (int i = 0; i < observableValues.size(); i++) {
				series.getData().add(new XYChart.Data(i, observableValues.get(i).getValue()));
			}

			chartSeries.add(series);

		});

		//defining the axes
		final NumberAxis xAxis = new NumberAxis();
		xAxis.setAutoRanging(true);
		xAxis.setForceZeroInRange(false);

		final NumberAxis yAxis = new NumberAxis();
		yAxis.setAutoRanging(true);
		yAxis.setForceZeroInRange(false);

		// creating chart
		final AreaChart<Number,Number> lineChart =
				new AreaChart<>(xAxis, yAxis, chartSeries);

		// set chart as center item
		borderPane.setCenter(lineChart);

		// show window
		stage.show();
	}

}
