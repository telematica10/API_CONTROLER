package net.gshp.apiencuesta.model.DTO;

import java.io.Serializable;

public class PreguntaRespuesta implements Serializable {
	/**
	 * 
	 */
	// Pregunta
	// public long id;
	public long idSeccion;
	public long idEncuesta;
	public long idGrupo;
	public String pregunta;
	public long parentId;
	public long idTipoPregunta;
	public boolean obligatoria;
	public String RangoMinimo;
	public String RangoMaximo;
	public int orden;
	public int peso;
	public String operadorDependencia;// <,>,<=,>=,<>, BETWEEN
	public String valorDependencia1;
	public String valorDependencia2;

	// public boolean visible;

	// Respuesta
	public long ida;// Id autoincrementable

	public long idPregunta;
	public long idReporte;
	public long idReporteLocal;
	// ////////////////
	// public long idEncuesta;
	public String nombreEncuesta;
	// ////////////////
	public String respuesta;
	public String hash;
	public int enviado;

	public int numeroEncuesta;

	public String queryOpcionesDependencia;
	public String queryOpciones;

	public String campoExtra1;
	public String campoExtra2;

	public String timeStamp;

	// La visibilidad de la pregunta podría depender de una query aleatoria
	public String queryVisibility;

	@Override
	public String toString() {
		return "PreguntaRespuesta [idSeccion=" + idSeccion + ", idEncuesta="
				+ idEncuesta + ", idGrupo=" + idGrupo + ", pregunta="
				+ pregunta + ", parentId=" + parentId + ", idTipoPregunta="
				+ idTipoPregunta + ", obligatoria=" + obligatoria
				+ ", RangoMinimo=" + RangoMinimo + ", RangoMaximo="
				+ RangoMaximo + ", orden=" + orden + ", peso=" + peso
				+ ", operadorDependencia=" + operadorDependencia
				+ ", valorDependencia1=" + valorDependencia1
				+ ", valorDependencia2=" + valorDependencia2 + ", ida=" + ida
				+ ", idPregunta=" + idPregunta + ", idReporte=" + idReporte
				+ ", idReporteLocal=" + idReporteLocal + ", nombreEncuesta="
				+ nombreEncuesta + ", respuesta=" + respuesta + ", hash="
				+ hash + ", enviado=" + enviado + ", numeroEncuesta="
				+ numeroEncuesta + ", queryVisibility=" + queryVisibility
				+ ", queryOpcionesDependencia" + queryOpcionesDependencia + "]";
	}

}
