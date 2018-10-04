package net.gshp.apiencuesta.model.DTO;

public class EAPregunta {
	public long id;
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
	public String operadorDependencia;//<,>,<=,>=,<>, BETWEEN
	public String valorDependencia1;
	public String valorDependencia2;
	
	//Las opciones de la pregunta podrían ser dependientes de lo que se responda en el padre o de algun catalogo en base
	public String queryOpcionesDependencia;
	public String queryOpciones;
	
	//La visibilidad de la pregunta podría depender de una query aleatoria
	public String queryVisibility;
}
