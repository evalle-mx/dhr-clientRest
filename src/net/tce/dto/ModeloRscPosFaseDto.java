package net.tce.dto;

public class ModeloRscPosFaseDto {

	private long idmodeloRscPosFase;
	private long idTrackingPostulante;
	private long idRelacionEmpresaPersona;
	
	public long getIdTrackingPostulante() {
		return idTrackingPostulante;
	}
	public void setIdTrackingPostulante(long idTrackingPostulante) {
		this.idTrackingPostulante = idTrackingPostulante;
	}
	public long getIdmodeloRscPosFase() {
		return idmodeloRscPosFase;
	}
	public void setIdmodeloRscPosFase(long idmodeloRscPosFase) {
		this.idmodeloRscPosFase = idmodeloRscPosFase;
	}
	public long getIdRelacionEmpresaPersona() {
		return idRelacionEmpresaPersona;
	}
	public void setIdRelacionEmpresaPersona(long idRelacionEmpresaPersona) {
		this.idRelacionEmpresaPersona = idRelacionEmpresaPersona;
	}
}
