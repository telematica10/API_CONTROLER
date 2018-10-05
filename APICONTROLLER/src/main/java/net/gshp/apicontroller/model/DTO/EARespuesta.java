package net.gshp.apicontroller.model.DTO;

public class EARespuesta{
	public long ida;//Id autoincrementable
	public long idPregunta;
	public long idReporte;
	public long idReporteLocal;
	//////////////////
	public long idEncuesta;
	public String nombreEncuesta;
	//////////////////
	public String respuesta;
	public String hash;
	public int enviado;
	
	public int numeroEncuesta;
	
	
	//Dependiendo el proyecto se pueden utilizar estos 2 campos
	public String campoExtra1;
	public String campoExtra2;
	
	public String timeStamp;
}
