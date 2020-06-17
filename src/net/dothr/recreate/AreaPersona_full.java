package net.dothr.recreate;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.dothr.MainAppTester;
import net.utils.ClientRestUtily;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class AreaPersona_full extends MainAppTester {
	
	private static Logger log4j = Logger.getLogger( AreaPersona_full.class );
	
	private static final String inPathAreaPersona = "/home/dothr/Documents/SELEX/Pruebas/Pre-Candidatos/"
			//+ "areaPersona.175p.csv";
			+ "areaPersona.1081p.csv";
	private static final String logSetAreaPersona = "/home/dothr/workspace/ClientRest/files/out/areaPersonaLoad.txt";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			insertaAreaPersonaFull();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Lee un archivo csv para cargar en sistema las areas por cada idPersona
	 * @throws Exception
	 */
	public static void insertaAreaPersonaFull() throws Exception{
		log4j.debug("<insertaAreaPersonaFull> ");
		log4j.debug("inPathAreaPersona: " + inPathAreaPersona );
		List<String> lsAreaP = 
				ClientRestUtily.readListaTxt(inPathAreaPersona);
		StringBuilder sb = new StringBuilder();
		
		if(!lsAreaP.isEmpty()){			
			log4j.debug("<insertaAreaPersonaFull> total a insertar: " + lsAreaP.size());
			String lineaAp, idPersona, nombre, stAreas;
			Iterator<String> itAreaP = lsAreaP.iterator();
			List<String> tokens;
			JSONObject jsonReq, jsonArea;
			JSONArray jsArrAreas;
			String areas[];
					
			
			while(itAreaP.hasNext()){
				lineaAp = itAreaP.next();
				log4j.debug("lineaAp: " + lineaAp );
				tokens = Arrays.asList(lineaAp.split("\\s*;\\s*"));
				log4j.debug("tokens: " + tokens );//idPersona;Nombre;areas
				idPersona = tokens.get(0);
				if(tokens.size()<2){
					log4j.debug("Persona invalida sin información");
				}else{
					nombre = tokens.get(1);
					if(tokens.size()>2){
						stAreas = tokens.get(2);
						
						jsonReq = new JSONObject();				
						jsArrAreas = new JSONArray();
						jsonReq.put("idEmpresaConf", IDCONF_DOTHR );
						jsonReq.put("idPersona", idPersona);
						areas = stAreas.split("\\s*/\\s*");
						if(areas.length>0){
							//Por cada area
							
							/*for(String idArea : areas){
								jsonArea.put("principal", "true");
								jsonArea.put("idArea", idArea);
								jsArrAreas.put(jsonArea);
							}// */
							for(int x=0;x<areas.length;x++){
								jsonArea = new JSONObject();
								jsonArea.put("idArea", areas[x]);
								if(x==0){
									jsonArea.put("principal", "true");
								}
								jsArrAreas.put(jsonArea);
							}
							jsonReq.put("areaPersona", jsArrAreas );
							log4j.debug("<insertaAreaPersonaFull> Insertando areas " + stAreas + " a "+ nombre );
							sb.append("\n==> Json: ").append(jsonReq.toString())
							.append("\n==> URI: ").append(URI_CURRICULUM+URI_UPDATE);
							
							sb.append("\n<== response:\n").
							append(getJsonFromService(jsonReq.toString(), URI_CURRICULUM+URI_UPDATE)).append("\n");
						}
					}
					else{
						log4j.debug(nombre + " No tiene areas asignadas");
					}
				}
			}
			
		}else{
			sb.append("<insertaAreaPersonaFull>No hay nada por insertar ");
		}
		ClientRestUtily.writeStringInFile(logSetAreaPersona, sb.toString(), false);
		log4j.debug("<insertaAreaPersonaFull> Se Creo el archivo: \n"+logSetAreaPersona);
	}
}
