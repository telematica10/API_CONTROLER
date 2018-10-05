package com.gosharp.apis.db;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils.InsertHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public final class DBInstance extends SQLiteOpenHelper {

	private static String DATABASE_FILEPATH;
	private List<DBTableDescriptor> database;
	private Context ctx;

	public SQLiteDatabase current_database;

	/**
	 * Creates the database Instance
	 * 
	 * @param context
	 *            This database context
	 * 
	 * @param name
	 *            The database name
	 * 
	 * @param version
	 *            The database version
	 * 
	 * @param filepath
	 *            The database filepath
	 * 
	 * @return The Database Helper instance
	 */
	public DBInstance(final Context context, final String name,
			final int version, final String filepath) {
		super(context, filepath, null, version);
		System.out.println("Intento crear base pero no pudo");
		DATABASE_FILEPATH = filepath;
	}

	/**
	 * Creates the database Instance
	 * 
	 * @param context
	 *            This database context
	 * 
	 * @param name
	 *            The database name
	 * 
	 * @param version
	 *            The database version
	 * 
	 * @param filepath
	 *            The database filepath
	 * 
	 * @param db
	 *            The list of table descriptors (Database)
	 * 
	 * @return The Database Helper instance
	 */
	public DBInstance(Context context, String name, int version,
			String filepath, List<DBTableDescriptor> db) {
		super(context, filepath, null, version);
		DATABASE_FILEPATH = filepath;
		database = db;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		System.out.println("ONCREATE DATABASE");
		if (database == null || database.size() < 1) {
			return;
		}
		for (DBTableDescriptor table : database) {
			System.out.println("CREATING TABLE " + table.getTable_name());
			String sql = "CREATE TABLE " + table.getTable_name() + " ( ";
			List<DBTableFieldDescriptor> fields = table.getFields();
			int totalFields = fields.size();
			int current = 0;
			for (DBTableFieldDescriptor tdf : fields) {
				sql += tdf.getCname() + " " + tdf.getTypeAstring() + " ";
				if (tdf.isPrimaryKey())
					sql += " PRIMARY KEY ";
				if (tdf.isAutoIncrement())
					sql += " AUTOINCREMENT ";
				if (!tdf.isNullable())
					sql += " NOT NULL ";
				if (current < (totalFields - 1))
					sql += ",";
				current++;
			}
			sql += ");";
			System.out.println("Executing SQL: " + sql);
			db.execSQL(sql);

		}
	}

	// ///////////Daniel WAS here
	/**
	 * Creates a table from a tableDescriptor
	 * 
	 * @return true if the table was created\n false otherwise
	 */
	public boolean createTable(DBTableDescriptor tableDescriptor) {
		System.out.println("CREATING TABLE " + tableDescriptor.getTable_name());
		String sql = "CREATE TABLE " + tableDescriptor.getTable_name() + " ( ";
		List<DBTableFieldDescriptor> fields = tableDescriptor.getFields();
		int totalFields = fields.size();
		int current = 0;
		for (DBTableFieldDescriptor tdf : fields) {
			sql += tdf.getCname() + " " + tdf.getTypeAstring() + " ";
			if (tdf.isPrimaryKey())
				sql += " PRIMARY KEY ";
			if (tdf.isAutoIncrement())
				sql += " AUTOINCREMENT ";
			if (!tdf.isNullable())
				sql += " NOT NULL ";
			if (current < (totalFields - 1))
				sql += ",";
			current++;
		}
		sql += ");";
		System.out.println("Executing SQL: " + sql);
		try {
			current_database.execSQL(sql);
		} catch (SQLiteException e) {
			return false;
		}
		return true;
	}

	// ///////////Daniel WAS here

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		for (DBTableDescriptor table : database) {
			db.execSQL("DROP TABLE IF EXISTS " + table.getTable_name());
		}
		for (DBTableDescriptor table : database) {
			System.out.println("CREATING TABLE " + table.getTable_name());
			String sql = "CREATE TABLE " + table.getTable_name() + " ( ";
			List<DBTableFieldDescriptor> fields = table.getFields();
			int totalFields = fields.size();
			int current = 0;
			for (DBTableFieldDescriptor tdf : fields) {
				sql += tdf.getCname() + " " + tdf.getTypeAstring() + " ";
				if (tdf.isPrimaryKey())
					sql += " PRIMARY KEY ";
				if (tdf.isAutoIncrement())
					sql += " AUTOINCREMENT ";
				if (!tdf.isNullable())
					sql += " NOT NULL ";
				if (current < (totalFields - 1))
					sql += ",";
				current++;
			}
			sql += ");";
			System.out.println("Executing SQL: " + sql);
			db.execSQL(sql);

		}

	}

	final public void createDatabase(SQLiteDatabase db) {
		for (DBTableDescriptor table : database) {
			System.out.println("CREATING TABLE " + table.getTable_name());
			String sql = "CREATE TABLE " + table.getTable_name() + " ( ";
			List<DBTableFieldDescriptor> fields = table.getFields();
			int totalFields = fields.size();
			int current = 0;
			for (DBTableFieldDescriptor tdf : fields) {
				sql += tdf.getCname() + " " + tdf.getTypeAstring() + " ";
				if (tdf.isPrimaryKey())
					sql += " PRIMARY KEY ";
				if (tdf.isAutoIncrement())
					sql += " AUTOINCREMENT ";
				if (!tdf.isNullable())
					sql += " NOT NULL ";
				if (current < (totalFields - 1))
					sql += ",";
				current++;
			}
			sql += ");";
			System.out.println("Executing SQL: " + sql);
			db.execSQL(sql);

		}
	}

	/**
	 * 
	 * This method will errase all data from all tables
	 */
	final public void emptyAllTables() {
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			for (DBTableDescriptor table : database) {
				db.execSQL("DELETE FROM " + table.getTable_name());
			}
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}

	/**
	 * 
	 * This method will erase all data from a table
	 * 
	 * @param table
	 *            The table name for the table that will be deleted
	 */
	final public void emptyTable(String table) {
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			db.execSQL("DELETE FROM " + table);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}

	/**
	 * Query the database using SQL and return a cursor
	 * 
	 * Note from Daniel: it does not work for delete or update statements. Use
	 * execSQL instead.
	 * 
	 * @see DBInstance#execSQL(String)
	 * @param sql
	 *            The SQL query to be executed
	 * 
	 * @return The database Cursor that holds thequery result
	 */
	final public Cursor queryDatabase(String sql) {
		try {
			SQLiteDatabase db = this.getReadableDatabase();
			return db.rawQuery(sql, null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * This methis? will check if the database exists
	 * 
	 * @return True if the database exists, False if it does not
	 */
	final protected boolean databaseExists() {
		SQLiteDatabase checkDB = null;
		try {
			checkDB = SQLiteDatabase.openDatabase(DATABASE_FILEPATH, null,
					SQLiteDatabase.OPEN_READONLY);
			checkDB.close();
		} catch (SQLiteException e) {
			e.printStackTrace();
			// database doesn't exist yet.
		}
		System.out.println("checkDB != null: " + (checkDB != null));
		return checkDB != null;
	}

	/**
	 * This method will check if a table exists
	 * 
	 * @param tableName
	 *            The name of the table
	 * 
	 * @param openDB
	 *            Whenever the API should try to open the database prior to the
	 *            check
	 * 
	 * @return True if the table exists, False if its not
	 */
	final public boolean tableExists(String tableName, boolean openDb) {
		if (openDb) {
			if (current_database == null || !current_database.isOpen()) {
				current_database = getReadableDatabase();
			}
			if (!current_database.isReadOnly()) {
				current_database.close();
				current_database = getReadableDatabase();
			}
		}

		Cursor cursor = current_database.rawQuery(
				"select DISTINCT tbl_name from sqlite_master where tbl_name = '"
						+ tableName + "'", null);
		if (cursor != null) {
			if (cursor.getCount() > 0) {
				cursor.close();
				return true;
			}
			cursor.close();
		}
		return false;
	}

	/**
	 * This method will check if a table exists
	 * 
	 * @param tableName
	 *            The name of the table
	 * 
	 * @param openDB
	 *            Whenever the API should try to open the database prior to the
	 *            check
	 * 
	 * @return True if the table exists, False if its not
	 */
	final public boolean tableExists(String tableName) {
		// if (openDb) {
		// if (current_database == null || !current_database.isOpen()) {
		// current_database = getReadableDatabase();
		// }
		//
		// if (!current_database.isReadOnly()) {
		// current_database.close();
		// current_database = getReadableDatabase();
		// }
		// }

		Cursor cursor = current_database.rawQuery(
				"select DISTINCT tbl_name from sqlite_master where tbl_name = '"
						+ tableName + "'", null);
		if (cursor != null) {
			if (cursor.getCount() > 0) {
				cursor.close();
				return true;
			}
			cursor.close();
		}
		return false;
	}

	/**
	 * This method will insert an Object into a SQLite table using the APIs
	 * DBTable Descriptor as a helper
	 * 
	 * @param o
	 *            The object to be inserted
	 * 
	 * @param td
	 *            The table descriptor
	 * 
	 * @return The long value of the insert operation, in most cases the
	 *         inserted row _ID
	 */
	final public long insert(Object o, DBTableDescriptor td) {
		ContentValues cv = new ContentValues();
		List<DBTableFieldDescriptor> tfields = td.getFields();
		DBTableFieldDescriptor idfield = null;
		Class<? extends Object> c = o.getClass();

		try {
			for (DBTableFieldDescriptor tdf : tfields) {
				if (!tdf.IgnoreOnInsert()) {
					int type = tdf.getType();
					Class fc = c.getDeclaredField(tdf.getClassFieldName())
							.getType();

					switch (type) {
					case 1:
						if (fc == Integer.class || fc.equals(int.class)) {
							cv.put(tdf.getCname(), (Integer) c
									.getDeclaredField(tdf.getClassFieldName())
									.get(o));
						} else if (fc == Long.class || fc.equals(long.class)) {
							cv.put(tdf.getCname(),
									(Long) c.getDeclaredField(
											tdf.getClassFieldName()).get(o));
						} else {
							throw new NullPointerException(
									"Declared field was of type int and no int or long field was found in the class");
						}
						break;
					case 2:
						if (fc == Double.class || fc.equals(double.class)) {
							cv.put(tdf.getCname(),
									(Double) c.getDeclaredField(
											tdf.getClassFieldName()).get(o));
						} else if (fc == Float.class || fc.equals(float.class)) {
							cv.put(tdf.getCname(),
									(Float) c.getDeclaredField(
											tdf.getClassFieldName()).get(o));
						} else {

							throw new NullPointerException(
									"Declared field was of type real and no float or double field was found in the class");
						}

						break;
					case 3:
						cv.put(tdf.getCname(),
								(String) c.getDeclaredField(
										tdf.getClassFieldName()).get(o));
						break;
					case 4:
						cv.put(tdf.getCname(),
								(String) c.getDeclaredField(
										tdf.getClassFieldName()).get(o));
						break;
					case 5:
						// Perdón por esto pero sería más tardado de otra forma
						// System.out.println("tdf.getCname(): " +
						// tdf.getCname());
						// if(!tdf.IgnoreOnInsert())
						// cv.putNull(tdf.getCname());
						// else {
						try {
							cv.put(tdf.getCname(), (Integer) c
									.getDeclaredField(tdf.getClassFieldName())
									.get(o));
						} catch (ClassCastException e) {
							cv.put(tdf.getCname(),
									(Long) c.getDeclaredField(
											tdf.getClassFieldName()).get(o));
						}
						// }
						break;
					default:
						break;
					}
				}
				if (tdf.isPrimaryKey() && tdf.IgnoreOnInsert())
					idfield = tdf;
			}

			SQLiteDatabase db = this.getWritableDatabase();
			long result;
			if (idfield != null)
				result = db.insert(td.getTable_name(), idfield.getCname(), cv);
			else
				result = db.insert(td.getTable_name(), null, cv);

			db.close();
			return result;

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	/*
	 * This method will select all objects in a table and return them in a List
	 * using the object as an archetype
	 * 
	 * @param o The archetype object to be used to list the database contents
	 * 
	 * @param td The table descriptor to be used for selection
	 * 
	 * @return The <T> List of objects from the table
	 */
	final public <T> List<T> selectAll(Object o, DBTableDescriptor td) {
		List<T> resultList = (List<T>) new ArrayList<Object>();
		Constructor<?> con = o.getClass().getConstructors()[0];
		List<DBTableFieldDescriptor> tfields = td.getFields();
		try {
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor c = db.rawQuery("SELECT * FROM " + td.getTable_name(), null);
			if (c.moveToFirst()) {
				do {
					Object newObject = (T) con.newInstance(null);
					for (DBTableFieldDescriptor dbTableFieldDescriptor : tfields) {
						DBTableFieldDescriptor testField = dbTableFieldDescriptor;
						int type = testField.getType();

						switch (type) {
						case 1:
							Field f = o.getClass().getDeclaredField(
									testField.getClassFieldName());
							f.set(newObject, c.getLong(c
									.getColumnIndex(testField.getCname())));// valor
																			// de
																			// la
																			// base
																			// por
																			// columna
							break;
						case 2:
							Field f2 = o.getClass().getDeclaredField(
									testField.getClassFieldName());
							f2.set(newObject, c.getFloat(c
									.getColumnIndex(testField.getCname())));// valor
																			// de
																			// la
																			// base
																			// por
																			// columna
							break;
						case 3:
							Field f3 = o.getClass().getDeclaredField(
									testField.getClassFieldName());
							f3.set(newObject, c.getString(c
									.getColumnIndex(testField.getCname())));// valor
																			// de
																			// la
																			// base
																			// por
																			// columna
							break;
						case 4:
							Field f4 = o.getClass().getDeclaredField(
									testField.getClassFieldName());
							f4.set(newObject, c.getString(c
									.getColumnIndex(testField.getCname())));// valor
																			// de
																			// la
																			// base
																			// por
																			// columna
							break;
						case 5:
							Field f5 = o.getClass().getDeclaredField(
									testField.getClassFieldName());
							f5.set(newObject, c.getInt(c
									.getColumnIndex(testField.getCname())));// valor
																			// de
																			// la
																			// base
																			// por
																			// columna
							break;
						default:
							break;
						}

					}
					resultList.add((T) newObject);
				} while (c.moveToNext());
			}
			db.close();
			return resultList;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * This method selects one row from a table and returns it as an object It
	 * requires that the table has a primary key field
	 * 
	 * @param id The id to select from the database
	 * 
	 * @param td The table descriptor
	 * 
	 * @return A <T>Object containing the selected row from the database or null
	 * if empty
	 */
	public <T> Object selectById(int id, Object o, DBTableDescriptor td) {
		try {
			Constructor<?> con = o.getClass().getConstructors()[0];
			Object newObject = con.newInstance(null);
			List<DBTableFieldDescriptor> tfields = td.getFields();

			String pkfield = "_ID";
			for (DBTableFieldDescriptor dbTableFieldDescriptor : tfields) {
				if (dbTableFieldDescriptor.isPrimaryKey())
					pkfield = dbTableFieldDescriptor.getCname();
			}

			SQLiteDatabase db = this.getReadableDatabase();
			Cursor c = db.rawQuery("SELECT * FROM " + td.getTable_name()
					+ " WHERE " + pkfield + "=" + id, null);

			if (c.moveToFirst()) {
				for (DBTableFieldDescriptor dbTableFieldDescriptor : tfields) {
					DBTableFieldDescriptor testField = dbTableFieldDescriptor;
					int type = testField.getType();

					switch (type) {
					case 1:
						Field f = o.getClass().getDeclaredField(
								testField.getClassFieldName());
						f.set(newObject, c.getLong(c.getColumnIndex(testField
								.getCname())));// valor de la base por columna
						break;
					case 2:
						Field f2 = o.getClass().getDeclaredField(
								testField.getClassFieldName());
						f2.set(newObject, c.getFloat(c.getColumnIndex(testField
								.getCname())));// valor de la base por columna
						break;
					case 3:
						Field f3 = o.getClass().getDeclaredField(
								testField.getClassFieldName());
						f3.set(newObject, c.getString(c
								.getColumnIndex(testField.getCname())));// valor
																		// de la
																		// base
																		// por
																		// columna
						break;
					case 4:
						Field f4 = o.getClass().getDeclaredField(
								testField.getClassFieldName());
						f4.set(newObject, c.getString(c
								.getColumnIndex(testField.getCname())));// valor
																		// de la
																		// base
																		// por
																		// columna
						break;
					case 5:
						Field f5 = o.getClass().getDeclaredField(
								testField.getClassFieldName());
						f5.set(newObject, c.getInt(c.getColumnIndex(testField
								.getCname())));// valor de la base por columna
						break;
					default:
						break;
					}

				}
				db.close();
				return (T) newObject;
			} else {
				db.close();
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public <T> Object selectById(long id, Object o, DBTableDescriptor td) {
		try {
			Constructor<?> con = o.getClass().getConstructors()[0];
			Object newObject = con.newInstance(null);
			List<DBTableFieldDescriptor> tfields = td.getFields();

			String pkfield = "_ID";
			for (DBTableFieldDescriptor dbTableFieldDescriptor : tfields) {
				if (dbTableFieldDescriptor.isPrimaryKey())
					pkfield = dbTableFieldDescriptor.getCname();
			}

			SQLiteDatabase db = this.getReadableDatabase();
			Cursor c = db.rawQuery("SELECT * FROM " + td.getTable_name()
					+ " WHERE " + pkfield + "=" + id, null);

			if (c.moveToFirst()) {
				for (DBTableFieldDescriptor dbTableFieldDescriptor : tfields) {
					DBTableFieldDescriptor testField = dbTableFieldDescriptor;
					int type = testField.getType();

					switch (type) {
					case 1:
						Field f = o.getClass().getDeclaredField(
								testField.getClassFieldName());
						f.set(newObject, c.getLong(c.getColumnIndex(testField
								.getCname())));// valor de la base por columna
						break;
					case 2:
						Field f2 = o.getClass().getDeclaredField(
								testField.getClassFieldName());
						f2.set(newObject, c.getFloat(c.getColumnIndex(testField
								.getCname())));// valor de la base por columna
						break;
					case 3:
						Field f3 = o.getClass().getDeclaredField(
								testField.getClassFieldName());
						f3.set(newObject, c.getString(c
								.getColumnIndex(testField.getCname())));// valor
																		// de la
																		// base
																		// por
																		// columna
						break;
					case 4:
						Field f4 = o.getClass().getDeclaredField(
								testField.getClassFieldName());
						f4.set(newObject, c.getString(c
								.getColumnIndex(testField.getCname())));// valor
																		// de la
																		// base
																		// por
																		// columna
						break;
					case 5:
						Field f5 = o.getClass().getDeclaredField(
								testField.getClassFieldName());
						f5.set(newObject, c.getInt(c.getColumnIndex(testField
								.getCname())));// valor de la base por columna
						break;
					default:
						break;
					}

				}
				db.close();
				return (T) newObject;
			} else {
				db.close();
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public <T> List<T> selectByQuery(Object o, DBTableDescriptor td, String sql) {
		List<T> resultList = (List<T>) new ArrayList<Object>();
		try {
			Constructor<?> con = o.getClass().getConstructors()[0];

			List<DBTableFieldDescriptor> tfields = td.getFields();

			SQLiteDatabase db = this.getReadableDatabase();
			Cursor c = db.rawQuery(sql, null);

			if (c.moveToFirst()) {
				do {
					Object newObject = con.newInstance(null);
					for (DBTableFieldDescriptor dbTableFieldDescriptor : tfields) {

						DBTableFieldDescriptor testField = dbTableFieldDescriptor;
						int type = testField.getType();
						switch (type) {
						case DBTableFieldDescriptor.TYPE_NUMERIC:
							Field f = o.getClass().getDeclaredField(
									testField.getClassFieldName());
							if (f.getType().equals(boolean.class)) {
								f.set(newObject,
										c.getLong(c.getColumnIndex(testField
												.getCname())) != 0);// si fiera
																	// 0 es
																	// false.
																	// DANIEl
																	// WAZ JIR
							} else {
								f.set(newObject, c.getLong(c
										.getColumnIndex(testField.getCname())));// valor
																				// de
																				// la
																				// base
																				// por
																				// columna
							}
							break;
						case DBTableFieldDescriptor.TYPE_REAL:
							Field f2 = o.getClass().getDeclaredField(
									testField.getClassFieldName());
							if (f2.getType() == Float.class
									|| f2.getType().equals(float.class)) {
								f2.set(newObject, c.getFloat(c
										.getColumnIndex(testField.getCname())));
							} else if (f2.getType() == Double.class
									|| f2.getType().equals(double.class)) {
								f2.set(newObject, c.getDouble(c
										.getColumnIndex(testField.getCname())));
							} else {
								throw new NullPointerException(
										"Declared field was of type REAL and no float or double field was found in the class");
							}
							break;
						case DBTableFieldDescriptor.TYPE_TEXT:
							Field f3 = o.getClass().getDeclaredField(
									testField.getClassFieldName());
							f3.set(newObject, c.getString(c
									.getColumnIndex(testField.getCname())));// valor
																			// de
																			// la
																			// base
																			// por
																			// columnaz
							break;
						case DBTableFieldDescriptor.TYPE_BLOB:
							Field f4 = o.getClass().getDeclaredField(
									testField.getClassFieldName());
							f4.set(newObject, c.getString(c
									.getColumnIndex(testField.getCname())));// valor
																			// de
																			// la
																			// base
																			// por
																			// columna
							break;
						case DBTableFieldDescriptor.TYPE_INTEGER:
							Field f5 = o.getClass().getDeclaredField(
									testField.getClassFieldName());
							if (f5.getType() == Integer.class
									|| f5.getType().equals(int.class)) {
								f5.set(newObject, c.getInt(c
										.getColumnIndex(testField.getCname())));
							} else if (f5.getType() == Long.class
									|| f5.getType().equals(long.class)) {
								f5.set(newObject, c.getLong(c
										.getColumnIndex(testField.getCname())));
							} else {
								throw new NullPointerException(
										"Declared field was of type int and no int or long field was found in the class");
							}
							break;
						default:
							break;
						}

					}
					db.close();
					resultList.add((T) newObject);
				} while (c.moveToNext());
				return resultList;
			} else {
				db.close();
				return resultList;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean delete(Object o, DBTableDescriptor td) {
		try {
			int total = 0;
			SQLiteDatabase db = this.getWritableDatabase();
			List<DBTableFieldDescriptor> tfields = td.getFields();
			String id_val;
			Class<? extends Object> c = o.getClass();
			String pkfield;
			String where = "";
			List<String> wargs = new ArrayList<String>();
			for (DBTableFieldDescriptor dbTableFieldDescriptor : tfields) {
				if (dbTableFieldDescriptor.isPrimaryKey()) {
					pkfield = dbTableFieldDescriptor.getCname();
					if (dbTableFieldDescriptor.getType() == 5
							|| dbTableFieldDescriptor.getType() == 1)
						id_val = (Integer) c.getDeclaredField(
								dbTableFieldDescriptor.getClassFieldName())
								.get(o)
								+ "";
					else
						id_val = (String) c.getDeclaredField(
								dbTableFieldDescriptor.getClassFieldName())
								.get(o);
					total = db.delete(td.getTable_name(), pkfield + "=?",
							new String[] { id_val });
					db.close();
					return total > 0;
				}
				if (wargs.size() == 0)
					where += dbTableFieldDescriptor.getCname() + "=?";
				else
					where += " AND " + dbTableFieldDescriptor.getCname() + "=?";
				wargs.add(c.getDeclaredField(
						dbTableFieldDescriptor.getClassFieldName()).get(o)
						+ "");
			}
			String[] args = new String[wargs.size()];
			int pos = 0;
			for (String string : wargs) {
				args[pos] = string;
				pos++;
			}
			total = db.delete(td.getTable_name(), where, args);
			db.close();
			return total > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public int deleteById(int id, DBTableDescriptor td) {
		SQLiteDatabase db = this.getWritableDatabase();
		List<DBTableFieldDescriptor> tfields = td.getFields();
		// String where = "";
		// List<String> wargs = new ArrayList<String>(); TODO implementar delete
		// por cada atributo
		for (DBTableFieldDescriptor dbTableFieldDescriptor : tfields) {
			if (dbTableFieldDescriptor.isPrimaryKey()) {
				int total = db.delete(td.getTable_name(),
						dbTableFieldDescriptor.getCname() + "=?",
						new String[] { "" + id });
				db.close();
				return total;
			}
		}
		return 0;
	}

	// TODO reescribir esta operación, el código está muy feo
	public boolean update(Object o, DBTableDescriptor td) {
		ContentValues cv = new ContentValues();
		List<DBTableFieldDescriptor> tfields = td.getFields();
		DBTableFieldDescriptor idfield = null;
		Class<? extends Object> c = o.getClass();
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			String whereClause = "";
			List<String> whereArgs = new ArrayList<String>();
			String pkfield = "";
			String id_val = "";
			for (DBTableFieldDescriptor tdf : tfields) {
				if (tdf.isPrimaryKey()) {
					pkfield = tdf.getCname();
					if (tdf.getType() == 5 || tdf.getType() == 1) {
						id_val = (Integer) c.getDeclaredField(
								tdf.getClassFieldName()).get(o)
								+ "";
						whereArgs.add((Integer) c.getDeclaredField(
								tdf.getClassFieldName()).get(o)
								+ "");
					} else {
						id_val = (String) c.getDeclaredField(
								tdf.getClassFieldName()).get(o);
						whereArgs.add((String) c.getDeclaredField(
								tdf.getClassFieldName()).get(o));
					}
					if (!tdf.IgnoreOnInsert()) {
						cv.put(tdf.getCname(),
								c.getDeclaredField(tdf.getClassFieldName())
										.get(o) + "");
					}
					if (whereArgs.size() == 0)
						whereClause += tdf.getCname() + "=?";
					else
						whereClause += " AND " + tdf.getCname() + "=?";

				} else {
					Class fc = c.getDeclaredField(tdf.getClassFieldName())
							.getType();
					if (!tdf.IgnoreOnInsert()) {

						if (tdf.getType() == 5 || tdf.getType() == 1) {
							if (fc == Integer.class || fc.equals(int.class)) {
								cv.put(tdf.getCname(),
										(Integer) c.getDeclaredField(
												tdf.getClassFieldName()).get(o));
							} else if (fc == Long.class
									|| fc.equals(long.class)) {
								cv.put(tdf.getCname(),
										(Long) c.getDeclaredField(
												tdf.getClassFieldName()).get(o));
							} else {
								throw new NullPointerException(
										"Declared field was of type int and no int or long field was found in the class");
							}
						}
						if (tdf.getType() == 2) {
							if (fc == Float.class || fc.equals(float.class)) {
								cv.put(tdf.getCname(),
										(Float) c.getDeclaredField(
												tdf.getClassFieldName()).get(o));
							} else if (fc == Double.class
									|| fc.equals(double.class)) {
								cv.put(tdf.getCname(),
										(Double) c.getDeclaredField(
												tdf.getClassFieldName()).get(o));
							} else {
								throw new NullPointerException(
										"Declared field was of type real and no float or double field was found in the class");
							}

						}

						if (tdf.getType() == 3)
							cv.put(tdf.getCname(),
									(String) c.getDeclaredField(
											tdf.getClassFieldName()).get(o));
					}

					// if(tdf.getType() == 5 || tdf.getType() == 1) {
					// System.out.println("1 o 5");
					// whereArgs.add((Integer)c.getDeclaredField(tdf.getClassFieldName()).get(o)+"");
					// }
					// else {
					// whereArgs.add((String)c.getDeclaredField(tdf.getClassFieldName()).get(o));
					if (tdf.getType() == 5 || tdf.getType() == 1) {
						if (fc == Integer.class || fc.equals(int.class)) {
							whereArgs.add((Integer) c.getDeclaredField(
									tdf.getClassFieldName()).get(o)
									+ "");
						} else if (fc == Long.class || fc.equals(long.class)) {
							whereArgs.add((Long) c.getDeclaredField(
									tdf.getClassFieldName()).get(o)
									+ "");
						} else {
							throw new NullPointerException(
									"Declared field was of type int and no int or long field was found in the class");
						}
					}
					if (tdf.getType() == 2) {
						if (fc == Float.class || fc.equals(float.class)) {
							whereArgs.add((Float) c.getDeclaredField(
									tdf.getClassFieldName()).get(o)
									+ "");
						} else if (fc == Double.class
								|| fc.equals(double.class)) {
							whereArgs.add((Double) c.getDeclaredField(
									tdf.getClassFieldName()).get(o)
									+ "");
						} else {
							throw new NullPointerException(
									"Declared field was of type real and no float or double field was found in the class");
						}

					}

					if (tdf.getType() == 3)
						cv.put(tdf.getCname(),
								(String) c.getDeclaredField(
										tdf.getClassFieldName()).get(o));
					// }
					if (whereArgs.size() == 0) {
						whereClause += tdf.getCname() + "=? ";
					} else {
						whereClause += " AND " + tdf.getCname() + "=? ";
					}
				}

			}

			String[] args = new String[whereArgs.size()];
			int pos = 0;
			for (String string : whereArgs) {
				args[pos] = string;
				pos++;
			}

			if (!pkfield.equals("")) {
				int total = db.update(td.getTable_name(), cv, pkfield + "=?",
						new String[] { id_val });
				db.close();
				return total > 0;
			}

			long result = db.update(td.getTable_name(), cv, whereClause, args);
			db.close();
			return result > 0;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			db.close();
		}
	}

	// TODO reescribir esta operación, el código está muy feo
	public long persist(Object o, DBTableDescriptor td) {
		List<DBTableFieldDescriptor> tfields = td.getFields();
		Class<? extends Object> c = o.getClass();
		int id = 0;
		try {
			for (DBTableFieldDescriptor tdf : tfields) {
				if (tdf.isPrimaryKey()) {
					if (tdf.getType() == 5 || tdf.getType() == 1) {
						try {
							id = (Integer) c.getDeclaredField(
									tdf.getClassFieldName()).get(o);
						} catch (NullPointerException e) {
							return insert(o, td);
						}

						if (selectById(id, o, td) == null) {
							return insert(o, td);
						} else {
							update(o, td);
							return (long) id;
						}
					}
				}
			}
			return (long) id;

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public <T> void batchInsert(List<T> list, DBTableDescriptor td) {
		long current = System.currentTimeMillis();
		SQLiteDatabase db = this.getWritableDatabase();
		db.setLockingEnabled(false);
		InsertHelper ihelper = new InsertHelper(db, td.getTable_name());

		List<DBTableFieldDescriptor> tfields = td.getFields();

		try {
			Class<? extends Object> c = list.get(0).getClass();
			for (T o : list) {

				ihelper.prepareForInsert();
				// checar performance vs arreglo de DBTableField
				for (DBTableFieldDescriptor tdf : tfields) {
					if (tdf.getType() == 5 || tdf.getType() == 1) {
						ihelper.bind(
								ihelper.getColumnIndex(tdf.getCname()),
								(Integer) c.getDeclaredField(
										tdf.getClassFieldName()).get(o));
					} else {
						ihelper.bind(
								ihelper.getColumnIndex(tdf.getCname()),
								(String) c.getDeclaredField(
										tdf.getClassFieldName()).get(o));
					}

				}
				ihelper.execute();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("TOTAL TIME BATCH1 "
					+ (System.currentTimeMillis() - current));
			if (ihelper != null)
				ihelper.close();
			db.setLockingEnabled(true);
			db.close();
		}
	}

	public <T> void batchInsert2(List<T> list, DBTableDescriptor td) {
		if(list.size()<1){
			return;
		}
		long current = System.currentTimeMillis();
		SQLiteDatabase db = this.getWritableDatabase();

		List<DBTableFieldDescriptor> tfields = td.getFields();

		String[] fields = new String[tfields.size()];

		for (int i = 0; i < fields.length; i++) {
			fields[i] = tfields.get(i).getCname();
		}

		String insertQuery = createInsert(td.getTable_name(), fields);
		System.out.println("INSERT QUERY: " + insertQuery);

		final SQLiteStatement stm = db.compileStatement(insertQuery);

		// db.setLockingEnabled(false);
		db.beginTransaction();

		try {
			Class<? extends Object> c = list.get(0).getClass();
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				T o = (T) iterator.next();
				stm.clearBindings();
				for (int i = 0; i < fields.length; i++) {
					DBTableFieldDescriptor tfd = tfields.get(i);

					if (c.getDeclaredField(tfd.getClassFieldName()).get(o) == null)

						stm.bindNull(i + 1);

					else if (tfd.getType() == 5 || tfd.getType() == 1) {
						
						
						Class clas=c.getDeclaredField(
								tfd.getClassFieldName()).get(o).getClass();
						
						if(clas==int.class){
							stm.bindLong(
									(i + 1),
									Integer.valueOf((Integer) c.getDeclaredField(
											tfd.getClassFieldName()).get(o)));
						}
						else if(clas==Integer.class){
							stm.bindLong(
									(i + 1),
									Integer.valueOf((Integer) c.getDeclaredField(
											tfd.getClassFieldName()).get(o)));
						}
						else if(clas==long.class){
							stm.bindLong(
									(i + 1),
									Long.valueOf((Long) c.getDeclaredField(
											tfd.getClassFieldName()).get(o)));
						}
						else if(clas==Long.class){
							stm.bindLong(
									(i + 1),
									Long.valueOf((Long) c.getDeclaredField(
											tfd.getClassFieldName()).get(o)));
						}
						
					} else if (tfd.getType() == 2) {
						Class fc = c.getDeclaredField(tfd.getClassFieldName())
								.getType();

						if (fc == Float.class || fc.equals(float.class)) {
							stm.bindString(
									(i + 1),
									(Float) c.getDeclaredField(
											tfd.getClassFieldName()).get(o)
											+ "");
						} else if (fc == Double.class
								|| fc.equals(double.class)) {
							stm.bindString((i + 1), (Double) c
									.getDeclaredField(tfd.getClassFieldName())
									.get(o)
									+ "");
						} else {
							throw new NullPointerException(
									"Declared field was of type real and no float or double field was found in the class");
						}
					} else {
						stm.bindString(
								(i + 1),
								(String) c.getDeclaredField(
										tfd.getClassFieldName()).get(o));
					}

				}
				stm.execute();

			}

			db.setTransactionSuccessful();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
			System.out.println("TOTAL TIME BATCH2 "
					+ (System.currentTimeMillis() - current));
			// db.setLockingEnabled(true);
			db.close();
		}
	}

	static public String createInsert(final String tableName,
			final String[] columnNames) {
		if (tableName == null || columnNames == null || columnNames.length == 0) {
			throw new IllegalArgumentException();
		}
		final StringBuilder s = new StringBuilder();
		s.append("INSERT INTO ").append(tableName).append(" (");
		for (String column : columnNames) {
			s.append(column).append(" ,");
		}
		int length = s.length();
		s.delete(length - 2, length);
		s.append(") VALUES( ");
		for (int i = 0; i < columnNames.length; i++) {
			s.append(" ? ,");
		}
		length = s.length();
		s.delete(length - 2, length);
		s.append(")");
		return s.toString();
	}

	// ////////////////DANIEL ESTUVO AQUI

	final public void execSQL(String sql) {
		SQLiteDatabase db = null;
		try {
			db = this.getReadableDatabase();
			db.execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// finally {
		// if (db != null)
		// db.close();
		// }
	}

	public <T> List<T> selectByQueryD(Object o, DBTableDescriptor td, String sql) {
		List<T> resultList = (List<T>) new ArrayList<Object>();
		try {
			Constructor<?> con = o.getClass().getConstructors()[0];

			List<DBTableFieldDescriptor> tfields = td.getFields();

			SQLiteDatabase db = this.getReadableDatabase();
			Cursor c = db.rawQuery(sql, null);

			if (c.moveToFirst()) {
				do {
					Object newObject = con.newInstance(null);
					for (DBTableFieldDescriptor dbTableFieldDescriptor : tfields) {

						DBTableFieldDescriptor testField = dbTableFieldDescriptor;
						int type = testField.getType();

						switch (type) {
						case 1:
							Field f = o.getClass().getDeclaredField(
									testField.getClassFieldName());
							f.set(newObject, c.getLong(c
									.getColumnIndex(testField.getCname())));// valor
																			// de
																			// la
																			// base
																			// por
																			// columna
							break;
						case 2:
							Field f2 = o.getClass().getDeclaredField(
									testField.getClassFieldName());
							f2.set(newObject, c.getFloat(c
									.getColumnIndex(testField.getCname())));// valor
																			// de
																			// la
																			// base
																			// por
																			// columna
							break;
						case 3:
							Field f3 = o.getClass().getDeclaredField(
									testField.getClassFieldName());
							f3.set(newObject, c.getString(c
									.getColumnIndex(testField.getCname())));// valor
																			// de
																			// la
																			// base
																			// por
																			// columna
							break;
						case 4:
							Field f4 = o.getClass().getDeclaredField(
									testField.getClassFieldName());
							f4.set(newObject, c.getString(c
									.getColumnIndex(testField.getCname())));// valor
																			// de
																			// la
																			// base
																			// por
																			// columna
							break;
						case 5:
							Field f5 = o.getClass().getDeclaredField(
									testField.getClassFieldName());
							f5.set(newObject, c.getInt(c
									.getColumnIndex(testField.getCname())));// valor
																			// de
																			// la
																			// base
																			// por
																			// columna
							break;
						default:
							break;
						}

					}
					db.close();
					resultList.add((T) newObject);
				} while (c.moveToNext());
				return resultList;
			} else {
				db.close();
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public <T> List<T> selectByQuery(Object o, DBTableDescriptor td,
			String sql, String[] params) {

		List<T> resultList = (List<T>) new ArrayList<Object>();
		try {
			Constructor<?> con = o.getClass().getConstructors()[0];

			List<DBTableFieldDescriptor> tfields = td.getFields();

			SQLiteDatabase db = this.getReadableDatabase();
			Cursor c = db.rawQuery(sql, params);

			if (c.moveToFirst()) {
				do {
					Object newObject = con.newInstance(null);
					for (DBTableFieldDescriptor dbTableFieldDescriptor : tfields) {

						DBTableFieldDescriptor testField = dbTableFieldDescriptor;
						int type = testField.getType();
						switch (type) {
						case DBTableFieldDescriptor.TYPE_NUMERIC:
							Field f = o.getClass().getDeclaredField(
									testField.getClassFieldName());
							if (f.getType().equals(boolean.class)) {
								f.set(newObject,
										c.getLong(c.getColumnIndex(testField
												.getCname())) != 0);// si fiera
																	// 0 es
																	// false.
																	// DANIEl
																	// WAZ JIR
							} else {
								f.set(newObject, c.getLong(c
										.getColumnIndex(testField.getCname())));// valor
																				// de
																				// la
																				// base
																				// por
																				// columna
							}
							break;
						case DBTableFieldDescriptor.TYPE_REAL:
							Field f2 = o.getClass().getDeclaredField(
									testField.getClassFieldName());
							f2.set(newObject, c.getFloat(c
									.getColumnIndex(testField.getCname())));// valor
																			// de
																			// la
																			// base
																			// por
																			// columna
							break;
						case DBTableFieldDescriptor.TYPE_TEXT:
							Field f3 = o.getClass().getDeclaredField(
									testField.getClassFieldName());
							f3.set(newObject, c.getString(c
									.getColumnIndex(testField.getCname())));// valor
																			// de
																			// la
																			// base
																			// por
																			// columna
							break;
						case DBTableFieldDescriptor.TYPE_BLOB:
							Field f4 = o.getClass().getDeclaredField(
									testField.getClassFieldName());
							f4.set(newObject, c.getString(c
									.getColumnIndex(testField.getCname())));// valor
																			// de
																			// la
																			// base
																			// por
																			// columna
							break;
						case DBTableFieldDescriptor.TYPE_INTEGER:
							Field f5 = o.getClass().getDeclaredField(
									testField.getClassFieldName());
							f5.set(newObject, c.getInt(c
									.getColumnIndex(testField.getCname())));// valor
																			// de
																			// la
																			// base
																			// por
																			// columna
							break;
						default:
							break;
						}

					}
					db.close();
					resultList.add((T) newObject);
				} while (c.moveToNext());
				return resultList;
			} else {
				db.close();
				return resultList;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
