package net.dothr.transactional;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

import net.dothr.MainAppTester;
import net.tce.dto.ModeloRscDto;

/**
 * Clase de pruebas para el servicio <b>MODELORSC</b>
 * @author dothr
 *
 */
public class ModeloRscTester extends MainAppTester {
	
	private static final String IDCONF = IDCONF_DOTHR;
//	public final static String URI_TRACK_PLANTILLA = "/module/tracktemplate";
	public final static String URI_MODELORSC = "/module/modeloRsc";
	public final static String URI_ROLS_TEMPLATE ="/getRols";
	
	private static Logger log4j = Logger.getLogger( ModeloRscTester.class );
	
	public static void main(String[] args) {
		try {
//			trackingPlantCreate();
//			trackingPlantRead();
//			trackingPlantUpdate();
			trackingPlantFaseUpdate();
//			trackingPlantGet();
//			trackingPlantDelete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 
	 * Ejemplo para crear nuevo ModeloRSC (plantilla) con Fases (Actividades)<br>
	 * <b>MODELRSC.C</b>
	 * @return
	 * @throws Exception
	 */
	public static String trackingPlantCreate() throws Exception {
		log4j.debug("<trackingPlantCreate>");
		
		gson = new Gson();
		ModeloRscDto dto = new ModeloRscDto();
		dto.setIdEmpresaConf(IDCONF);
		dto.setNombre("Plantilla Prueba");
		dto.setDescripcion("Plantilla de prueba nueva configuración");
		dto.setActivo("1");
		
		List<ModeloRscDto> fases = new ArrayList<ModeloRscDto>();
		
		ModeloRscDto dtoFase = new ModeloRscDto();
		dtoFase.setNombre("Actividad 1");
		dtoFase.setOrden("1");
		dtoFase.setDescripcion("Primera Actividad");
		fases.add(dtoFase);
		dtoFase = new ModeloRscDto();
		dtoFase.setNombre("Actividad 2");
		dtoFase.setOrden("2");
		dtoFase.setDescripcion("Segunda Actividad");
		fases.add(dtoFase);
		dtoFase = new ModeloRscDto();
		dtoFase.setNombre("Actividad 3a");
		dtoFase.setOrden("3");
		dtoFase.setDescripcion("Tercera Actividad uno");
		fases.add(dtoFase);
		dtoFase = new ModeloRscDto();
		dtoFase.setNombre("Actividad 3b");
		dtoFase.setOrden("3");
		dtoFase.setDescripcion("Tercera Actividad dos");
		fases.add(dtoFase);
		
		dto.setFases(fases);
		
		String jSon = getJsonFromService(gson.toJson(dto), URI_MODELORSC+URI_CREATE );
		return jSon;
	}
	
	/**
	 * NOFUNCIONA<br>Ejemplo de lectura de ModeloRSC
	 * <b>MODELRSC.R</b>
	 * @return
	 * @throws Exception
	 */
	public static String trackingPlantRead() throws Exception {
		gson = new Gson();
		ModeloRscDto dto = new ModeloRscDto();
		dto.setIdEmpresaConf(IDCONF);
		dto.setIdModeloRsc("5");
//		dto.setModo("0");
		String jSon = getJsonFromService(gson.toJson(dto), URI_MODELORSC+URI_READ );	
		return jSon; 
	}
	
	/**
	 * Actualizar Modelo RSC (y fases)<br>
	 * <b>MODELRSC.U</b>
	 * @return
	 * @throws Exception
	 */
	public static String trackingPlantUpdate() throws Exception {
		gson = new Gson();
		ModeloRscDto dto = new ModeloRscDto();
		dto.setIdEmpresaConf(IDCONF);
		dto.setIdModeloRsc("5");
		dto.setNombre("Prueba ModeloRSC");
		dto.setDescripcion("Plantilla ModeloRSC con Fases");
		dto.setActivo("1");
		
		/*
		List<ModeloRscDto> fases = new ArrayList<ModeloRscDto>();
		ModeloRscDto dtoFase = new ModeloRscDto();
		dtoFase.setNombre("Publicar CV");
		dtoFase.setOrden("1");
		dtoFase.setDescripcion("El Candidato valida y publica su información");
		fases.add(dtoFase);
		dtoFase = new ModeloRscDto();
		dtoFase.setNombre("Contacto Contratante");
		dtoFase.setOrden("2");
		dtoFase.setDescripcion("El contratante hará un contacto inicial");
		fases.add(dtoFase);
		dtoFase = new ModeloRscDto();
		dtoFase.setNombre("Entrevistas Técnica");
		dtoFase.setOrden("3");
		dtoFase.setDescripcion("Se realiza la entrevista técnica");
		fases.add(dtoFase);
		dtoFase = new ModeloRscDto();
		dtoFase.setNombre("Entrevista RH");
		dtoFase.setOrden("3");
		dtoFase.setDescripcion("Entrevista con Recursos Humanos");
		fases.add(dtoFase);
		dtoFase = new ModeloRscDto();
		dtoFase.setNombre("Oferta");
		dtoFase.setOrden("4");
		dtoFase.setDescripcion("Se emite una oferta para ser evaluada por el candidato");
		fases.add(dtoFase);
		dtoFase = new ModeloRscDto();
		dtoFase.setNombre("Contrato");
		dtoFase.setOrden("5");
		dtoFase.setDescripcion("Después de la aceptación de la Oferta, se firma el Contrato");
		fases.add(dtoFase);
		
		dto.setFases(fases);//*/
		

		String jSon = getJsonFromService(gson.toJson(dto), URI_MODELORSC+URI_UPDATE );	
		return jSon; 
	}
	
	/**
	 * Actualiza unicamente la fase determinada<br>
	 * <b>MODELRSC.U</b>
	 * @return
	 * @throws Exception
	 */
	public static String trackingPlantFaseUpdate() throws Exception {
		
		gson = new Gson();
		ModeloRscDto dto = new ModeloRscDto();
		dto.setIdEmpresaConf(IDCONF);
		
		dto.setIdModeloRscFase("6");
		dto.setNombre("Publicación de CV");
		dto.setDescripcion("Candidato valida y publica información.");
		String jSon = getJsonFromService(gson.toJson(dto), URI_MODELORSC+URI_UPDATE );
		return jSon; 
	}
	

	/**
	 * Obtiene el listado de plantillas<br>
	 * <b>MODELRSC.G</b>
	 * 
	 * [No funciona con modo completo "1"]
	 * @return
	 * @throws Exception
	 */
	public static String trackingPlantGet() throws Exception {
		gson = new Gson();
		ModeloRscDto dto = new ModeloRscDto();
		dto.setIdEmpresaConf(IDCONF);
		dto.setActivo("1");
		
//		dto.setModo("1");//falla
		
		String jSon = getJsonFromService(gson.toJson(dto), URI_MODELORSC+URI_GET );
		
		return jSon; 
	}
	
	/**
	 * Elimina (Desactiva) la plantilla solicitada <br>
	 * <b>MODELRSC.G</b>
	 * @return
	 * @throws Exception
	 */
	public static String trackingPlantDelete() throws Exception {
		gson = new Gson();
		ModeloRscDto dto = new ModeloRscDto();
		dto.setIdEmpresaConf(IDCONF);
		dto.setIdModeloRsc("5");
		
		String jSon = getJsonFromService(gson.toJson(dto), URI_MODELORSC+URI_DELETE );	
		return jSon; 
	}
	
//	public static String trackingPlantGetRols() throws Exception {
//		JSONObject jsCont = new JSONObject();
//		jsCont.put(P_JSON_IDCONF, IDCONF );
//		
//		String jSon = getJsonFromService(jsCont.toString(), URI_TRACK_PLANTILLA+URI_ROLS_TEMPLATE );	
//		return jSon; 
//	}
	
	
	
	/* ****************************  FASES   ************************* */
	public static String modRscFaseCreate() throws Exception {
		log4j.debug("<modRscFaseCreate>");
		
		gson = new Gson();
		ModeloRscDto dto = new ModeloRscDto();
		dto.setIdEmpresaConf(IDCONF);
		dto.setIdPersona("2");
		
		dto.setIdModeloRsc("5");
		dto.setNombre("ENtrevista 2(cliente)");
		dto.setDescripcion("Act.4, enviado ClienteRest");
		
		
		String jSon = getJsonFromService(gson.toJson(dto), URI_MODELORSC+URI_CREATE );
		return jSon;
	}
}
