package net.dothr.transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.tce.dto.AreaPersonaDto;
import net.utils.ClientRestUtily;

import org.json.JSONArray;
import org.json.JSONObject;

public class AreaPersonaTester extends CurriculumTester {

	private static final String pathInsertaArea = "/home/dothr/workspace/ClientRest/files/out/areaPersonaPrueba.txt";
	
	public static void main(String[] args) {
		try {
//			processAreaPersona();
//			empataListas();
//			insertaMultipleAreaPersona();
			insertaAreaPersonaPrueba();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void insertaMultipleAreaPersona() throws Exception{
		int iInicial = 10, iFinal = 184;
		JSONObject jsonReq, jsonArea;
		JSONArray jsArrAreas;
		StringBuilder sb = new StringBuilder();
		for(int x=iInicial;x<=iFinal;x++){
			jsonReq = new JSONObject();
			jsonArea = new JSONObject();
			jsArrAreas = new JSONArray();
			jsonReq.put("idEmpresaConf", IDCONF_DOTHR );
			jsonReq.put("idPersona", String.valueOf(x));
			//Por cada area
				jsonArea.put("principal", "true");
				jsonArea.put("idArea", "18");
				jsArrAreas.put(jsonArea);
			jsonReq.put("areaPersona", jsArrAreas );
			sb.append("\n==> Json: ").append(jsonReq.toString())
			.append("\n==> URI: ").append(URI_CURRICULUM+URI_UPDATE);
			
			sb.append("\n<== response:\n").
			append(getJsonFromService(jsonReq.toString(), URI_CURRICULUM+URI_UPDATE)).append("\n");
		}
	}
	
	public static void insertaAreaPersonaPrueba() throws Exception{
		log4j.debug("<insertaAreaPersonaPrueba> ");
		List<String> lsAreaP = 
				ClientRestUtily.readListaTxt("/home/dothr/workspace/ClientRest/files/areaPersona.csv");
		StringBuilder sb = new StringBuilder();
		
		if(!lsAreaP.isEmpty()){			
			log4j.debug("<insertaAreaPersonaPrueba> total a insertar: " + lsAreaP.size());
			String lineaAp, idPersona, idArea;
			Iterator<String> itAreaP = lsAreaP.iterator();
			List<String> tokens;
			JSONObject jsonReq, jsonArea;
			JSONArray jsArrAreas;
			
			while(itAreaP.hasNext()){
				lineaAp = itAreaP.next();
				log4j.debug("lineaAp: " + lineaAp );
				tokens = Arrays.asList(lineaAp.split("\\s*;\\s*"));
				log4j.debug("tokens: " + tokens );
				idPersona = tokens.get(0);
				idArea = tokens.get(1);
				
				jsonReq = new JSONObject();
				jsonArea = new JSONObject();
				jsArrAreas = new JSONArray();
				jsonReq.put("idEmpresaConf", IDCONF_DOTHR );
				jsonReq.put("idPersona", idPersona);
				//Por cada area
					jsonArea.put("principal", "true");
					jsonArea.put("idArea", idArea);
					jsArrAreas.put(jsonArea);
				jsonReq.put("areaPersona", jsArrAreas );
				log4j.debug("<insertaAreaPersonaPrueba> Insertando " + idArea + " a "+tokens.get(2));
				sb.append("\n==> Json: ").append(jsonReq.toString())
				.append("\n==> URI: ").append(URI_CURRICULUM+URI_UPDATE);
				
				sb.append("\n<== response:\n").
				append(getJsonFromService(jsonReq.toString(), URI_CURRICULUM+URI_UPDATE)).append("\n");
			
			}
			
		}else{
			sb.append("<insertaAreaPersonaPrueba>No hay nada por insertar ");
		}
		ClientRestUtily.writeStringInFile(pathInsertaArea, sb.toString(), false);
		log4j.debug("<insertaAreaPersonaPrueba> Se Creo el archivo : "+pathInsertaArea);
	}
	
	
	/**
	 * Metodo para integrar listado de AreasPersona a partir de los nuevos (UI) y los almacenados en BD (Modelo) 
	 * @param lsNuevos
	 * @param lsModelo
	 * @return
	 */
	private static List<AreaPersonaDto> mergedList(List<AreaPersonaDto> lsNuevos, List<AreaPersonaDto> lsModelo){
		if(lsNuevos.size()>0){
			if(lsModelo.size()<1){
				//finalList.addAll(lsNuevos);
				log4j.debug("\n NO HAY DATOS EN BD, TODOS SON CREATE");
			}
			else{//Si hay datos guardados en BD (lsModelo)
				log4j.debug("\n HAY DATOS EN BD, SE COMBINAN DATOS DE ENTRADA CON BD...");
				AreaPersonaDto dtoMod;
				List<String> lsIdsAp = new ArrayList<String>();
				Iterator<AreaPersonaDto> itNuevos;
				Iterator<AreaPersonaDto> itMod;
				
				//1 Se obtienen los ID's de los elementos susceptibles a update (Solo personales)
				itMod = lsModelo.iterator();
				while(itMod.hasNext()){
					dtoMod = itMod.next();
					// Solo si es personal y no está confirmada, se utiliza para update
					if(dtoMod.getPersonal() && !dtoMod.getConfirmada()){
						lsIdsAp.add(dtoMod.getIdAreaPersona());
					}
					
				}
				
				//2.Se insertan Id's Modelo disponibles en Dto's nuevos
				//a) son iguales (se agregan uno a uno y solo son UPDATES)
				if(lsNuevos.size()==lsIdsAp.size()){
					log4j.debug("a) son iguales (se agregan uno a uno y solo son UPDATES)");
					int indx = 0;
					itNuevos = lsNuevos.iterator();
					while(itNuevos.hasNext()){
						itNuevos.next().setIdAreaPersona(lsIdsAp.get(indx++));
					}
				}
				//b) Nuevos son Más que los disponibles (UPDATE's + CREATE's)
				else if(lsNuevos.size()>lsIdsAp.size()){
					log4j.debug("b) Nuevos son Más que los disponibles (UPDATE's + CREATE's)");
					int indx = 0;
					itNuevos = lsNuevos.iterator();
					while(itNuevos.hasNext() && (indx<lsIdsAp.size())){
						itNuevos.next().setIdAreaPersona(lsIdsAp.get(indx++));
					}
				}
				//c) Nuevos son Menos que disponibles (UPDATE's + DELETE's)
				else if(lsNuevos.size()<lsIdsAp.size()){
					int indx = 0;
					itNuevos = lsNuevos.iterator();
					log4j.debug("c) Nuevos son Menos que disponibles (UPDATE's + DELETE's) ");
					while(itNuevos.hasNext()){
						itNuevos.next().setIdAreaPersona(lsIdsAp.get(indx++));
					}
					
					//C.1) Agregar eliminables a finalList
					for(;indx<lsIdsAp.size();indx++){
						//L-iAreaPersona, L-iArea, L-iPersona, S-lbArea, B-principal, B-confirmada, B-personal
						AreaPersonaDto delDto = new AreaPersonaDto();
						delDto.setIdAreaPersona(lsIdsAp.get(indx));
						delDto.setToDelete(true);
						lsNuevos.add( delDto );
					}
				}
			}
		}
		return lsNuevos;
	}
	
	protected static void empataListas() {
		List<AreaPersonaDto> lsNuevos, lsModelo, lsFinal;
		Long idPersona = new Long(2);
		StringBuilder sb = new StringBuilder();
		
		lsNuevos=new ArrayList<AreaPersonaDto>();
		//L-iAreaPersona, L-iArea, L-iPersona, S-lbArea, B-principal, B-confirmada, B-personal
		lsNuevos.add(new AreaPersonaDto(null,new Long(18), null,true,null,null) );
//		lsNuevos.add(new AreaPersonaDto(null,new Long(5), null,null,null,null,null) );
//		lsNuevos.add(new AreaPersonaDto(null,new Long(6), null,null,null,null,null) );
		
		
		lsModelo=new ArrayList<AreaPersonaDto>();
//		/* B: hay Datos guardados (No confirmados por Admin)
		lsModelo.add(new AreaPersonaDto(new Long(1),new Long(21), "Tecnologías de Información (TI)",false,false,true) );
		lsModelo.add(new AreaPersonaDto(new Long(2),new Long(22), "Ventas",false,false,true) );
		lsModelo.add(new AreaPersonaDto(new Long(3),new Long(11), "Administración",true,false,true) ); //*/
		
		
		lsFinal = mergedList(lsNuevos, lsModelo);
		
		
		if(lsFinal.size()>0){
			log4j.debug("Elementos a procesar: "+ lsFinal.size());
			sb = new StringBuilder();
			
			for (AreaPersonaDto apDto : lsFinal)
			{
				sb.append(apDto.getIdArea()!=null?" idArea: "+apDto.getIdArea()+",":"")
					.append(apDto.getPrincipal()!=null?" principal: "+apDto.getPrincipal()+",":"")
			    	.append(apDto.getLbArea()!=null?" descripcion: "+apDto.getLbArea()+",":"")
			    	.append(apDto.getIdAreaPersona()!=null?" idAreaPersona: "+apDto.getIdAreaPersona():"")
			    	.append(
			    			apDto.getIdAreaPersona()!=null?
			    					(apDto.getToDelete()!=null && apDto.getToDelete()?", ==> DELETE":" ==> UPDATE" )
			    					:" ==> CREATE"
			    			)
			    	.append("\n")
			    	;
			}
			log4j.debug("\n"+sb);
		}else{
			log4j.debug("\nNO HAY ELEMENTOS EN LA LISTA FINAL. ¿BORRAR LOS DE BD?");
		}
		
	}
	/**
	 * Genera los registros para area persona
	 * @return
	 * @throws Exception
	 */
	protected static String processAreaPersona() throws Exception {
		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, IDCONF_DOTHR);
		json.put(P_JSON_PERSONA, "2");	//Probar con 1 para obtener nulls
		/*
		{
		"idPersona":"2","idEmpresaConf":"1"
		"areaPersona":[{"principal":true,"idArea":"18"},{"idArea":"4"},{"idArea":"21"}],
		}
		*/
		String areasPersona = "[" +
				" {\"idArea\":\"18\", \"principal\":true}" +
				",{\"idArea\":\"21\"}" +
				",{\"idArea\":\"6\"}" +
				"]";
		JSONArray areasP = new JSONArray(areasPersona);
		json.put("areaPersona", areasP);
		
		
		String jSon = getJsonFromService(json.toString(), URI_CURRICULUM+URI_UPDATE );
		return jSon;
	}
	
}
