package net.dothr.transactional;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.dothr.MainAppTester;

@SuppressWarnings("unused")
public class ProfileTester extends MainAppTester {

	private static final String IDCONF = IDSELEX;
//	protected final static String URI_PROFILE ="/module/profile";
	private final static String URI_CREATE_PROFPUBLIC="/createpublic";
	private final static String URI_CREATE_PROFPRIVATE="/createprivate";
	
	private final static String P_JSON_PERFPERSONA = "idPersonaPerfil";
	
	public static void main(String[] args) {
		try {
			getProfiles();
//			readProfile();
//			createProfile();
//			updateProfile();
			
//			createProfileText();
//			updateProfileText();
//			deleteProfileText();
//			populateProfileText();
			
//			deleteProfile();
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}
	}
	
	/**
	 * Verifica funcionamiento de PROFILE.C
	 * @throws Exception
	 */
	public static void createProfile() throws Exception{
		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, IDCONF);
		json.put(P_JSON_PERFPERSONA, "1");//Para cuestiones de permiso de modificar/Eliminar
		
		boolean publica = false;
		
		//json.put("idPosicion", idPosicion); 	//DEBE SER OPCIONAL, Y REQUERIDO EN CASO DE INTERNO EN SERVICIO
		
		if(publica){
			String jSon = getJsonFromService(json.toString(), URI_PROFILE+URI_CREATE_PROFPUBLIC );
		}else{
			String jSon = getJsonFromService(json.toString(), URI_PROFILE+URI_CREATE_PROFPRIVATE );
		}
		/*
		 *   Si se usa idEmpresaConf? 
		 *   se debe agregar un nuevo registro en empresa_conf?
		 *   se debe agregar un nuevo registro en conf? 
		 *   y cual ocupar en la creación de publicas 
		 */
	}
	
	/**
	 * Verifica funcionamiento de PROFILE.G
	 * @throws Exception
	 */
	public static void getProfiles() throws Exception {
		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, IDSELEX);	//IDCONF | IDSELEX
		
		//1. Desde DOTHR debe traer solo las publicas (idTipoPerfil = 1)
		//2. Desde SELEX debe publicas (idTipoPerfil = 1) y privadas Selex (idTipoPerfil = 2 && idEmpresa = ?)
		json.put("idPersonaPerfil", "2"); //Opcional para obtener relacionados con posición, validar AdminSIStema o AdminAPP
		
		
//		json.put("perfilesDetalleTextosNgram", "1,2");
//		json.put("idEstatusPerfil", "2");
		json.put("incluyeInactivos", "true"); 
		
/*/		json.put("idEmpresa", "1"); /* Solo si esta activo la busqueda por empresa en ProfileServiceImpl.get() */
		
		String jSon = getJsonFromService(json.toString(), URI_PROFILE+URI_GET );
	}

	/**
	 * Verifica funcionamiento de PROFILE.R
	 * @throws Exception
	 */
	public static void readProfile() throws Exception{
		String idPerfil = "10";
		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, IDCONF);
		
		json.put("idPerfil", idPerfil); 
		
		String jSon = getJsonFromService(json.toString(), URI_PROFILE+URI_READ );
	}
	
	/**
	 * Verifica funcionamiento de PROFILE.U
	 * @throws Exception
	 */
	public static void updateProfile() throws Exception{
		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, IDCONF);
//		json.put(P_JSON_PERFPERSONA, "9");//Para cuestiones de permiso de modificar/Eliminar		
		json.put("idPerfil", "7"); 
		
//		json.put("nombre", "PRIV-Programador de Sistemas");
//		json.put("descripcion", "Desarrollador  Analista de sistemas y desarrollo web"); 
//		json.put("idEstatusPerfil", "2");
		
		String jSon = getJsonFromService(json.toString(), URI_PROFILE+URI_UPDATE );
	}
	
	/**
	 * Verifica funcionamiento de PROFILE.D
	 * @throws Exception
	 */
	public static void deleteProfile() throws Exception{
		String idPerfil = "11";
		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, IDCONF);
		json.put(P_JSON_PERFPERSONA, "1");//Para cuestiones de permiso de modificar/Eliminar
		
		json.put("idPerfil", idPerfil); 
		
		String jSon = getJsonFromService(json.toString(), URI_PROFILE+URI_DELETE );
	}
	
	
	/* *********** perfilTexto (FUnciones) de perfil ********************* */
	/**
	 * Verifica funcionamiento de PROFILETEXT.C
	 * @throws Exception
	 */
	public static void createProfileText() throws Exception{
		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, IDCONF);
		json.put(P_JSON_PERFPERSONA, "1");//Para cuestiones de permiso de modificar/Eliminar
		
		json.put("idPerfil", "8");
		
		String jSon = getJsonFromService(json.toString(), URI_PROFILE+URI_TEXT_C );
		
	}
	
	/**
	 * Verifica funcionamiento de PROFILETEXT.U
	 * @throws Exception
	 */
	public static void updateProfileText() throws Exception{
		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, IDCONF);
//		json.put("idPosicion","1");
		json.put("idPerfil","8");
		json.put(P_JSON_PERFPERSONA, "1");//Para cuestiones de permiso de modificar/Eliminar
		
		json.put("idPerfilTextoNgram", "109");
		json.put("texto", "Atracción de talento 3");
		json.put("ponderacion", "AVD");	//Precision: (3:0)	
		
		String jSon = getJsonFromService(json.toString(), URI_PROFILE+URI_TEXT_U );		
	}
	
	
	/**
	 * Verifica funcionamiento de PROFILETEXT.D
	 * @throws Exception
	 */
	public static void deleteProfileText() throws Exception{
		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, IDCONF);
		json.put(P_JSON_PERFPERSONA, "1");//Para cuestiones de permiso de modificar/Eliminar
		
		json.put("idPerfilTextoNgram", "109");		
		
		String jSon = getJsonFromService(json.toString(), URI_PROFILE+URI_TEXT_D );		
	}
	
	
	public static void populateProfileText() throws Exception {
		
		StringBuilder sb = new StringBuilder();
		String idPerfil = "26";
		JSONObject jsonCreate = new JSONObject(); //Comun para create (FIJO)
		JSONObject jsonUpdate = null; //comun para el update
		
		jsonCreate.put(P_JSON_IDCONF, IDCONF);
		jsonCreate.put(P_JSON_PERFPERSONA, "7");//Para cuestiones de permiso de modificar/Eliminar
		jsonCreate.put("idPerfil", idPerfil);
		
		JSONArray jsArrResponse = null;//contendra el arreglo de respuesta del create
		JSONObject jsonResponse = null; //Contiene la respuesta (idTextoNgram) del create
		
		//1 obtener arreglo de textos
		JSONArray jsArrTextos =  getTextosPerfil();
		if(jsArrTextos.length()>0){
			String idPerfilTextoNgram = null;
			sb.append("Insertando ").append(jsArrTextos.length())
			.append(" Funciones a perfil ").append(idPerfil).append(":\n");
			
			for(int x=0; x<jsArrTextos.length();x++){					
				try{
					jsonUpdate = jsArrTextos.getJSONObject(x);
					//1) Crear texto
					String stResponse = getJsonFromService( jsonCreate.toString(),URI_PROFILE+URI_TEXT_C);
					sb.append("\n<CREATE> ").append(stResponse).append("\n");
					jsArrResponse = new JSONArray(stResponse);
					jsonResponse = jsArrResponse.getJSONObject(0);
					
					//2) obtener idTexto creado
					idPerfilTextoNgram = jsonResponse.getString("value");
					if(idPerfilTextoNgram!=null && !idPerfilTextoNgram.equals("")){
						//3) Actualizar Texto
						jsonUpdate.put(P_JSON_IDCONF, IDCONF );
						jsonUpdate.put("idPerfil", idPerfil );
						jsonUpdate.put("idPerfilTextoNgram", idPerfilTextoNgram);
						//System.out.println(x + "> " + jsonUpdate.toString());
						sb.append(x).append("> ").append(jsonUpdate).append("\n");
						stResponse = getJsonFromService(jsonUpdate.toString(), URI_PROFILE+URI_TEXT_U  );
					}
					
				}catch (Exception e){
					//log4j.error("Error en objeto "+x, e);
					e.printStackTrace();
				}
			}
		}
		
		System.out.println(sb);
	}
	
	
	
	
	protected static JSONArray getTextosPerfil() {
		JSONArray jsArrTextos = new JSONArray();
		
		JSONObject jsono=new JSONObject();
		try {
			/*// TEXTOS PARA PERFIL DE SISTEMAS
			jsono.put("texto","Sistemas operativos.");
			jsono.put("ponderacion", "50");
			jsArrTextos.put(jsono);
			
			jsono=new JSONObject();
			jsono.put("texto","Lenguajes de programación.");
			jsono.put("ponderacion", "50");
			jsArrTextos.put(jsono);
			
			jsono=new JSONObject();
			jsono.put("texto","Herramientas para el análisis y desarrollo de programas.");
			jsono.put("ponderacion", "50");
			jsArrTextos.put(jsono);
			
			jsono=new JSONObject();
			jsono.put("texto","Sistemas operativos.");
			jsono.put("ponderacion", "50");
			jsArrTextos.put(jsono);
			
			jsono=new JSONObject();
			jsono.put("texto","Depurar programas.");
			jsono.put("ponderacion", "50");
			jsArrTextos.put(jsono);
			
			jsono=new JSONObject();
			jsono.put("texto","cálculos numérico.");
			jsono.put("ponderacion", "50");
			jsArrTextos.put(jsono);
			
			jsono=new JSONObject();
			jsono.put("texto","Instalación de software.");
			jsono.put("ponderacion", "50");
			jsArrTextos.put(jsono); //*/
			
			//*  // TEXTOS PARA PERFIL DE GERENTE DE R.H.
			jsono.put("texto","Atraer, retener y desarrollar al mejor talento para las Unidades de Negocio.");
			jsono.put("ponderacion", "100");
			jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Atracción de talento");
					jsono.put("ponderacion", "85");
					jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Reclutamiento");
					jsono.put("ponderacion", "85");
					jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Reclutador");
					jsono.put("ponderacion", "85");
					jsArrTextos.put(jsono);					
			jsono=new JSONObject();
					jsono.put("texto","Posiciones vacantes, perfiles de puesto"); jsono.put("ponderacion", "85"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Retención de talento"); jsono.put("ponderacion", "85"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Permanencia, rotación"); jsono.put("ponderacion", "85"); jsArrTextos.put(jsono);			
			jsono=new JSONObject();
					jsono.put("texto","Desarrollo de talento, de personal"); jsono.put("ponderacion", "85"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Garantizar que se tenga una base de datos de candidatos externos preseleccionados para cubrir en tiempo y forma las vacantes que se generen."); jsono.put("ponderacion", "60"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Bolsa de trabajo"); jsono.put("ponderacion", "30"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Screening"); jsono.put("ponderacion", "30"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Asesorar a sus clientes internos en los procesos de selección de personal."); jsono.put("ponderacion", "80"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Asesoría para seleccionar personal"); jsono.put("ponderacion", "40"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Vigilar que los procesos de inducción de personal se den en tiempo y forma"); jsono.put("ponderacion", "80"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Inducción al puesto"); jsono.put("ponderacion", "40"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Inducción a la empresa"); jsono.put("ponderacion", "40"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Mantener comunicación continua y estrecha con los Gerentes de las Unidades de Negocio, con el fin de asesorarlos y dar solución a situaciones derivadas de la relación laboral de sus empleados."); jsono.put("ponderacion", "100"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Relaciones laborales"); jsono.put("ponderacion", "66"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Comunicación con gerentes"); jsono.put("ponderacion", "66"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Asesoría laboral"); jsono.put("ponderacion", "66"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Trabajar en forma coordinada con el área de capacitación y desarrollo para asegurar que el personal cumpla con su programa de cursos, así como los Gerentes de las Unidades de Negocio se encuentren certificados en procesos de enseñanza."); jsono.put("ponderacion", "80"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Seguimiento a capacitación"); jsono.put("ponderacion", "68"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Cubrimiento de planes de capacitación y programa de cursos"); jsono.put("ponderacion", "68"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Cumplimiento a planes de capacitación y programa de cursos"); jsono.put("ponderacion", "68"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Certificación en el puesto"); jsono.put("ponderacion", "68"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Habilidades de enseñanza"); jsono.put("ponderacion", "68"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Capacitación de personal"); jsono.put("ponderacion", "68"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Capacitar, entrenar, certificar personal"); jsono.put("ponderacion", "68"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Realizar visitas (conforme a calendario) a las Unidades de Negocio  para diagnóstico, evaluación del clima laboral que prevalece en estas."); jsono.put("ponderacion", "100"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Clima laboral"); jsono.put("ponderacion", "66"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Q12"); jsono.put("ponderacion", "66"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Evaluación 360"); jsono.put("ponderacion", "66"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Aplicar diversas herramientas de medición/evaluación solicitadas por su Dirección Territorial y/o Corporativo."); jsono.put("ponderacion", "80"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Evaluación de personal"); jsono.put("ponderacion", "53"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Medición de desempeño"); jsono.put("ponderacion", "53"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Evaluar, medir desempeño"); jsono.put("ponderacion", "53"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Trabajar en forma coordinada con el área jurídica de Relaciones Laborales para minimizar contingencias laborales en los procesos de terminación laboral. "); jsono.put("ponderacion", "75"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Contingencias laborales"); jsono.put("ponderacion", "60"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Desincorporación, despidos"); jsono.put("ponderacion", "60"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Outplacement"); jsono.put("ponderacion", "60"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Derecho laboral"); jsono.put("ponderacion", "60"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Renuncias"); jsono.put("ponderacion", "60"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Asegurar que se tenga la información base de la plantilla de personal que atiende."); jsono.put("ponderacion", "100"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Plantilla de personal"); jsono.put("ponderacion", "66"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Control de plantilla"); jsono.put("ponderacion", "66"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Métricas de personal"); jsono.put("ponderacion", "66"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Generar la información necesaria para los diversos indicadores/métricas de la función de RH."); jsono.put("ponderacion", "100"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Métricas"); jsono.put("ponderacion", "50"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Indicadores"); jsono.put("ponderacion", "50"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Asegurar la implementación de los diversos procesos, sistemas, etc., generados por las áreas corporativas en materia de RH."); jsono.put("ponderacion", "100");jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Sistemas de RH"); jsono.put("ponderacion", "80"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Vacaciones"); jsono.put("ponderacion", "80"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Incapacidades"); jsono.put("ponderacion", "80"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Nómina"); jsono.put("ponderacion", "80"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Comprobantes de empleo"); jsono.put("ponderacion", "80"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","RH"); jsono.put("ponderacion", "85"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","RRHH"); jsono.put("ponderacion", "85"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Manejo de personal"); jsono.put("ponderacion", "80"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Esquemas de compensación"); jsono.put("ponderacion", "80"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Compensación fija"); jsono.put("ponderacion", "80"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Compensación variable"); jsono.put("ponderacion", "80"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Programas de capacitación"); jsono.put("ponderacion", "75"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Presupuesto de capacitación"); jsono.put("ponderacion", "75"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Selección de talento"); jsono.put("ponderacion", "90"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Psicométricos"); jsono.put("ponderacion", "90"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Pruebas psicométricas"); jsono.put("ponderacion", "90"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Seguridad y Salud Ocupacional"); jsono.put("ponderacion", "70"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Entrevistas por competencias"); jsono.put("ponderacion", "90"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Definición de perfiles"); jsono.put("ponderacion", "60"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Contrataciones masivas"); jsono.put("ponderacion", "80"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Volanteo"); jsono.put("ponderacion", "30"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Finiquitos"); jsono.put("ponderacion", "70"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Liquidaciones"); jsono.put("ponderacion", "70"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","IMSS"); jsono.put("ponderacion", "80"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Fonacot"); jsono.put("ponderacion", "50"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Infonavit"); jsono.put("ponderacion", "50"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Prestaciones"); jsono.put("ponderacion", "70"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Assessment"); jsono.put("ponderacion", "40"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Planes de sucesión"); jsono.put("ponderacion", "40"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Planes de carrera"); jsono.put("ponderacion", "40"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Cultura organizacional"); jsono.put("ponderacion", "30"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Ausentismo"); jsono.put("ponderacion", "20"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Ley Federal del Trabajo"); jsono.put("ponderacion", "40"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Actas administrativas"); jsono.put("ponderacion", "20"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","E-learning"); jsono.put("ponderacion", "60"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Blended learning"); jsono.put("ponderacion", "80"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","Bonos de desempeño"); jsono.put("ponderacion", "10"); jsArrTextos.put(jsono);
			jsono=new JSONObject();
					jsono.put("texto","DNC"); jsono.put("ponderacion", "20"); jsArrTextos.put(jsono); //*/
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return jsArrTextos;
	}
}
