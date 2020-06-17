package net.tce.dto;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class ConverterJsonDto {

	private static Logger log4j = Logger.getLogger( ConverterJsonDto.class );
	
	public static CurriculumDto cvDto(JSONObject jsonPersona){
		log4j.debug("<cvDto>");
		CurriculumDto dtoCV = new CurriculumDto();
		
		JSONObject jsonTmp = null;
		JSONArray jsArrTmp=null;

		try{
			if(jsonPersona!=null){
				
				/* 1. Datos de 1er nivel */
				dtoCV.setIdPersona(jsonPersona.has("idPersona")?jsonPersona.getString("idPersona"):null);
				dtoCV.setEmail(jsonPersona.has("email")?jsonPersona.getString("email"):null);
				dtoCV.setIdEmpresa(jsonPersona.has("idEmpresa")?jsonPersona.getString("idEmpresa"):null);
				dtoCV.setIdEmpresaConf(jsonPersona.has("idEmpresaConf")?jsonPersona.getString("idEmpresaConf"):"1");
				dtoCV.setIdEstatusInscripcion(jsonPersona.has("idEstatusInscripcion")?jsonPersona.getString("idEstatusInscripcion"):null);
				//(jsonPersona.has("estatus")?jsonPersona.getString("estatus"):null);
				dtoCV.setFechaCreacion(jsonPersona.has("fechaCreacion")?jsonPersona.getString("fechaCreacion"):null);
				dtoCV.setFechaModificacion(jsonPersona.has("fechaModificacion")?jsonPersona.getString("fechaModificacion"):null);
				dtoCV.setNombre(jsonPersona.has("nombre")?jsonPersona.getString("nombre"):null);
				dtoCV.setApellidoPaterno(jsonPersona.has("apellidoPaterno")?jsonPersona.getString("apellidoPaterno"):null);
				dtoCV.setApellidoMaterno(jsonPersona.has("apellidoMaterno")?jsonPersona.getString("apellidoMaterno"):null);
				dtoCV.setAnioNacimiento(jsonPersona.has("anioNacimiento")?jsonPersona.getString("anioNacimiento"):null);
				dtoCV.setMesNacimiento(jsonPersona.has("mesNacimiento")?jsonPersona.getString("mesNacimiento"):null);
				dtoCV.setDiaNacimiento(jsonPersona.has("diaNacimiento")?jsonPersona.getString("diaNacimiento"):null);
				dtoCV.setFechaNacimiento(jsonPersona.has("fechaNacimiento")?jsonPersona.getString("fechaNacimiento"):null);
				dtoCV.setEdad(jsonPersona.has("edad")?jsonPersona.getString("edad"):null);
				dtoCV.setIdTipoGenero(jsonPersona.has("idTipoGenero")?jsonPersona.getString("idTipoGenero"):null);
				dtoCV.setIdEstadoCivil(jsonPersona.has("idEstadoCivil")?jsonPersona.getString("idEstadoCivil"):null);
				dtoCV.setIdPais(jsonPersona.has("idPais")?jsonPersona.getString("idPais"):null);
				dtoCV.setPermisoTrabajo(jsonPersona.has("permisoTrabajo")?jsonPersona.getString("permisoTrabajo"):null);
				dtoCV.setCambioDomicilio(jsonPersona.has("cambioDomicilio")?jsonPersona.getString("cambioDomicilio"):null);
				dtoCV.setIdTipoDispViajar(jsonPersona.has("idTipoDispViajar")?jsonPersona.getString("idTipoDispViajar"):null);
				dtoCV.setDisponibilidadHorario(jsonPersona.has("disponibilidadHorario")?jsonPersona.getString("disponibilidadHorario"):null);
				dtoCV.setSalarioMin(jsonPersona.has("salarioMin")?jsonPersona.getString("salarioMin"):null);
				dtoCV.setSalarioMax(jsonPersona.has("salarioMax")?jsonPersona.getString("salarioMax"):null);
				dtoCV.setNombreCompleto(jsonPersona.has("nombreCompleto")?jsonPersona.getString("nombreCompleto"):null);
				dtoCV.setLbGenero(jsonPersona.has("lbGenero")?jsonPersona.getString("lbGenero"):null);
				dtoCV.setLbEstadoCivil(jsonPersona.has("lbEstadoCivil")?jsonPersona.getString("lbEstadoCivil"):null);
				dtoCV.setStPais(jsonPersona.has("stPais")?jsonPersona.getString("stPais"):null);
				dtoCV.setLbDispViajar(jsonPersona.has("lbDispViajar")?jsonPersona.getString("lbDispViajar"):null);
				
				dtoCV.setLbEstatusMax(jsonPersona.has("lbEstatusMax")?jsonPersona.getString("lbEstatusMax"):null);
				dtoCV.setLbGradoMax(jsonPersona.has("lbGradoMax")?jsonPersona.getString("lbGradoMax"):null);
				dtoCV.setDiasExperienciaLaboral(jsonPersona.has("diasExperienciaLaboral")?jsonPersona.getString("diasExperienciaLaboral"):null);
				dtoCV.setIdGradoAcademicoMax(jsonPersona.has("idGradoAcademicoMax")?jsonPersona.getString("idGradoAcademicoMax"):null);
				dtoCV.setIdEstatusEscolarMax(jsonPersona.has("idEstatusEscolarMax")?jsonPersona.getString("idEstatusEscolarMax"):null);
				dtoCV.setIdAmbitoGeografico(jsonPersona.has("idAmbitoGeografico")?jsonPersona.getString("idAmbitoGeografico"):null);
				
				/* 1.a Imagen de Perfil */
				if(jsonPersona.has("imgPerfil")){
					jsonTmp = jsonPersona.getJSONObject("imgPerfil");
					FileDto dto = new FileDto();
					dto.setIdContenido( jsonTmp.getString("idContenido") );
					dto.setFileDescripcion(jsonTmp.getString("fileDescripcion"));
				    dto.setIdTipoContenido(jsonTmp.getString("idTipoContenido"));
				    dto.setUrl(jsonTmp.getString("url"));
				    dtoCV.setImgPerfil(dto);
				    jsonTmp=null;
				}
				
				int x=0;
				/* 2. Datos de 2o Nivel (Relacionados) */
				if(jsonPersona.has("contacto")){
					jsArrTmp = jsonPersona.getJSONArray("contacto");
					List<ContactInfoDto> lsContacto = new ArrayList<ContactInfoDto>();
					for(x=0;x<jsArrTmp.length();x++){
						lsContacto.add(parseContacto(jsArrTmp.getJSONObject(x)));
					}
					dtoCV.setContacto(lsContacto);
				}
				if(jsonPersona.has("localizacion")){
					jsArrTmp = jsonPersona.getJSONArray("localizacion");
					List<LocationInfoDto> lsLocalizacion = new ArrayList<LocationInfoDto>();
					for(x=0;x<jsArrTmp.length();x++){
						lsLocalizacion.add(parseLocalizacion(jsArrTmp.getJSONObject(x)));
					}
					dtoCV.setLocalizacion(lsLocalizacion);
				}

				if(jsonPersona.has("escolaridad")){
					jsArrTmp = jsonPersona.getJSONArray("escolaridad");
					List<AcademicBackgroundDto> lsAcad = new ArrayList<AcademicBackgroundDto>();
					for(x=0;x<jsArrTmp.length();x++){
						lsAcad.add(parseAcad(jsArrTmp.getJSONObject(x)));
					}
					dtoCV.setEscolaridad(lsAcad);
				}
				
				if(jsonPersona.has("experienciaLaboral")){
					jsArrTmp = jsonPersona.getJSONArray("experienciaLaboral");
					List<WorkExperienceDto> lsWork = new ArrayList<WorkExperienceDto>();
					for(x=0;x<jsArrTmp.length();x++){
						lsWork.add(parseWork(jsArrTmp.getJSONObject(x)));
					}
					dtoCV.setExperienciaLaboral(lsWork);
				}
				
				if(jsonPersona.has("idioma")){
					jsArrTmp = jsonPersona.getJSONArray("idioma");
					List<IdiomaDto> lsIdioma = new ArrayList<IdiomaDto>();
					for(x=0;x<jsArrTmp.length();x++){
						lsIdioma.add(parseIdioma(jsArrTmp.getJSONObject(x)));
					}
					dtoCV.setIdioma(lsIdioma);
				}
				
				if(jsonPersona.has("certificacion")){
					jsArrTmp = jsonPersona.getJSONArray("certificacion");
					List<CertificacionDto> lsCert = new ArrayList<CertificacionDto>();
					for(x=0;x<jsArrTmp.length();x++){
						lsCert.add(parseCert(jsArrTmp.getJSONObject(x)));
					}
					dtoCV.setCertificacion(lsCert);
				}
				
				if(jsonPersona.has("habilidad")){
					jsArrTmp = jsonPersona.getJSONArray("habilidad");
					List<PersonSkillDto> lsHab = new ArrayList<PersonSkillDto>();
					for(x=0;x<jsArrTmp.length();x++){
						lsHab.add(parseHab(jsArrTmp.getJSONObject(x)));
					}
					dtoCV.setHabilidad(lsHab);
				}
				
				if(jsonPersona.has("areaPersona")){
					jsArrTmp = jsonPersona.getJSONArray("habilidad");
					List<AreaPersonaDto> lsArea = new ArrayList<AreaPersonaDto>();
					for(x=0;x<jsArrTmp.length();x++){
						lsArea.add(parseAreaP(jsArrTmp.getJSONObject(x)));
					}
					dtoCV.setAreaPersona(lsArea);
				}
			}
		}catch (Exception e){
			log4j.fatal("Error al procesar: "+e.getMessage(), e);
		}
		
		return dtoCV;
	}

	private static AreaPersonaDto parseAreaP(JSONObject jsonOb) {
		AreaPersonaDto dto = new AreaPersonaDto();
		try{
			dto.setIdEmpresaConf(jsonOb.has("idEmpresaConf")?jsonOb.getString("idEmpresaConf"):null);
			dto.setIdAreaPersona(jsonOb.has("idAreaPersona")?jsonOb.getString("idAreaPersona"):null );
			dto.setIdArea(jsonOb.has("idArea")?jsonOb.getString("idArea"):null );
			dto.setIdPersona(jsonOb.has("idPersona")?jsonOb.getString("idPersona"):null );
		}catch (Exception e){
			log4j.fatal(e);
		}
		return dto;
	}

	private static PersonSkillDto parseHab(JSONObject jsonOb) {
		PersonSkillDto dto = new PersonSkillDto();
		try{
			dto.setIdHabilidad(jsonOb.has("idHabilidad")?jsonOb.getString("idHabilidad"):null );
			dto.setDescripcion(jsonOb.has("descripcion")?jsonOb.getString("descripcion"):null );
			dto.setIdDominio(jsonOb.has("idDominio")?jsonOb.getString("idDominio"):null );
			dto.setLbDominio(jsonOb.has("lbDominio")?jsonOb.getString("lbDominio"):null );
			
			dto.setIdEmpresaConf(jsonOb.has("idEmpresaConf")?jsonOb.getString("idEmpresaConf"):null);
		}catch (Exception e){
			log4j.fatal(e);
		}
		return dto;
	}

	private static CertificacionDto parseCert(JSONObject jsonOb) {
		CertificacionDto dto = new CertificacionDto();
		try{
			dto.setIdCertificacion(jsonOb.has("idCertificacion")?jsonOb.getString("idCertificacion"):null);
			dto.setTituloCert(jsonOb.has("tituloCert")?jsonOb.getString("tituloCert"):null);
//			dto.setTerminado(jsonOb.has("terminado")?jsonOb.getString("terminado"):null);
			dto.setIdGrado(jsonOb.has("idGrado")?jsonOb.getString("idGrado"):null);
			dto.setLbGrado(jsonOb.has("lbGrado")?jsonOb.getString("lbGrado"):null);
			
			dto.setIdEmpresaConf(jsonOb.has("idEmpresaConf")?jsonOb.getString("idEmpresaConf"):null);
		}catch (Exception e){
			log4j.fatal(e);
		}
		return dto;
	}

	private static IdiomaDto parseIdioma(JSONObject jsonOb) {
		IdiomaDto dto = new IdiomaDto();
		try{
			dto.setIdIdioma(jsonOb.has("idIdioma")?jsonOb.getString("idIdioma"):null);
			dto.setLbIdioma(jsonOb.has("lbIdioma")?jsonOb.getString("lbIdioma"):null);
			dto.setIdDominio(jsonOb.has("idDominio")?jsonOb.getString("idDominio"):null);
			dto.setLbDominio(jsonOb.has("lbDominio")?jsonOb.getString("lbDominio"):null);
			dto.setIdPersona(jsonOb.has("idPersona")?jsonOb.getString("idPersona"):null);
			dto.setIdPersonaIdioma(jsonOb.has("idPersonaIdioma")?jsonOb.getString("idPersonaIdioma"):null);
			
			dto.setIdEmpresaConf(jsonOb.has("idEmpresaConf")?jsonOb.getString("idEmpresaConf"):null);
		}catch (Exception e){
			log4j.fatal(e);
		}
		return dto;
	}

	private static WorkExperienceDto parseWork(JSONObject jsonOb) {
		WorkExperienceDto dto = new WorkExperienceDto();
		try{
			dto.setIdExperienciaLaboral(jsonOb.has("idExperienciaLaboral")?jsonOb.getString("idExperienciaLaboral"):null);
			dto.setNombreEmpresa(jsonOb.has("nombreEmpresa")?jsonOb.getString("nombreEmpresa"):null);
			dto.setIdTipoJornada(jsonOb.has("idTipoJornada")?jsonOb.getString("idTipoJornada"):null);
			dto.setTexto(jsonOb.has("texto")?jsonOb.getString("texto"):null);
			dto.setMotivoSeparacion(jsonOb.has("motivoSeparacion")?jsonOb.getString("motivoSeparacion"):null);
			dto.setTextoFiltrado(jsonOb.has("textoFiltrado")?jsonOb.getString("textoFiltrado"):null);
			dto.setLbPais(jsonOb.has("lbPais")?jsonOb.getString("lbPais"):null);
			dto.setIdTipoContrato(jsonOb.has("idTipoContrato")?jsonOb.getString("idTipoContrato"):null);
			dto.setTrabajoActual(jsonOb.has("trabajoActual")?jsonOb.getString("trabajoActual"):null);
			dto.setLbNivelJerarquico(jsonOb.has("lbNivelJerarquico")?jsonOb.getString("lbNivelJerarquico"):null);
			dto.setIdNivelJerarquico(jsonOb.has("idNivelJerarquico")?jsonOb.getString("idNivelJerarquico"):null);
			dto.setMesFin(jsonOb.has("mesFin")?jsonOb.getString("mesFin"):null);
			dto.setGenteACargo(jsonOb.has("genteACargo")?jsonOb.getString("genteACargo"):null);
			dto.setAnioInicio(jsonOb.has("anioInicio")?jsonOb.getString("anioInicio"):null);
			dto.setDiaInicio(jsonOb.has("diaInicio")?jsonOb.getString("diaInicio"):null);
			dto.setMesInicio(jsonOb.has("mesInicio")?jsonOb.getString("mesInicio"):null);
			dto.setIdPais(jsonOb.has("idPais")?jsonOb.getString("idPais"):null);
			dto.setIdTipoPrestacion(jsonOb.has("idTipoPrestacion")?jsonOb.getString("idTipoPrestacion"):null);
			dto.setPuesto(jsonOb.has("puesto")?jsonOb.getString("puesto"):null);
			dto.setLbTipoJornada(jsonOb.has("lbTipoJornada")?jsonOb.getString("lbTipoJornada"):null);
			dto.setDiaFin(jsonOb.has("diaFin")?jsonOb.getString("diaFin"):null);
			dto.setAnioFin(jsonOb.has("anioFin")?jsonOb.getString("anioFin"):null);
			
			dto.setIdEmpresaConf(jsonOb.has("idEmpresaConf")?jsonOb.getString("idEmpresaConf"):null);

		}catch (Exception e){
			log4j.fatal(e);
		}
		return dto;
	}

	private static AcademicBackgroundDto parseAcad(JSONObject jsonOb) {
		AcademicBackgroundDto dto = new AcademicBackgroundDto();
		try{
			dto.setIdEscolaridad(jsonOb.has("idEscolaridad")?jsonOb.getString("idEscolaridad"):null);
			dto.setTexto(jsonOb.has("texto")?jsonOb.getString("texto"):null);
			dto.setTextoFiltrado(jsonOb.has("textoFiltrado")?jsonOb.getString("textoFiltrado"):null);
			dto.setIdEstatusEscolar(jsonOb.has("idEstatusEscolar")?jsonOb.getString("idEstatusEscolar"):null);
			dto.setLbPais(jsonOb.has("lbPais")?jsonOb.getString("lbPais"):null);
			dto.setMesFin(jsonOb.has("mesFin")?jsonOb.getString("mesFin"):null);
			dto.setTitulo(jsonOb.has("titulo")?jsonOb.getString("titulo"):null);
			dto.setAnioInicio(jsonOb.has("anioInicio")?jsonOb.getString("anioInicio"):null);
			dto.setIdPais(jsonOb.has("idPais")?jsonOb.getString("idPais"):null);
			dto.setMesInicio(jsonOb.has("mesInicio")?jsonOb.getString("mesInicio"):null);
			dto.setDiaInicio(jsonOb.has("diaInicio")?jsonOb.getString("diaInicio"):null);
			dto.setLbEstatus(jsonOb.has("lbEstatus")?jsonOb.getString("lbEstatus"):null);
			dto.setNombreInstitucion(jsonOb.has("nombreInstitucion")?jsonOb.getString("nombreInstitucion"):null);
			dto.setIdGradoAcademico(jsonOb.has("idGradoAcademico")?jsonOb.getString("idGradoAcademico"):null);
			dto.setDiaFin(jsonOb.has("diaFin")?jsonOb.getString("diaFin"):null);
			dto.setLbGrado(jsonOb.has("lbGrado")?jsonOb.getString("lbGrado"):null);
			dto.setAnioFin(jsonOb.has("anioFin")?jsonOb.getString("anioFin"):null);
			
			dto.setIdEmpresaConf(jsonOb.has("idEmpresaConf")?jsonOb.getString("idEmpresaConf"):null);
		}catch (Exception e){
			log4j.fatal(e);
		}
		return dto;
	}

	private static LocationInfoDto parseLocalizacion(JSONObject jsonOb) {
		LocationInfoDto dto = new LocationInfoDto();
		try{
			dto.setIdDomicilio(jsonOb.has("idDomicilio")?jsonOb.getString("idDomicilio"):null);
			dto.setCodigoPostal(jsonOb.has("codigoPostal")?jsonOb.getString("codigoPostal"):null);
			dto.setIdMunicipio(jsonOb.has("idMunicipio")?jsonOb.getString("idMunicipio"):null);
			dto.setStEstado(jsonOb.has("stEstado")?jsonOb.getString("stEstado"):null);
			dto.setIdDomicilio(jsonOb.has("idDomicilio")?jsonOb.getString("idDomicilio"):null);
			dto.setStColonia(jsonOb.has("stColonia")?jsonOb.getString("stColonia"):null);
			dto.setIdCodigoProceso(jsonOb.has("idCodigoProceso")?jsonOb.getString("idCodigoProceso"):null);
			dto.setGoogleLatitude(jsonOb.has("googleLatitude")?jsonOb.getString("googleLatitude"):null);
			dto.setGoogleLongitude(jsonOb.has("googleLongitude")?jsonOb.getString("googleLongitude"):null);
			dto.setStMunicipio(jsonOb.has("stMunicipio")?jsonOb.getString("stMunicipio"):null);
			dto.setIdTipoDomicilio(jsonOb.has("idTipoDomicilio")?jsonOb.getString("idTipoDomicilio"):null);
			dto.setNumeroExterior(jsonOb.has("numeroExterior")?jsonOb.getString("numeroExterior"):null);
			dto.setNumeroInterior(jsonOb.has("numeroInterior")?jsonOb.getString("numeroInterior"):null);
			dto.setIdAsentamiento(jsonOb.has("idAsentamiento")?jsonOb.getString("idAsentamiento"):null);
			dto.setIdPais(jsonOb.has("idPais")?jsonOb.getString("idPais"):null);
			dto.setStPais(jsonOb.has("stPais")?jsonOb.getString("stPais"):null);
			dto.setIdEstado(jsonOb.has("idEstado")?jsonOb.getString("idEstado"):null);			
			dto.setCalle(jsonOb.has("calle")?jsonOb.getString("calle"):null);
			
			dto.setIdEmpresaConf(jsonOb.has("idEmpresaConf")?jsonOb.getString("idEmpresaConf"):null);

		}catch (Exception e){
			log4j.fatal(e);
		}
		return dto;
	}

	private static ContactInfoDto parseContacto(JSONObject jsonOb) {
		ContactInfoDto dto = new ContactInfoDto();
		try{
			dto.setIdContacto(jsonOb.has("idContacto")?jsonOb.getString("idContacto"):null);
			dto.setIdTipoContacto(jsonOb.has("idTipoContacto")?jsonOb.getString("idTipoContacto"):null);
			dto.setCodigoArea(jsonOb.has("codigoArea")?jsonOb.getString("codigoArea"):null);
			dto.setNumero(jsonOb.has("numero")?jsonOb.getString("numero"):null);
			
			dto.setIdEmpresaConf(jsonOb.has("idEmpresaConf")?jsonOb.getString("idEmpresaConf"):null);
		}catch (Exception e){
			log4j.fatal(e);
		}
		
		return dto;
	}
}
