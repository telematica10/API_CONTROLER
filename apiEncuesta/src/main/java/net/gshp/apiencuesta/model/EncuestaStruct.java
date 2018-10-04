package net.gshp.apiencuesta.model;

import java.util.ArrayList;
import java.util.List;

import net.gshp.apiencuesta.model.DTO.PreguntaRespuesta;

public class EncuestaStruct {

	private String NombreEncuesta;
	// private List<SeccionStruct> listSeccion;
	// private List<PreguntaRespuesta> listPreguntaSinSeccion;

	private List<Object> elementos;

	List<PreguntaRespuesta> listPreguntaRespuesta;

	public EncuestaStruct(List<PreguntaRespuesta> listPreguntaRespuesta) {
		this.listPreguntaRespuesta = listPreguntaRespuesta;
		elementos = new ArrayList<Object>();
		if (listPreguntaRespuesta.size() > 0) {
			NombreEncuesta = listPreguntaRespuesta.get(0).nombreEncuesta;
		}
		// System.out.println("nombreEncuesta: " + NombreEncuesta);
		elementos.add(NombreEncuesta);
		generate();
	}

	private void generate() {
		for (PreguntaRespuesta pregunta : listPreguntaRespuesta) {
			if (pregunta.idSeccion == 0) {
				elementos.add(pregunta);
			} else {
				addToSeccion(pregunta);
			}
		}
		// generateSecciones();
	}

	// private void generateSecciones() {
	// for (Object o : elementos) {
	// Class c = o.getClass();
	// if (c.equals(SeccionStruct.class)) {
	// ((SeccionStruct) o).generate();
	// }
	// }
	// }

	private void addToSeccion(PreguntaRespuesta pregunta) {
		for (Object o : elementos) {
			Class c = o.getClass();
			if (c.equals(SeccionStruct.class)) {
				if (((SeccionStruct) o).getId() == pregunta.idSeccion) {
					((SeccionStruct) o).add(pregunta);
					return;
				}
			}
		}
		elementos.add(new SeccionStruct(pregunta));
	}

	public int getCount() {
		return (elementos != null) ? elementos.size() : 0;
	}

	public List<Object> getElementos() {
		return elementos;
	}

	public Object getElementAt(int position) {
		return elementos.get(position);
	}

	@Override
	public String toString() {
		String s = "Encuesta: ";
		for (Object o : elementos) {
			Class c = o.getClass();
			if (c.equals(String.class)) {
				s = s + ((String) o);
			}
			if (c.equals(SeccionStruct.class)) {
				s = s + "\n\t" + ((SeccionStruct) o).toString();
			}
			if (c.equals(PreguntaRespuesta.class)) {
				s = s + "\n\t" + ((PreguntaRespuesta) o).pregunta;
			}
		}
		return s;
	}

}
