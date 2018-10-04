package com.gosharp.apis.db;

import java.util.List;

public class DBObjectController {
	
	private DBInstance db;
	private DBTableDescriptor td;
	private Object archetype;

	public DBObjectController(DBInstance db, DBTableDescriptor td, Object pojo) throws Exception {
		this.db = db;
		this.td = td;
		this.archetype = pojo;
		
		if(this.db == null) throw new Exception("Database instance can not be null");
	}
	
	public long insert(Object o) {
		if(db != null) {
			return db.insert(o, td);
		}
		return -1;
	}
	
	public <T> Object selectById(int id) {
		return db.selectById(id, archetype, td);
	}
	
	public <T> List<T> selectByQuery(String sql) {
		if(db != null) {
			return db.selectByQuery(archetype, td, sql);
		}
		return null;
	}
	
	public <T> List<T> selectAll() {
		if(db != null) {
			return db.selectAll(archetype, td);
		}
		return null;
	}
	
	public boolean update(Object o) {
		return db.update(o, td);
	}
	
	public boolean delete(Object o) {
		return db.delete(o, td);
	}
	
	public int deleteById(int id) {
		return db.deleteById(id, td);
	}
	
	//TODO regresar los long en todos
	
	public <T> void batchInsert(List<T> list) {
		db.batchInsert(list, td);
	}

	public <T> void batchInsert2(List<T> list) {
		db.batchInsert2(list, td);
	}
	
	public long persist(Object o) {
		return db.persist(o, td);
	}

	public DBTableDescriptor getTd() {
		return td;
	}

	public void setTd(DBTableDescriptor td) {
		this.td = td;
	}
	
}
