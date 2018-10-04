package com.alphasoluciones.antonio.api_controler.dao;

import java.util.List;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.alphasoluciones.antonio.api_controler.dto.DTOEA_question_type_cat;

public class DAOEATipoPregunta extends DAO{

	private SQLiteDatabase db;
	
	public static String TABLE_NAME="EATipoPregunta";
	public static String PK_FIELD="id";
	
	public DAOEATipoPregunta(){
		super(TABLE_NAME, PK_FIELD);
	}
	
	/**
	 * Insert
	 */
	public int Insert( List<DTOEA_question_type_cat> obj)
	{
		db=helper.getWritableDatabase();
		ContentValues cv;
		int resp=0;
		try {
			db.beginTransaction();
			for (int i = 0; i < obj.size(); i++) {
				cv=new ContentValues();
				cv.put("id", obj.get(i).id);
				cv.put("tipo", obj.get(i).type);
				resp=(int)db.insert(TABLE_NAME, null, cv);
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("error db");
		}
		finally
		{
			db.endTransaction();
		}
		db.close();
		return resp;
	}

}
