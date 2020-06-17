package net.tce.dto;

import java.util.List;

public class CandidatoTrackingDto extends ComunDto {

	private String idCandidato;
	private String idPostulante;
	private String idPersona;
	private String idEsquemaPerfilPosicion;
	private String idRelacionEmpresaPersona;
	private String idPerfilPosicion;
	private String idPosibleCandidato;
	private String idTipoPosibleCandidato;
	private String ordenTrackDefaultCumplido;
	private String comentario;
	private String email;
	private String nombre;
	private String apellidoPaterno;
	private String apellidoMaterno;
	private String orden;
	private String tipoPostulante;
	private boolean error=false;
	private String confirmado;
	private String pwsCad;
	private List<TrackingEsquemaDto> lsTrackingEsquemaDto;
	
	public String getIdCandidato() {
		return idCandidato;
	}
	public void setIdCandidato(String idCandidato) {
		this.idCandidato = idCandidato;
	}
	
	public String getIdEsquemaPerfilPosicion() {
		return idEsquemaPerfilPosicion;
	}
	public void setIdEsquemaPerfilPosicion(String idEsquemaPerfilPosicion) {
		this.idEsquemaPerfilPosicion = idEsquemaPerfilPosicion;
	}
	public String getIdRelacionEmpresaPersona() {
		return idRelacionEmpresaPersona;
	}
	public void setIdRelacionEmpresaPersona(String idRelacionEmpresaPersona) {
		this.idRelacionEmpresaPersona = idRelacionEmpresaPersona;
	}
	public String getIdPerfilPosicion() {
		return idPerfilPosicion;
	}
	public void setIdPerfilPosicion(String idPerfilPosicion) {
		this.idPerfilPosicion = idPerfilPosicion;
	}
	public List<TrackingEsquemaDto> getLsTrackingEsquemaDto() {
		return lsTrackingEsquemaDto;
	}
	public void setLsTrackingEsquemaDto(List<TrackingEsquemaDto> lsTrackingEsquemaDto) {
		this.lsTrackingEsquemaDto = lsTrackingEsquemaDto;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidoPaterno() {
		return apellidoPaterno;
	}

	public void setApellidoPaterno(String apellidoPaterno) {
		this.apellidoPaterno = apellidoPaterno;
	}

	public String getApellidoMaterno() {
		return apellidoMaterno;
	}

	public void setApellidoMaterno(String apellidoMaterno) {
		this.apellidoMaterno = apellidoMaterno;
	}
	
	public String getOrden() {
		return orden;
	}
	public void setOrden(String orden) {
		this.orden = orden;
	}
	public String getIdPosibleCandidato() {
		return idPosibleCandidato;
	}
	public void setIdPosibleCandidato(String idPosibleCandidato) {
		this.idPosibleCandidato = idPosibleCandidato;
	}
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	public String getTipoPostulante() {
		return tipoPostulante;
	}
	public void setTipoPostulante(String tipoPostulante) {
		this.tipoPostulante = tipoPostulante;
	}
	public String getIdPersona() {
		return idPersona;
	}
	public void setIdPersona(String idPersona) {
		this.idPersona = idPersona;
	}
	public String getIdPostulante() {
		return idPostulante;
	}
	public void setIdPostulante(String idPostulante) {
		this.idPostulante = idPostulante;
	}
	public boolean isError() {
		return error;
	}
	public void setError(boolean error) {
		this.error = error;
	}
	public String getConfirmado() {
		return confirmado;
	}
	public void setConfirmado(String confirmado) {
		this.confirmado = confirmado;
	}
	public String getPwsCad() {
		return pwsCad;
	}
	public void setPwsCad(String pwsCad) {
		this.pwsCad = pwsCad;
	}
	public String getIdTipoPosibleCandidato() {
		return idTipoPosibleCandidato;
	}
	public void setIdTipoPosibleCandidato(String idTipoPosibleCandidato) {
		this.idTipoPosibleCandidato = idTipoPosibleCandidato;
	}
	public String getOrdenTrackDefaultCumplido() {
		return ordenTrackDefaultCumplido;
	}
	public void setOrdenTrackDefaultCumplido(String ordenTrackDefaultCumplido) {
		this.ordenTrackDefaultCumplido = ordenTrackDefaultCumplido;
	}
}
