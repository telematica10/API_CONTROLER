package net.gshp.apicontroller.model;

import java.util.ArrayList;
import java.util.List;

import net.gshp.apicontroller.model.DAO.DAOEncuestas;
import net.gshp.apicontroller.model.DTO.PreguntaRespuesta;

public class SeccionStruct {

	private String NombreSeccion;
//	private List<GrupoStruct> listGrupo;
//	private List<PreguntaRespuesta> listPregunta;

	private List<Object> elementos;

	private long id;

	public SeccionStruct(PreguntaRespuesta pregunta) {
		id = pregunta.idSeccion;
		elementos = new ArrayList<Object>();
		NombreSeccion = new DAOEncuestas().getSeccionByID(pregunta.idSeccion).nombre;
		elementos.add(NombreSeccion);
		add(pregunta);
	}

	public long getId() {
		return id;
	}

	public void add(PreguntaRespuesta pregunta) {
		if (pregunta.idGrupo == 0) {
			elementos.add(pregunta);
		} else {
			addToGrupo(pregunta);
		}
	}

//	public void generate() {
//		for (PreguntaRespuesta pregunta : listPregunta) {
//			if (pregunta.idGrupo == 0) {
//				elementos.add(pregunta);
//			} else {
//				addToGrupo(pregunta);
//			}
//		}
//	}

	private void addToGrupo(PreguntaRespuesta pregunta) {
		for (Object o : elementos) {
			Class c = o.getClass();
			if (c.equals(GrupoStruct.class)) {
				if (((GrupoStruct) o).getId() == pregunta.idGrupo) {
					((GrupoStruct) o).add(pregunta);
					return;
				}
			}
		}
		elementos.add(new GrupoStruct(pregunta));
	}

	public String getNombreSeccion() {
		return NombreSeccion;
	}
	
	public List<Object> getElementos() {
		return elementos;
	}
	
	public Object getElementAt(int position){
		return elementos.get(0);
	}
	
	@Override
	public String toString() {
		String s = "Seccion: ";
		for (Object o : elementos) {
			Class c = o.getClass();
			if (c.equals(String.class)) {
				s = s+((String)o);
			}
			if (c.equals(GrupoStruct.class)) {
				s = s+"\n\t\t"+((GrupoStruct)o).toString();
			}
			if (c.equals(PreguntaRespuesta.class)) {
				s = s+"\n\t\t"+((PreguntaRespuesta)o).pregunta;
			}
		}
		return s;
	}

}
