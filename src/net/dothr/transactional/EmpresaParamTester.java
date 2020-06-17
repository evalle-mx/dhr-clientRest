package net.dothr.transactional;

import org.json.JSONArray;
import org.json.JSONObject;

import net.dothr.MainAppTester;

public class EmpresaParamTester extends MainAppTester {
	

	protected static final String URI_EMPPARAM = "/module/enterpriseParameter";
	protected static final String URI_MULTIPLE_UPDATE = "/updmultiple";
	protected final static String URI_RELOAD="/reload";
	/*
		EPARAMS.G
		EPARAMS.U
		EPARAMS.UM
		EPARAMS.RL
		EPARAMS.D

	 */
	
	public static void main(String[] args) {
		String resp;
		try {
			resp = 
//				empresaParamUpdMultiple();
//				empresaParamUpd();
				empresaParamGet();
//				empresaParamReload();
			
			printJson(resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Método de prueba para servicio EPARAMS.G [ /module/enterpriseParameter/get ] 
	 * @return
	 * @throws Exception
	 */
	public static String empresaParamGet() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF_DOTHR );
		jsCont.put("idConf", "1" );
		/*
		 	1 =>Atributo requerido para que un CV pueda considerarse PUBLICABLE
				2 =>Ubicación de archivos de estilo para la GUI, incluye CSS´s, imagenes, fonts
				4 =>Políticas de aplicación general, independientemente de la empresa usuaria (Error)
			5 =>Atributo requerido para que una POSICION pueda considerarse PUBLICABLE
				6 =>Atributo requerido para que un CURRICULUM EMPRESARIAL pueda considerarse publicable
				7 =>Parámetros generales relacionados con Notificaciones
			8 =>Parámetros relacionados con DataConf
		 */
		jsCont.put("idTipoParametro", "1"); 
//		jsCont.put("contexto", "AreaPerfil");	//Solo para tipo 8
		String jSon = getJsonFromService(jsCont.toString(), URI_EMPPARAM+URI_GET );
		
		return jSon; 
	}
	
	/**
	 * Método de prueba para servicio EPARAMS.RL [ /module/enterpriseParameter/reload ]
	 * @return
	 * @throws Exception
	 */
	public static String empresaParamReload() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF_DOTHR );
//		jsCont.put("idConf", "1" );
		/*
		 	1 =>Atributo requerido para que un CV pueda considerarse PUBLICABLE
				2 =>Ubicación de archivos de estilo para la GUI, incluye CSS´s, imagenes, fonts
				4 =>Políticas de aplicación general, independientemente de la empresa usuaria (Error)
			5 =>Atributo requerido para que una POSICION pueda considerarse PUBLICABLE
				6 =>Atributo requerido para que un CURRICULUM EMPRESARIAL pueda considerarse publicable
				7 =>Parámetros generales relacionados con Notificaciones
			8 =>Parámetros relacionados con DataConf
		 */
		jsCont.put("idTipoParametro", "1");
		String jSon = getJsonFromService(jsCont.toString(), URI_EMPPARAM+URI_RELOAD );
		
		return jSon; 
	}
	
	/**
	 * Método de prueba para servicio EPARAMS.U [ /module/enterpriseParameter/update ]
	 * @return
	 * @throws Exception
	 */
	public static String empresaParamUpd() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF_DOTHR );
		/*
		  id_empresa_parametro =>r/o
		  id_tipo_parametro =>r/o (filtro)
		  orden =>W
		  contexto => W (filtro)
		  valor =>W
		  condicion =>W
		  descripcion => W
		  fecha_creacion =>NO (o r/o)
		  fecha_modificacion =>NO (o r/o)
		  funcion =>W
		  id_conf =>r/o (filtro)
		*/
//		jsCont.put("idEmpresaParametro", "21"); //21 : "idAreaPrincipal !=null" 
//		jsCont.put("valor", "idAreaPrincipal !=null");	//idAreaPrincipal !=null
	
		jsCont.put("idEmpresaParametro", "2");
		jsCont.put("valor", "escolaridad.tamanio() > 0");
		
//		jsCont.put("actualizaCache", true);
		
		String jSon = getJsonFromService(jsCont.toString(), URI_EMPPARAM+URI_UPDATE );
		
		return jSon; 
	}
	
	/**
	 * Método de prueba para servicio EPARAMS.M [ /module/enterpriseParameter/updmultiple ]
	 * @return
	 * @throws Exception
	 */
	public static String empresaParamUpdMultiple() throws Exception {
		JSONObject jsCont;
		JSONArray jsRequest = new JSONArray();
		/*for(int x=15; x<22;x++){
			jsCont = new JSONObject();
			jsCont.put(P_JSON_IDCONF, IDCONF_DOTHR );
			jsCont.put("idEmpresaParametro", ""+x);
			jsCont.put("valor", "PRUEBA de cambio en "+x);
			jsRequest.put(jsCont);
		}*/
		
		
		jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF_DOTHR );
		jsCont.put("idEmpresaParametro", "2015");
		jsCont.put("valor", "cambioDomicilio != null");
		jsRequest.put(jsCont);
		
		jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF_DOTHR );
		jsCont.put("idEmpresaParametro", "2016");
		jsCont.put("valor", "disponibilidadHorario != null");
		jsRequest.put(jsCont);
		
		jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF_DOTHR );
		jsCont.put("idEmpresaParametro", "2017");
		jsCont.put("valor", "alMenosUnaEscOExp  ==  true");
		jsRequest.put(jsCont);
		
		
		jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF_DOTHR );
		jsCont.put("idEmpresaParametro", "2018");
		jsCont.put("valor", "diasExperienciaLaboral != null");
		jsRequest.put(jsCont);
		jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF_DOTHR );
		jsCont.put("idEmpresaParametro", "2019");
		jsCont.put("valor", "localizacion.size() > 0");
		jsRequest.put(jsCont);
		jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF_DOTHR );
		jsCont.put("idEmpresaParametro", "20");
		jsCont.put("valor", "contacto.size() >0");
		jsRequest.put(jsCont);
		
		String jSon = getJsonFromService(jsRequest.toString(), URI_EMPPARAM+URI_MULTIPLE_UPDATE );
		
		return jSon; 
	}
	
	

}
