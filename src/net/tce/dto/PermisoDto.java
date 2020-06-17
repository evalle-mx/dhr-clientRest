package net.tce.dto;

public class PermisoDto extends ComunDto{

	private Long idPermiso;
	private Long idPermisoRelacionada;
	private String contexto;
	private String valor;
	private String idTipoPermiso;
	
	/* para servicio de Permisos y Roles */
	private String idTipoRelacion;
	private String idTipoRelacionPermiso;
//	private String activo;
	private Boolean activo;
	private String descripcion;
	
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public PermisoDto(){}
	
	public PermisoDto(String contexto,String valor,Byte idTipoPermiso){
		this.contexto=contexto;
		this.valor=valor;
		this.setIdTipoPermiso(idTipoPermiso.toString());
	}
	
	public PermisoDto(Long idPermiso, Long idPermisoRelacionada,String contexto,String valor,Byte idTipoPermiso){
		this.idPermiso=idPermiso;
		this.idPermisoRelacionada=idPermisoRelacionada;
		this.contexto=contexto;
		this.valor=valor;
		this.setIdTipoPermiso(idTipoPermiso.toString());
	}
	
	public PermisoDto(Long idPermiso, Long idPermisoRelacionada, Long idTipoRelacionPermiso, String contexto,String valor,Byte idTipoPermiso,
			Long idTipoRelacion, Boolean activo){
		this.idPermiso=idPermiso;
		this.idPermisoRelacionada=idPermisoRelacionada;
		this.idTipoRelacionPermiso=idTipoRelacionPermiso!=null?String.valueOf(idTipoRelacionPermiso):null;
		this.contexto=contexto;
		this.valor=valor;
		this.setIdTipoPermiso(idTipoPermiso.toString());
		this.idTipoRelacion=idTipoRelacion!=null?String.valueOf(idTipoRelacion):null;
		this.activo=activo;
				//activo!=null?String.valueOf(activo):null;
	}
	
	public PermisoDto(Long idPermiso, Long idPermisoRelacionada, Long idTipoRelacionPermiso, String contexto,String valor, Byte idTipoPermiso,
			Long idTipoRelacion, Boolean activo, String descripcion){
		this.idPermiso=idPermiso;
		this.idPermisoRelacionada=idPermisoRelacionada;
		this.idTipoRelacionPermiso=idTipoRelacionPermiso!=null?String.valueOf(idTipoRelacionPermiso):null;
		this.contexto=contexto;
		this.valor=valor;
		this.setIdTipoPermiso(idTipoPermiso.toString());
		this.idTipoRelacion=idTipoRelacion!=null?String.valueOf(idTipoRelacion):null;
		this.activo=activo;
		this.descripcion=descripcion;
				//activo!=null?String.valueOf(activo):null;
	}
	
	public String getContexto() {
		return contexto;
	}
	public void setContexto(String contexto) {
		this.contexto = contexto;
	}
	
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getIdTipoPermiso() {
		return idTipoPermiso;
	}

	public void setIdTipoPermiso(String idTipoPermiso) {
		this.idTipoPermiso = idTipoPermiso;
	}

	public Long getIdPermiso() {
		return idPermiso;
	}

	public void setIdPermiso(Long idPermiso) {
		this.idPermiso = idPermiso;
	}

	public Long getIdPermisoRelacionada() {
		return idPermisoRelacionada;
	}

	public void setIdPermisoRelacionada(Long idPermisoRelacionada) {
		this.idPermisoRelacionada = idPermisoRelacionada;
	}

	public String getIdTipoRelacion() {
		return idTipoRelacion;
	}

	public void setIdTipoRelacion(String idTipoRelacion) {
		this.idTipoRelacion = idTipoRelacion;
	}

//	public String getActivo() {
//		return activo;
//	}
//
//	public void setActivo(String activo) {
//		this.activo = activo;
//	}

	public String getIdTipoRelacionPermiso() {
		return idTipoRelacionPermiso;
	}

	public void setIdTipoRelacionPermiso(String idTipoRelacionPermiso) {
		this.idTipoRelacionPermiso = idTipoRelacionPermiso;
	}

	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	
	
	
}
