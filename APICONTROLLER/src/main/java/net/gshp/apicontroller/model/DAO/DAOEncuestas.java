package net.gshp.apicontroller.model.DAO;

import java.util.ArrayList;
import java.util.List;

import net.gshp.apicontroller.model.DTO.EAEncuesta;
import net.gshp.apicontroller.model.DTO.EAGrupo;
import net.gshp.apicontroller.model.DTO.EAOpcionPregunta;
import net.gshp.apicontroller.model.DTO.EAPregunta;
import net.gshp.apicontroller.model.DTO.EARespuesta;
import net.gshp.apicontroller.model.DTO.EASeccion;
import net.gshp.apicontroller.model.DTO.EATipoPregunta;
import net.gshp.apicontroller.model.DTO.PreguntaRespuesta;
import net.gshp.apicontroller.util.Util;

import android.database.Cursor;
import android.util.Log;

import com.gosharp.apis.db.DBAPI;

public class DAOEncuestas {

	public DAOEncuestas() {
		createTables();
	}

	/**
	 * */
	public List<EAEncuesta> selectTipoEncuestas(long idReporte, int idCanal,
			int idCliente, int idPdv, int idRtm) {

		String query = "SELECT DISTINCT\n" +
		// "Encuesta.id,\n" +
		// "Encuesta.nombre\n" +
				"*\n" + "FROM\n" + "EAEncuesta where \n"
				+ System.currentTimeMillis() + "\n"
				+ "BETWEEN vigenciaInicial and vigenciaFinal and\n" + "(\n"
				+ "EAEncuesta.canal like \"%@" + idCanal + "@%\" or \n"
				+ "EAEncuesta.cliente like \"%@" + idCliente + "@%\" or \n"
				+ "EAEncuesta.pdv like \"%@" + idPdv + "@%\" or \n"
				+ "EAEncuesta.rtm like \"%@" + idRtm + "@%\" or \n" + "(\n"
				+ "EAEncuesta.canal is null and\n"
				+ "EAEncuesta.cliente is null and\n"
				+ "EAEncuesta.pdv is null and\n" + "EAEncuesta.rtm is null \n"
				+ ")\n" + ")";
		List<EAEncuesta> list = new ArrayList<EAEncuesta>();
		list = DBAPI.getInstance().selectByQuery(new EAEncuesta(),
				"EAEncuesta", query);
		// System.out.println("list.size(): " + list.size());
		return list;
	}

	/**
	 * */
	public List<EAEncuesta> selectEncuestas(long idEncuesta) {
		// TODO
		List<EAEncuesta> list = new ArrayList<EAEncuesta>();
		return list;
	}

	public List<EARespuesta> selectEncuestasbyNumero(long idEncuesta,
			long idReporteLocal) {
		String query = "SELECT DISTINCT\n" +
				"EARespuesta.respuesta,\n" +
				"EARespuesta.hash,\n" +
				"EARespuesta.idEncuesta,\n" +
				"EARespuesta.idPregunta,\n" +
				"EARespuesta.idReporte,\n" +
				"EARespuesta.idReporteLocal,\n" +
				"EARespuesta.ida,\n" +
				"EARespuesta.numeroEncuesta,\n" +
				"EARespuesta.enviado,\n" +
				"EARespuesta.nombreEncuesta,\n" +
				"EARespuesta.campoExtra1,\n" +
				"EARespuesta.campoExtra2,\n" +
				"EARespuesta.timeStamp\n" +
				"FROM  EARespuesta\n" +
				"where EARespuesta.idEncuesta="+idEncuesta+"   and EARespuesta.idReporteLocal="+idReporteLocal+"\n" +
				"GROUP BY EARespuesta.numeroEncuesta\n" +
				"ORDER BY EARespuesta.numeroEncuesta";
		List<EARespuesta> list = DBAPI.getInstance().selectByQuery(
				new EARespuesta(), "EARespuesta", query);
		return list;
	}

	/**
	 * */
	public List<PreguntaRespuesta> selectPreguntaRespuesta(long idEncuesta,
			long idReporteLocal, int numeroEncuesta, String campoExtra1,
			String campoExtra2) {
		// String query = "SELECT DISTINCT\n"
		// + "EAPregunta.RangoMaximo,\n"
		// + "EAPregunta.RangoMinimo,\n"
		// + "EAPregunta.valorDependencia2,\n"
		// + "EAPregunta.valorDependencia1,\n"
		// + "EAPregunta.pregunta,\n"
		// + "EAPregunta.operadorDependencia,\n"
		// + "EAPregunta.idTipoPregunta,\n"
		// + "EAPregunta.idSeccion,\n"
		// + "EAPregunta.idEncuesta,\n"
		// + "EAPregunta.parentId,\n"
		// + "EAPregunta.id as idPregunta,\n"
		// + "EAPregunta.idGrupo,\n"
		// + "EAPregunta.peso,\n"
		// + "EAPregunta.orden,\n"
		// + "EAPregunta.obligatoria,\n"
		// + "EARespuesta.respuesta,\n"
		// + "EARespuesta.hash,\n"
		// + "EARespuesta.idPregunta,\n"
		// + "EARespuesta.idReporte,\n"
		// + "EARespuesta.idReporteLocal,\n"
		// + "EARespuesta.ida,\n"
		// + "EARespuesta.numeroEncuesta,\n"
		// + "EARespuesta.enviado,\n"
		// + "EAEncuesta.nombre as nombreEncuesta\n"
		// + "FROM\n"
		// + "EAPregunta\n"
		// +
		// "LEFT JOIN EARespuesta ON EARespuesta.idPregunta = EAPregunta.id AND EARespuesta.idEncuesta = "
		// + idEncuesta
		// + "\n"
		// + "INNER JOIN EAEncuesta ON EAEncuesta.id = EARespuesta.idEncuesta\n"
		// + "where numeroEncuesta=" + numeroEncuesta + "\n"
		// + "and idReporteLocal=" + idReporteLocal;
		String query2 = "SELECT\n"
				+ "EAPregunta.RangoMaximo,\n"
				+ "EAPregunta.RangoMinimo,\n"
				+ "EAPregunta.valorDependencia2,\n"
				+ "EAPregunta.valorDependencia1,\n"
				+ "EAPregunta.pregunta,\n"
				+ "EAPregunta.operadorDependencia,\n"
				+ "EAPregunta.idTipoPregunta,\n"
				+ "EAPregunta.idSeccion,\n"
				+ "EAPregunta.idEncuesta,\n"
				+ "EAPregunta.parentId,\n"
				+ "EAPregunta.id as idPregunta,\n"
				+ "EAPregunta.idGrupo,\n"
				+ "EAPregunta.peso,\n"
				+ "EAPregunta.queryOpcionesDependencia,\n"
				+ "EAPregunta.queryOpciones,\n"
				// TODO Borrar
				+ "EAPregunta.queryVisibility,\n"
				+ "EAPregunta.orden,\n"
				+ "EAPregunta.obligatoria,\n"
				+ "EARespuesta.respuesta,\n"
				+ "EARespuesta.hash,\n"
				+ "EARespuesta.enviado,\n"
				+ "EARespuesta.idReporte,\n"
				+ "EARespuesta.idReporteLocal,\n"
				+ "EARespuesta.ida,\n"
				+ "EARespuesta.numeroEncuesta,\n"
				+ "EARespuesta.campoExtra1,\n"
				+ "EARespuesta.campoExtra2,\n"
				+ "EARespuesta.timeStamp,\n"
				+ "EAEncuesta.nombre AS nombreEncuesta\n"
				+ "FROM\n"
				+ "EAPregunta\n"
				+ "INNER JOIN EAEncuesta ON EAPregunta.idEncuesta = EAEncuesta.id\n"
				+ "LEFT JOIN EARespuesta ON EARespuesta.idPregunta = EAPregunta.id\n"
				+ "and EARespuesta.idReporteLocal="
				+ idReporteLocal
				+ "\n"
				+ "and EARespuesta.numeroEncuesta="
				+ numeroEncuesta
				+ "\n"

				+ ((campoExtra1 != null) ? ("and EARespuesta.campoExtra1=\"" + campoExtra1)
						+ ("\"")
						: "")
				+ ((campoExtra2 != null) ? ("and EARespuesta.campoExtra2=\"" + campoExtra2)
						+ ("\"")
						: "")

				+ "WHERE EAPregunta.idEncuesta=" + idEncuesta
				+ " order by orden, peso";
		List<PreguntaRespuesta> list = DBAPI.getInstance().selectByQuery(
				new PreguntaRespuesta(), query2);
		// for (PreguntaRespuesta pr : list) {
		// System.out.println(pr.toString());
		// }
		return list;
	}

	public boolean createTables() {
		DBAPI.getInstance().createTable(EAEncuesta.class);
		DBAPI.getInstance().createTable(EAGrupo.class);
		DBAPI.getInstance().createTable(EAOpcionPregunta.class);
		DBAPI.getInstance().createTable(EAPregunta.class);
		DBAPI.getInstance().createTable(EARespuesta.class);
		DBAPI.getInstance().createTable(EASeccion.class);
		DBAPI.getInstance().createTable(EATipoPregunta.class);
		return true;
	}

	public EASeccion getSeccionByID(long idSeccion) {
		return (EASeccion) (DBAPI.getInstance().selectById(idSeccion,
				new EASeccion()));
	}

	public EAGrupo getGrupoByID(long idGrupo) {
		return (EAGrupo) (DBAPI.getInstance()
				.selectById(idGrupo, new EAGrupo()));
	}

	public List<EAOpcionPregunta> getOpcionesByIDPregunta(long idPregunta) {
		String query = "SELECT\n" + "*\n" + "FROM\n" + "EAOpcionPregunta\n"
				+ "where \n" + "EAOpcionPregunta.idPregunta=" + idPregunta+" ORDER BY opcion";
		List<EAOpcionPregunta> list = DBAPI.getInstance().selectByQuery(
				new EAOpcionPregunta(), query);
		return list;
	}

	public List<EAOpcionPregunta> getOpcionesByQuery(String query) {
		List<EAOpcionPregunta> list = DBAPI.getInstance().selectByQuery(
				new EAOpcionPregunta(), query);
		return list;
	}

	public List<EAOpcionPregunta> getOpcionesByQuery(String query,
			String[] params) {
		List<EAOpcionPregunta> list = DBAPI.getInstance().selectByQuery(
				new EAOpcionPregunta(), query, params);
		return list;
	}

	public List<EARespuesta> selectToSend() {
		String selectQuery = "SELECT\n" + "EARespuesta.ida,\n"
				+ "EARespuesta.idPregunta,\n" + "EARespuesta.idReporte,\n"
				+ "EARespuesta.idReporteLocal,\n" + "EARespuesta.idEncuesta,\n"
				+ "EARespuesta.nombreEncuesta,\n" + "EARespuesta.respuesta,\n"
				+ "EARespuesta.hash,\n" + "EARespuesta.enviado,\n"
				+ "EARespuesta.numeroEncuesta,\n"
				+ "EARespuesta.campoExtra1,\n" + "EARespuesta.campoExtra2,\n"
				+ "EARespuesta.timeStamp\n" + "FROM\n" + "EARespuesta\n"
				+ "WHERE enviado<>1\n" + "and idReporte<>0";
		System.out.println("SELECT_TO_SEND_QUERY: " + selectQuery);
		List<EARespuesta> list = DBAPI.getInstance().selectByQuery(
				new EARespuesta(), selectQuery);
		return list;
	}

	public void updateRespuestaEnviado() {
		String updateQuery = "UPDATE EARespuesta SET enviado=1 where\n"
				+ "EARespuesta.enviado<>1";
		System.out.println("UPDATE_QUERY: " + updateQuery);
		DBAPI.getInstance().execSQL(updateQuery);
	}

	public void updateReportServer(long idReporteLocal, long idReporte) {
		String updateQuery = "UPDATE EARespuesta SET idReporte=" + idReporte
				+ " \n" + "where EARespuesta.idReporteLocal=" + idReporteLocal;
		System.out.println("UPDATE_QUERY: " + updateQuery);
		DBAPI.getInstance().execSQL(updateQuery);
	}

	public boolean persistRespuestas(
			List<PreguntaRespuesta> listPreguntaRespuesta, int numeroEncuesta,
			int idReporteLocal, String campoExtra1, String campoExtra2) {
		String deleteQuery = "delete\n"
				+ "FROM\n"
				+ "EARespuesta\n"
				+ "where \n"
				+ "EARespuesta.idEncuesta=\n"
				+ listPreguntaRespuesta.get(0).idEncuesta
				+ "\n"
				+ "and EARespuesta.numeroEncuesta=\n"
				+ numeroEncuesta
				+ "\n"
				+ "and EARespuesta.idReporteLocal=\n"
				+ idReporteLocal
				+ ((campoExtra1 != null) ? " and EARespuesta.campoExtra1=\""
						+ campoExtra1 + "\"" : "")
				+ ((campoExtra2 != null) ? " and EARespuesta.campoExtra2=\""
						+ campoExtra2 + "\"" : "");

		System.out.println("deleteQuery: " + deleteQuery);
		try {
			DBAPI.getInstance().execSQL(deleteQuery);

			for (PreguntaRespuesta preguntaRespuesta : listPreguntaRespuesta) {
				if (preguntaRespuesta.respuesta != null
						&& preguntaRespuesta.respuesta.length() > 0) {
					preguntaRespuesta.numeroEncuesta = numeroEncuesta;
					preguntaRespuesta.idReporteLocal = idReporteLocal;
					preguntaRespuesta.campoExtra1 = campoExtra1;
					preguntaRespuesta.campoExtra2 = campoExtra2;
					preguntaRespuesta.timeStamp = System.currentTimeMillis()
							+ "";
					preguntaRespuesta.hash = Util.md5(""
							+ System.currentTimeMillis()
							+ preguntaRespuesta.idReporteLocal + ""
							+ preguntaRespuesta.idEncuesta + ""
							+ preguntaRespuesta.idPregunta);

					DBAPI.getInstance().insert(preguntaRespuesta,
							new EARespuesta());
				}
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * */
	public boolean isRespuesta(long idEncuesta, long idReporteLocal,
			int numeroEncuesta) {
		String query2 = "SELECT\n"
				+ "EAPregunta.RangoMaximo,\n"
				+ "EAPregunta.RangoMinimo,\n"
				+ "EAPregunta.valorDependencia2,\n"
				+ "EAPregunta.valorDependencia1,\n"
				+ "EAPregunta.pregunta,\n"
				+ "EAPregunta.operadorDependencia,\n"
				+ "EAPregunta.idTipoPregunta,\n"
				+ "EAPregunta.idSeccion,\n"
				+ "EAPregunta.idEncuesta,\n"
				+ "EAPregunta.parentId,\n"
				+ "EAPregunta.id as idPregunta,\n"
				+ "EAPregunta.idGrupo,\n"
				// TODO borrar
				+ "EAPregunta.queryVisibility,\n"
				+ "EAPregunta.peso,\n"
				+ "EAPregunta.queryOpcionesDependencia,\n"
				+ "EAPregunta.queryOpciones,\n"
				+ "EAPregunta.orden,\n"
				+ "EAPregunta.obligatoria,\n"
				+ "EARespuesta.respuesta,\n"
				+ "EARespuesta.hash,\n"
				+ "EARespuesta.enviado,\n"
				+ "EARespuesta.idReporte,\n"
				+ "EARespuesta.idReporteLocal,\n"
				+ "EARespuesta.ida,\n"
				+ "EARespuesta.numeroEncuesta,\n"
				+ "EARespuesta.campoExtra1,\n"
				+ "EARespuesta.campoExtra2,\n"
				+ "EARespuesta.timeStamp,\n"
				+ "EAEncuesta.nombre AS nombreEncuesta\n"
				+ "FROM\n"
				+ "EAPregunta\n"
				+ "INNER JOIN EAEncuesta ON EAPregunta.idEncuesta = EAEncuesta.id\n"
				+ "INNER JOIN EARespuesta ON EARespuesta.idPregunta = EAPregunta.id\n"
				+ "and EARespuesta.idReporteLocal=" + idReporteLocal + "\n"
				+ "and EARespuesta.numeroEncuesta=" + numeroEncuesta + "\n"
				+ "WHERE EAPregunta.idEncuesta=" + idEncuesta;
		List<PreguntaRespuesta> list = DBAPI.getInstance().selectByQuery(
				new PreguntaRespuesta(), query2);
		return list.size() > 0;
	}

	public boolean isRespuesta(long idEncuesta, long idReporteLocal,
			int numeroEncuesta, String campoExtra1) {
		String query2 = "SELECT\n"
				+ "EAPregunta.RangoMaximo,\n"
				+ "EAPregunta.RangoMinimo,\n"
				+ "EAPregunta.valorDependencia2,\n"
				+ "EAPregunta.valorDependencia1,\n"
				+ "EAPregunta.pregunta,\n"
				+ "EAPregunta.operadorDependencia,\n"
				+ "EAPregunta.idTipoPregunta,\n"
				+ "EAPregunta.idSeccion,\n"
				+ "EAPregunta.idEncuesta,\n"
				+ "EAPregunta.parentId,\n"
				+ "EAPregunta.id as idPregunta,\n"
				+ "EAPregunta.idGrupo,\n"
				// TODO borrar
				+ "EAPregunta.queryVisibility,\n"
				+ "EAPregunta.peso,\n"
				+ "EAPregunta.queryOpcionesDependencia,\n"
				+ "EAPregunta.queryOpciones,\n"
				+ "EAPregunta.orden,\n"
				+ "EAPregunta.obligatoria,\n"
				+ "EARespuesta.respuesta,\n"
				+ "EARespuesta.hash,\n"
				+ "EARespuesta.enviado,\n"
				+ "EARespuesta.idReporte,\n"
				+ "EARespuesta.idReporteLocal,\n"
				+ "EARespuesta.ida,\n"
				+ "EARespuesta.numeroEncuesta,\n"
				+ "EARespuesta.campoExtra1,\n"
				+ "EARespuesta.campoExtra2,\n"
				+ "EARespuesta.timeStamp,\n"
				+ "EAEncuesta.nombre AS nombreEncuesta\n"
				+ "FROM\n"
				+ "EAPregunta\n"
				+ "INNER JOIN EAEncuesta ON EAPregunta.idEncuesta = EAEncuesta.id\n"
				+ "INNER JOIN EARespuesta ON EARespuesta.idPregunta = EAPregunta.id\n"
				+ "and EARespuesta.idReporteLocal="
				+ idReporteLocal
				+ "\n"
				+ "and EARespuesta.numeroEncuesta="
				+ numeroEncuesta
				+ "\n"
				+ ((campoExtra1 != null) ? "and EARespuesta.campoExtra1=\""
						+ campoExtra1 + "\"\n" : "")
				+ "WHERE EAPregunta.idEncuesta=" + idEncuesta;
		Log.i("API_ ENCUESTA query2: ",  "query2: "+query2);
		List<PreguntaRespuesta> list = DBAPI.getInstance().selectByQuery(
				new PreguntaRespuesta(), query2);
		return list.size() > 0;
	}

	/**
	 * */
	public boolean evaluateQueryByRows(String queryToEvaluate) {
		Cursor c = DBAPI.getInstance().queryDatabse(queryToEvaluate);
		System.out.println("c: " + c.toString());
		System.out.println("c.count: " + c.getCount());
		boolean b = c.getCount() > 0;
		try {
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}
}