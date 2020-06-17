package net.tce.dto;


import javax.activation.DataHandler;

public class FileDto extends ComunDto{

	  private String idContenido;
	  private String idPersona;
	  private String idPosicion;
	  private String idEmpresa;
	  private String tipoArchivo;
	  private DataHandler dhContenido;
	  private String idTipoContenido;
	  private String fileDescripcion;
	  private String repParams;
	  private String url;

	  public FileDto(){}
	  public FileDto(String idEmpresaConf,String idPersona,String idEmpresa ,String idTipoContenido){
		   this.setIdEmpresaConf(idEmpresaConf);
		   this.idPersona=idPersona;
		   this.idTipoContenido=idTipoContenido;
		   this.idEmpresa=idEmpresa;
	   }	
	
	public void setTipoArchivo(String tipoArchivo) {
		this.tipoArchivo = tipoArchivo;
	}
	public String getTipoArchivo() {
		return tipoArchivo;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUrl() {
		return url;
	}
	
	public void setIdTipoContenido(String idTipoContenido) {
		this.idTipoContenido = idTipoContenido;
	}
	public String getIdTipoContenido() {
		return idTipoContenido;
	}
	
	public void setDhContenido(DataHandler dhContenido) {
		this.dhContenido = dhContenido;
	}
	public DataHandler getDhContenido() {
		return dhContenido;
	}
	public void setIdContenido(String idContenido) {
		this.idContenido = idContenido;
	}
	public String getIdContenido() {
		return idContenido;
	}
	public void setIdPersona(String idPersona) {
		this.idPersona = idPersona;
	}
	public String getIdPersona() {
		return idPersona;
	}
	public void setIdEmpresa(String idEmpresa) {
		this.idEmpresa = idEmpresa;
	}
	public String getIdEmpresa() {
		return idEmpresa;
	}
	
	
	
	public String getFileDescripcion() {
		return fileDescripcion;
	}
	public void setFileDescripcion(String fileDescripcion) {
		this.fileDescripcion = fileDescripcion;
	}
	/**
     * Override method para mostrar contenido del Dto
     */
    public String toString(){
    	return "idEmpresaConf: ".concat(getIdEmpresaConf()!=null?getIdEmpresaConf():"").concat("\n idContenido: ").concat(idContenido!=null?idContenido:"")
    	.concat("\n idTipoContenido: ").concat(idTipoContenido!=null?String.valueOf(idTipoContenido):"")
    	.concat("\n idPersona: ").concat(idPersona!=null?idPersona:"")
    	.concat("\n idEmpresa: ").concat(idEmpresa!=null?idEmpresa:"")
    	.concat("\n tipoArchivo: ").concat(tipoArchivo!=null?tipoArchivo:"")
    	.concat("\n url: ").concat(url!=null?url:"")
    	//.concat("\n descripcion: ").concat(desscripcion!=null?desscripcion:"")
    	
    	.concat("\n descripcion: ").concat(fileDescripcion!=null?fileDescripcion:"")
    	
    	.concat(getCode()!=null?"\n code: ".concat(getCode()):"")
    	.concat(getType()!=null?"\n type: ".concat(getType()):"")
    	.concat(getMessage()!=null?"\n Message: ".concat(getMessage()):"")
    	;
    }
	public String getIdPosicion() {
		return idPosicion;
	}
	public void setIdPosicion(String idPosicion) {
		this.idPosicion = idPosicion;
	}
	public String getRepParams() {
		return repParams;
	}
	public void setRepParams(String repParams) {
		this.repParams = repParams;
	}
	
}
