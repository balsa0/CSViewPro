package com.csviewpro.domain.model.enumeration;

/**
 * Created by Balsa on 2016. 10. 31..
 */
public enum ColumnRole {

	XCOORDINATE("X"),
	YCOORDINATE("Y"),
	ZCOORDINATE("Z"),
	POINTNAME("PontNév"),
	POINTCODE("PontKód"),
	OTHER("Egyéb");

	private final String defaultTitle;

	ColumnRole(String defaultTitle) {
		this.defaultTitle = defaultTitle;
	}

	public String getDefaultTitle() {
		return defaultTitle;
	}
}
