package com.alphasoluciones.antonio.api_controler.sqlite;

public class Tablas {

	public String Table_C_Tipo_Reporte = "CREATE TABLE c_tipo_reporte("
			+ "id INTEGER NOT NULL," + "value TEXT NOT NULL)";

	public String Table_Client = "CREATE TABLE client("
			+ "id INTEGER NOT NULL," + "value TEXT NOT NULL)";

	public String Table_Geolocalizacion = "CREATE TABLE geolocalizacion("
			+ "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + "lat TEXT,"
			+ "lon TEXT," + "battery TEXT," + "accuracy TEXT," + "imei TEXT,"
			+ "satelliteUTC REAL," + "date TEXT," + "tz TEXT)";

	public String Table_Log = "CREATE TABLE log("
			+ "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
			+ "name_app TEXT," + "version_app TEXT," + "user_name TEXT,"
			+ "service_name TEXT," + "response_service TEXT,"
			+ "status_service TEXT)";

	public String Table_Pdv = "CREATE TABLE pdv(" + "id INTEGER NOT NULL,"
			+ "id_client INTEGER NOT NULL," + "id_rtm INTEGER," + "name TEXT,"
			+ "address TEXT," + "pdv_code INTEGER," + "lat REAL," + "lon REAL,"
			+ "nip TEXT)";

	public String Table_Report = "CREATE TABLE report("
			+ "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
			+ "id_schedule INTEGER," + "version TEXT," + "date TEXT,"
			+ "tz TEXT," + "IMEI TEXT," + "hash TEXT," + "enviado INTEGER,"
			+ "tipo_report INTEGER," + "id_report_server INTEGER)";

	public String Table_Report_Check = "CREATE TABLE report_check("
			+ "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
			+ "id_report INTEGER," + "date TEXT," + "tz TEXT," + "lat TEXT,"
			+ "lon TEXT," + "accuracy TEXT," + "imei TEXT,"
			+ "satelliteUTC TEXT," + "type INTEGER," + "path TEXT," + "hash TEXT,"+ "send INTEGER,"
			+ "enviado INTEGER)";

	public String Table_Report_Incidencia = "CREATE TABLE report_incidencia("
			+ "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
			+ "id_report INTEGER," + "id_pdv INTEGER," + "incidencia TEXT,"
			+ "path TEXT," + "hash TEXT," + "enviado INTEGER)";

	public String Table_agenda = "CREATE TABLE agenda("
			+ "id INTEGER NOT NULL," + "id_user INTEGER," + "id_place INTEGER,"
			+ "start_datetime TEXT," + "end_datetime TEXT)";

	/**
	 * service_type
	 */
	public String Table_service_type = "CREATE TABLE service_type("
			+ "id INTEGER PRIMARY KEY," + "value TEXT);";
	
	public String Table_Report_ServiceType = "CREATE TABLE reportServiceType ("
			+ "id INTEGER PRIMARY KEY AUTOINCREMENT,idReporte INTEGER NOT NULL,idServiceType INTEGER NOT NULL,enviado INTEGER DEFAULT 0)";
	/**
	 * Encuesta
	 */
	public String Table_EAEncuesta = "CREATE TABLE EAEncuesta("
			+ "id INTEGER PRIMARY KEY NOT NULL," + "nombre TEXT,"
			+ "vigenciaInicial INTEGER," + "vigenciaFinal INTEGER,"
			+ "repeticiones INTEGER," + "canal TEXT," + "rtm TEXT,"
			+ "cliente TEXT," + "pdv TEXT," + "query TEXT,"
			+ "descripcion TEXT)";

	public String Table_EAGrupo = "CREATE TABLE EAGrupo("
			+ "id INTEGER PRIMARY KEY NOT NULL," + "nombre TEXT)";

	public String Table_EAOpcionPregunta = "CREATE TABLE EAOpcionPregunta("
			+ "idPregunta INTEGER NOT NULL," + "opcion TEXT,"
			+ "image TEXT)";

	public String Table_EAPregunta = "CREATE TABLE EAPregunta("
			+ "id INTEGER PRIMARY KEY NOT NULL," + "idSeccion INTEGER,"
			+ "idEncuesta INTEGER," + "idGrupo INTEGER," + "pregunta TEXT,"
			+ "parentId INTEGER," + "idTipoPregunta INTEGER,"
			+ "obligatoria INTEGER," + "RangoMinimo TEXT,"
			+ "RangoMaximo TEXT," + "orden INTEGER," + "peso INTEGER,"
			+ "operadorDependencia TEXT," + "valorDependencia1 TEXT,"
			+ "valorDependencia2 TEXT," + "queryOpcionesDependencia TEXT,"
			+ "queryVisibility INTEGER," + "queryOpciones TEXT)";

	public String Table_EARespuesta = "CREATE TABLE EARespuesta("
			+ "ida INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
			+ "idPregunta INTEGER," + "idReporte INTEGER,"
			+ "idReporteLocal INTEGER," + "idEncuesta INTEGER,"
			+ "nombreEncuesta TEXT," + "respuesta TEXT," + "hash TEXT,"
			+ "enviado INTEGER," + "numeroEncuesta INTEGER,"
			+ "campoExtra1 TEXT," + "campoExtra2 TEXT," + "timeStamp TEXT)";

	public String Table_EASeccion = "CREATE TABLE EASeccion("
			+ "id INTEGER PRIMARY KEY NOT NULL," + "idEncuesta INTEGER,"
			+ "idParent INTEGER," + "orden INTEGER," + "peso INTEGER,"
			+ "nombre TEXT)";

	public String Table_EATipoPregunta = "CREATE TABLE EATipoPregunta("
			+ "id INTEGER PRIMARY KEY NOT NULL," + "tipo TEXT)";
	/**
	 * Termina la Encuesta
	 */
	
	
	/**
	 * alter para enviar el checkin
	 */
	public static String Alter_Table_send ="ALTER TABLE report_check ADD COLUMN send INTEGER";
	public static String Alter_Table_hash ="ALTER TABLE report_check ADD COLUMN hash Text";

}
