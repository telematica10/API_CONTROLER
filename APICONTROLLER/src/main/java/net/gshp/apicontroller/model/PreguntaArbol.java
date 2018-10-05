package net.gshp.apicontroller.model;

import java.util.ArrayList;
import java.util.List;

import net.gshp.apicontroller.model.DTO.PreguntaRespuesta;
import android.content.Context;

public class PreguntaArbol {

	/**
	 * 
	 */
	List<NodoPregunta> listNodos;
	Context context;

	public PreguntaArbol(List<PreguntaRespuesta> listPreguntas, Context context) {
		this.context=context;
		listNodos = new ArrayList<NodoPregunta>();
		for (PreguntaRespuesta pregunta : listPreguntas) {
			add(pregunta);
		}
	}

	private void add(PreguntaRespuesta pregunta) {
		NodoPregunta nodo = new NodoPregunta(pregunta, context);
		for (NodoPregunta nodoDeLista : listNodos) {
			if (nodoDeLista.addHijo(nodo)) {
				listNodos.add(nodo);
				nodo.setNodoPadre(nodoDeLista);
				return;
			}
		}
		listNodos.add(nodo);
	}

	public NodoPregunta getNodo(int position) {
		return (listNodos != null && listNodos.size() > 0) ? listNodos
				.get(position) : null;
	}

	@Override
	public String toString() {
		String s = "";
		for (NodoPregunta nodo : listNodos) {
			s += nodo.toString() + "\n";
		}
		return s;
	}

}
