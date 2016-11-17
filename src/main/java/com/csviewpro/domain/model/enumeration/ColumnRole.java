package com.csviewpro.domain.model.enumeration;

/**
 * Created by Balsa on 2016. 10. 31..
 */
public enum ColumnRole {

	XCOORDINATE("X koord."),
	YCOORDINATE("Y koord."),
	ZCOORDINATE("Z koord."),
	POINTNAME("Pont"),
	POINTCODE("Pontkód"),
	OTHER("");

	private final String defaultTitle;

	ColumnRole(String defaultTitle) {
		this.defaultTitle = defaultTitle;
	}

	public String getDefaultTitle() {
		return defaultTitle;
	}
}
