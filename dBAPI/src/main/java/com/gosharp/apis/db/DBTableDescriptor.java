package com.gosharp.apis.db;


import java.util.ArrayList;
import java.util.List;

public class DBTableDescriptor {
	
	private boolean hasId;
	private String table_name;
	private List<DBTableFieldDescriptor> fields;
	private long version;
	
	public DBTableDescriptor() {
		fields = new ArrayList<DBTableFieldDescriptor>();
	}
	
	public DBTableDescriptor(boolean hasId, String table_name,
			List<DBTableFieldDescriptor> fields, long version) {
		super();
		this.hasId = hasId;
		this.table_name = table_name;
		this.fields = fields;
		this.version = version;
	}
	
	public boolean isHasId() {
		return hasId;
	}
	public void setHasId(boolean hasId) {
		this.hasId = hasId;
	}
	public String getTable_name() {
		return table_name;
	}
	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}
	public List<DBTableFieldDescriptor> getFields() {
		return fields;
	}
	public void setFields(List<DBTableFieldDescriptor> fields) {
		this.fields = fields;
	}
	public long getVersion() {
		return version;
	}
	public void setVersion(long version) {
		this.version = version;
	}
	public boolean addField(DBTableFieldDescriptor field) {
		fields.add(field);
		return true;
	}

}
