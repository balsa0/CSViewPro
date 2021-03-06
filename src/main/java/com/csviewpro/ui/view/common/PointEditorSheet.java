package com.csviewpro.ui.view.common;

import com.csviewpro.domain.model.ColumnDescriptor;
import com.csviewpro.domain.model.DataSetMetaData;
import com.csviewpro.domain.model.RowData;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import org.controlsfx.control.PropertySheet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Balsa on 2016. 11. 17..
 */
public class PointEditorSheet extends PropertySheet {

	// / editable properties
	private RowData point;
	private DataSetMetaData dataSetMetaData;

	public RowData getPoint() {
		return point;
	}

	public DataSetMetaData getDataSetMetaData() {
		return dataSetMetaData;
	}

	public PointEditorSheet(RowData point, DataSetMetaData dataSetMetaData) {
		this.point = point;
		this.dataSetMetaData = dataSetMetaData;

		List<PointEditorItem> items = new ArrayList<>();

		// go trough every column
		for(Map.Entry entry : dataSetMetaData.getDescriptorData().entrySet()){
			items.add(
					new PointEditorItem(
							(ColumnDescriptor) entry.getValue(),
							point,
							(Integer)entry.getKey()
					)
			);
		}

		// add all items
		this.getItems().addAll(items);

		// set some properties
		setSearchBoxVisible(false);



	}


	// inner class
	class PointEditorItem implements Item {

		private ColumnDescriptor descriptor;
		private RowData point;
		private Integer index;

		public PointEditorItem(ColumnDescriptor descriptor, RowData point, Integer index) {
			this.descriptor = descriptor;
			this.point = point;
			this.index = index;
		}

		@Override
		public Class<?> getType() {
			return descriptor.getType();
		}

		@Override
		public String getCategory() {
			switch (descriptor.getRole()){
				case POINTNAME:
				case POINTCODE:
					return "Azonosító";
				case XCOORDINATE:
				case YCOORDINATE:
				case ZCOORDINATE:
					return "Koordináták";
				default:
					return "Egyéb tulajdonságok";
			}
		}

		@Override
		public String getName() {
			return descriptor.getName();
		}

		@Override
		public String getDescription() {
			return "";
		}

		@Override
		public Object getValue() {
			return point.get(index).getValue();
		}

		@Override
		public void setValue(Object o) {

			ObservableValue value = point.get(index);

			// set value of the cell
			if(descriptor.getType().equals(Long.class))
				((LongProperty) value).set((Long) o);
			else if(descriptor.getType().equals(Double.class))
				((DoubleProperty) value).set((Double) o);
			else
				((StringProperty) value).set((String) o);

		}

		@Override
		public Optional<ObservableValue<?>> getObservableValue() {
			return Optional.of(point.get(index));
		}
	}

}
