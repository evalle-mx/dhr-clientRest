//package net.dothr.temp;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.log4j.Logger;
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import net.dothr.recreate.PersonasFromFiles;
//import net.dothr.util.ClientRestUtily;
//import net.utils.DateUtily;
//
//public class PersonaComparaconDB extends PersonasFromFiles {
//
//	static Logger log4j = Logger.getLogger( PersonaComparaconDB.class );
//	protected static final String DIRECTORIO_JSON = LOCAL_HOME+"/JsonUI/module/curriculumManagement/";
//	
//	/**
//	 * Obtiene un mapa donde se tiene el idPersona y email en BD, 
//	 * después itera una lista de archivos .json, convierte en objeto JSON
//	 * verifica si el correo existe en BD, 
//	 * reasigna idPersona de BD, y genera nuevo archivo .json en directorio AWS
//	 * si no existe, genera nuevo archivo .json en directorio awsNoPub
//	 * @throws Exception
//	 */
//	@SuppressWarnings("unused")
//	public static void populateData() throws Exception {
//		log4j.debug("<populateData> Iniciando procedimiento");
//		String reportPath = DIRECTORIO_JSON+"../reportePersonaData.txt";
//		String awsDir = DIRECTORIO_JSON+"AWS/";
//		String awsNoPubDir = DIRECTORIO_JSON+"awsNoPub/";
//		/* 
//		 * Se toma como pre-requisito haber hecho los inserts de persona con id_persona, email para al leer los archivos .json
//		 * se comparen los emails de archivo con el de sistema
//		 */
//		StringBuilder sbProceso = new StringBuilder();
//		List<String> lsFiles = listaArchivosOriginal();
//		String fileName = null, stFilePersona, email;
//		JSONObject jsonPersona;
//		JSONArray jsArrFilePersona;
//		Map<String,Integer> mapa = getBDPers();	
//
//		String emailUser, nombre, apellido, idPersonaAnt, fileOutName;
//		
//		Iterator<String> itFiles = lsFiles.iterator();
//		/* Se itera cada uno de los archivos Json de persona almacenados, 
//		 * Si existen en BD se insertan Datos y se manda archivo a AWS
//		 * SI no Existe en BD, solo se envia a awsNoPub
//		 */
//		while(itFiles.hasNext()){
//			fileName = itFiles.next();
//			log4j.debug("Procesando " + fileName);
//			Map<String,Integer> mapaAgregados = new HashMap<String,Integer>();
//			sbProceso.append("===========> ").append(fileName).append("<============\n");			
//			try{
//				log4j.debug("paso 1) Obtener texto Json ");
//				stFilePersona = ClientRestUtily.getJsonFile(fileName, DIRECTORIO_JSON);
//				log4j.debug("paso 2) texto a Objeto JsonArray... ");
//				jsArrFilePersona = new JSONArray(stFilePersona);
//				log4j.debug("paso 3) Obtener Json de Arreglo [get(0)]... ");
//				jsonPersona=jsArrFilePersona.getJSONObject(0); 
//				
//				log4j.debug("paso 4) Obtener email de archivo... ");
//				email = jsonPersona.getString("email");
//				
//				log4j.debug("paso 5) Comprobar que existe en BD... ");
//				Integer idBD =  mapa.get(email);
//				//sbOutput = new StringBuilder("[");
//				if(idBD!=null){
//					log4j.debug("Existe Persona en BD, se procede a replicar datos del archivo Json, y se escribe archivo en "+awsDir);
//					jsonPersona.put("idPersona", String.valueOf(idBD));
//					if(mapaAgregados.get(email)!=null){
//						/* INFORMACION DUPLICADA, SOLO SE GENERA ARCHIVO */
//						log4j.debug("YA existe, esta duplicado");
//						fileOutName = "read-"+idBD + DateUtily.getDateInLong()+".json";
//						sbProceso.append("Existe ").append(email).append(" con ID: ")
//						.append(idBD).append(" pero la información esta duplicada, se crea archivo: ").append(idBD+fileOutName).append("\n");
//					}else{
//						/* INFORMACIÓN NUEVA, SE ESCRIBE EN APLICACION */
//						fileOutName = "read-"+idBD+".json";
//						sbProceso.append("Existe ").append(email).append(" con ID: ")
//						.append(idBD).append(" en BD, se procede a replicar datos del archivo Json ").append(idBD+fileOutName).append("\n");
//					}
//					ClientRestUtily.writeStringInFile(awsDir+fileOutName, "["+jsonPersona.toString()+"]", false);
//					
//					sbProceso.append("\n").append(procesaPersona(jsonPersona));	//<< En este punto, realiza la persistencia por APP (Transactional)
//					mapaAgregados.put(email, idBD);
//					
//				}else{
//					/* PERSONA NO EXISTE, SE GENERA ARCHIVO DE RESPALDO */
//					log4j.debug("No Existe Persona en BD, se omite escritura de datos y se escribe nuevo archivo en "+ awsNoPubDir);
//					emailUser = email.substring(0, email.indexOf("@"));
//					nombre = (jsonPersona.has("nombre")? jsonPersona.getString("nombre"):"--SIN NOMBRE--" );
//					apellido = (jsonPersona.has("apellidoPaterno")? jsonPersona.getString("apellidoPaterno"):"" );
//					idPersonaAnt = jsonPersona.getString("idPersona");
//					
//					fileOutName = emailUser+".json";
//					jsonPersona.remove("idPersona");
//					jsonPersona.put("idPersonaAnt", idPersonaAnt);
//					ClientRestUtily.writeStringInFile(awsNoPubDir+fileOutName, "["+jsonPersona.toString()+"]", false);
//					sbProceso.append("NO Existe ").append(email).append(" en BD, se procede a replicar datos del archivo Json ").append(awsNoPubDir+fileOutName).append("\n");
//				}
//				
//				ClientRestUtily.delFile(DIRECTORIO_JSON+fileName);		
//			} catch (Exception e){
//				log4j.fatal("Error al procesar "+fileName, e);
//				sbProceso.append("\n<Excepcion>").append(e.getMessage());
//			}
//			sbProceso.append("\n");
//		}
//		
//		ClientRestUtily.writeStringInFile(reportPath, sbProceso.toString(), false);
//	}
//	
//	
//	/**
//	 * Mapa de <Email,idPersona>, obtenido en QUery en la BD, sobre la tabla Persona
//	 * @return
//	 */
//	private static Map<String, Integer> getBDPers(){
//		Map<String,Integer> mapa = new HashMap<String, Integer>();
//		
//		mapa.put("netto.speed@gmail.com", 2);
//		mapa.put("monica.quintero@pirh.com.mx",10);
//	 	 mapa.put("isabel.giles@pirh.com.mx",11);
//	 	 mapa.put("eflores@selex.com.mx",12);
//	 	 mapa.put("hwgutierrez12@gmail.com",13);
//	 	 mapa.put("ilejgmtz@gmail.com",14);
//	 	 mapa.put("robalvarez.or@gmail.com",15);
//	 	 mapa.put("orlandoibpe@hotmail.com",16);
//	 	 mapa.put("tania.luz.gonzalez@gmail.com",17);
//	 	 mapa.put("ramiro.barron.marquez@gmail.com",18);
//	 	 mapa.put("bettymoralesh@gmail.com",19);
//	 	 mapa.put("erickpsique@hotmail.com",20);
//	 	 mapa.put("mayra.camacho@outlook.com",21);
//	 	 mapa.put("martin_moreno49@hotmail.com",22);
//	 	 mapa.put("LEM.AARONESPINOZA@GMAIL.COM",23);
//	 	 mapa.put("alex.castro.85@gmail.com",24);
//	 	 mapa.put("vaikra.d@gmail.com",25);
//	 	 mapa.put("erikazipol@hotmail.com",26);
//	 	 mapa.put("ccaldera57@yahoo.com.mx",27);
//	 	 mapa.put("juca_1_6@hotmail.com",28);
//	 	 mapa.put("jbarraganc@hotmail.com",29);
//	 	 mapa.put("israel-becerra@hotmail.com",30);
//	 	 mapa.put("camila_Z69@hotmail.com",31);
//	 	 mapa.put("lhmtz@live.com.mx",32);
//	 	 mapa.put("psicsalaya@hotmail.com",33);
//	 	 mapa.put("terejulian@hotmail.com",34);
//	 	 mapa.put("mayela_rios@yahoo.com.mx",35);
//	 	 mapa.put("daenny1@hotmail.com",36);
//	 	 mapa.put("albapilarcruzcerero@ymail.com",37);
//	 	 mapa.put("keyu_07@hotmail.com",38);
//	 	 mapa.put("karinaelizondos@hotmail.com",39);
//	 	 mapa.put("recluta7707@yahoo.com.mx",40);
//	 	 mapa.put("azaralaz@hotmail.com",41);
//	 	 mapa.put("angel.oran27@gmail.com",42);
//	 	 mapa.put("alvaradojmfa@gmail.com",43);
//	 	 mapa.put("danhya_74@hotmail.com",44);
//	 	 mapa.put("julio@grupocaadmin.com",45);
//	 	 mapa.put("munoz_daniel10@hotmail.com",46);
//	 	 mapa.put("jervenzor@gmail.com",47);
//	 	 mapa.put("karolexk@hotmail.com",48);
//	 	 mapa.put("jonathanalanc@gmail.com",49);
//	 	 mapa.put("javito0315@hotmail.com",50);
//	 	 mapa.put("augustogcesar4@hotmail.com",51);
//	 	 mapa.put("normapms@hotmail.com",52);
//	 	 mapa.put("lizethlr4@hotmail.com",53);
//	 	 mapa.put("menito15486@yahoo.com.mx",54);
//	 	 mapa.put("lau_8585@hotmail.com",55);
//	 	 mapa.put("jorgerodrigo.castroreyes@hotmail.com",56);
//	 	 mapa.put("victornufer@hotmail.com",57);
//	 	 mapa.put("joseantoniocruzoteiza@gmail.com",58);
//	 	 mapa.put("ncorderor1@gmail.com",59);
//	 	 mapa.put("adriana.diazgomez@yahoo.com.mx",60);
//	 	 mapa.put("pablinrv@yahoo.com.mx",61);
//	 	 mapa.put("regamez_g77@hotmail.com",62);
//	 	 mapa.put("narvaezja8383@hotmail.com",63);
//	 	 mapa.put("iwaly11@hotmail.com",64);
//	 	 mapa.put("yari_neil@hotmail.com",65);
//	 	 mapa.put("lic.ricardo.alvarez@gmail.com",66);
//	 	 mapa.put("edgargomeza.76@hotmail.com",67);
//	 	 mapa.put("grysselc@hotmail.com",68);
//	 	 mapa.put("jofri29@hotmail.es",69);
//	 	 mapa.put("hiramcarr81@gmail.com",70);
//	 	 mapa.put("fco_leyvag@hotmail.com",71);
//	 	 mapa.put("pamela_apm11@hotmail.com",72);
//	 	 mapa.put("flortorresalvarado@gmail.com",73);
//	 	 mapa.put("noel.espinoza@hotmail.com",74);
//	 	 mapa.put("emois@hotmail.com",75);
//	 	 mapa.put("jorammtz@yahoo.com.mx",76);
//	 	 mapa.put("lore_der@hotmail.com",77);
//	 	 mapa.put("e-manu-flores@hotmail.com",78);
//	 	 mapa.put("jesus_qv@hotmail.com",79);
//	 	 mapa.put("aida_ocnuj@hotmail.com",80);
//	 	 mapa.put("gerardo.tlacaelel@gmail.com",81);
//	 	 mapa.put("ramon_2913@hotmail.com",82);
//	 	 mapa.put("luisfe_rh@hotmail.com",83);
//	 	 mapa.put("aleexac@gmail.com",84);
//	 	 mapa.put("andres@stoa.com.mx",85);
//	 	 mapa.put("ernestomarincvz@gmail.com",86);
//	 	 mapa.put("cuevasvalery@gmail.com",87);
//	 	 mapa.put("lrcruizmtz@hotmail.com",88);
//	 	 mapa.put("joseluistoledo8@hotmail.com",89);
//	 	 mapa.put("tiggger85_26@hotmail.com",90);
//	 	 mapa.put("ficoazcorra20@icloud.com",91);
//	 	 mapa.put("delia_gomez03@hotmail.com",92);
//	 	 mapa.put("lizropelca@live.com.mx",93);
//	 	 mapa.put("1960joal@gmail.com",94);
//		
//		return mapa;
//	}
//	
//	private static List<String> listaArchivosOriginal(){
//		List<String> lsFiles = new ArrayList<String>();
//		/* Lista de originales */
//		lsFiles.add("read-1005.json");
//		lsFiles.add("read-100.json");
//		lsFiles.add("read-101.json");
//		lsFiles.add("read-1025.json");
//		lsFiles.add("read-102.json");
//		lsFiles.add("read-103.json");
//		lsFiles.add("read-1045.json");
//		lsFiles.add("read-104.json");
//		lsFiles.add("read-105.json");
//		lsFiles.add("read-1065.json");
//		lsFiles.add("read-106.json");
//		lsFiles.add("read-107.json");
//		lsFiles.add("read-1085.json");
//		lsFiles.add("read-108.json");
//		lsFiles.add("read-109.json");
//		lsFiles.add("read-10.json");
//		lsFiles.add("read-1105.json");
//		lsFiles.add("read-110.json");
//		lsFiles.add("read-111.json");
//		lsFiles.add("read-1125.json");
//		lsFiles.add("read-112.json");
//		lsFiles.add("read-113.json");
//		lsFiles.add("read-1145.json");
//		lsFiles.add("read-114.json");
//		lsFiles.add("read-115.json");
//		lsFiles.add("read-1165.json");
//		lsFiles.add("read-116.json");
//		lsFiles.add("read-117.json");
//		lsFiles.add("read-1185.json");
//		lsFiles.add("read-118.json");
//		lsFiles.add("read-119.json");
//		lsFiles.add("read-11.json");
//		lsFiles.add("read-1205.json");
//		lsFiles.add("read-120.json");
//		lsFiles.add("read-121.json");
//		lsFiles.add("read-122.json");
//		lsFiles.add("read-123.json");
//		lsFiles.add("read-124.json");
//		lsFiles.add("read-125.json");
//		lsFiles.add("read-126.json");
//		lsFiles.add("read-127.json");
//		lsFiles.add("read-128.json");
//		lsFiles.add("read-129.json");
//		lsFiles.add("read-12.json");
//		lsFiles.add("read-130.json");
//		lsFiles.add("read-131.json");
//		lsFiles.add("read-132.json");
//		lsFiles.add("read-133.json");
//		lsFiles.add("read-13.json");
//		lsFiles.add("read-14.json");
//		lsFiles.add("read-15.json");
//		lsFiles.add("read-165.json");
//		lsFiles.add("read-16.json");
//		lsFiles.add("read-17.json");
//		lsFiles.add("read-185.json");
//		lsFiles.add("read-18.json");
//		lsFiles.add("read-19.json");
//		lsFiles.add("read-1.json");
//		lsFiles.add("read-205.json");
//		lsFiles.add("read-20.json");
//		lsFiles.add("read-21.json");
//		lsFiles.add("read-225.json");
//		lsFiles.add("read-22.json");
//		lsFiles.add("read-23.json");
//		lsFiles.add("read-245.json");
//		lsFiles.add("read-24.json");
//		lsFiles.add("read-25.json");
//		lsFiles.add("read-265.json");
//		lsFiles.add("read-26.json");
//		lsFiles.add("read-27.json");
//		lsFiles.add("read-285.json");
//		lsFiles.add("read-286.json");
//		lsFiles.add("read-287.json");
//		lsFiles.add("read-288.json");
//		lsFiles.add("read-289.json");
//		lsFiles.add("read-28.json");
//		lsFiles.add("read-290.json");
//		lsFiles.add("read-291.json");
//		lsFiles.add("read-292.json");
//		lsFiles.add("read-293.json");
//		lsFiles.add("read-294.json");
//		lsFiles.add("read-296.json");
//		lsFiles.add("read-297.json");
//		lsFiles.add("read-298.json");
//		lsFiles.add("read-299.json");
//		lsFiles.add("read-29.json");
//		lsFiles.add("read-2.json");
//		lsFiles.add("read-305.json");
//		lsFiles.add("read-30.json");
//		lsFiles.add("read-31.json");
//		lsFiles.add("read-325.json");
//		lsFiles.add("read-326.json");
//		lsFiles.add("read-327.json");
//		lsFiles.add("read-328.json");
//		lsFiles.add("read-329.json");
//		lsFiles.add("read-32.json");
//		lsFiles.add("read-330.json");
//		lsFiles.add("read-331.json");
//		lsFiles.add("read-332.json");
//		lsFiles.add("read-333.json");
//		lsFiles.add("read-334.json");
//		lsFiles.add("read-335.json");
//		lsFiles.add("read-336.json");
//		lsFiles.add("read-337.json");
//		lsFiles.add("read-338.json");
//		lsFiles.add("read-339.json");
//		lsFiles.add("read-33.json");
//		lsFiles.add("read-340.json");
//		lsFiles.add("read-341.json");
//		lsFiles.add("read-342.json");
//		lsFiles.add("read-343.json");
//		lsFiles.add("read-344.json");
//		lsFiles.add("read-345.json");
//		lsFiles.add("read-346.json");
//		lsFiles.add("read-347.json");
//		lsFiles.add("read-34.json");
//		lsFiles.add("read-35.json");
//		lsFiles.add("read-365.json");
//		lsFiles.add("read-36.json");
//		lsFiles.add("read-37.json");
//		lsFiles.add("read-385.json");
//		lsFiles.add("read-386.json");
//		lsFiles.add("read-387.json");
//		lsFiles.add("read-388.json");
//		lsFiles.add("read-38.json");
//		lsFiles.add("read-39.json");
//		lsFiles.add("read-3.json");
//		lsFiles.add("read-405.json");
//		lsFiles.add("read-40.json");
//		lsFiles.add("read-41.json");
//		lsFiles.add("read-425.json");
//		lsFiles.add("read-42.json");
//		lsFiles.add("read-43.json");
//		lsFiles.add("read-445.json");
//		lsFiles.add("read-44.json");
//		lsFiles.add("read-45.json");
//		lsFiles.add("read-465.json");
//		lsFiles.add("read-466.json");
//		lsFiles.add("read-467.json");
//		lsFiles.add("read-46.json");
//		lsFiles.add("read-47.json");
//		lsFiles.add("read-485.json");
//		lsFiles.add("read-486.json");
//		lsFiles.add("read-487.json");
//		lsFiles.add("read-488.json");
//		lsFiles.add("read-489.json");
//		lsFiles.add("read-48.json");
//		lsFiles.add("read-491.json");
//		lsFiles.add("read-492.json");
//		lsFiles.add("read-493.json");
//		lsFiles.add("read-494.json");
//		lsFiles.add("read-495.json");
//		lsFiles.add("read-496.json");
//		lsFiles.add("read-497.json");
//		lsFiles.add("read-498.json");
//		lsFiles.add("read-499.json");
//		lsFiles.add("read-49.json");
//		lsFiles.add("read-500.json");
//		lsFiles.add("read-501.json");
//		lsFiles.add("read-502.json");
//		lsFiles.add("read-503.json");
//		lsFiles.add("read-504.json");
//		lsFiles.add("read-505.json");
//		lsFiles.add("read-506.json");
//		lsFiles.add("read-507.json");
//		lsFiles.add("read-50.json");
//		lsFiles.add("read-51.json");
//		lsFiles.add("read-525.json");
//		lsFiles.add("read-52.json");
//		lsFiles.add("read-53.json");
//		lsFiles.add("read-545.json");
//		lsFiles.add("read-54.json");
//		lsFiles.add("read-55.json");
//		lsFiles.add("read-565.json");
//		lsFiles.add("read-566.json");
//		lsFiles.add("read-567.json");
//		lsFiles.add("read-568.json");
//		lsFiles.add("read-569.json");
//		lsFiles.add("read-56.json");
//		lsFiles.add("read-570.json");
//		lsFiles.add("read-571.json");
//		lsFiles.add("read-572.json");
//		lsFiles.add("read-573.json");
//		lsFiles.add("read-574.json");
//		lsFiles.add("read-575.json");
//		lsFiles.add("read-576.json");
//		lsFiles.add("read-577.json");
//		lsFiles.add("read-578.json");
//		lsFiles.add("read-579.json");
//		lsFiles.add("read-57.json");
//		lsFiles.add("read-580.json");
//		lsFiles.add("read-581.json");
//		lsFiles.add("read-582.json");
//		lsFiles.add("read-585.json");
//		lsFiles.add("read-586.json");
//		lsFiles.add("read-587.json");
//		lsFiles.add("read-588.json");
//		lsFiles.add("read-589.json");
//		lsFiles.add("read-58.json");
//		lsFiles.add("read-590.json");
//		lsFiles.add("read-59.json");
//		lsFiles.add("read-605.json");
//		lsFiles.add("read-606.json");
//		lsFiles.add("read-60.json");
//		lsFiles.add("read-61.json");
//		lsFiles.add("read-625.json");
//		lsFiles.add("read-626.json");
//		lsFiles.add("read-627.json");
//		lsFiles.add("read-62.json");
//		lsFiles.add("read-63.json");
//		lsFiles.add("read-645.json");
//		lsFiles.add("read-646.json");
//		lsFiles.add("read-64.json");
//		lsFiles.add("read-65.json");
//		lsFiles.add("read-665.json");
//		lsFiles.add("read-66.json");
//		lsFiles.add("read-67.json");
//		lsFiles.add("read-685.json");
//		lsFiles.add("read-68.json");
//		lsFiles.add("read-69.json");
//		lsFiles.add("read-705.json");
//		lsFiles.add("read-706.json");
//		lsFiles.add("read-70.json");
//		lsFiles.add("read-71.json");
//		lsFiles.add("read-725.json");
//		lsFiles.add("read-72.json");
//		lsFiles.add("read-73.json");
//		lsFiles.add("read-745.json");
//		lsFiles.add("read-74.json");
//		lsFiles.add("read-75.json");
//		lsFiles.add("read-765.json");
//		lsFiles.add("read-76.json");
//		lsFiles.add("read-77.json");
//		lsFiles.add("read-785.json");
//		lsFiles.add("read-78.json");
//		lsFiles.add("read-79.json");
//		lsFiles.add("read-805.json");
//		lsFiles.add("read-80.json");
//		lsFiles.add("read-81.json");
//		lsFiles.add("read-825.json");
//		lsFiles.add("read-826.json");
//		lsFiles.add("read-827.json");
//		lsFiles.add("read-828.json");
//		lsFiles.add("read-829.json");
//		lsFiles.add("read-82.json");
//		lsFiles.add("read-830.json");
//		lsFiles.add("read-831.json");
//		lsFiles.add("read-832.json");
//		lsFiles.add("read-833.json");
//		lsFiles.add("read-834.json");
//		lsFiles.add("read-835.json");
//		lsFiles.add("read-83.json");
//		lsFiles.add("read-845.json");
//		lsFiles.add("read-84.json");
//		lsFiles.add("read-85.json");
//		lsFiles.add("read-865.json");
//		lsFiles.add("read-86.json");
//		lsFiles.add("read-87.json");
//		lsFiles.add("read-885.json");
//		lsFiles.add("read-88.json");
//		lsFiles.add("read-89.json");
//		lsFiles.add("read-905.json");
//		lsFiles.add("read-90.json");
//		lsFiles.add("read-91.json");
//		lsFiles.add("read-925.json");
//		lsFiles.add("read-92.json");
//		lsFiles.add("read-93.json");
//		lsFiles.add("read-945.json");
//		lsFiles.add("read-946.json");
//		lsFiles.add("read-94.json");
//		lsFiles.add("read-95.json");
//		lsFiles.add("read-965.json");
//		lsFiles.add("read-96.json");
//		lsFiles.add("read-97.json");
//		lsFiles.add("read-985.json");
//		lsFiles.add("read-98.json");
//		lsFiles.add("read-99.json");
//				
//		return lsFiles;
//	}
//}
