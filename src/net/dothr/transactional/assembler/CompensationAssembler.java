package net.dothr.transactional.assembler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.tce.dto.AutomovilDto;
import net.tce.dto.BonoDto;
import net.tce.dto.CompensacionDto;
import net.tce.dto.ContactInfoDto;
import net.tce.dto.PlanDto;
import net.tce.dto.ReferenceDto;
import net.tce.dto.SeguroDto;
import net.tce.dto.SueldoDto;
import net.tce.dto.VacacionesDto;
import net.tce.dto.ValeDto;
import net.utils.ClientRestUtily;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;

public class CompensationAssembler {
	
	static Logger log4j = Logger.getLogger( CompensationAssembler.class );
	private static Gson gson;
	
	public static JSONObject getJsonObject(String fileName, String fileDir) throws Exception {
		String stPersona = ClientRestUtily.getJsonFile(fileName, fileDir);
		JSONArray jsResp = new JSONArray(stPersona);
		return jsResp.getJSONObject(0);
	}
	
	/** 
	 * Carga de datos (Complementarios) al DTO a partir de un POJO de compensacion
	 * @param dto
	 * @param compensacion
	 * @return
	 */
	//public CompensacionDto getCompensacionDto(CompensacionDto dto, Compensacion compensacion){
	public static CompensacionDto getCompensacionDto(CompensacionDto dto, JSONObject jsonCompensa){
		if(dto==null){
			dto = new CompensacionDto();
		}
		JSONArray arrConts;
		JSONArray arrVales;
		JSONArray arrBonos;
		JSONArray arrPlans;
		JSONArray arrSeguros;
		JSONArray arrRefs;
		try{
			//Nombre
			if(jsonCompensa.has("nombreCompleto")){
				dto.setNombreCompleto(jsonCompensa.getString("nombreCompleto"));
			}
			//CURP
			if(jsonCompensa.has("curp")){
				dto.setCurp(jsonCompensa.getString("curp"));
			}
			//edad
			if(jsonCompensa.has("edad")){
				dto.setEdad(jsonCompensa.getString("edad"));
			}
			//fechaNacimiento
			if(jsonCompensa.has("fechaNacimiento")){
				dto.setFechaNacimiento(jsonCompensa.getString("fechaNacimiento"));
			}
			//hijos
			if(jsonCompensa.has("numeroHijos")){
				dto.setNumeroHijos(jsonCompensa.getString("numeroHijos"));
			}
			//idTipoGenero
			if(jsonCompensa.has("idTipoGenero")){
				dto.setIdTipoGenero(jsonCompensa.getString("idTipoGenero"));
				if(jsonCompensa.has("lbGenero")){
					dto.setLbGenero(jsonCompensa.getString("lbGenero"));
				}
			}
			//idEstadoCivil
			if(jsonCompensa.has("idEstadoCivil")){
				dto.setIdEstadoCivil(jsonCompensa.getString("idEstadoCivil"));
				if(jsonCompensa.has("lbEstadoCivil")){
					dto.setLbEstadoCivil(jsonCompensa.getString("lbEstadoCivil"));
				}
			}
			//idPais
			if(jsonCompensa.has("idPais")){
				dto.setIdPais(jsonCompensa.getString("idPais"));
				if(jsonCompensa.has("stPais")){
					dto.setStPais(jsonCompensa.getString("stPais"));
				}
			}

			//Contactos
			if(jsonCompensa.has("contactos")){
				arrConts = jsonCompensa.getJSONArray("contactos");
				log4j.debug("Contactos: " + arrConts );
				List<ContactInfoDto> lsContactoDto = new ArrayList<ContactInfoDto>();
			}
			//Vales
			if(jsonCompensa.has("vales")){
				arrVales = jsonCompensa.getJSONArray("vales");
				log4j.debug("Vales: " + arrVales );
			}
			//Bonos
			if(jsonCompensa.has("bonos")){
				arrBonos = jsonCompensa.getJSONArray("bonos");
				log4j.debug("Bonos: " + arrBonos );
			}
			//Plans
			if(jsonCompensa.has("plans")){
				arrPlans = jsonCompensa.getJSONArray("plans");
				log4j.debug("Plans: " + arrPlans );
			}
			//Plans
			if(jsonCompensa.has("seguros")){
				arrSeguros = jsonCompensa.getJSONArray("seguros");
				log4j.debug("Seguros: " + arrSeguros );
			}		
			//Referencias
			if(jsonCompensa.has("referencias")){
				arrRefs = jsonCompensa.getJSONArray("referencias");
				log4j.debug("Referencias: " + arrRefs );
				List<ReferenceDto> lsReferenciaDto = new ArrayList<ReferenceDto>();
			}
		}catch (Exception e){
			log4j.error("Error al convertir JSON ", e);
		}
		
		
		
//		/* B. Datos en lista vacias */
//		if(dto.getPlans()!=null && dto.getPlans().isEmpty()){
//			log4j.debug("<getCompensacionDto> dto.getPlans()!=null:" + dto.getPlans() );
//			dto.setPlans(null);
//		}
//		if(dto.getSeguros()!=null && dto.getSeguros().isEmpty()){
//			log4j.debug("<getCompensacionDto> dto.getSeguros()!=null:" + dto.getSeguros() );
//			dto.setSeguros(null);
//		}
//		if(dto.getVales()!=null && dto.getVales().isEmpty()){
//			log4j.debug("<getCompensacionDto> dto.getVales()!=null:" + dto.getVales() );
//			dto.setVales(null);
//		}
//		if(dto.getBonos()!=null && dto.getBonos().isEmpty()){
//			log4j.debug("<getCompensacionDto> dto.getBonos()!=null:" + dto.getBonos() );
//			dto.setBonos(null);
//		}
		return dto;
	}
	
	
	public static CompensacionDto getDto (JSONObject jsonCompensa){
		gson = new Gson();
		CompensacionDto dto = new CompensacionDto();
		dto = gson.fromJson(jsonCompensa.toString(), CompensacionDto.class);
		return dto;
	}

}
