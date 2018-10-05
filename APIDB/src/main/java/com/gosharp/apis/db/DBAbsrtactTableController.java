/*
 * @author Remo Loaiza and the gang
 * @version 2013.06.24
 * @since 0.1
 * */

package com.gosharp.apis.db;

import java.util.List;

import android.database.Cursor;

public class DBAbsrtactTableController extends DBObjectController{

	private String table_name;

	private DBInstance db;
	private DBTableDescriptor td;

	
	/*
	 * 
	 * Fields defined by querying PRAGMA TABLE_INFO
	 * By default it uses the following colum indexes:
	 * 0 cid, 1 name, 2 type, 3 notnull, 4 dflt_value, 5 pk
	 * 
	 * */
	
	private static final int COLUMN_NAME_INDEX = 1;
	private static final int COLUMN_TYPE_INDEX = 2;
	private static final int COLUMN_NULLABLE_INDEX = 3;
	private static final int COLUMN_PK_INDEX = 5;

	/*
	 * Creates the Table Controller
	 * 
	 * @param table_name The table name
	 * @param db The SQLite database to use
	 * @param archetype The POJO that will be used to create the table mapping to object.
	 * 
	 * @return The table controller
	 * */
	public DBAbsrtactTableController(final DBInstance db, final String table_name, 
			final Object archetype) throws Exception {
		super(db, new DBTableDescriptor(), archetype);
		this.table_name = table_name;
		this.db = db;
		this.td = new DBTableDescriptor();
		td.setTable_name(table_name);
		createTableDescriptor();
		super.setTd(td);
	}

	/*
	 * Used privately to create the APIs network descriptor from a pragma query
	 * 
	 * @return The table descriptor
	 * @throws Exception from database cursor
	 * 
	 * */
	
	public void createTableDescriptor() throws Exception{
		try {
			Cursor c = db.queryDatabase("PRAGMA TABLE_INFO ("+table_name+")");
			if(c.moveToFirst()) {
				do {
					DBTableFieldDescriptor tfd = new DBTableFieldDescriptor();
					int type;
					tfd.setCname(c.getString(COLUMN_NAME_INDEX));
					tfd.setClassFieldName(c.getString(COLUMN_NAME_INDEX));
					
					String t = c.getString(COLUMN_TYPE_INDEX);
					if (t.equalsIgnoreCase("INTEGER") || t.equalsIgnoreCase("NUMERIC")) type = DBTableFieldDescriptor.TYPE_INTEGER;
					else if (t.equalsIgnoreCase("TEXT")) type = DBTableFieldDescriptor.TYPE_TEXT;
					else if (t.equalsIgnoreCase("REAL")) type = DBTableFieldDescriptor.TYPE_REAL;
					else if (t.equalsIgnoreCase("BLOB")) type = DBTableFieldDescriptor.TYPE_BLOB;
					else type = DBTableFieldDescriptor.TYPE_TEXT;
					tfd.setType(type);
					
					//if(c.getString(3).equalsIgnoreCase("0")) tfd.setNullable(false);
					tfd.setNullable(c.getString(COLUMN_NULLABLE_INDEX).equalsIgnoreCase("0") ? false : true);
					tfd.setPrimaryKey(c.getString(COLUMN_PK_INDEX).equalsIgnoreCase("0") ? false : true);

					td.addField(tfd);
					
				} while (c.moveToNext());
			}
			c.close();
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
