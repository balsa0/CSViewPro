package com.csviewpro.domain.model;

import com.csviewpro.domain.model.enumeration.ColumnRole;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

/**
 * Created by Balsa on 2016. 10. 31..
 */
public class ColumnDescriptor {

	public static final String DEFAULT_TITLE_X = "X";
	public static final String DEFAULT_TITLE_Y = "Y";
	public static final String DEFAULT_TITLE_Z = "Z";
	public static final String DEFAULT_TITLE_PNAME = "Pontnév";
	public static final String DEFAULT_TITLE_PCODE = "Kód";
	public static final String DEFAULT_TITLE_DATETIME = "Dátum/Idő";
	public static final String DEFAULT_TITLE_UNKNOWN = "Oszlop";

	private Class type;
	private String name;
	private ColumnRole role;

	public ColumnDescriptor(Class type, String name, ColumnRole role) {
		this.type = type;
		this.name = name;
		this.role = role;
	}

	public ColumnDescriptor(String name, ColumnRole role) {
		this.type = String.class;
		this.name = name;
		this.role = role;
	}

	public Class getType() {
		return type;
	}

	public void setType(Class type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ColumnRole getRole() {
		return role;
	}

	public void setRole(ColumnRole role) {
		this.role = role;
	}
}
