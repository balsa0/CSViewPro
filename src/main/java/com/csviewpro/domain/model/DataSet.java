package com.csviewpro.domain.model;

import com.csviewpro.domain.model.enumeration.GeodeticSystem;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
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
	private List<GeoPoint> points;


	/**
	 * This constructor creates a data set.
	 * @param headerDescriptor the header descriptor metadata.
	 * @param points the points to store.
	 */
	public DataSet(HeaderDescriptor headerDescriptor, List<GeoPoint> points) {
		this.headerDescriptor = headerDescriptor;
		this.points = points;
	}


}
