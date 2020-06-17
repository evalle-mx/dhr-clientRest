package net.dothr.recreate;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;

import net.dothr.MainAppTester;
import net.tce.dto.ModeloRscDto;
import net.utils.ClientRestUtily;
import net.utils.ConstantesREST;

public class ModeloPosicionFromJson extends MainAppTester {

	public final static String URI_TRACK_MODELO_RSC_POSICION = "/module/modeloRscPos";
	private final static String PATH_MRSC_POS_REP = "/home/dothr/workspace/ClientRest/files/out/Rep.MRSC-Posicion.txt";
	private static Logger log4j = Logger.getLogger( ModeloPosicionFromJson.class );
	
	public static void main(String[] args) {
		try {
			StringBuilder sb = loadModelos();
			System.out.println(sb);
			ClientRestUtily.writeStringInFile(PATH_MRSC_POS_REP, sb.toString(), false);
			log4j.debug("Se escribio resultado de carga de Modelos Posicion en \n" + PATH_MRSC_POS_REP );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public static StringBuilder loadModelos() throws Exception {
		gson = new Gson();
		String idPersona = "2";
		String idPosicion = "5";
		
		StringBuilder sb = new StringBuilder();
		String jSon=null;
		ModeloRscDto dto = new ModeloRscDto();
		dto.setIdEmpresaConf(ConstantesREST.IDEMPRESA_CONF);
		
		dto.setIdPersona(idPersona);
		dto.setIdPosicion(idPosicion);
		
		
		//A. ModeloRscPos principal (Pivote)
		//1. Creación a partir de plantilla
		sb.append("Creación a partir de plantilla \n");
		dto.setIdModeloRsc("5");	//5,6,7
		dto.setNombre("ModeloRscPos Pivote");
		dto.setDescripcion("Plantilla RSC abstracta con todos los pasos a seguir en el proceso");		
		jSon = getJsonFromService(gson.toJson(dto), URI_TRACK_MODELO_RSC_POSICION+URI_CREATE ); /* [{"name":"idModeloRscPos","value":"1","code":"004","type":"I"}] */
		
		sb.append(jSon).append("\n");
		//convertir a JSONArray+JSONObject para obtener idModeloRscPos
		JSONArray jsRsp = new JSONArray(jSon);
		JSONObject jsonZero = jsRsp.getJSONObject(0);
		String idModeloRscPos = null;
		if(jsonZero.has("name") && jsonZero.has("value") && 
				(jsonZero.has("type") && jsonZero.getString("type").equals("I"))  ){
			idModeloRscPos= jsonZero.getString("value");
			sb.append("Se obtuvo idModeloRscPos Principal: ").append(idModeloRscPos).append("\n\n");

			
			//B. ModeloRscPos secundario
			//*/2. Creación Postulante a partir de plantillaPrincipal (Clonación)
			sb.append("B. Postulante a partir de plantillaPrincipal \n");
			dto = new ModeloRscDto();
			dto.setIdEmpresaConf(ConstantesREST.IDEMPRESA_CONF);
			dto.setIdPersona(idPersona);
			
			dto.setIdModeloRscPos(idModeloRscPos);
			dto.setNombre("ModeloRscPos Postulante");
			dto.setDescripcion("ModRscPos con perfil de Postulante");
			dto.setIdRol("3");
			dto.setMonitor("0");
			
			jSon = getJsonFromService(gson.toJson(dto), URI_TRACK_MODELO_RSC_POSICION+URI_CREATE ); /*  [{"name":"idModeloRscPos","value":"2","code":"004","type":"I"}]*/
			sb.append(jSon).append("\n\n");
			
			//*/3. Creación Monitor principal a partir de plantillaPrincipal
			sb.append("C. Creación Monitor principal a partir de plantillaPrincipal \n");
			dto = new ModeloRscDto();
			dto.setIdEmpresaConf(ConstantesREST.IDEMPRESA_CONF);
			dto.setIdPersona(idPersona);
			
			dto.setIdModeloRscPos(idModeloRscPos);
			dto.setNombre("ModeloRscPos Monitor Principal");
			dto.setDescripcion("ModRscPos con perfil de Contratante (Principal)");
			dto.setIdRol("4");		
			dto.setMonitor("1");
			dto.setPrincipal("1");
			
			jSon = getJsonFromService(gson.toJson(dto), URI_TRACK_MODELO_RSC_POSICION+URI_CREATE ); /*   [{"name":"idModeloRscPos","value":"3","code":"004","type":"I"}] */
			sb.append(jSon).append("\n\n");
		}else{
			sb.append("Error al crear modelo Principal \n");
		}
		
		sb.append("FIN");
		return sb;
	}
}
