package oneclick.ticket.dto;

import java.util.List;

public class CatalogoDto {

	private String nombre;
	private String llave;
	private String valor;
	private String descripcion;
	private List<CatalogoDto> lista;
	private List<String> lsNombre;	
	
	public CatalogoDto() { }	
	
	public CatalogoDto(String llave, String valor) { 
		this.llave=llave;
		this.valor=valor;
	}
	
	public CatalogoDto(Long idCat, String valor) { 
		this.llave=idCat!=null?String.valueOf(idCat):null;
		this.valor=valor;
	}
	public CatalogoDto(Long idCat, String valor, String descripcion) { 
		this.llave=idCat!=null?String.valueOf(idCat):null;
		this.valor=valor;
		this.descripcion=descripcion;
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getLlave() {
		return llave;
	}
	public void setLlave(String llave) {
		this.llave = llave;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public List<CatalogoDto> getLista() {
		return lista;
	}
	public void setLista(List<CatalogoDto> lista) {
		this.lista = lista;
	}
	public List<String> getLsNombre() {
		return lsNombre;
	}
	public void setLsNombre(List<String> lsNombre) {
		this.lsNombre = lsNombre;
	}
}
