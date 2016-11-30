package com.csviewpro.ui.view.common;

import com.csviewpro.controller.action.SelectionController;
import com.csviewpro.controller.util.ImageUtil;
import com.csviewpro.domain.math.GeoMath;
import com.csviewpro.domain.model.RowData;
import com.csviewpro.domain.model.enumeration.ColumnRole;
import com.csviewpro.domain.model.enumeration.GeodeticSystem;
import com.csviewpro.service.WorkspaceDataService;
import com.sun.javafx.scene.control.skin.TableHeaderRow;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.controlsfx.control.StatusBar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.DecimalFormat;

/**
 * Created by Balsa on 2016. 11. 26..
 */
@Component
public class DistanceMatrixView{

	private final String STATUSBAR_DEFAULT_TEXT = "Jelöljön ki egy értéket!";

	@Autowired
	private SelectionController selectionController;

	@Autowired
	private WorkspaceDataService workspaceDataService;

	@Autowired
	private ImageUtil imageUtil;

	// chart window
	private Stage stage = new Stage();

	// main layout
	private BorderPane borderPane = new BorderPane();

	// main scene
	private Scene scene = new Scene(borderPane);

	// status bar
	private StatusBar statusBar = new StatusBar();

	// decimal format
	private DecimalFormat decimalFormat = new DecimalFormat("#.###");


	@PostConstruct
	private void init(){
		stage.setScene(scene);
		stage.setTitle("Távolság mátrix");
		stage.getIcons().add(
				imageUtil.getResourceIconImage("actions/length_sm.png").getImage()
		);

		// set default text for status bar
		statusBar.setText(STATUSBAR_DEFAULT_TEXT);

		borderPane.setBottom(statusBar);
	}

	public void showAndUpdate(){

		// get geodetic system
		GeodeticSystem geodeticSystem = workspaceDataService
				.getActiveDataSet()
				.getDataSetMetaData()
				.getGeodeticSystem();

		// currently only EOV is supported
		if(!geodeticSystem.equals(GeodeticSystem.EOV)){
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Nem támogatott vetületi rendszer.");
			alert.setHeaderText(null);
			alert.setContentText("Távolság-mátrix jelenleg csak EOV vetületi rendszer esetében támogatott.");
			alert.show();
			return;
		}

		// get selected values
		ObservableList<RowData> selectedValues = selectionController.getSelectedPoints();

		// set title
		stage.setTitle("Távolság mátrix ("+selectedValues.size()+" pont)");

		// generate matrix from data
		TableView matrix = buildDistanceMatrix(selectedValues);

		// set chart as center item
		borderPane.setCenter(matrix);

		// show window
		stage.show();

	}

	/**
	 * This method builds a new distance matrix from the data given as argument.
	 * @param data
	 * @return
	 */
	private TableView buildDistanceMatrix(ObservableList<RowData> data){

		// create table
		TableView resultView = new TableView();

		// adding all items
		resultView.getItems().addAll(data);

		// set properties for table view
		resultView.setEditable(false);
		resultView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		resultView.getSelectionModel().setCellSelectionEnabled(true);

		// disable reordering
		resultView.widthProperty().addListener((source, oldWidth, newWidth) -> {
			TableHeaderRow header = (TableHeaderRow) resultView.lookup("TableHeaderRow");
			header.reorderingProperty().addListener((observable, oldValue, newValue) -> header.setReordering(false));
		});

		// get point name index
		Integer nameColumnIndex = workspaceDataService
				.getActiveDataSet()
				.getDataSetMetaData()
				.getColumIndexForRole(ColumnRole.POINTNAME);

		// get point x coordinate index
		Integer xColumnIndex = workspaceDataService
				.getActiveDataSet()
				.getDataSetMetaData()
				.getColumIndexForRole(ColumnRole.XCOORDINATE);

		// get point y coordinate index
		Integer yColumnIndex = workspaceDataService
				.getActiveDataSet()
				.getDataSetMetaData()
				.getColumIndexForRole(ColumnRole.YCOORDINATE);

		// add first column for names
		TableColumn nameColumn = new TableColumn();
		nameColumn.setEditable(false);
		nameColumn.setSortable(false);
		nameColumn.setStyle("-fx-background-color: #AAAAAA;");
		nameColumn.setCellValueFactory(param -> {
			TableColumn.CellDataFeatures<RowData, Object> p = (TableColumn.CellDataFeatures<RowData, Object>) param;
			// row data
			RowData row = p.getValue();

			return new SimpleStringProperty("" + row.get(nameColumnIndex).getValue());
		});

		// add name column
		resultView.getColumns().add(0,nameColumn);

		// do the calculation for every cell
		for(RowData columnData : data) {

			// get point data for every point
			String pointName = null;

			if (nameColumnIndex != null)
				pointName = columnData.get(nameColumnIndex).getValue().toString();

			if (pointName == null || "".equals(pointName.trim()))
				pointName = "névtelen pont";

			// get coordinates for column
			Double x1 = (Double) columnData.get(xColumnIndex).getValue();
			Double y1 = (Double) columnData.get(yColumnIndex).getValue();

			// create column for actual point
			TableColumn column = new TableColumn();
			column.setEditable(false);
			column.setSortable(false);
			column.setText(pointName);

			// calculate cell value
			column.setCellValueFactory(param -> {
				TableColumn.CellDataFeatures<RowData, Object> p = (TableColumn.CellDataFeatures<RowData, Object>) param;

				// row data
				RowData row = p.getValue();

				// row values
				double x2 = (Double) row.get(xColumnIndex).getValue();
				double y2 = (Double) row.get(yColumnIndex).getValue();

				// calculate distance
				return new SimpleStringProperty(
						decimalFormat.format(
								GeoMath.distance(x1, y1, x2, y2)
						)
				);

			});

			// add column for the point
			resultView.getColumns().add(column);

		}

		// set up selection action
		this.setupSelectionAction(resultView, data, nameColumnIndex, xColumnIndex, yColumnIndex);

		return resultView;
	}

	/**
	 * Sets up element selection action for table
	 */
	private void setupSelectionAction(
			TableView tableView,
			ObservableList<RowData> data,
			Integer nameColumnIndex,
			Integer xColumnIndex,
			Integer yColumnIndex
	){

		// create selection listener
		tableView.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {

			// selected cells
			ObservableList<TablePosition> selectedCells = tableView.getSelectionModel().getSelectedCells();

			// there should be only one item selected
			TablePosition position = selectedCells.get(0);

			// clear status bar
			statusBar.getLeftItems().clear();

			// return if no cell is selected or the selected column is the first
			if(position == null || position.getColumn() == 0) {
				statusBar.setText(STATUSBAR_DEFAULT_TEXT);
				return;
			}

			// remove status bar text
			statusBar.setText("");

			// get two points
			RowData columnPoint = data.get(position.getColumn() - 1); // -1 is for ignoring the first column
			RowData rowPoint = data.get(position.getRow());

			// select the two points on the main table
//			selectionController.unSelectAction();
//			selectionController.selectRowAction(columnPoint);
//			selectionController.selectRowAction(rowPoint);

			// get point data for every point
			String pointName1 = null;
			String pointName2 = null;

			if (nameColumnIndex != null){
				pointName1 = rowPoint.get(nameColumnIndex).getValue().toString();
				pointName2 = columnPoint.get(nameColumnIndex).getValue().toString();
			}

			if (pointName1 == null || "".equals(pointName1.trim()))
				pointName1 = "névtelen pont";

			if (pointName2 == null || "".equals(pointName2.trim()))
				pointName2 = "névtelen pont";

			// add status bar column for names
			Label pointsLabel = new Label(pointName1 + " \uD83E\uDC7A " + pointName2);
			pointsLabel.setPadding(new Insets(2,6,1,2));
			pointsLabel.setGraphic(
					imageUtil.getResourceIconImage("actions/points_sm.png")
			);
			statusBar.getLeftItems().add(pointsLabel);

			// count delta X and Y
			double x1 = (Double) rowPoint.get(xColumnIndex).getValue();
			double y1 = (Double) rowPoint.get(yColumnIndex).getValue();
			double x2 = (Double) columnPoint.get(xColumnIndex).getValue();
			double y2 = (Double) columnPoint.get(yColumnIndex).getValue();

			double dx = x2 - x1;
			double dy = y2 - y1;

			// delta Y
			Label deltaYLabel = new Label("\u0394Y = "+decimalFormat.format(dy)+"m");
			deltaYLabel.setPadding(new Insets(2,6,1,2));
			deltaYLabel.setGraphic(
					imageUtil.getResourceIconImage("actions/ycoo_sm.png")
			);
			statusBar.getLeftItems().add(deltaYLabel);

			// delta X
			Label deltaXLabel = new Label("\u0394X = "+decimalFormat.format(dx)+"m");
			deltaXLabel.setPadding(new Insets(2,6,1,2));
			deltaXLabel.setGraphic(
					imageUtil.getResourceIconImage("actions/xcoo_sm.png")
			);
			statusBar.getLeftItems().add(deltaXLabel);

			// heading angle
			double heading = GeoMath.headingAngle(dx, dy);
			String headingDMS = GeoMath.formatDegreeToDMS(heading);

			Label deltaAngleLabel = new Label(
					"δ = " + headingDMS + " (" + decimalFormat.format(heading) + "°)"
			);
			deltaAngleLabel.setPadding(new Insets(2,6,1,2));
			deltaAngleLabel.setGraphic(
					imageUtil.getResourceIconImage("actions/degree_sm.png")
			);
			statusBar.getLeftItems().add(deltaAngleLabel);

		});

	}

	public void close(){
		stage.close();
	}
}
