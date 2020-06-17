package net.tce.dto;

public class AreaPersonaDto extends ComunDto {

	private String idAreaPersona;
	private String idArea;
	private String idPersona;
	private String lbArea;
//	private String principal;
	private Boolean principal;
	private Boolean confirmada;
	private Boolean personal;
	
	private Boolean toDelete;
	
	public AreaPersonaDto() {
	}
	
	public AreaPersonaDto(Long idAreaPersona, Long idArea, Long idPersona) {
		this.idAreaPersona=idAreaPersona!=null?String.valueOf(idAreaPersona):null;
		this.idArea=idArea!=null?String.valueOf(idArea):null;
		this.idPersona=idPersona!=null?String.valueOf(idPersona):null;
	}
	
//	public AreaPersonaDto(String idArea, String principal){
//		this.idArea=idArea;
//		this.principal= principal!=null
//				?((principal.equals("true")||principal.equals("1"))?true:false)
//				:null; 
//	}
	
	public AreaPersonaDto(Long idAreaPersona, Long idArea, String descArea,
			Boolean bPrincipal, Boolean bConfirmada, Boolean bPersonal) {
		
		this.idAreaPersona=idAreaPersona!=null?String.valueOf(idAreaPersona):null;
		this.idArea=idArea!=null?String.valueOf(idArea):null;
		this.lbArea=descArea;
		this.principal=bPrincipal;
		this.confirmada=bConfirmada;
		this.personal=bPersonal;
	}
	
	public String getIdAreaPersona() {
		return idAreaPersona;
	}
	public void setIdAreaPersona(String idAreaPersona) {
		this.idAreaPersona = idAreaPersona;
	}
	public String getIdArea() {
		return idArea;
	}
	public void setIdArea(String idArea) {
		this.idArea = idArea;
	}
	public String getIdPersona() {
		return idPersona;
	}
	public void setIdPersona(String idPersona) {
		this.idPersona = idPersona;
	}

	public String getLbArea() {
		return lbArea;
	}

	public void setLbArea(String lbArea) {
		this.lbArea = lbArea;
	}

	public Boolean getPrincipal() {
		return principal;
	}

	public void setPrincipal(Boolean principal) {
		this.principal = principal;
	}

	public Boolean getConfirmada() {
		return confirmada;
	}

	public void setConfirmada(Boolean confirmada) {
		this.confirmada = confirmada;
	}	

	public Boolean getPersonal() {
		return personal;
	}

	public void setPersonal(Boolean personal) {
		this.personal = personal;
	}

	public Boolean getToDelete() {
		return toDelete;
	}

	public void setToDelete(Boolean toDelete) {
		this.toDelete = toDelete;
	}
}
