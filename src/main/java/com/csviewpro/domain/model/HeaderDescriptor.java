package com.csviewpro.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Balsa on 2016. 10. 31..
 */
public class HeaderDescriptor implements Serializable{

	@JsonProperty("descriptors")
	private Map<Integer, ColumnDescriptor> descriptorData;

	public HeaderDescriptor(){
		descriptorData = new HashMap<>();
	}

	public HeaderDescriptor(Map<Integer, ColumnDescriptor> descriptorData) {
		this.descriptorData = descriptorData;
	}
}
