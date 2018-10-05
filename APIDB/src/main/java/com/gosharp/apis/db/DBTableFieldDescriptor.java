package com.gosharp.apis.db;


public class DBTableFieldDescriptor {
	public static final int TYPE_NUMERIC = 1;
	public static final int TYPE_REAL = 2;
	public static final int TYPE_TEXT = 3;
	public static final int TYPE_BLOB = 4;
	public static final int TYPE_INTEGER = 5;
	
	private String cname;
	private int type;
	private boolean isPrimaryKey;
	private boolean isNullable;
	private boolean isAutoIncrement;
	private String classFieldName;
	private boolean ignoreOnInsert; //para ignorar al hacer un insert ie campo ID auto increment
	
	public DBTableFieldDescriptor() {
		setPrimaryKey(false);
		setNullable(true);
	}
	
	public DBTableFieldDescriptor(String name) {
		setPrimaryKey(false);
		setNullable(true);
		setCname(name);
	}
	
	public DBTableFieldDescriptor(String name, int ttype) {
		setPrimaryKey(false);
		setNullable(true);
		setCname(name);
		setType(ttype);
	}
	
	public DBTableFieldDescriptor(String cname, int type,
			boolean isPrimaryKey, boolean isNullable, boolean isAutoIncrement,
			String classFieldName, boolean ignoreOnInsert) {
		super();
		this.cname = cname;
		this.type = type;
		this.isPrimaryKey = isPrimaryKey;
		this.isNullable = isNullable;
		this.isAutoIncrement = isAutoIncrement;
		this.classFieldName = classFieldName;
		this.ignoreOnInsert = ignoreOnInsert;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean isPrimaryKey() {
		return isPrimaryKey;
	}

	public void setPrimaryKey(boolean isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
	}

	public boolean isNullable() {
		return isNullable;
	}

	public void setNullable(boolean isNullable) {
		this.isNullable = isNullable;
	}

	public boolean isAutoIncrement() {
		return isAutoIncrement;
	}

	public void setAutoIncrement(boolean isAutoIncrement) {
		this.isAutoIncrement = isAutoIncrement;
	}
	
	public String getClassFieldName() {
		return classFieldName;
	}

	public void setClassFieldName(String classFieldName) {
		this.classFieldName = classFieldName;
	}

	public boolean IgnoreOnInsert() {
		return ignoreOnInsert;
	}

	public void setIgnoreOnInsert(boolean ignoreOnInsert) {
		this.ignoreOnInsert = ignoreOnInsert;
	}

	public String getTypeAstring() {
		String t = null;
		switch (type) {
		case 1:
			t = "NUMERIC";
			break;
		case 2:
			t = "REAL";
			break;
		case 3:
			t = "TEXT";
			break;
		case 4:
			t = "BLOB";
			break;
		case 5:
			t = "INTEGER";
			break;

		default:
			break;
		}
		return t;
	}

}
