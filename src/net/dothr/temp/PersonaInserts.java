package net.dothr.temp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.dothr.MainAppTester;
import net.utils.ClientRestUtily;
import net.utils.ConstantesREST;

public class PersonaInserts extends MainAppTester {

	static Logger log4j = Logger.getLogger( PersonaInserts.class );
	
	private static final String DIRECTORIO_CVJSON  = LOCAL_HOME+"/JsonUI/module/curriculumManagement/";
	private static String sqlInsPersona = "INSERT INTO persona (id_persona,id_tipo_persona,id_tipo_genero,id_estado_civil,id_tipo_jornada,id_ambito_geografico,id_tipo_contrato,id_tipo_prestacion,id_tipo_estatus_padres,id_tipo_convivencia,id_tipo_vivienda,id_periodo_estado_civil,id_tipo_disp_viajar,id_grado_academico_max,id_estatus_escolar_max,id_estatus_inscripcion,id_texto_clasificacion,nombre,apellido_paterno,apellido_materno,email,anio_nacimiento,mes_nacimiento,dia_nacimiento,fecha_nacimiento,salario_min,salario_max,permiso_trabajo,numero_hijos,numero_dependientes_economicos,antiguedad_domicilio,cambio_domicilio,disponibilidad_horario,dias_experiencia_laboral,fecha_creacion,fecha_modificacion,codigo_look_and_feel,clasificado,token_inicio,edad)  VALUES ";	
	private static String sqlInsHistPwd = "INSERT INTO historico_password (id_historico_password,id_persona,password,fecha) VALUES ";
	private static String sqlInsRelEmpPers = "INSERT INTO relacion_empresa_persona VALUES ";
	
	public static final String PLANTILLA_CREATE_PERSONA = "Insert into PERSONA "+
			"(ID_PERSONA,ID_TIPO_PERSONA,ID_TIPO_GENERO,ID_ESTADO_CIVIL,ID_TIPO_JORNADA,ID_AMBITO_GEOGRAFICO,ID_TIPO_CONTRATO,ID_TIPO_PRESTACION," +
			"ID_TIPO_ESTATUS_PADRES,ID_TIPO_CONVIVENCIA,ID_TIPO_VIVIENDA,ID_PERIODO_ESTADO_CIVIL,ID_TIPO_DISP_VIAJAR,ID_GRADO_ACADEMICO_MAX," +
			"ID_ESTATUS_ESCOLAR_MAX,ID_ESTATUS_INSCRIPCION,ID_TEXTO_CLASIFICACION,NOMBRE,APELLIDO_PATERNO,APELLIDO_MATERNO,EMAIL," +
			"ANIO_NACIMIENTO,MES_NACIMIENTO,DIA_NACIMIENTO,FECHA_NACIMIENTO,SALARIO_MIN,SALARIO_MAX,PERMISO_TRABAJO,NUMERO_HIJOS," +
			"NUMERO_DEPENDIENTES_ECONOMICOS,ANTIGUEDAD_DOMICILIO,CAMBIO_DOMICILIO,DISPONIBILIDAD_HORARIO,DIAS_EXPERIENCIA_LABORAL," +
			"FECHA_CREACION,FECHA_MODIFICACION,CODIGO_LOOK_AND_FEEL,CLASIFICADO,TOKEN_INICIO,EDAD)"+
			" values "+ "(<idPersona>,null,null,null,null,null,null,null,null,null,null,null,null,null,null,"+"<idEstatusInscripcion>"+
			",null,null,null,null,'<emailPersona>'," +
			"null,null,null,null,null,null,true,null,null,null,null,null,null,to_date('15-JUL-15','DD-MON-RR'),null,null,false,null,null);";
	public static final String PLANTILLA_CREATE_HISTORICOPWD = "Insert into HISTORICO_PASSWORD (ID_HISTORICO_PASSWORD,ID_PERSONA,PASSWORD,FECHA) values (" +
			"<idPersona>,<idPersona>,'<password>',to_date('15-JUL-15','DD-MON-RR'));";
	public static final String PLANTILLA_CREATE_RELACION = "Insert into RELACION_EMPRESA_PERSONA " +
			"(ID_RELACION_EMPRESA_PERSONA,ID_TIPO_RELACION,ID_EMPRESA,ID_PERSONA,FECHA_INICIO,FECHA_FIN,REPRESENTANTE,ESTATUS_REGISTRO,CLAVE_INTERNA) " +
			"values (<idPersona>,<idTipoRelacion>,0,<idPersona>,to_date('15-JUL-15','DD-MON-RR'),null,false,true,null);";
		
	
	public static void main(String[] args) {
		try {
			generaInsertsOtros();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Genera los Inserts para los archivos de Persona en la carpeta Otros (No publicables), con Contraseña para acceder a datos.
	 * @throws Exception
	 */
	protected static void generaInsertsOtros() throws Exception{
		StringBuilder sb = new StringBuilder("/* INSERTS PARA PERSONAS EN CARPETA ANteriores (No en AWS) */");
		
		StringBuilder sbPersona = new StringBuilder();
		StringBuilder sbHistorico = new StringBuilder();
		StringBuilder sbRelacEmpresa = new StringBuilder();
		
		JSONObject jsP;
		JSONArray jsArrFilePersona;
		String fileName, stFilePersona;
		List<String> lsFiles = listaAnteriores();	//listaAWS();
		Iterator<String> itFiles = lsFiles.iterator();
		//int idPersona = 101, idHistoricoPwd = 16, idRelEmpPersona = 97;
		int idPersona = 169, idHistoricoPwd = 84, idRelEmpPersona = 164;
		String defaultPassword = "15e2b0d3c33891ebb0f1ef609ec419420c20e320ce94c65fbc8c3312448eb225"; //123456789
		
		while(itFiles.hasNext()){
			fileName = itFiles.next();
			log4j.debug("Procesando " + fileName);
			try{
				log4j.debug("paso 1) Obtener texto Json ");
				stFilePersona = ClientRestUtily.getJsonFile(fileName, DIRECTORIO_CVJSON+"anterior/");	//"awsNoPub/");
				log4j.debug("paso 2) texto a Objeto JsonArray... ");
				jsArrFilePersona = new JSONArray(stFilePersona);
				log4j.debug("paso 3) Obtener Json de Arreglo [get(0)]... ");
				jsP=jsArrFilePersona.getJSONObject(0); 
				
				sbPersona.append(sqlInsPersona).append("(").append(idPersona).append(",null,null,null,null,null,null,null,null,null,")
				.append("null,null,null,null,null,2,null,'")
				.append( (jsP.has("nombre")? jsP.getString("nombre"):"") ).append("','")
				.append( (jsP.has("apellidoPaterno")? jsP.getString("apellidoPaterno"):"") ).append("','")
				.append( (jsP.has("apellidoMaterno")? jsP.getString("apellidoMaterno"):"") ).append("','")
				.append( (jsP.has("email")? jsP.getString("email"):"") ).append("',")
				.append( (jsP.has("anioNacimiento")? jsP.getString("anioNacimiento"):"null") ).append(",")
				.append( (jsP.has("mesNacimiento")? jsP.getString("mesNacimiento"):"null") ).append(",")
				.append( (jsP.has("diaNacimiento")? jsP.getString("diaNacimiento"):"null") ).append(",").append("null,")
				.append( (jsP.has("salarioMin")? jsP.getString("salarioMin"):"null") ).append(",")
				.append( (jsP.has("salarioMax")? jsP.getString("salarioMax"):"null") ).append(",")
				.append("true,null,null,null,true,true,null,'2016-01-28 13:27:46.295','2016-01-28 13:27:46.295',null,false,null,null")
				.append("); \n");
				
				// HISTORICO_PW: Insert into HISTORICO_PASSWORD (ID_HISTORICO_PASSWORD,ID_PERSONA,PASSWORD,FECHA) values (1,1,'2ef85fbe1a34abf0bc7bdfb9159436ce2934470e76a79b173b8e2b8ded0619df',to_date('15-JUL-15','DD-MON-RR'));
				sbHistorico.append(sqlInsHistPwd) .append("(").append(idHistoricoPwd).append(",").append(idPersona).append(",'")
				.append( (jsP.has("password")? jsP.getString("password"):defaultPassword) ).append("',to_date('15-JUL-15','DD-MON-RR')")
				.append(");\n");
				
				//relacion_empresa_persona:  INSERT INTO relacion_empresa_persona VALUES (5,8,1,10,to_date('15-JUL-15','DD-MON-RR'),false,true,null);
				sbRelacEmpresa.append(sqlInsRelEmpPers).append("(")
				.append(idRelEmpPersona).append(",8,1,").append(idPersona).append(",to_date('15-JUL-15','DD-MON-RR'),false,true,null")
				.append(");\n");
				
				jsP.put("idPersona", String.valueOf(idPersona) );
				
				//ClientRestUtily.writeStringInFile(DIRECTORIO_CVJSON+"read-noAws-"+idPersona+".json", "["+jsP.toString()+"]"), false);		
				ClientRestUtily.writeStringInFile(DIRECTORIO_CVJSON+"read-ant-"+idPersona+".json", "["+jsP.toString()+"]", false);
				
				idPersona++; idHistoricoPwd++; idRelEmpPersona++;
			}catch (Exception e){
				log4j.fatal("Error al procesar "+fileName, e);
			}	
		}
		
		sb.append("/* PERSONAS */ \n").append(sbPersona)
		.append("\n /* HistoricoPassword */ \n").append(sbHistorico)
		.append("\n /* Relacion Empresa persona */ \n").append(sbRelacEmpresa)
		
		.append("\n /* ***********>>>  SECUENCIAS  <<<< ********************* */ \n")
		.append("ALTER SEQUENCE SEQ_PERSONA RESTART WITH ").append(idPersona).append(";\n")
		.append("ALTER SEQUENCE SEQ_HISTORICO_PASSWORD RESTART WITH ").append(idHistoricoPwd).append(";\n")
		.append("ALTER SEQUENCE SEQ_RELACION_EMPRESA_PERSONA RESTART WITH ").append(idRelEmpPersona).append(";\n");
		
		//ClientRestUtily.writeStringInFile("/home/dothr/JsonUI/InsertsOtros.sql", sb.toString(), false);
		ClientRestUtily.writeStringInFile("/home/dothr/JsonUI/InsertsAnteriores.sql", sb.toString(), false);
	}
	
	
	/** 
	 * A partir de la lista de archivos json de Persona <i>read-??.json</i>
	 * genera un nuevo archivo sql, el cual se ejecuta antes de realizar el procedimiento
	 * de carga desde archivos json
	 * @return
	 * @throws JSONException
	 * @throws Exception
	 */
	public static StringBuilder generaMultipleInsertsPersona()  throws JSONException, Exception{
		StringBuilder stMainBuilder = new StringBuilder();
		log4j.debug("VERIFICA QUE NO SEA PRODUCCION:....");
		if(WEB_RESOURCE.equals(ConstantesREST.WEB_TRANSACT_AWS) || WEB_RESOURCE.indexOf("localhost")== -1){
			log4j.fatal("No esta apuntando a DESARROLLO "+WEB_RESOURCE, new NullPointerException() );
			stMainBuilder.append("No esta apuntando a DESARROLLO "+WEB_RESOURCE);
		}else{
			
			List<String> lista = listaAWS();
			
			if(lista!=null && lista.size()>0){
				for(String element: lista){
					System.out.println(element);
					if(element.startsWith("read")){
						stMainBuilder.append(generaInsertPersona(element)).append("\n\n");
					}
				}
			}
			String filePath = DIRECTORIO_CVJSON+"sql/insertPersonasAW.sql";
			ClientRestUtily.writeStringInFile(filePath, stMainBuilder.toString(), false);
		}
		
		return stMainBuilder;
	}
	
	/**
	 * Procesa una cadena de JSON, ([ {"nombre":"", "email":"",...]) <br>
	 * para generar Inserts de creación en la Base de datos (tipo SOlicitante)
	 * utilizando el idPersona de archivo como el ID_Persona en el insert
	 * @param archivoPersona
	 * @return
	 * @throws JSONException
	 * @throws Exception
	 */
	public static StringBuilder generaInsertPersona(String archivoPersona) throws JSONException, Exception{
		StringBuilder stBuilder = new StringBuilder();
		final String ID_ESTATUS_INSCRIPCION = "2";
		final String ID_TIPO_REL_SOLICITANTE = "8";
		
		
		if(archivoPersona.startsWith("read")){
			
			log4j.debug("paso 1) cadena de persona: ");
			String stPersona = ClientRestUtily.getJsonFile(archivoPersona, DIRECTORIO_CVJSON);
			
			log4j.debug(stPersona);
			if(stPersona.equals("[]") || (stPersona.indexOf("email")==-1)){
				log4j.debug("NO EXISTE ARCHIVO JSON (persona) o es invalido");
				stBuilder.append("NO EXISTE ARCHIVO JSON (persona) o es invalido");
			}else{
				log4j.debug("paso 2) Persona Se convierte a objeto json ");
				JSONArray jsonResponse = new JSONArray(stPersona);
				
				JSONObject jsonPersona = jsonResponse.getJSONObject(0);
				
				String idPersona = jsonPersona.getString("idPersona");
				String emailPersona = jsonPersona.getString("email");
				
				stBuilder.append("/* ---------------- INSERTS PARA PERSONA ").append(idPersona).append("  ------------------------------------*/ \n");
				
				stBuilder.append(PLANTILLA_CREATE_PERSONA.replace("<idPersona>", idPersona).replace("<emailPersona>", emailPersona).replace("<idEstatusInscripcion>", ID_ESTATUS_INSCRIPCION));
				
				stBuilder.append("\n").append(PLANTILLA_CREATE_HISTORICOPWD.replace("<idPersona>", idPersona).replace("<password>", PWD_123456789));
				stBuilder.append("\n").append(PLANTILLA_CREATE_RELACION.replace("<idPersona>", idPersona).replace("<idTipoRelacion>", ID_TIPO_REL_SOLICITANTE));
			}
			
		}else{
			stBuilder.append("el nombre de archivo no es el esperado [read-IDPERSONA]");
		}
		return stBuilder;
	}
	
	/* =================================================================================================== */
	/* =================================================================================================== */
	/* =================================================================================================== */
	
	/**
	 * Lista de archivos en la ruta definida para procesar 
	 * @return
	 */
	@SuppressWarnings("unused")
	private static List<String> listaNoPubAWS(){
		List<String> lsFiles = new ArrayList<String>();
		/* Lista de No Publicables AWS */
		
		 lsFiles.add("acollantesr.json"); 
		 lsFiles.add("adnkey.json"); 
		 lsFiles.add("adreanola.json"); 
		 lsFiles.add("ahsotelo.json"); 
		 lsFiles.add("alex_vay2.json"); 
		 lsFiles.add("arielrxba.json"); 
		 lsFiles.add("bere_2824.json"); 
		 lsFiles.add("betty120583.json"); 
		 lsFiles.add("bre.carrizales.json"); 
		 lsFiles.add("canonicomiguel.json"); 
		 lsFiles.add("carlosgzzang.json"); 
		 lsFiles.add("cesar_romero105.json"); 
		 lsFiles.add("cesar.soto21.json"); 
		 lsFiles.add("cesar.soto.json"); 
		 lsFiles.add("cindy_aril.json"); 
		 lsFiles.add("cintiuxrodriguez.json"); 
		 lsFiles.add("danny_starclub.json"); 
		 lsFiles.add("ddluna.json"); 
		 lsFiles.add("edgar_gascpec.json"); 
		 lsFiles.add("edgar_gaspec.json"); 
		 lsFiles.add("eli_cantisani.json"); 
		 lsFiles.add("ez23ch.json"); 
		 lsFiles.add("fabiolabella66.json"); 
		 lsFiles.add("fredybrend.json"); 
		 lsFiles.add("ingenierornoriega.json"); 
		 lsFiles.add("ingesol77.json"); 
		 lsFiles.add("ing_mgc1982.json"); 
		 lsFiles.add("jfernandovel.json"); 
		 lsFiles.add("j.gonzalez12.json"); 
		 lsFiles.add("jmqp.1975.json"); 
		 lsFiles.add("jonathanhector.json"); 
		 lsFiles.add("jorgespra.json"); 
		 lsFiles.add("jpablo_bbeltran.json"); 
		 lsFiles.add("judithgaribay.json"); 
		 lsFiles.add("karlagoa.json"); 
		 lsFiles.add("kblancarte.json"); 
		 lsFiles.add("kmleosan.json"); 
		 lsFiles.add("landero.fuentes.json"); 
		 lsFiles.add("liscano.json"); 
		 lsFiles.add("lista.txt"); 
		 lsFiles.add("lucero.rodriguez.g.json"); 
		 lsFiles.add("luis.agarza.json"); 
		 lsFiles.add("luisrodrigou.json"); 
		 lsFiles.add("luna_pao.json"); 
		 lsFiles.add("mach_palma.json"); 
		 lsFiles.add("mangelperezs.json"); 
		 lsFiles.add("marbella.json"); 
		 lsFiles.add("marianita.mv78.json"); 
		 lsFiles.add("marisolmartinez0221.json"); 
		 lsFiles.add("marug718.json"); 
		 lsFiles.add("monsecasas.json"); 
		 lsFiles.add("montoyalorenz.json"); 
		 lsFiles.add("mvalladares2.json"); 
		 lsFiles.add("oaocampoc.json"); 
		 lsFiles.add("pilar.castro.json"); 
		 lsFiles.add("psic.carmin.json"); 
		 lsFiles.add("rafamtzdiaz.json"); 
		 lsFiles.add("richyherga.json"); 
		 lsFiles.add("rigorizza.json"); 
		 lsFiles.add("rousyab.json"); 
		 lsFiles.add("tereflores.json"); 
		 lsFiles.add("vanrro85.json"); 
		 lsFiles.add("verduzco36.json"); 
		 lsFiles.add("viri_181.json"); 
		 lsFiles.add("vmo0323.json"); 
		 lsFiles.add("wendy14_2.json"); 
		 lsFiles.add("yareli.montes.json"); 
		 lsFiles.add("zurisa_0812.json");
		 
		 return lsFiles;
	}
	
	private static List<String> listaAWS(){
		List<String> lsFiles = new ArrayList<String>();
		
		/* Lista de AWS */
		lsFiles.add("read-13.json");
		lsFiles.add("read-14.json");
		lsFiles.add("read-15.json");
		lsFiles.add("read-16.json");
		lsFiles.add("read-17.json");
		lsFiles.add("read-18.json");
		lsFiles.add("read-19.json");
		lsFiles.add("read-20.json");
		lsFiles.add("read-21.json");
		lsFiles.add("read-22.json");
		lsFiles.add("read-23.json");
		lsFiles.add("read-24.json");
		lsFiles.add("read-25.json");
		lsFiles.add("read-26.json");
		lsFiles.add("read-27.json");
		lsFiles.add("read-28.json");
		lsFiles.add("read-29.json");
		lsFiles.add("read-30.json");
		lsFiles.add("read-31.json");
		lsFiles.add("read-32.json");
		lsFiles.add("read-33.json");
		lsFiles.add("read-34.json");
		lsFiles.add("read-35.json");
		lsFiles.add("read-36.json");
		lsFiles.add("read-37.json");
		lsFiles.add("read-38.json");
		lsFiles.add("read-39.json");
		lsFiles.add("read-40.json");
		lsFiles.add("read-41.json");
		lsFiles.add("read-42.json");
		lsFiles.add("read-43.json");
		lsFiles.add("read-44.json");
		lsFiles.add("read-45.json");
		lsFiles.add("read-46.json");
		lsFiles.add("read-47.json");
		lsFiles.add("read-48.json");
		lsFiles.add("read-49.json");
		lsFiles.add("read-50.json");
		lsFiles.add("read-51.json");
		lsFiles.add("read-52.json");
		lsFiles.add("read-53.json");
		lsFiles.add("read-54.json");
		lsFiles.add("read-55.json");
		lsFiles.add("read-56.json");
		lsFiles.add("read-57.json");
		lsFiles.add("read-58.json");
		lsFiles.add("read-59.json");
		lsFiles.add("read-60.json");
		lsFiles.add("read-61.json");
		lsFiles.add("read-62.json");
		lsFiles.add("read-63.json");
		lsFiles.add("read-64.json");
		lsFiles.add("read-65.json");
		lsFiles.add("read-66.json");
		lsFiles.add("read-67.json");
		lsFiles.add("read-68.json");
		lsFiles.add("read-69.json");
		lsFiles.add("read-70.json");
		lsFiles.add("read-71.json");
		lsFiles.add("read-72.json");
		lsFiles.add("read-73.json");
		lsFiles.add("read-74.json");
		lsFiles.add("read-75.json");
		lsFiles.add("read-76.json");
		lsFiles.add("read-77.json");
		lsFiles.add("read-78.json");
		lsFiles.add("read-79.json");
		lsFiles.add("read-80.json");
		lsFiles.add("read-81.json");
		lsFiles.add("read-82.json");
		lsFiles.add("read-83.json");
		lsFiles.add("read-84.json");
		lsFiles.add("read-85.json");
		lsFiles.add("read-86.json");
		lsFiles.add("read-87.json");
		lsFiles.add("read-88.json");
		lsFiles.add("read-89.json");
		lsFiles.add("read-90.json");
		lsFiles.add("read-91.json");
		lsFiles.add("read-92.json");
		lsFiles.add("read-93.json");
		lsFiles.add("read-94.json");
				
		return lsFiles;
	}
	
	private static List<String> listaAnteriores(){
		List<String> lsFiles = new ArrayList<String>();
		lsFiles.add("read-15.json");
		lsFiles.add("read-16.json");
		lsFiles.add("read-17.json");
		lsFiles.add("read-18.json");
		lsFiles.add("read-19.json");
		lsFiles.add("read-20.json");
		lsFiles.add("read-21.json");
		lsFiles.add("read-22.json");
		lsFiles.add("read-23.json");
		lsFiles.add("read-24.json");
		lsFiles.add("read-25.json");
		lsFiles.add("read-26.json");
		lsFiles.add("read-27.json");
		lsFiles.add("read-28.json");
		lsFiles.add("read-29.json");
		lsFiles.add("read-30.json");
		lsFiles.add("read-31.json");
		lsFiles.add("read-32.json");
		lsFiles.add("read-33.json");
		lsFiles.add("read-34.json");
		lsFiles.add("read-35.json");
		lsFiles.add("read-36.json");
		lsFiles.add("read-37.json");
		lsFiles.add("read-38.json");
		lsFiles.add("read-39.json");
		lsFiles.add("read-40.json");
		lsFiles.add("read-41.json");
		lsFiles.add("read-42.json");
		lsFiles.add("read-43.json");
		lsFiles.add("read-44.json");
		lsFiles.add("read-45.json");
		lsFiles.add("read-46.json");
		lsFiles.add("read-47.json");
		lsFiles.add("read-48.json");
		lsFiles.add("read-49.json");
		lsFiles.add("read-50.json");
		return lsFiles;
	}
}
