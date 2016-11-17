package com.csviewpro.controller;

import com.csviewpro.domain.model.ColumnDescriptor;
import com.csviewpro.domain.model.DataSet;
import com.csviewpro.domain.model.RowData;
import com.csviewpro.service.WorkspaceDataService;
import com.csviewpro.ui.view.common.PointEditorSheet;
import com.csviewpro.ui.view.numeric.NumericView;
import com.csviewpro.ui.view.numeric.assets.TableGrid;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Created by Balsa on 2016. 10. 31..
 */
@Controller
public class TableGridController {

	@Autowired
	private NumericView numericView;

	@Autowired
	private TableGrid tableGrid;

	@Autowired
	private WorkspaceDataService workspaceDataService;

	public void rebuildTable(){

		// clear the table
		clearTable();

		// retrieve the data set from the service
		DataSet dataSet = workspaceDataService.getActiveDataSet();

		// null check
		if(dataSet == null)
			throw new NullPointerException("Can not rebuild table. Data set is null.");

		// start building the table
		dataSet.getHeaderDescriptor().getDescriptorData()
			.entrySet()
			.stream()
			.forEach(entry -> {
				// add column to the table
				tableGrid.getColumns().add(
						entry.getKey(), generateTableColumn(entry.getKey(), entry.getValue())
				);
			});

		// add all points to the list
		tableGrid.getItems().addAll(
				dataSet.getPoints()
		);

		// on right click
		tableGrid.setOnContextMenuRequested(event -> {

			PointEditorSheet editor = new PointEditorSheet(
					(RowData) tableGrid.getSelectionModel().getSelectedItem(),
					workspaceDataService.getActiveDataSet().getHeaderDescriptor()
			);

			numericView.setRight(editor);

		});

	}

	public void clearTable(){
		tableGrid.getItems().clear();
		tableGrid.getColumns().clear();
	}

	/**
	 * This method generates a new table column with all properties set.
	 * @param descriptor the descriptor of the column.
	 * @return the generated table column
	 */
	private TableColumn generateTableColumn(Integer index, ColumnDescriptor descriptor){

		// create a new column
		TableColumn column = new TableColumn<RowData, Object>(descriptor.getName());

//		// cell factory
//		if(Number.class.isAssignableFrom(descriptor.getType()))
//			column.setCellFactory(TextFieldTableCell.<RowData, Number>forTableColumn(new NumberStringConverter()));
//		else
//			column.setCellFactory(TextFieldTableCell.<RowData>forTableColumn());


		// cell value factory
		column.setCellValueFactory(param -> {
			TableColumn.CellDataFeatures<RowData, Object> p = (TableColumn.CellDataFeatures<RowData, Object>) param;

			// row data
			Object cell = p.getValue().get(index);

			// add some content dependent
			switch (descriptor.getRole()){
				case XCOORDINATE:
				case YCOORDINATE:
				case ZCOORDINATE:
					column.setId("coo");
					break;
				case POINTNAME:
					column.setId("pname");
					break;
				case POINTCODE:
				default:
					column.setId("general");
					break;
			}

			return cell;
		});

		column.setSortable(false);
		column.setEditable(true);

		return column;
	}

}
