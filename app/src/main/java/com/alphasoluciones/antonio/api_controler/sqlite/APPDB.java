package com.alphasoluciones.antonio.api_controler.sqlite;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.alphasoluciones.antonio.api_controler.ContextApp;

public class APPDB extends SQLiteOpenHelper{
	
	private Tablas tablas;
	public static final String sttfstrpathDB=
"/data/data/com.alphasoluciones.antonio.api_controler/databases/API_CONTROLER.db";
		/*Environment
		.getExternalStorageDirectory().getPath()
		+ "/sdcard/API_CONTROLER/API_CONTROLER.db";*/
	

	public APPDB( ) {

		super(ContextApp.context ,sttfstrpathDB, null, 1);
		tablas=new Tablas();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(tablas.Table_C_Tipo_Reporte);
		db.execSQL(tablas.Table_Client);
		db.execSQL(tablas.Table_Geolocalizacion);
		db.execSQL(tablas.Table_Pdv);
		db.execSQL(tablas.Table_Report);
		db.execSQL(tablas.Table_Report_Check);
		db.execSQL(tablas.Table_Report_Incidencia);
		db.execSQL(tablas.Table_agenda);
		db.execSQL(tablas.Table_service_type);
		db.execSQL(tablas.Table_Report_ServiceType);
		/**
         * Encuesta
         */
        db.execSQL(tablas.Table_EAEncuesta);
        db.execSQL(tablas.Table_EAGrupo);
        db.execSQL(tablas.Table_EAOpcionPregunta);
        db.execSQL(tablas.Table_EAPregunta);
        db.execSQL(tablas.Table_EARespuesta);
        db.execSQL(tablas.Table_EASeccion);
        db.execSQL(tablas.Table_EATipoPregunta); 

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		switch (oldVersion) {
		case 1:
			db.execSQL(Tablas.Alter_Table_send);
			db.execSQL(Tablas.Alter_Table_hash);
			Log.d("appdb", "alterar tabla");

		default:
			break;
		}
	}
	

}
