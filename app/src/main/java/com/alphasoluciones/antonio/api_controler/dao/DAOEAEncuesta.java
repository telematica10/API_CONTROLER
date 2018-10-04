package com.alphasoluciones.antonio.api_controler.dao;

import java.util.List;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.alphasoluciones.antonio.api_controler.dto.DTOEA_encuesta;

public class DAOEAEncuesta extends DAO{
	
	private SQLiteDatabase db;
	
	public static String TABLE_NAME="EAEncuesta";
	public static String PK_FIELD="id";
	
	public final String id="id";
	public final String nombre="nombre";
	public final String vigenciaInicial="vigenciaInicial";
	public final String vigenciaFinal="vigenciaFinal";
	public final String repeticiones="repeticiones";
	public final String canal="canal";
	public final String rtm="rtm";
	public final String cliente="cliente";
	public final String pdv="pdv";
	public final String query="query";
	public final String descripcion="descripcion";

	public DAOEAEncuesta() {
		super(TABLE_NAME, PK_FIELD);
		// TODO Auto-generated constructor stub
	}
	/**
	 * Insert
	 */
	public int Insert( List<DTOEA_encuesta> obj)
	{
		db=helper.getWritableDatabase();
		int resp=0;
		try {
			SQLiteStatement insStmnt=db.compileStatement("INSERT INTO " +
					TABLE_NAME +" ("+id 
									+","+nombre
									+","+vigenciaInicial
									+","+vigenciaFinal+","
									+repeticiones+","
									+descripcion+") VALUES(?,?,?,?,?,?);");
			db.beginTransaction();
			
			for (DTOEA_encuesta dto : obj) {

				try {
					insStmnt.bindLong(1, dto.id);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					insStmnt.bindNull(1);
				}
				try {
					insStmnt.bindString(2, dto.name);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					insStmnt.bindNull(2);
				}
				try {
					insStmnt.bindLong(3, dto.date_start);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					insStmnt.bindNull(3);
				}
				try {
					insStmnt.bindLong(4, dto.date_end);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					insStmnt.bindNull(4);
				}
				try {
					insStmnt.bindLong(5, dto.repeat);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					insStmnt.bindNull(5);
				}
				try {
					insStmnt.bindString(6, dto.description);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					insStmnt.bindNull(6);
				}
				insStmnt.executeInsert();
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
	
	
	/**
	 * Select 
	 */
	public boolean isEncuesta(int idReporteLocal,int idEncuesta)
	{
		boolean isEncuesta=false;
		db=helper.getReadableDatabase();
		cursor=db.rawQuery("SELECT\n" +
				"count(*) as count\n" +
				"FROM\n" +
				"EARespuesta\n" +
				"WHERE idReporteLocal="+idReporteLocal+"   \n" +
				"and idEncuesta="+idEncuesta,null);
		if(cursor.moveToFirst())
		{
			int count=cursor.getColumnIndexOrThrow("count");
			isEncuesta=cursor.getInt(count)>0;
		}
		cursor.close();
		db.close();
		return isEncuesta;
	}

}
