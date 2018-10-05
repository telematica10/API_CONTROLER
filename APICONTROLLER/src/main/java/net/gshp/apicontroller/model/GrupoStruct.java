package net.gshp.apicontroller.model;

import java.util.ArrayList;
import java.util.List;

import net.gshp.apicontroller.model.DAO.DAOEncuestas;
import net.gshp.apicontroller.model.DTO.PreguntaRespuesta;

public class GrupoStruct {
	private List<Object> elementos;
	private long id;
	private String nombreGrupo;

	public GrupoStruct(PreguntaRespuesta pregunta) {
		id = pregunta.idGrupo;
		elementos = new ArrayList<Object>();
		elementos.add(new DAOEncuestas().getGrupoByID(pregunta.idGrupo).nombre);
		add(pregunta);
	}

	public long getId() {
		return id;
	}

	public String getNombreGrupo() {
		return nombreGrupo;
	}

	public void add(PreguntaRespuesta pregunta) {
		elementos.add(pregunta);
	}
	
	public Object getElementAt(int position){
		return elementos.get(0);
	}
	
	public List<Object> getElementos(){
		return elementos;
	}

	@Override
	public String toString() {
		String s = "Grupo: ";
		for (Object o : elementos) {
			Class c = o.getClass();
			if (c.equals(String.class)) {
				s = s + ((String) o);
			}
			if (c.equals(PreguntaRespuesta.class)) {
				s = s + "\n\t\t\t" + ((PreguntaRespuesta) o).pregunta + "\n";
			}
		}
		return s;
	}

}