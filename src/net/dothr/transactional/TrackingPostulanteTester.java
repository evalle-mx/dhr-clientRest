package net.dothr.transactional;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.google.gson.Gson;

import net.dothr.MainAppTester;
import net.tce.dto.CandidatoTrackingDto;
import net.tce.dto.TrackingEsquemaPersonaDto;
import net.tce.dto.TrackingMonitorDto;

public class TrackingPostulanteTester extends MainAppTester {

	private static final String IDCONF = IDCONF_DOTHR;
//	public final static String URI_TRACK_PERSONA = "/module/trackEsquemaPersona";
	public final static String URI_TRACK_POSTULANTE= "/module/trackPostulante";
	public final static String URI_TRACK_MONITOR = "/module/trackMonitor";
		
	final static String idEsquemaPerPos_Candidato = "1";
	final static String idEsquemaPP_Contratante = "2";
	
	public static void main(String[] args) {
		try {
			
			readTrackPostulante();			
//			getTrackingPostulante();
			
//			trackingEsqPersCreate();
			
//			updateTrackPersona();
			
//			testing();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
//	/**
//	 * Prueba el servicio TRACKPOSTULANT.AV (Octavio creo un servicio  llamado CALENDAR.GD para T.Monitor
//	 * @throws Exception
//	 */
//	public static void availableDate() throws Exception{
//		
//		JSONObject json = new JSONObject();
//		json.put(P_JSON_IDCONF, IDCONF);
//		json.put("idModeloRscPos", "45");
//		json.put("idModeloRscPosFase", "145");
//		//json.put("idTrackingPostulante", "18");   //Para mostrar las horas o días asignadas
//		
//		json.put("diaDisp", "5");
//		
//		
//		String jSon = getJsonFromService(json.toString(), URI_TRACK_POSTULANTE+URI_DISPONIBILIDAD );
//	}
	
	/**
	 * Obtiene un listado de Candidato y arreglo de trackingPostulante
	 * <nl>
	 * 	<li><b>IdPersona</b>. Candidaturas y tracking para persona</li>
	 *  <li><b>IdPosicion</b>. Candidatos con tracking por Posición</li>
	 * <nl>
	 * 	<br>
	 * Se usa en la pantalla de Procesos de Persona
	 * @return
	 * @throws Exception
	 */
	public static String getTrackingPostulante() throws Exception {
//		gson = new Gson();
//		TrackingEsquemaPersonaDto trackDto = new TrackingEsquemaPersonaDto();
//		trackDto.setIdEmpresaConf(IDCONF);
		
		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, IDCONF);
		json.put("idPersona", "128");
//		json.put("idPosicion", "5");
		
		String jSon = getJsonFromService(json.toString(), URI_TRACK_POSTULANTE+URI_GET );	
//		String jSon = getJsonFromService(json.toString(), URI_TRACK_POSTULANTE+"/test" );
		return jSon;
	}
	
	public static void readTrackPostulante() throws Exception{
		
		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, IDCONF);
		json.put(P_JSON_PERSONA, "2");
		
		json.put("idCandidato", "59");	//paola p5=>59,p9=>142
//		json.put("idPosibleCandidato", "1");
		
		String jSon = getJsonFromService(json.toString(), URI_TRACK_POSTULANTE+URI_READ );
	}
	
	public static void updateTrackPersona() throws Exception{
		
		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, IDCONF);
		json.put("idTrackingEsquemaPersona", "2");
		json.put("comentario", "Prueba de Update Candidato");
		json.put("idEstadoTracking", "2");
		
//		String jSon = getJsonFromService(json.toString(), URI_TRACK_PERSONA+URI_UPDATE );
	}
	
	/**
	 * Ejemplo para crear una nueva plantilla-rol con Estados
	 * @return
	 * @throws Exception
	 */
	public static String trackingEsqPersCreate() throws Exception {
		gson = new Gson();
		
		
		List<CandidatoTrackingDto> preCandidatos = 
//				getPreCandidato_C();
//				getPreCandidato_B(); 
				getPreCandidato_A();  
		//TODO * TipoPostulante? por qué no ocupar la tabla estatus_postulante y relacionarla con 	posible_candidato
			
		//Monitores (Se agrega a Esq-Persona)
		List<TrackingMonitorDto> monitores = new ArrayList<TrackingMonitorDto>();
		TrackingMonitorDto monDto = new TrackingMonitorDto();
		//
		monDto.setIdPersona("2");
//		monDto.setIdEsquemaPerfilPosicion(idEsquemaPP_Contratante);//Tracking-Contratante
		monDto.setCandidatos(preCandidatos);
		monitores.add(monDto);
		
		//Objeto Request
		TrackingEsquemaPersonaDto trackDto = new TrackingEsquemaPersonaDto();
		trackDto.setIdEmpresaConf(IDCONF);
		trackDto.setIdPersona("2");
		trackDto.setIdPosicion("5");
		trackDto.setMonitores(monitores);
//		trackDto.setOrdenTrackDefaultCumplido("1");		
		
		String jSon = "";// getJsonFromService(gson.toJson(trackDto), URI_TRACK_PERSONA+URI_CREATE );	
		return jSon;
	}
	
	/**
	 * Genera listado de Pre-candidatos A (tipoPostulante* = 1):
	 * cv publicado y coincide su area con la de la posicion 
	 * @return
	 * @throws Exception
	 */
	private static List<CandidatoTrackingDto> getPreCandidato_A() throws Exception{
		
		/*
		 * MOdifica la tabla candidato (id_estatus_postulante = 2)
		 */
		List<CandidatoTrackingDto> candidatos = new ArrayList<CandidatoTrackingDto>();
		CandidatoTrackingDto candDto;
		String idTipoPostulante = "1";
		/* **** 1) posible candidato_a  (Aceptado-Sin confirmar) *** */
			//Angel Pedro Elu (1o en indexado)
			candDto = new CandidatoTrackingDto();
			candDto.setIdEsquemaPerfilPosicion(idEsquemaPerPos_Candidato);
			candDto.setTipoPostulante(idTipoPostulante);
			candDto.setIdCandidato("1");
			candDto.setOrden("1");
//			candDto.setConfirmado("1");candDto.setOrdenTrackDefaultCumplido("1");
			
			candidatos.add(candDto);
			
//			//Lucero guadalupe rodriguez gutierrez (Aceptada-Mejor indexada)
//			candDto = new CandidatoTrackingDto();
//			candDto.setIdEsquemaPerfilPosicion(idEsquemaPerPos_Candidato);//Tracking-Candidato
//			candDto.setTipoPostulante(idTipoPostulante);
//			candDto.setIdCandidato("57");
//			candDto.setOrden("1");
//			
//			candDto.setConfirmado("1");candDto.setOrdenTrackDefaultCumplido("1");
//			candidatos.add(candDto);
			
//			//Héctor Wilfrido Gutiérrez (Rechazado por salario)
//			candDto = new CandidatoTrackingDto();
//			candDto.setIdEsquemaPerfilPosicion(idEsquemaPerPos_Candidato);
//			candDto.setTipoPostulante(idTipoPostulante);
//			candDto.setIdCandidato("36");
//			candDto.setOrden("1");
////			candDto.setConfirmado("1");candDto.setOrdenTrackDefaultCumplido("1");
//			
//			candidatos.add(candDto);//*/
		
		/* *********2) posible candidato_a  (Rechazado-Confirmado)*** * /
			//ALFREDO  RIOS
			candDto = new CandidatoTrackingDto();
			candDto.setIdEsquemaPerfilPosicion(idEsquemaPerPos_Candidato);
			candDto.setTipoPostulante(idTipoPostulante);
			candDto.setIdCandidato("2");
			candDto.setOrden("1");
			candDto.setConfirmado("1");candDto.setOrdenTrackDefaultCumplido("1");
			
			candidatos.add(candDto); //*/
			
		return candidatos;
	}
	/**
	 * Genera listado de Pre-candidatos B (tipoPostulante* = 2): 
	 * CV en el sistema: publicado(no coincide su area con la de la posicion)
	 *  o no publicado (Sin area de clasificacion/Indexado)
	 * @return
	 * @throws Exception
	 */
	private static List<CandidatoTrackingDto> getPreCandidato_B() throws Exception{
		/*
		 * Inserta en la tabla posible_candidato
		 */
		List<CandidatoTrackingDto> candidatos = new ArrayList<CandidatoTrackingDto>();
		CandidatoTrackingDto candDto;
		String idTipoPostulante = "2";
		
		/* ************  Posible Candidato B (SIn Confirmar) ***** *
			//Ana Paula Fuentes (Area 5, Ciencias de la Salud)
			candDto = new CandidatoTrackingDto();
			candDto.setIdEsquemaPerfilPosicion(idEsquemaPerPos_Candidato);
			candDto.setIdPersona("10");
			candDto.setTipoPostulante(idTipoPostulante);
			
			candidatos.add(candDto);
			
			//Violeta Zetzengari Romero (Area 13: Humanidades y Artes)
			candDto = new CandidatoTrackingDto();
			candDto.setIdEsquemaPerfilPosicion(idEsquemaPerPos_Candidato);
			candDto.setIdPersona("45");
			candDto.setTipoPostulante(idTipoPostulante);
			
			candidatos.add(candDto);
			
			
			
			//*/
		
			//Carlos Rafael Galicia (area 21: Tecnologías de Información (TI))
			candDto = new CandidatoTrackingDto();
			candDto.setIdEsquemaPerfilPosicion(idEsquemaPerPos_Candidato);
			candDto.setTipoPostulante(idTipoPostulante);
			candDto.setIdPersona("21");
			candDto.setConfirmado("1");
			candidatos.add(candDto);
			
		return candidatos;
	}
	/**
	 * Genera listado de Pre-candidatos C (tipoPostulante* = 3):no esta en el sistema y es ingresado por contratante de manera manual
	 * 
	 * <i>Se extrae info de /home/dothr/JsonUI/PersonaRecreate/curriculumManagement/XE.xls</i>
	 * @return
	 * @throws Exception
	 */
	private static List<CandidatoTrackingDto> getPreCandidato_C() throws Exception{
		List<CandidatoTrackingDto> candidatos = new ArrayList<CandidatoTrackingDto>();
		CandidatoTrackingDto candDto;
		String idTipoPostulante = "3";
		/*
		 * posible candidato C (Sin COnfirmar)
		 * [Inserta en posible_candidato y Persona (persona, hist_pwd, relacion_emp-per)]
		 *
		//Eduardo Castro (castroboullosa@gmail.com, carga-P3)
		candDto = new CandidatoTrackingDto();
		candDto.setIdEsquemaPerfilPosicion(idEsquemaPerPos_Candidato);
		candDto.setTipoPostulante(idTipoPostulante);
		candDto.setEmail("castroboullosa@gmail.com");
		candDto.setNombre("Eduardo");
		candDto.setApellidoPaterno("Castro");

		candidatos.add(candDto);		
		
		//Jorge Pedroza (yorgos@live.it)
		candDto = new CandidatoTrackingDto();
		candDto.setIdEsquemaPerfilPosicion(idEsquemaPerPos_Candidato);
		candDto.setTipoPostulante(idTipoPostulante);
		candDto.setEmail("yorgos@live.it");
		candDto.setNombre("Jorge");
		candDto.setApellidoPaterno("Pedroza");

		candidatos.add(candDto); //*/
		
		/* Precandidatos 3 (Confirmados) */
		
		//Mauricio Agustín Meza (maumo_91@hotmail.com)
		candDto = new CandidatoTrackingDto();
		candDto.setIdEsquemaPerfilPosicion(idEsquemaPerPos_Candidato);
		candDto.setTipoPostulante(idTipoPostulante);
		candDto.setEmail("maumo_91@hotmail.com");
		candDto.setNombre("Mauricio Agustín");
		candDto.setApellidoPaterno("Meza");

		candidatos.add(candDto); //*/
		
		return candidatos;
	}
	
	public static String testing() throws Exception {
		
		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, IDCONF);
		json.put("idPersona", "128");
		json.put("idTrackingPostulante", "17");
		
		String jSon = getJsonFromService(json.toString(), URI_TRACK_POSTULANTE+"/test" );
		return jSon;
	}
	
}
