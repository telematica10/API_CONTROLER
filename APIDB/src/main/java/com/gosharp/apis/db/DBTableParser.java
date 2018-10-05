package com.gosharp.apis.db;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class DBTableParser {

	private List<DBTableDescriptor> database;
	
	public DBTableParser() {
	}

	public void parseDatabaseFromXML(InputStream in) throws IOException {
		System.out.println("PARSING FILE FROM INPUT STREAM");
		try {
			XmlPullParser parser = Xml.newPullParser();
	        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
	        parser.setInput(in, null);
	        parser.nextTag();
	        

	        
	        database = new ArrayList<DBTableDescriptor>();
	        
	        
	        while (parser.next() != XmlPullParser.END_DOCUMENT) {
	            if (parser.getEventType() != XmlPullParser.START_TAG) {
	                continue;
	            }
	            String name = parser.getName();
	            if (name.equals("table")) {
	            	System.out.println("Tabla");
	        		boolean hasId;
	        		String table_name;
	        		long version;
	        		List<DBTableFieldDescriptor> fields = new ArrayList<DBTableFieldDescriptor>();
	        		String idString = parser.getAttributeValue(null, "hasId");
	        		if (idString != null && idString.length() > 0) {
	        			hasId = Boolean.parseBoolean(idString);
	        		} else {
	        			hasId = false;
	        		}

	        		table_name = parser.getAttributeValue(null, "name");
	        		String ver = parser.getAttributeValue(null, "version");

	        		if (ver != null && ver.length() > 0) {
	        			version = Long.parseLong(ver);
	        		} else {
	        			throw new IllegalArgumentException("Table version can not be null");
	        		}
	        		DBTableDescriptor current = new DBTableDescriptor();
	        		current.setFields(fields);
	        		current.setHasId(hasId);
	        		current.setTable_name(table_name);
	        		current.setVersion(version);
	        		database.add(current);
	            } else if(name.equals("field")) {
	            	System.out.println("Campo");
	            	String cname;
	        		int type;
	        		boolean isPrimaryKey = false;
	        		boolean isNullable = true;
	        		boolean isAutoIncrement = false;
	        		String classFieldName;
	        		boolean ignoreOnInsert = false;
	        		
	        		cname = parser.getAttributeValue(null, "columnName");
	        		type = getTypeAsInt(parser.getAttributeValue(null, "type"));
	        		classFieldName = parser.getAttributeValue(null, "classFieldName");
	        		
	        		String pk = parser.getAttributeValue(null, "isPrimaryKey");
	        		if (pk != null && pk.length() > 0) {
	        			isPrimaryKey = Boolean.parseBoolean(pk);
	        		}
	        		String nt = parser.getAttributeValue(null, "isNullable");
	        		if (nt != null && nt.length() > 0) {
	        			isNullable = Boolean.parseBoolean(nt);
	        		}
	        		String ai = parser.getAttributeValue(null, "isAutoIncrement");
	        		if (ai != null && ai.length() > 0) {
	        			isAutoIncrement = Boolean.parseBoolean(ai);
	        		}
	        		String ii = parser.getAttributeValue(null, "ignoreOnInsert");
	        		if (ii != null && ii.length() > 0) {
	        			ignoreOnInsert = Boolean.parseBoolean(ii);
	        		}
	        		database.get(database.size()-1).getFields().add(new DBTableFieldDescriptor(cname, type, isPrimaryKey, isNullable, isAutoIncrement, classFieldName, ignoreOnInsert));
	        		//current.getFields().add(new DBTableFieldDescriptor(cname, type, isPrimaryKey, isNullable, isAutoIncrement, classFieldName, ignoreOnInsert));
	        		parser.nextTag();
	            }
	            else {
	            	skipXML(parser);
	            }
	        }

	        System.out.println("PARSING COMPLETE");

		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} finally {
			in.close();
		}
	}

	public int getTypeAsInt(String t) {
		if(t.equals("NUMERIC")) return 1;
		else if(t.equals("REAL")) return 2;
		else if(t.equals("TEXT")) return 3;
		else if(t.equals("BLOB")) return 4;
		else if(t.equals("INTEGER")) return 5;
		else throw new IllegalArgumentException("Can not parse Table field type: " + t);
	}
	
	private void skipXML(XmlPullParser parser) throws XmlPullParserException, IOException {
	    if (parser.getEventType() != XmlPullParser.START_TAG) {
	        throw new IllegalStateException();
	    }
	    int depth = 1;
	    while (depth != 0) {
	        switch (parser.next()) {
	        case XmlPullParser.END_TAG:
	            depth--;
	            break;
	        case XmlPullParser.START_TAG:
	            depth++;
	            break;
	        }
	    }
	 }

	public List<DBTableDescriptor> getDatabase() {
		return database;
	}
	
}
