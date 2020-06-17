package net.dothr.transactional;

import java.lang.reflect.Type;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


import net.dothr.MainAppTester;
import net.tce.dto.CatalogueDto;

@SuppressWarnings("unused")
public class CatalogoTester extends MainAppTester  {

	protected static String idConf = IDCONF_DOTHR;;//new Long(0);	
	private static final String IDCONF = IDCONF_DOTHR;
	
	private static final String URI_GET_AREAS="getAreas";
	static Logger log4j = Logger.getLogger( CatalogoTester.class );
	
	
	public static void main(String[] args) {
		try {
//			pruebaCatalogosValues("Area");	//Pais | Area  |EstatusOperativo?
//			pruebaCatalogosByFilter("Area");
//			pruebaGetAreas();
			
			
			
//			pruebaCatalogoCreate("Area", "510", "Area Prueba (tester2)", true);
//			pruebaCatalogoUpdate("256", "Area", "Anarquismo antiSocial UPDT", true);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void pruebaCatalogoCreate(String stCatalogo, String stAreaPadre, String stDescripcion, boolean status) throws Exception{
		log4j.debug("pruebaCatalogoCreate");
		
		final String URI_OPERATION="createCatalogueRecord";
		Long idForanea = Long.parseLong(stAreaPadre);
		
		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, IDCONF);
		//pruebaCatalogoCreate("Area", "510", "Area Prueba (tester2)", true);
		// {"idConf":"1","idForeign1":510,"status":"true","catalogueName":"Area","descripcion":"Area Prueba (tester2)"}

		json.put("catalogueName", stCatalogo);
		json.put("idForeign1", idForanea);
		json.put("descripcion", stDescripcion);
		json.put("status", status?"true":"false");
		
//		CatalogueDto catalogoDto = new CatalogueDto();
//		catalogoDto.setIdConf(IDCONF);
//		catalogoDto.setCatalogueName(stCatalogo);
//		catalogoDto.setDescripcion(stNvoValor);
//		catalogoDto.setStatus(status?"true":"false");
//		catalogoDto.setIdForeign1(idForanea);
		
		
//		Gson gson = new GsonBuilder().setDateFormat("MMM dd, yyyy HH:mm:ss a").create();
		String stJson = getJsonFromService(json.toString(), URI_CATALOGO.concat(URI_OPERATION) );
		
		
	}
	
	public static void pruebaCatalogoUpdate(String idPrimary, String stCatalogo, String stDescripcion, boolean status) throws Exception{
		log4j.debug("pruebaCatalogoCreate");
		
//		final String URI_SERVICE = "/admin/catalogue/";
		final String URI_UPDATECATALOGUE="updateCatalogueRecord";
		
//		CatalogueDto catalogoDto = new CatalogueDto();
//		catalogoDto.setIdConf(IDCONF);		
//		catalogoDto.setIdPrimary(idRec);
//		catalogoDto.setCatalogueName(stCatalogo);		
//		catalogoDto.setDescripcion(stDescripcion);
//		catalogoDto.setStatus(status?"true":"false");	
		
//		Gson gson = new GsonBuilder().setDateFormat("MMM dd, yyyy HH:mm:ss a").create();
		
//		String stJson = getJsonFromService(gson.toJson(catalogoDto), URI_CATALOGO.concat(URI_OPERATION) );
		
		//*  pruebaCatalogoUpdate("256", "Area", "Anarquismo antiSocial UPDT", true);  //*/
		
		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, IDCONF);
		json.put("catalogueName", stCatalogo);
		json.put("idPrimary", idPrimary);
		json.put("descripcion", stDescripcion);
		json.put("status", status?"true":"false");
		
		// {"idConf":"1","idPrimary":"256","status":"true","catalogueName":"Area","descripcion":"Anarquismo antiSocial UPDT"}
		String stJson = getJsonFromService(json.toString(), URI_CATALOGO.concat(URI_UPDATECATALOGUE) );
	}
	
	/**
	 * Obtiene listado de areas padres con un nivel de areas hijo
	 * @throws Exception
	 */
	public static void pruebaGetAreas() throws Exception {
		log4j.debug("pruebaGetAreas");
		
		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, IDCONF);
		
				
		String stJson = getJsonFromService(json.toString(), URI_CATALOGO.concat(URI_GET_AREAS) );
		//System.out.println(">>>>\n" + stJson);
 	   
		List<CatalogueDto> lista = null;
		Gson gson = new GsonBuilder().setDateFormat("MMM dd, yyyy HH:mm:ss a").create();
		Type type= new TypeToken<List<CatalogueDto>>(){}.getType();
		lista = gson.fromJson(stJson, type);
		
  		if(null!=lista){
			 log4j.debug("Areas Padre: " + lista.size());
			 System.out.println("lista.size() " + lista.size());
		 }else{
			 log4j.debug("lista es null");
			 System.out.println("lista es null");
		 }
	}
	
	/**
	 * Obtiene el Json de Valores del catalogo solicitado con formato 
	 * <br> {"Area":{"Administración de empresas":1, 
	 * @param stCatalogueName
	 * @throws Exception
	 */
	public static void pruebaCatalogosValues(String stCatalogueName) throws Exception{
		log4j.debug("pruebaCatalogosValues");
		String uriOperation = "getCatalogueValues"; 
		long tiempoInicio = System.currentTimeMillis();
		
		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, IDCONF);
		json.put("catalogueName", stCatalogueName);
		
		String stJson = getJsonFromService(json.toString(), URI_CATALOGO.concat(uriOperation) );
	}
	
//	/**
//	 * Obtiene el listado del catalogo en formato estandard
//	 * @param stCatalogueName
//	 * @throws Exception
//	 */
//	public static void pruebaCatalogosByFilter(String stCatalogueName) throws Exception{
//		log4j.debug("pruebaCatalogosByFilter");
//		String uriOperation = "getCatalogueByFilter"; 
//		long tiempoInicio = System.currentTimeMillis();
//		
//		JSONObject json = new JSONObject();
//		json.put(P_JSON_IDCONF, IDCONF);
//		json.put("catalogueName", stCatalogueName);
//		
//		String stJson = getJsonFromService(json.toString(), URI_CATALOGO.concat(uriOperation) );
//		
//		Gson gson = new GsonBuilder().setDateFormat("MMM dd, yyyy HH:mm:ss a").create();
//		List<CatalogueDto> lista = null;
//		Type type= new TypeToken<List<CatalogueDto>>(){}.getType();
//		lista = gson.fromJson(stJson, type);
//		
//  		if(null!=lista){
//			 log4j.debug("lista.size() " + lista.size());
//			 System.out.println("lista.size() " + lista.size());
//			 Iterator<CatalogueDto> itCatalogo = lista.iterator();
//			 while(itCatalogo.hasNext()){
//				 CatalogueDto dto = itCatalogo.next();
//				 System.out.println(dto.getIdPrimary() + ","+dto.getDescripcion());
//			 }
//		 }else{
//			 log4j.debug("lista es null");
//			 System.out.println("lista es null");
//		 }
//   	    log4j.debug("El tiempo de ejecucion es:"+
// 			   ((System.currentTimeMillis() - tiempoInicio)) + " miliseg");
//	}

}
