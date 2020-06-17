package net.dothr.transactional;

import java.util.ArrayList;
import java.util.List;

//import org.apache.log4j.Logger;
import com.google.gson.Gson;

import net.dothr.MainAppTester;
import net.tce.dto.ModeloRscDto;
import net.tce.dto.TrackingDto;
/**
 * Clase de pruebas para el servicio <b>MODELRSCPOS</b>
 * @author dothr
 *
 */
public class ModRscPosicionTester extends MainAppTester {
	
	private static final String IDCONF = IDCONF_DOTHR;
//	public final static String URI_TRACKING = "/module/tracking";
	public final static String URI_TRACK_MODELO_RSC_POSICION = "/module/modeloRscPos";
	public final static String URI_TRACK_MODELO_RSC_POSFASE = "/module/modeloRscPosFase";
	
	//URI_CURRICULUM
//	public final static String URI_CVTRACK="/tracking";
	
//	private static Logger log4j = Logger.getLogger( ModRscPosicionTester.class );
	
	public static void main(String[] args) {
		try {
			
//			trackingPosCreate();
//			trackingPosRead();
			trackingPosGet();
//			trackingPosUpdate();
			trackingPosDelete();
			
//			trackingPosFaseUpdate();
			
//			curriculumTracking();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	/* ++++++  IMPLEMENTACION DE TRACKING (POSICION) ++++++ */
	public static String trackingPosCreate() throws Exception {
		gson = new Gson();
		String jSon=null;
		ModeloRscDto dto = new ModeloRscDto();
		dto.setIdEmpresaConf(IDCONF);
		dto.setIdPersona("2");
		dto.setIdPosicion("5");
		
		//A. ModeloRscPos principal (Pivote)
		/*/1. Creación a partir de plantilla
		dto.setIdModeloRsc("6");	//5,6,7
		dto.setNombre("ModeloRscPos Pivote");
		dto.setDescripcion("Plantilla RSC abstracta con todos los pasos a seguir en el proceso");//*/
		
		//B. ModeloRscPos secundario
		//*/2. Creación Postulante a partir de plantillaPrincipal (Clonación)
		dto.setIdModeloRscPos("1");	//5
		dto.setIdRol("3");
		dto.setNombre("ModeloRscPos Postulante");
		dto.setDescripcion("ModRscPos con perfil de Postulante");
		dto.setMonitor("0"); //*/
		
		/*/3. Creación Monitor principal a partir de plantillaPrincipal
		dto.setIdModeloRscPosicion("5");	//5
		dto.setNombre("ModeloRscPos Monitor Principal");
		dto.setDescripcion("ModRscPos con perfil de Contratante (Principal)");
		dto.setIdRol("4");		
		dto.setMonitor("1");
		dto.setPrincipal("1");//Monitor principal //*/
		
		/*/C. Actualizacion de fase/actividad		
		dto.setIdModeloRscPosicion("5");	//5
		dto.setNombre("Bienvenida");
		dto.setDescripcion("Actividad nueva al final ");
		dto.setOrden("8");		
		dto.setSubirArchivo("0");
		dto.setBajarArchivo("0");
		dto.setEditarComentario("0");//*/
		
		if(dto.getNombre()!=null){
			jSon = getJsonFromService(gson.toJson(dto), URI_TRACK_MODELO_RSC_POSICION+URI_CREATE );
		}
		
		return jSon;
	}
	
	public static String trackingPosUpdate() throws Exception {
		gson = new Gson();
		ModeloRscDto dto = new ModeloRscDto();
		dto.setIdEmpresaConf(IDCONF);
		dto.setIdPersona("2");
		
		/*/1. ModeloRscPosicion 
		//I. Información general
		dto.setIdModeloRscPosicion("5");
		dto.setNombre("ModeloRscPos Pivote actualizado");
		dto.setDescripcion("Plantilla RSC abstracta con pasos a seguir en el proceso");
		
		//II. Arreglo de Fases
		List<ModeloRscDto> fases = new ArrayList<ModeloRscDto>();		
		ModeloRscDto dtoFase = new ModeloRscDto();
		dtoFase.setNombre("Actividad 1");
		dtoFase.setOrden("1");
		dtoFase.setDescripcion("Primera Actividad");
		dtoFase.setSubirArchivo("0");
		dtoFase.setBajarArchivo("0");
		dtoFase.setEditarComentario("1");
		//dtoFase.setActivo("1");//Solo aplica para secundarios
		fases.add(dtoFase);
		dtoFase = new ModeloRscDto();
		dtoFase.setNombre("Actividad 2");
		dtoFase.setOrden("2");
		dtoFase.setDescripcion("Segunda Actividad");
		dtoFase.setSubirArchivo("1");
		dtoFase.setBajarArchivo("1");
		dtoFase.setEditarComentario("0");
//		dtoFase.setActivo("1");//Solo aplica para secundarios
		fases.add(dtoFase);
		dtoFase = new ModeloRscDto();
		dtoFase.setNombre("Evaluación");
		dtoFase.setOrden("2");
		dtoFase.setDescripcion("Evaluación, Segunda Actividad Bis");
		dtoFase.setIdModeloRscPosFase("53");//idModeloRscPosFase
		dtoFase.setSubirArchivo("1");
		dtoFase.setBajarArchivo("0");
		dtoFase.setEditarComentario("1");
		//dtoFase.setActivo("1");//Solo aplica para secundarios
		fases.add(dtoFase);		
		dtoFase = new ModeloRscDto();
		dtoFase.setNombre("Actividad 3a");
		dtoFase.setOrden("3");
		dtoFase.setDescripcion("Tercera Actividad uno");
		dtoFase.setSubirArchivo("0");
		dtoFase.setBajarArchivo("0");
		dtoFase.setEditarComentario("0");
		//dtoFase.setActivo("1");//Solo aplica para secundarios
		fases.add(dtoFase);
		dtoFase = new ModeloRscDto();
		dtoFase.setNombre("Actividad 3b");
		dtoFase.setOrden("3");
		dtoFase.setDescripcion("Tercera Actividad dos");
		dtoFase.setSubirArchivo("0");
		dtoFase.setBajarArchivo("0");
		dtoFase.setEditarComentario("1");
		//dtoFase.setActivo("1");//Solo aplica para secundarios
		fases.add(dtoFase);		
		dto.setFases(fases);//*/
		
		//2. Fase (Estado/Actividad)
		dto.setIdModeloRscPosFase("54");
		dto.setNombre("Varias Evaluaciones");
		dto.setDescripcion("Diversos Examen de aptitudes y conocimientos generales");
		
		String jSon = getJsonFromService(gson.toJson(dto), URI_TRACK_MODELO_RSC_POSICION+URI_UPDATE );
		return jSon;
	}
	
	public static String trackingPosDelete() throws Exception {
		gson = new Gson();
		ModeloRscDto dto = new ModeloRscDto();
		dto.setIdEmpresaConf(IDCONF);
		dto.setIdPersona("2");
		
//		/*/1. Todo el ModRsc-Posicion
		dto.setIdModeloRscPos("35"); // */
		
		/*/2. SOlo una fase
		dto.setIdModeloRscPosFase("52"); //*/
		
		
		String jSon = getJsonFromService(gson.toJson(dto), URI_TRACK_MODELO_RSC_POSICION+URI_DELETE );
		return jSon;
	}
	
	public static String trackingPosRead() throws Exception {
		gson = new Gson();
		ModeloRscDto dto = new ModeloRscDto();
		dto.setIdEmpresaConf(IDCONF);
		dto.setIdPersona("2");
		/*/1. ModRscPos
		dto.setIdModeloRscPos("32");//*/
//		dto.setModo("0");
		
		//2. Fase
//		dto.setIdModeloRscPosFase("54");//*/
		
		String jSon = getJsonFromService(gson.toJson(dto), URI_TRACK_MODELO_RSC_POSICION+URI_READ );
		return jSon;
	}
	
	public static String trackingPosGet() throws Exception {
		gson = new Gson();
		TrackingDto trackDto = new TrackingDto();
		trackDto.setIdEmpresaConf(IDCONF);
		trackDto.setIdPersona("2");
		
//		trackDto.setMonitor("0");
		trackDto.setActivo("1");
//		trackDto.setIdRol("3");	//1-Adm Sistema, 2-AdmOper, 3-Candidato, 4-contratante, 5-cliente, 
		
		//* idPosicion (All Esquema_perfil_posicion assigned to position id:x) 
		trackDto.setIdPosicion("9");
		trackDto.setIdRol("7"); //7-Consultor
		trackDto.setModo("1"); //  0 | 1 (Full info -with TrackEsquema's-) //*/ 
		
		
		
		/* EsquemaPerfil's por Posicion (obtiene todos los trackings asignados a perfil) 
		trackDto.setIdPerfilPosicion("5");
		trackDto.setModo("0");// 0 | 1  */
		
		/* TrackingEsquema (Obtiene los Estados de un Tracking) 
		trackDto.setIdEsquemaPerfilPosicion("1");//*/
		
		String jSon = getJsonFromService(gson.toJson(trackDto), URI_TRACK_MODELO_RSC_POSICION+URI_GET );	
		return jSon; 
	}
	
	/* **********  SERVICIO EN CURRICULUM VITAE PARA TRACKING  *********** */
	
//	public static String curriculumTracking() throws Exception {
//		JSONObject jsCont = new JSONObject();
//		jsCont.put(P_JSON_IDCONF, IDCONF );
//		jsCont.put("idPersona", "2");
//		
//		String jSon = getJsonFromService(jsCont.toString(), URI_CURRICULUM+URI_CVTRACK );	
//		return jSon; 
//	}
	
	
	
	protected static List<TrackingDto> generaTrackingEsquemas(){ /*geTrackingEsquemas*/
		List<TrackingDto> lsTrack = new ArrayList<TrackingDto>();
		int dia = 10;
		String fecha = "2018-03-<DAY> 00:00:00";	//2018-03-01 18:02:53
		for(int x=0; x<5;x++){
			lsTrack.add( setInfoDto("Estdo-"+(x+1), "Estado n: "+(x+1), ""+(x+1), ""+(x%2), ""+(x/2), "1",  
					fecha.replace("<DAY>", String.valueOf(dia)), fecha.replace("<DAY>", String.valueOf(dia+1))) );
			dia = dia+2;
		}
		return lsTrack;
	}
	
	private static TrackingDto setInfoDto(String nombre, String des, String orden, String upload, 
				String download, String editCom, String fechaIni, String fechaFin){
		TrackingDto dto = new TrackingDto();
		dto.setNombre(nombre);
		dto.setDescripcion(des);
		dto.setOrden(orden);
		dto.setSubirArchivo(upload);
		dto.setBajarArchivo(download);
		dto.setEditarComentario(editCom);
		dto.setFechaInicio(fechaIni);
		dto.setFechaFin(fechaFin);
		return dto;
	}
	
	
	
	/* ************************ MODELO FASE  ******************************* */
	public static String trackingPosFaseUpdate() throws Exception {
		gson = new Gson();
		ModeloRscDto dto = new ModeloRscDto();
		dto.setIdEmpresaConf(IDCONF);
		dto.setIdPersona("2");
		
		dto.setDescripcion("El contratante generá y publica la vacante");
		dto.setEditarComentario("1");
		dto.setFechaInicio("2019-05-01 00:00:00"); //yyyy-MM-dd HH:mm:ss
		dto.setBajarArchivo("0");
		dto.setDias("2");
		dto.setSubirArchivo("1");
		dto.setOrden("1");
		dto.setIdModeloRscPosFase("1");
		dto.setNombre("Publicar Posición");
		dto.setActivo("1");
			
		
		String jSon = getJsonFromService(gson.toJson(dto), URI_TRACK_MODELO_RSC_POSFASE+URI_UPDATE );
		return jSon;
	}
}
