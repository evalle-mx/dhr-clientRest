package net.tce.dto;

import java.util.List;

public class TrackingPostulanteDto extends ModeloRscDto {

	private String idTrackingPostulante;
	private String idTrackingMonitor;
//	private String idModeloRscPosFase;
//	private String idPosibleCandidato;
	private String idEstadoTracking;
	private String comentario;
	private String ordenTrackDefaultCumplido;
	private List<TrackingMonitorDto> monitores;
	private List<CandidatoTrackingDto> candidatos;
	private List<PosibleCandidatoDto> lsPosibleCandidatoDto;
	
	private List<TrackingPostulanteDto> lsTracking;
	
	
	public String getIdTrackingPostulante() {
		return idTrackingPostulante;
	}
	public void setIdTrackingPostulante(String idTrackingPostulante) {
		this.idTrackingPostulante = idTrackingPostulante;
	}

	public String getIdEstadoTracking() {
		return idEstadoTracking;
	}
	public void setIdEstadoTracking(String idEstadoTracking) {
		this.idEstadoTracking = idEstadoTracking;
	}
	
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	public List<TrackingMonitorDto> getMonitores() {
		return monitores;
	}
	public void setMonitores(List<TrackingMonitorDto> monitores) {
		this.monitores = monitores;
	}
	public String getOrdenTrackDefaultCumplido() {
		return ordenTrackDefaultCumplido;
	}
	public void setOrdenTrackDefaultCumplido(String ordenTrackDefaultCumplido) {
		this.ordenTrackDefaultCumplido = ordenTrackDefaultCumplido;
	}
	public List<CandidatoTrackingDto> getCandidatos() {
		return candidatos;
	}
	public void setCandidatos(List<CandidatoTrackingDto> candidatos) {
		this.candidatos = candidatos;
	}
	public List<PosibleCandidatoDto> getLsPosibleCandidatoDto() {
		return lsPosibleCandidatoDto;
	}
	public void setLsPosibleCandidatoDto(List<PosibleCandidatoDto> lsPosibleCandidatoDto) {
		this.lsPosibleCandidatoDto = lsPosibleCandidatoDto;
	}
	public String getIdTrackingMonitor() {
		return idTrackingMonitor;
	}
	public void setIdTrackingMonitor(String idTrackingMonitor) {
		this.idTrackingMonitor = idTrackingMonitor;
	}
	public List<TrackingPostulanteDto> getLsTracking() {
		return lsTracking;
	}
	public void setLsTracking(List<TrackingPostulanteDto> lsTracking) {
		this.lsTracking = lsTracking;
	}
}
