package com.csviewpro.domain.model;

import com.csviewpro.domain.model.enumeration.ColumnRole;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

/**
 * Created by Balsa on 2016. 10. 31..
 */
public class HeaderDescriptor {

	public static final String DEFAULT_TITLE_X = "X";
	public static final String DEFAULT_TITLE_Y = "Y";
	public static final String DEFAULT_TITLE_Z = "Z";
	public static final String DEFAULT_TITLE_PNAME = "Pontnév";
	public static final String DEFAULT_TITLE_PCODE = "Kód";

	// <Index, Role, Title>
	private Table<Integer, ColumnRole, String> descriptorData;

	public HeaderDescriptor(){
		descriptorData = HashBasedTable.create();
	}

	public HeaderDescriptor(Table<Integer, ColumnRole, String> descriptorData) {
		this.descriptorData = descriptorData;
	}

	public Table<Integer, ColumnRole, String> getDescriptorData() {
		return descriptorData;
	}

	public void setDescriptorData(Table<Integer, ColumnRole, String> descriptorData) {
		this.descriptorData = descriptorData;
	}
}
