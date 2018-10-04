package com.alphasoluciones.antonio.api_controler.dao;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.alphasoluciones.antonio.api_controler.sqlite.APPDB;

public class DAO {

	protected APPDB helper;
	protected SQLiteDatabase db;
	protected Cursor cursor;
	
	public  String TABLE_NAME;
	public String PK_FIELD;
	
	public DAO(String TABLE_NAME,String PK_FIELD){
		helper=new APPDB();
		this.TABLE_NAME=TABLE_NAME;
		this.PK_FIELD=PK_FIELD;
	}
	
	/**
	 * Insert
	 */
	public int Insert()
	{
		return 0;
	}
	/**
	 * Delete
	 */
	public int deleteById(int _id)
	{
		int resp=0;
		try {
			db=helper.getWritableDatabase();
			resp=db.delete(TABLE_NAME, PK_FIELD + "=?", new String[]{String.valueOf(_id)});
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		finally
		{
			db.close();
		}
		return resp;
	}
	/**
	 * Delete
	 */
	public int delete()
	{
		try {
			db = helper.getWritableDatabase();
			int resp = db.delete(TABLE_NAME, null, null);
			db.close();
			return resp;
		} catch(SQLException e) {
			e.printStackTrace();
			return -1;
		} finally {
			if (db.isOpen()) {
				db.close();
			}
		}
	}
	/**
	 * Cursor
	 */
	public Cursor list(){
		db=helper.getReadableDatabase();
		Cursor c=db.rawQuery("SELECT * FROM "+ TABLE_NAME, null);
		return c;
	}
	
	/**
	 * Count
	 */
	public int count()
	{
		try {
			db = helper.getReadableDatabase();
			cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
			int count = cursor.getCount();
			cursor.close();
			db.close();
			return count;
		} catch (SQLException e) {
			return -1;
		} finally {
			if (db.isOpen()) {
				db.close();
			}
		}
	}
}
