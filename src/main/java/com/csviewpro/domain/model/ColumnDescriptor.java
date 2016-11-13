package com.csviewpro.domain.model;

import com.csviewpro.domain.model.enumeration.ColumnRole;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * Created by Balsa on 2016. 10. 31..
 */
public class  ColumnDescriptor {

	@JsonProperty
	private Class type;

	@JsonProperty
	private String name;

	@JsonProperty
	private ColumnRole role;

	public ColumnDescriptor(Class type, String name, ColumnRole role) {
		this.type = type;
		this.name = getColName(name, role);
		this.role = role;
	}

	public ColumnDescriptor(String name, ColumnRole role) {
		this(String.class, name, role);
	}

	private String getColName(String name, ColumnRole role){
		return null == name || "".equals(name)
				? role.getDefaultTitle()
				: name;
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

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("type", type != null ? type.getSimpleName() : "null")
				.add("name", name)
				.add("role", role)
				.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ColumnDescriptor that = (ColumnDescriptor) o;
		return Objects.equal(type, that.type) &&
				Objects.equal(name, that.name) &&
				role == that.role;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(type, name, role);
	}
}
