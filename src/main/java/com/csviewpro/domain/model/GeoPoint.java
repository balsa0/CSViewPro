package com.csviewpro.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Balsa on 2016. 10. 31..
 */
public class GeoPoint implements Serializable {

	@JsonProperty
	private String name;

	@JsonProperty("x")
	private Double xCoo;

	@JsonProperty("y")
	private Double yCoo;

	@JsonProperty("z")
	private Double zCoo;

	@JsonProperty
	private String code;

	@JsonProperty("valueMap")
	private Map<Integer, Object> additional = new HashMap<>();

	public GeoPoint() {
	}

	public GeoPoint(Double xCoo, Double yCoo, Double zCoo) {
		this.xCoo = xCoo;
		this.yCoo = yCoo;
		this.zCoo = zCoo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getxCoo() {
		return xCoo;
	}

	public void setxCoo(Double xCoo) {
		this.xCoo = xCoo;
	}

	public Double getyCoo() {
		return yCoo;
	}

	public void setyCoo(Double yCoo) {
		this.yCoo = yCoo;
	}

	public Double getzCoo() {
		return zCoo;
	}

	public void setzCoo(Double zCoo) {
		this.zCoo = zCoo;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Map<Integer, Object> getAdditional() {
		return additional;
	}

	public void setAdditional(Map<Integer, Object> additional) {
		this.additional = additional;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("name", name)
				.add("xCoo", xCoo)
				.add("yCoo", yCoo)
				.add("zCoo", zCoo)
				.add("code", code)
				.add("additional", additional)
				.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		GeoPoint geoPoint = (GeoPoint) o;
		return Objects.equal(name, geoPoint.name) &&
				Objects.equal(xCoo, geoPoint.xCoo) &&
				Objects.equal(yCoo, geoPoint.yCoo) &&
				Objects.equal(zCoo, geoPoint.zCoo) &&
				Objects.equal(code, geoPoint.code) &&
				Objects.equal(additional, geoPoint.additional);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(name, xCoo, yCoo, zCoo, code, additional);
	}
}
