package com.csviewpro.controller;

import com.csviewpro.controller.util.ImageUtil;
import com.csviewpro.domain.model.ColumnDescriptor;
import com.csviewpro.domain.model.DataSet;
import com.csviewpro.domain.model.RowData;
import com.csviewpro.service.WorkspaceDataService;
import com.csviewpro.ui.view.common.PointEditorSheet;
import com.csviewpro.ui.view.numeric.NumericView;
import com.csviewpro.ui.view.numeric.assets.TableGrid;
import com.sun.javafx.scene.control.skin.TableHeaderRow;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.util.Callback;
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

	@Autowired
	private ImageUtil imageUtil;

	public void rebuildTable() {

		// clear the table
		clearTable();

		// retrieve the data set from the service
		DataSet dataSet = workspaceDataService.getActiveDataSet();

		// null check
		if (dataSet == null)
			throw new NullPointerException("Can not rebuild table. Data set is null.");

		//http://stackoverflow.com/questions/29489366/how-to-add-button-in-javafx-table-view

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

		// add index column
		tableGrid.getColumns().add(generateIndexColumn());

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

		// disable reordering
		tableGrid.widthProperty().addListener((source, oldWidth, newWidth) -> {
			TableHeaderRow header = (TableHeaderRow) tableGrid.lookup("TableHeaderRow");
			header.reorderingProperty().addListener((observable, oldValue, newValue) -> header.setReordering(false));
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
					column.setId("coo");
					column.setGraphic(imageUtil.getResourceIconImage("actions/xcoo_sm.png"));
					break;
				case YCOORDINATE:
					column.setId("coo");
					column.setGraphic(imageUtil.getResourceIconImage("actions/ycoo_sm.png"));
					break;
				case ZCOORDINATE:
					column.setId("coo");
					column.setGraphic(imageUtil.getResourceIconImage("actions/zcoo_sm.png"));
					break;
				case POINTNAME:
					column.setId("pname");
					column.setGraphic(imageUtil.getResourceIconImage("actions/marker_sm.png"));
					break;
				case POINTCODE:
					column.setGraphic(imageUtil.getResourceIconImage("actions/pcode_sm.png"));
					column.setId("general");
					break;
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

	private TableColumn generateIndexColumn(){
		// create button table
		TableColumn indexColumn = new TableColumn();
		indexColumn.setEditable(false);
		indexColumn.setSortable(false);
		indexColumn.setGraphic(imageUtil.getResourceIconImage("actions/edit_multi_sm.png"));
		indexColumn.setText(null);

		// cell factory
		Callback<TableColumn<RowData, String>, TableCell<RowData, String>> cellFactory = //
				new Callback<TableColumn<RowData, String>, TableCell<RowData, String>>()
				{
					@Override
					public TableCell call( final TableColumn<RowData, String> param )
					{
						final TableCell<RowData, String> cell = new TableCell<RowData, String>()
						{

							final Button btn = new Button();

							@Override
							public void updateItem( String item, boolean empty )
							{
								super.updateItem( item, empty );
								if ( empty )
								{
									// hide everything
									setGraphic( null );
									setText( null );
								}
								else
								{
									// set button properties
									btn.setPadding(new Insets(2));
									btn.setId("editColumnBtn");
									btn.setGraphic(imageUtil.getResourceIconImage("actions/edit_sm.png", 18));
									btn.setOnAction( ( ActionEvent event ) ->
									{
										// get actual row
										RowData row = getTableView().getItems().get(getIndex());

										// create property editor for the row
										PointEditorSheet editor = new PointEditorSheet(
												row, workspaceDataService.getActiveDataSet().getHeaderDescriptor()
										);

										// highlight the row
										tableGrid.getSelectionModel().clearSelection();
										tableGrid.getSelectionModel().select(getIndex());

										// set property sheet
										numericView.setRight(editor);

										// set focus on editor
										editor.requestFocus();

									} );
									// cell padding
									this.setPadding(new Insets(0));
									this.setGraphic(btn);
									this.setText(null);
								}
							}
						};
						return cell;
					}
				};

		indexColumn.setCellFactory( cellFactory );

		return indexColumn;
	}

}
