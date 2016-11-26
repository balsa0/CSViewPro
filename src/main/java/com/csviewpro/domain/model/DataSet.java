package com.csviewpro.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.collections.ObservableList;

import java.io.Serializable;

/**
 * This class represents a data set with metadata.
 * This class can be serialized and stored to keep an actual working project.
 */
public class DataSet implements Serializable{

	// header metadata
	@JsonProperty
	private DataSetMetaData dataSetMetaData;

	// points data
	@JsonProperty
	private ObservableList<RowData> points;

	/**
	 * This constructor creates a data set.
	 * @param dataSetMetaData the header descriptor metadata.
	 * @param points the points to store.
	 */
	public DataSet(DataSetMetaData dataSetMetaData, ObservableList<RowData> points) {
		this.dataSetMetaData = dataSetMetaData;
		this.points = points;
	}

	public DataSetMetaData getDataSetMetaData() {
		return dataSetMetaData;
	}

	public void setDataSetMetaData(DataSetMetaData dataSetMetaData) {
		this.dataSetMetaData = dataSetMetaData;
	}

	public ObservableList<RowData> getPoints() {
		return points;
	}

	public void setPoints(ObservableList<RowData> points) {
		this.points = points;
	}
}
