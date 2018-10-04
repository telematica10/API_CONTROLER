package com.alphasoluciones.antonio.api_controler.dto;

public class DTOReportRespuesta {
	
	private int id;
	private String answer;
	private long time_taked;
	private String hashed;
	private long report;
	private int poll;
	private int question;
	private int poll_number;
	private long id_reporte_local;
	
	
	public long getId_reporte_local() {
		return id_reporte_local;
	}
	public void setId_reporte_local(long id_reporte_local) {
		this.id_reporte_local = id_reporte_local;
	}
	public int getPoll_number() {
		return poll_number;
	}
	public void setPoll_number(int poll_number) {
		this.poll_number = poll_number;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAnswer() {
		   return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public long getTime_taked() {
		return time_taked;
	}
	public void setTime_taked(long time_taked) {
		this.time_taked = time_taked;
	}
	public String getHashed() {
		return hashed;
	}
	public void setHashed(String hashed) {
		this.hashed = hashed;
	}
	public long getReport() {
		return report;
	}
	public void setReport(long report) {
		this.report = report;
	}
	public int getPoll() {
		return poll;
	}
	public void setPoll(int poll) {
		this.poll = poll;
	}
	public int getQuestion() {
		return question;
	}
	public void setQuestion(int question) {
		this.question = question;
	}
}
