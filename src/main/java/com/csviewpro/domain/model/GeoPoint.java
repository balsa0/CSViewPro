package com.csviewpro.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

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
}
