package net.tce.dto;

import java.util.Date;

public class ModeloRscPosicionFaseDto {

	private Long idMonitor;
	private Long idModeloRscPosFase;
	private Long idTrackingPostulante;
	private Long idRelacionEmpresaPersona;
	private Long idEstadoTracking;
	private String comentario;
	private Date fechaInicio;
	private Date fechaFin;
	
	public ModeloRscPosicionFaseDto(){}
	
	public ModeloRscPosicionFaseDto(Long idModeloRscPosFase,Long idTrackingPostulante,Long idRelacionEmpresaPersona,
			Long idEstadoTracking,String comentario,Date fechaInicio,Date fechaFin){
		this.idModeloRscPosFase=idModeloRscPosFase;
		this.idTrackingPostulante=idTrackingPostulante;
		this.idRelacionEmpresaPersona=idRelacionEmpresaPersona;
		this.idEstadoTracking=idEstadoTracking;
		this.comentario=comentario;
		this.fechaInicio=fechaInicio;
		this.fechaFin=fechaFin;
	}
	
	public ModeloRscPosicionFaseDto(Long idModeloRscPosFase){
		this.idModeloRscPosFase=idModeloRscPosFase;
	}
	
	public Long getIdTrackingPostulante() {
		return idTrackingPostulante;
	}
	public void setIdTrackingPostulante(Long idTrackingPostulante) {
		this.idTrackingPostulante = idTrackingPostulante;
	}
	public Long getIdModeloRscPosicionFase() {
		return idModeloRscPosFase;
	}
	public void setIdModeloRscPosicionFase(Long idModeloRscPosFase) {
		this.idModeloRscPosFase = idModeloRscPosFase;
	}
	
	public Long getIdEstadoTracking() {
		return idEstadoTracking;
	}

	public void setIdEstadoTracking(Long idEstadoTracking) {
		this.idEstadoTracking = idEstadoTracking;
	}
	
	public Long getIdRelacionEmpresaPersona() {
		return idRelacionEmpresaPersona;
	}
	public void setIdRelacionEmpresaPersona(Long idRelacionEmpresaPersona) {
		this.idRelacionEmpresaPersona = idRelacionEmpresaPersona;
	}
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	public Date getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public Date getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
	public Long getIdMonitor() {
		return idMonitor;
	}
	public void setIdMonitor(Long idMonitor) {
		this.idMonitor = idMonitor;
	}
}
