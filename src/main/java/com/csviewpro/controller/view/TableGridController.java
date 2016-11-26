package com.csviewpro.controller.view;

import com.csviewpro.controller.actioncontroller.RowActionsController;
import com.csviewpro.controller.actioncontroller.ViewActionController;
import com.csviewpro.controller.util.ImageUtil;
import com.csviewpro.domain.model.ColumnDescriptor;
import com.csviewpro.domain.model.DataSet;
import com.csviewpro.domain.model.RowData;
import com.csviewpro.service.WorkspaceDataService;
import com.csviewpro.ui.view.numeric.assets.NumericViewStatusBar;
import com.csviewpro.ui.view.numeric.assets.TableGrid;
import com.sun.javafx.scene.control.skin.TableHeaderRow;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;

/**
 * Created by Balsa on 2016. 10. 31..
 */
@Controller
public class TableGridController {

	@Autowired
	private TableGrid tableGrid;

	@Autowired
	private WorkspaceDataService workspaceDataService;

	@Autowired
	private ImageUtil imageUtil;

	@Autowired
	private RowActionsController rowActionsController;

	@Autowired
	private ViewActionController viewActionController;

	@Autowired
	private NumericViewStatusBar numericViewStatusBar;

	@PostConstruct
	private void init(){
		setupContextMenus();
	}

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
		dataSet.getDataSetMetaData().getDescriptorData()
				.entrySet()
				.stream()
				.forEach(entry -> {
					// add column to the table
					tableGrid.getColumns().add(
							entry.getKey(), generateTableColumn(entry.getKey(), entry.getValue())
					);
				});

		// add index column
		tableGrid.getColumns().add(generateEditColumn());

		// add all points to the list
		tableGrid.setItems(
				dataSet.getPoints()
		);

//		// on right click
//		tableGrid.setOnContextMenuRequested(event -> {
//
//
//
//		});

		// disable reordering
		tableGrid.widthProperty().addListener((source, oldWidth, newWidth) -> {
			TableHeaderRow header = (TableHeaderRow) tableGrid.lookup("TableHeaderRow");
			header.reorderingProperty().addListener((observable, oldValue, newValue) -> header.setReordering(false));
		});

		// request update for status bar
		numericViewStatusBar.updateIcons();

	}

	public void clearTable(){
		tableGrid.setItems(FXCollections.emptyObservableList());
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

	/**
	 * Generates an "edit action" column.
	 * @return generated {@link TableColumn}.
	 */
	private TableColumn generateEditColumn(){
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
								if ( empty ) {
									// hide everything
									setGraphic( null );
									setText( null );
								} else {
									// set button properties
									btn.setPadding(new Insets(2));
									btn.setId("editColumnBtn");
									btn.setGraphic(imageUtil.getResourceIconImage("actions/edit_sm.png", 18));
									btn.setOnAction( ( ActionEvent event ) ->
									{
										// get actual row
										RowData row = getTableView().getItems().get(getIndex());
										// edit the row
										rowActionsController.editRowAction(row);

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

	/**
	 * Sets up context menus.
	 */
	public void setupContextMenus(){

		// edit point menu
		MenuItem editPointMenu = new MenuItem(
				"Pont szerketszése",
				imageUtil.getResourceIconImage("actions/edit_sm.png")
		);
		editPointMenu.setOnAction(event -> {
			// edit row
			rowActionsController.editRowAction();
		});

		// delete point menu
		MenuItem deletePointMenu = new MenuItem(
				"Pont törlése",
				imageUtil.getResourceIconImage("actions/delete_sm.png")
		);
		deletePointMenu.setOnAction(event -> {
			// delete the row
			rowActionsController.deleteRowAction();
		});

		// add items to single selection menu
		tableGrid.getSingleSelectionContextMenu().getItems().addAll(
				editPointMenu,
				deletePointMenu
		);

		// graphical analysis menu
		MenuItem graphicalAnalysisMenu = new MenuItem(
				"Grafikus elemzés",
				imageUtil.getResourceIconImage("actions/chart_area_sm.png")
		);
		graphicalAnalysisMenu.setOnAction(event -> {
			viewActionController.graphicalAnalysisAction();
		});

		// point relation menu
		MenuItem pointRelationMenu = new MenuItem(
				"Térbeli elhelyezkedés",
				imageUtil.getResourceIconImage("actions/chart_scatter_sm.png")
		);
		pointRelationMenu.setOnAction(event -> {
			viewActionController.pointRelationAnalysis();
		});

		// delete points menu
		MenuItem deletePointsMenu = new MenuItem(
				"Pontok törlése",
				imageUtil.getResourceIconImage("actions/delete_sm.png")
		);
		deletePointsMenu.setOnAction(event -> {
			// delete the row
			rowActionsController.deleteRowAction();
		});

		// add items to multi-selection menu
		tableGrid.getMultiSelectionContextMenu().getItems().addAll(
				graphicalAnalysisMenu,
				pointRelationMenu,
				new SeparatorMenuItem(),
				deletePointsMenu
		);
	}




}
