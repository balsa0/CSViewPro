package com.csviewpro.controller;
import com.csviewpro.domain.model.ColumnDescriptor;
import com.csviewpro.domain.model.DataSet;
import com.csviewpro.domain.model.GeoPoint;
import com.csviewpro.service.WorkspaceDataService;
import com.csviewpro.ui.view.numericassets.TableGrid;
import javafx.beans.property.SimpleDoubleProperty;
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
		TableColumn column = new TableColumn<GeoPoint, Object>(descriptor.getName());

		column.setCellValueFactory(param -> {
			TableColumn.CellDataFeatures<GeoPoint, Object> p = (TableColumn.CellDataFeatures<GeoPoint, Object>) param;
			switch (descriptor.getRole()){
				case XCOORDINATE:
					return new SimpleDoubleProperty(p.getValue().getxCoo() == null ? 0 : p.getValue().getxCoo());
				case YCOORDINATE:
					return new SimpleDoubleProperty(p.getValue().getyCoo() == null ? 0 : p.getValue().getyCoo());
				case ZCOORDINATE:
					return new SimpleDoubleProperty(p.getValue().getzCoo() == null ? 0 : p.getValue().getzCoo());
				case POINTNAME:
					return new SimpleStringProperty(p.getValue().getName() == null ? "" : p.getValue().getName());
				case POINTCODE:
					return new SimpleStringProperty(p.getValue().getCode() == null ? "" : p.getValue().getCode());
				default:
					return new SimpleStringProperty("todo");
			}
		});

		return column;
	}

}
