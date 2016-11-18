package com.csviewpro.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.collections.ObservableList;

import java.io.Serializable;
import java.util.List;

/**
 * This class represents a data set with metadata.
 * This class can be serialized and stored to keep an actual working project.
 */
public class DataSet implements Serializable{

	// header metadata
	@JsonProperty
	private HeaderDescriptor headerDescriptor;

	// points data
	@JsonProperty
	private ObservableList<RowData> points;

	/**
	 * This constructor creates a data set.
	 * @param headerDescriptor the header descriptor metadata.
	 * @param points the points to store.
	 */
	public DataSet(HeaderDescriptor headerDescriptor, ObservableList<RowData> points) {
		this.headerDescriptor = headerDescriptor;
		this.points = points;
	}

	public HeaderDescriptor getHeaderDescriptor() {
		return headerDescriptor;
	}

	public void setHeaderDescriptor(HeaderDescriptor headerDescriptor) {
		this.headerDescriptor = headerDescriptor;
	}

	public ObservableList<RowData> getPoints() {
		return points;
	}

	public void setPoints(ObservableList<RowData> points) {
		this.points = points;
	}
}
