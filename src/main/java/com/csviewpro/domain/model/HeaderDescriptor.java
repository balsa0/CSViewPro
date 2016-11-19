package com.csviewpro.domain.model;

import com.csviewpro.domain.model.enumeration.ColumnRole;
import com.csviewpro.domain.model.enumeration.GeodeticSystem;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Balsa on 2016. 10. 31..
 */
public class HeaderDescriptor implements Serializable{

	@JsonProperty("descriptors")
	private Map<Integer, ColumnDescriptor> descriptorData;

	@JsonProperty("geodeticSystem")
	private GeodeticSystem geodeticSystem;

	@JsonProperty("numberFormat")
	private Locale numberFormatLocale;

	/**
	 * Creates a header descriptor based on the given properties.
	 * @param descriptorData Map of columnDescriptors.
	 * @param geodeticSystem the detected geodetic system.
	 * @param numberFormatLocale the detected number format locale.
	 */
	public HeaderDescriptor(
			Map<Integer, ColumnDescriptor> descriptorData,
			GeodeticSystem geodeticSystem,
			Locale numberFormatLocale
	) {
		this.descriptorData = descriptorData;
		this.geodeticSystem = geodeticSystem;
		this.numberFormatLocale = numberFormatLocale;
	}

	public Map<Integer, ColumnDescriptor> getDescriptorData() {
		return descriptorData;
	}

	public GeodeticSystem getGeodeticSystem() {
		return geodeticSystem;
	}

	public Locale getNumberFormatLocale() {
		return numberFormatLocale;
	}

	public Integer getColumIndexForRole(ColumnRole role){
		return getDescriptorData().entrySet()
				.stream()
				// filter for only
				.filter(entry -> role.equals(entry.getValue().getRole()))
				// map it to index
				.map(entry -> entry.getKey())
				// return the first index
				.findFirst()
				// or null if no index has been found
				.orElse(null);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("descriptorData", descriptorData)
				.add("geodeticSystem", geodeticSystem)
				.add("numberFormatLocale", numberFormatLocale)
				.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		HeaderDescriptor that = (HeaderDescriptor) o;
		return Objects.equal(descriptorData, that.descriptorData) &&
				geodeticSystem == that.geodeticSystem &&
				Objects.equal(numberFormatLocale, that.numberFormatLocale);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(descriptorData, geodeticSystem, numberFormatLocale);
	}
}
