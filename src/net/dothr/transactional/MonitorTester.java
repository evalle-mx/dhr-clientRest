package net.dothr.transactional;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.google.gson.Gson;

import net.dothr.MainAppTester;
import net.tce.dto.MonitorDto;
import net.tce.dto.TrackingMonitorDto;

public class MonitorTester extends MainAppTester {

	private static final String IDCONF = IDCONF_DOTHR;
	public final static String URI_MONITOR = "/module/monitor";
	public final static String URI_SUBSTITUTION= "/substitution";
	
	
	public static void main(String[] args) {
		try {
//			monitorCreate();
//			monitorGet();
//			monitorDelete();
			monitorSustitution();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static String monitorCreate() throws Exception {
		String jSon = null;
		
		gson = new Gson();
		MonitorDto mainDto;
		TrackingMonitorDto dto;
		//Principal
		mainDto = new MonitorDto();
		mainDto.setIdEmpresaConf(IDCONF);
		mainDto.setIdPersona("2");
		mainDto.setIdPosicion("5");
		
		List<TrackingMonitorDto> monitores = new ArrayList<TrackingMonitorDto>();
		
		dto = new TrackingMonitorDto();
		dto.setIdPersona("4");	//4-Giles, 7-Selex
		dto.setIdModeloRscPos("6");
		dto.setPrincipal("0");
		monitores.add(dto);
		//Json: {"nombre":"Cliente, DHR","idPosicion":"5","idPersona":"7","idRol":"5","idEmpresaConf":"1"}
		
		mainDto.setMonitores(monitores);
		
		System.out.println(gson.toJson(mainDto));
		jSon = getJsonFromService(gson.toJson(mainDto), URI_MONITOR+URI_CREATE );
		return jSon; 
	}
	
	
	public static String monitorGet() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );
		jsCont.put("idPersona", "2");
		jsCont.put("idPosicion", "13");
		
		String jSon = getJsonFromService(jsCont.toString(), URI_MONITOR+URI_GET );
//		System.out.println(jSon);
		return jSon; 
	}

	
	public static String monitorDelete() throws Exception {
		String jSon = null;
		
		gson = new Gson();
		MonitorDto mainDto;
		TrackingMonitorDto dto;
		//Principal
		mainDto = new MonitorDto();
		mainDto.setIdEmpresaConf(IDCONF);
		mainDto.setIdPersona("2");
		mainDto.setIdPosicion("7");
		
		List<TrackingMonitorDto> monitores = new ArrayList<TrackingMonitorDto>();
		
		dto = new TrackingMonitorDto();
		dto.setIdPersona("7");	//4-Giles, 7-Selex
//		dto.setIdModeloRscPos("6");
//		dto.setPrincipal("0");
		monitores.add(dto);
		//Json: {"nombre":"Cliente, DHR","idPosicion":"5","idPersona":"7","idRol":"5","idEmpresaConf":"1"}
		
		mainDto.setMonitores(monitores);
		
		System.out.println(gson.toJson(mainDto));
		jSon = getJsonFromService(gson.toJson(mainDto), URI_MONITOR+URI_DELETE );
		return jSon; 
	}
	
	/**
	 * MONITOR.S
	 * @return
	 * @throws Exception
	 */
	public static String monitorSustitution() throws Exception {
		String jSon = null;
		
		gson = new Gson();
		MonitorDto monitorDto;
		
		monitorDto = new MonitorDto();
		monitorDto.setIdEmpresaConf(IDCONF);
		monitorDto.setIdPersona("2");
		monitorDto.setIdPosicion("5");
		monitorDto.setIdPersonaMonitorSda("34");
		monitorDto.setIdPersonaMonitorSto("121");
		
		System.out.println(gson.toJson(monitorDto));
		jSon = getJsonFromService(gson.toJson(monitorDto), URI_MONITOR+URI_SUBSTITUTION );
		return jSon; 
	}
}
