package com.alphasoluciones.antonio.api_controler.dao;

import java.util.List;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.alphasoluciones.antonio.api_controler.dto.DTOEa_question_option;

public class DAOEAQuestionOption extends DAO{
	
	private SQLiteDatabase db;
	
	public static String TABLE_NAME="EAOpcionPregunta";
	public static String PK_FIELD="opcion";
	
	public final String opcion="opcion";
	public final String image="image";
	public final String idPregunta="idPregunta";

	public DAOEAQuestionOption() {
		super(TABLE_NAME, PK_FIELD);
		// TODO Auto-generated constructor stub
	}
	/**
	 * Insert
	 */
	public int Insert( List<DTOEa_question_option> obj)
	{
		db=helper.getWritableDatabase();
		int resp=0;
		try {
			SQLiteStatement insStmnt=db.compileStatement("INSERT INTO " +
					TABLE_NAME +" ("+opcion 
									+","+image
									+","+idPregunta
									+") VALUES(?,?,?);");
			db.beginTransaction();
			
			for (DTOEa_question_option dto : obj) {

				try {
					insStmnt.bindString(1, dto.option_value);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					insStmnt.bindNull(1);
				}
				try {
					insStmnt.bindString(2, dto.image);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					insStmnt.bindNull(2);
				}
				try {
					insStmnt.bindLong(3, dto.question);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					insStmnt.bindNull(3);
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

}
