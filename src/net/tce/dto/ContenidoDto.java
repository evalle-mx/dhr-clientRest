package net.tce.dto;

public class ContenidoDto extends ComunDto {

	private String idContenido;
	private String idPersona;
	private String idEmpresa;
	private String idTrackingMonitor;
	private String idTrackingPostulante;
	private String idTipoContenidoArchivo;	
	private String descripcion;
	
	private String fechaCarga;	
	private String url;	
	
	public ContenidoDto() {
	}
	
	public ContenidoDto(Long idContenido, String descripcion) {
		this.idContenido=idContenido!=null?String.valueOf(idContenido):null;
		this.descripcion=descripcion;
	}
	
	/* ****************** */
	public String getIdContenido() {
		return idContenido;
	}
	public void setIdContenido(String idContenido) {
		this.idContenido = idContenido;
	}
	public String getIdPersona() {
		return idPersona;
	}
	public void setIdPersona(String idPersona) {
		this.idPersona = idPersona;
	}
	public String getIdEmpresa() {
		return idEmpresa;
	}
	public void setIdEmpresa(String idEmpresa) {
		this.idEmpresa = idEmpresa;
	}
	public String getIdTrackingMonitor() {
		return idTrackingMonitor;
	}
	public void setIdTrackingMonitor(String idTrackingMonitor) {
		this.idTrackingMonitor = idTrackingMonitor;
	}
	public String getIdTrackingPostulante() {
		return idTrackingPostulante;
	}
	public void setIdTrackingPostulante(String idTrackingPostulante) {
		this.idTrackingPostulante = idTrackingPostulante;
	}
	public String getIdTipoContenidoArchivo() {
		return idTipoContenidoArchivo;
	}
	public void setIdTipoContenidoArchivo(String idTipoContenidoArchivo) {
		this.idTipoContenidoArchivo = idTipoContenidoArchivo;
	}
	public String getFechaCarga() {
		return fechaCarga;
	}
	public void setFechaCarga(String fechaCarga) {
		this.fechaCarga = fechaCarga;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}	
}
