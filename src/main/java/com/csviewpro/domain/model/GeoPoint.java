package com.csviewpro.domain.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Balsa on 2016. 10. 31..
 */
public class GeoPoint {

	private String name;
	private Double xCoo;
	private Double yCoo;
	private Double zCoo;
	private String code;

	private Map<Integer, Object> others = new HashMap<>();

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
