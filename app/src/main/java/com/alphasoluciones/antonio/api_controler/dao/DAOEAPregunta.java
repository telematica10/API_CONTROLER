package com.alphasoluciones.antonio.api_controler.dao;

import java.util.List;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.alphasoluciones.antonio.api_controler.dto.DTOEA_question;

public class DAOEAPregunta  extends DAO{
	
	private SQLiteDatabase db;
	
	public static String TABLE_NAME="EAPregunta";
	public static String PK_FIELD="id";
	
	public final String RangoMaximo="RangoMaximo";
	public final String RangoMinimo="RangoMinimo";
	public final String valorDependencia2="valorDependencia2";
	public final String valorDependencia1="valorDependencia1";
	public final String queryVisibility="queryVisibility";
	public final String queryOpcionesDependencia="queryOpcionesDependencia";
	public final String queryOpciones="queryOpciones";
	public final String pregunta="pregunta";
	public final String operadorDependencia="operadorDependencia";
	public final String orden="orden";
	public final String parentId="parentId";
	public final String id="id";
	public final String idEncuesta="idEncuesta";
	public final String idTipoPregunta="idTipoPregunta";
	public final String idSeccion="idSeccion";
	public final String idGrupo="idGrupo";
	public final String obligatoria="obligatoria";
	public final String peso="peso";

	public DAOEAPregunta() {
		super(TABLE_NAME, PK_FIELD);
		// TODO Auto-generated constructor stub
	}
	/**
	 * Insert
	 */
	public int Insert( List<DTOEA_question> obj)
	{
		db=helper.getWritableDatabase();
		int resp=0;
		try {
			SQLiteStatement insStmnt=db.compileStatement("INSERT INTO " +
					TABLE_NAME +" ("+RangoMaximo 
									+","+RangoMinimo
									+","+valorDependencia2
									+","+valorDependencia1+","
									+queryVisibility+","
									+queryOpcionesDependencia+","
									+queryOpciones+","
									+pregunta+","
									+operadorDependencia+","
									+orden+","
									+parentId+","
									+id+","
									+idEncuesta+","
									+idTipoPregunta+","
									+idSeccion+","
									+idGrupo+","
									+obligatoria+","
									+peso+") VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
			db.beginTransaction();
			
			for (DTOEA_question dto : obj) {

				try {
					insStmnt.bindString(1, dto.range_max);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					insStmnt.bindNull(1);
				}
				try {
					insStmnt.bindString(2, dto.range_min);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					insStmnt.bindNull(2);
				}
				try {
					insStmnt.bindString(3, dto.value_dependency2);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					insStmnt.bindNull(3);
				}
				try {
					insStmnt.bindString(4, dto.value_dependency);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					insStmnt.bindNull(4);
				}
				try {
					insStmnt.bindString(5, dto.query_visibility);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					insStmnt.bindNull(5);
				}
				try {
					insStmnt.bindString(6, dto.query_dependency);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					insStmnt.bindNull(6);
				}
				try {
					insStmnt.bindString(7, dto.query_option);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					insStmnt.bindNull(7);
				}
				try {
					insStmnt.bindString(8, dto.question);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					insStmnt.bindNull(8);
				}
				try {
					insStmnt.bindString(9, dto.operator_dependency);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					insStmnt.bindNull(9);
				}
				try {
					insStmnt.bindLong(10, dto.order);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					insStmnt.bindNull(10);
				}
				try {
					insStmnt.bindLong(11, dto.parent);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					insStmnt.bindNull(11);
				}
				try {
					insStmnt.bindLong(12, dto.id);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					insStmnt.bindNull(12);
				}
				try {
					insStmnt.bindLong(13, dto.poll);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					insStmnt.bindNull(13);
				}
				try {
					insStmnt.bindLong(14, dto.type_question);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					insStmnt.bindNull(14);
				}
				try {
					insStmnt.bindLong(15, dto.section);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					insStmnt.bindNull(15);
				}
				try {
					insStmnt.bindLong(16, dto.group);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					insStmnt.bindNull(16);
				}
				try {
					insStmnt.bindLong(17, dto.required );
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					insStmnt.bindNull(17);
				}
				try {
					insStmnt.bindLong(18, dto.weight);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					insStmnt.bindNull(18);
				}

				insStmnt.executeInsert();
			}
			db.setTransactionSuccessful();
			/*----------------------------------------*/
			/*
			db.execSQL("delete from service_type;");
			db.execSQL("insert into service_type values (1,'catalogo1');");
			db.execSQL("insert into service_type values (2,'catalogo2');");
			db.execSQL("insert into service_type values (3,'catalogo3');");
			
			insStmnt=db.compileStatement("INSERT INTO EAEncuesta (id,nombre,vigenciaInicial,vigenciaFinal,repeticiones,descripcion) VALUES(?,?,?,?,?,?);");
			insStmnt.bindLong(1, 5);
			insStmnt.bindString(2, "Encuesta ATM");
			insStmnt.bindLong(3, 0);
			insStmnt.bindString(4, "4098383593000");
			insStmnt.bindString(5, "Encuesta ATM");
			insStmnt.execute();
			
			insStmnt=db.compileStatement("INSERT INTO EAPregunta " +
					"(pregunta," +
					"id," +
					"idEncuesta," +
					"idTipoPregunta," +
					"obligatoria) " +
					"VALUES(?,?,?,?,?);");
			
			insStmnt.bindString(1, "¿El monitor se encuentra funcionando?");
			insStmnt.bindLong(2, 40);
			insStmnt.bindLong(3, 5);
			insStmnt.bindLong(4, 17);
			insStmnt.bindLong(5, 1);
			insStmnt.execute();
			*/
			/*----------------------------------------*/
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
