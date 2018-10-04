/*
 * @author Remo Loaiza and the gang
 * @version 2013.06.24
 * @since 0.1
 * */

package com.gosharp.apis.db;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Properties;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBAPI {

	private static String DATABASE_NAME;
	private static int DATABASE_VERSION;
	private static String DATABASE_FILEPATH;
	private static String TABLES_FILEPATH;

	private static final DBAPI INSTANCE = new DBAPI();

	private static DBInstance db;

	private List<DBTableDescriptor> database;

	private DBAPI() {
	}

	public static DBAPI getInstance() {
		return INSTANCE;
	}

	public long insert(Object o, DBTableDescriptor td) {

		if (db != null) {
			return db.insert(o, td);
		}

		return -1;
	}

	public <T> List<T> selectAll(Object o, DBTableDescriptor td) {
		if (db != null) {
			return db.selectAll(o, td);
		}
		return null;
	}

	/**
	 * Returns a controller for handling basic DB operations. Has a Table
	 * Descriptor and object so all subsecuent calls are done only through
	 * objects(pojo).
	 * 
	 * @param o
	 *            The pojo archetype for the controller.
	 * @param tablename
	 *            The name of the table as String
	 * @return The The object controller
	 * @see DBObjectController
	 * 
	 * */

	public DBObjectController getController(Object o, String tableName) {
		try {
			return new DBObjectController(db, findTableDescriptor(tableName), o);
		} catch (Exception e) {
			Log.e("com.gosharp.dbapi", "DB Controller creation failed\n"+e);
			return null;
		}
	}

	/**
	 * Returns the Abstract Table Controller
	 * 
	 * @param o
	 *            The pojo archetype for the controller.
	 * @param tablename
	 *            The name of the table as String
	 * 
	 * @return The table controller
	 * @see DBAbstractTableController
	 * */

	public DBObjectController getAbstractController(Object o, String tableName) {
		try {
			return new DBAbsrtactTableController(db, tableName, o);
		} catch (Exception e) {
			Log.e("com.gosharp.dbapi", "DB Abstract Controller creation failed");
			return null;
		}
	}

	public void loadPropertiesFromFile(Resources r) {
		// Resources resources = this.getResources();
		AssetManager assetManager = r.getAssets();
		try {
			InputStream inputStream = assetManager.open("dbapi.properties");
			Properties properties = new Properties();
			properties.load(inputStream);
			System.out.println("The properties are now loaded");
			System.out.println("properties: " + properties);

			setDatabaseProperties(properties.getProperty("database_name"),
					Integer.parseInt(properties.getProperty("version")),
				"/data/data/com.alphasoluciones.antonio.api_controler/databases/API_CONTROLER.db",
				//properties.getProperty("filepath"),
					properties.getProperty("tabledescriptor"));

		} catch (IOException e) {
			System.err.println("Failed to open database properties");
			e.printStackTrace();
		}
	}

	private void setDatabaseProperties(String name, int version,
			String filepath, String tabledesc) {
		DATABASE_NAME = name;
		DATABASE_VERSION = version;
		DATABASE_FILEPATH = filepath;
		TABLES_FILEPATH = tabledesc;

		System.out.println("DATABASE NAME: " + DATABASE_NAME);
		System.out.println("DATABASE VERSION: " + DATABASE_VERSION);
		System.out.println("DATABASE FILEPATH: " + DATABASE_FILEPATH);
		System.out.println("TABLES FILEPATH: " + TABLES_FILEPATH);
	}

	public void setDatabaseProperties(String name, int version, String filepath) {
		DATABASE_NAME = name;
		DATABASE_VERSION = version;
		DATABASE_FILEPATH = filepath;

		System.out.println("DATABASE NAME: " + DATABASE_NAME);
		System.out.println("DATABASE VERSION: " + DATABASE_VERSION);
		System.out.println("DATABASE FILEPATH: " + DATABASE_FILEPATH);
		System.out.println("TABLES FILEPATH: " + TABLES_FILEPATH);
	}

	public boolean loadDatabaseFromXML(Resources r) {
		System.out.println("STARTING PARSER");

		try {
			AssetManager assetManager = r.getAssets();
			InputStream in = assetManager.open(TABLES_FILEPATH);
			DBTableParser p = new DBTableParser();
			p.parseDatabaseFromXML(in);

			database = p.getDatabase();
			p = null;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public void showCreateSQL() {
		for (DBTableDescriptor table : database) {
			System.out.println("CREATING TALBE " + table.getTable_name()
					+ " of " + database.size());
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

			System.out.println(sql);
		}
	}

	public boolean databaseInMemory() {
		return database == null;
	}

	public void emptyAllTables() {
		db.emptyAllTables();
	}

	public void emptyTable(String table) {
		db.emptyTable(table);
	}

	public Cursor queryDatabse(String sql) {
		return db.queryDatabase(sql);
	}

	public void execSQL(String sql) {
		db.execSQL(sql);
	}

	public void createDB(Context c) {
		db = new DBInstance(c, DATABASE_NAME, DATABASE_VERSION,
				DATABASE_FILEPATH, database);
	}

	public void createDB(Context c, String dbname, int version, String filepath) {
		db = new DBInstance(c, dbname, version, filepath);
	}

	public DBTableDescriptor findTableDescriptor(String name) {
		for (DBTableDescriptor table : database) {
			if (table.getTable_name().equalsIgnoreCase(name))
				return table;
		}
		return null;
	}

	// ///DANIEL ESTUVO AQUI

	/**
	 * Creates a table from a Class object
	 * 
	 * @return true if the table was created succesfully, false otherwise
	 */
	public boolean createTable(Class objectClass) {
		// if (!db.databaseExists()) {
		// return false;
		// }
		if (db.tableExists(objectClass.getSimpleName(), true)) {
			return true;
		}
		DBTableDescriptor td = new DBTableDescriptor();
		td.setTable_name(objectClass.getSimpleName());
		Field[] fields = objectClass.getDeclaredFields();
		for (Field field : fields) {
			td.addField(parseField(field));
		}
		return db.createTable(td);
	}

	/**
	 * Returns the table Descriptor of an Object
	 */
	public DBTableDescriptor getTableDescriptor(Object object) {
		Class objectClass = object.getClass();
		DBTableDescriptor td = new DBTableDescriptor();
		td.setTable_name(objectClass.getSimpleName());
		Field[] fields = objectClass.getDeclaredFields();
		for (Field field : fields) {
			td.addField(parseField(field));
		}
		return td;
	}

	/**
	 * Parses a java.lang.reflect.Field into a
	 * com.gosharp.apis.db.DBTableFieldDescriptor
	 * */
	public DBTableFieldDescriptor parseField(Field field) {
		DBTableFieldDescriptor tfd = new DBTableFieldDescriptor();
		Class c = field.getType();
		tfd.setClassFieldName(field.getName());
		if (c.equals(int.class) || c.equals(Integer.class)
				|| c.equals(long.class) || c.equals(Long.class)) {
			tfd.setType(DBTableFieldDescriptor.TYPE_INTEGER);
		} else

		if (c == boolean.class || c.equals(Boolean.class)) {
			tfd.setType(DBTableFieldDescriptor.TYPE_NUMERIC);
		} else

		if (c == String.class) {
			tfd.setType(DBTableFieldDescriptor.TYPE_TEXT);
		} else

		if (c == Float.class || c.equals(float.class) || c == Double.class
				|| c.equals(double.class)) {
			tfd.setType(DBTableFieldDescriptor.TYPE_REAL);
		} else {
			tfd.setType(DBTableFieldDescriptor.TYPE_TEXT);
		}
		if (field.getName().equalsIgnoreCase("id")) {
			tfd.setPrimaryKey(true);
			tfd.setCname("id");
			tfd.setAutoIncrement(false);
			tfd.setNullable(false);
		} else if (field.getName().equalsIgnoreCase("ida")) {
			tfd.setPrimaryKey(true);
			tfd.setCname("ida");
			tfd.setAutoIncrement(true);
			tfd.setIgnoreOnInsert(true);
			tfd.setNullable(false);
		} else {
			tfd.setCname(field.getName());
		}
		return tfd;
	}

	public <T> List<T> selectByQuery(Object o, String tableName, String sql) {
		DBTableDescriptor tableDescriptor = getAbstractController(o, tableName)
				.getTd();
		return db.selectByQuery(o, tableDescriptor, sql);
	}

	public <T> List<T> selectByQuery(Object object, String sql) {
		DBTableDescriptor tableDescriptor = getTableDescriptor(object);
		return db.selectByQuery(object, tableDescriptor, sql);
	}
	
	public <T> List<T> selectByQuery(Object object, String sql, String[] params) {
		DBTableDescriptor tableDescriptor = getTableDescriptor(object);
		return db.selectByQuery(object, tableDescriptor, sql, params);
	}

	public <T> Object selectById(int id, Object o) {
		DBTableDescriptor tableDescriptor = getTableDescriptor(o);
		return db.selectById(id, o, tableDescriptor);
	}

	public <T> Object selectById(long id, Object o) {
		DBTableDescriptor tableDescriptor = getTableDescriptor(o);
		return db.selectById(id, o, tableDescriptor);
	}

	public long insert(Object o, Object tableObject) {

		DBTableDescriptor td = getTableDescriptor(tableObject);
		if (db != null) {
			return db.insert(o, td);
		}

		return -1;
	}
	
	public SQLiteDatabase getWritableDatabase(){
		return db.getWritableDatabase();
	}
	
	public SQLiteDatabase getReadableDatabase(){
		return db.getReadableDatabase();
	}

	// ///
}
