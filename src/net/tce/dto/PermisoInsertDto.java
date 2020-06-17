package net.tce.dto;

import java.util.List;

public class PermisoInsertDto extends ComunDto {
	
	private String idPermiso;
	private String contexto;
	private String valor;
	private String solicitante;
	private String contratante;
	private String administrador;
	//private String adminDothr;
	private String descripcion;
	private String idTipoPermiso;
	
	public PermisoInsertDto() {
	}
	
	public PermisoInsertDto(List<String> items) {
		
		this.idPermiso=items.get(0);
		this.contexto=items.get(1);
		this.valor=items.get(2);
		this.solicitante=items.get(3);
		this.contratante=items.get(4);
		this.administrador=items.get(5);
		this.descripcion=items.get(6);
		this.idTipoPermiso=items.get(7);
		//this.adminDothr=null;
		
	}

	public String getIdPermiso() {
		return idPermiso;
	}

	public void setIdPermiso(String idPermiso) {
		this.idPermiso = idPermiso;
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

	public String getSolicitante() {
		return solicitante;
	}

	public void setSolicitante(String solicitante) {
		this.solicitante = solicitante;
	}

	public String getContratante() {
		return contratante;
	}

	public void setContratante(String contratante) {
		this.contratante = contratante;
	}

	public String getAdministrador() {
		return administrador;
	}

	public void setAdministrador(String administrador) {
		this.administrador = administrador;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getIdTipoPermiso() {
		return idTipoPermiso;
	}

	public void setIdTipoPermiso(String idTipoPermiso) {
		this.idTipoPermiso = idTipoPermiso;
	}

}
