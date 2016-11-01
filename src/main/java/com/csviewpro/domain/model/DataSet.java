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

	// geodetic system metadata
	@JsonProperty
	private GeodeticSystem geodeticSystem;

	// points data
	@JsonProperty
	private List<GeoPoint> points;

	/**
	 * This constructor creates an empty data set.
	 */
	public DataSet() {
		this(new HeaderDescriptor(), new ArrayList<>());
	}

	/**
	 * This constructor creates a data set with EOV geodetic system.
	 * @param headerDescriptor the header descriptor metadata.
	 * @param points the points to store.
	 */
	public DataSet(HeaderDescriptor headerDescriptor, List<GeoPoint> points) {
		// using eov as default geodetic system
		this(headerDescriptor, points, GeodeticSystem.EOV);
	}

	/**
	 * This constructor creates a data set.
	 * @param headerDescriptor the header descriptor metadata.
	 * @param points the points to store.
	 * @param geodeticSystem the geodetic system descriptor.
	 */
	public DataSet(HeaderDescriptor headerDescriptor, List<GeoPoint> points, GeodeticSystem geodeticSystem) {
		this.headerDescriptor = headerDescriptor;
		this.points = points;
		this.geodeticSystem = geodeticSystem;
	}


}
