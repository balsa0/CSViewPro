package com.csviewpro.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

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
	private List<RowData> points;

	/**
	 * This constructor creates a data set.
	 * @param headerDescriptor the header descriptor metadata.
	 * @param points the points to store.
	 */
	public DataSet(HeaderDescriptor headerDescriptor, List<RowData> points) {
		this.headerDescriptor = headerDescriptor;
		this.points = points;
	}

	public HeaderDescriptor getHeaderDescriptor() {
		return headerDescriptor;
	}

	public void setHeaderDescriptor(HeaderDescriptor headerDescriptor) {
		this.headerDescriptor = headerDescriptor;
	}

	public List<RowData> getPoints() {
		return points;
	}

	public void setPoints(List<RowData> points) {
		this.points = points;
	}
}
