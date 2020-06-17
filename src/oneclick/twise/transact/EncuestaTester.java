package oneclick.twise.transact;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.google.gson.Gson;

import netto.AppTester;
import oneclick.twise.dto.EncuestaDto;
import oneclick.twise.dto.ReactivoDto;

public class EncuestaTester extends AppTester {
	
	public final static String URI_ENCUESTA = "/module/encuesta";
	
	public static void main(String[] args) {
		try { //ping(URI_ENCUESTA);
//			encuestaGet();
//			encuestaRead();
			encuestaCuestionario();
//			encuestaUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String encuestaGet() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put("idEmpresaConf", IDCONF );
//		jsCont.put("idUsuario", "46"); jsCont.put("modo", "1"); /*=>Evaluaciones por usuario */		
		
//		jsCont.put("modo", "2"); /*=>Lista de Evaluados y resultados por evaluador */

		jsCont.put("idEvaluado", "10"); jsCont.put("modo", "3");/* Solo para modo 3 (Lista de Evaluadores por evaluado) */
		String jSon = getJsonFromService(jsCont.toString(), URI_ENCUESTA+METHOD_GET );	
		return jSon; 
	}
	
	public static String encuestaRead() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put("idEmpresaConf", IDCONF );
		
		jsCont.put("idUsuario", "1");
		jsCont.put("idEvaluacion", "2");
		
		String jSon = getJsonFromService(jsCont.toString(), URI_ENCUESTA+METHOD_READ );	
		return jSon; 
	}
	
	
	public static String encuestaUpdate() throws Exception {
		gson = new Gson();
		/*//No funciona con JSON por los objetos anidados
		JSONObject jsCont = new JSONObject();
		jsCont.put("idEmpresaConf", IDCONF );
		jsCont.put("idUsuario", "2");
		jsCont.put("idEvaluacion", "1");
		jsCont.put("respuestas", "[{ \"idReactivo\": \"5\", \"texto\": \"DUMMY Es hábil para negociar efectivamente temas críticos, llegar a acuerdos en los que todas las partes ganen y sin perder el sentido del tiempo.\", \"nResp\": \"3\" }, { \"idReactivo\": \"2\", \"texto\": \"Actúa rápidamente para resolver problemas personales y previene su recurrencia.\", \"nResp\": \"3\" }, { \"idReactivo\": \"3\", \"texto\": \"Sabe identificar eficazmente las fuentes de conflicto y conciliar las diferencias que surjan.\", \"nResp\": \"3\" }, { \"idReactivo\": \"1\", \"texto\": \"Pueden establecer acuerdos duros y resolver las disputas equitativamente.\", \"nResp\": \"3\" }, { \"idReactivo\": \"6\", \"texto\": \"Puede negociar de manera diplomática, así como de una forma directa y enérgica cuando se requiere.\", \"nResp\": \"3\" }, { \"idReactivo\": \"7\", \"texto\": \"Cuenta con la capacidad para comunicar la misión, visión, valores y objetivos de la organización y motiva a su equipo a identificarlas y participar de ellos.\", \"nResp\": \"3\" }, { \"idReactivo\": \"4\", \"texto\": \"Describe las ventajas de sus propuestas a los demás y soluciona las diferencias con otras personas y equipos sin dañar las relaciones.\", \"nResp\": \"3\" }, { \"idReactivo\": \"8\", \"texto\": \"Identifica las oportunidades y problemas futuros y se anticipa generando planes de acción.\", \"nResp\": \"3\" }, { \"idReactivo\": \"9\", \"texto\": \"Realiza de una manera equilibrada evaluación de su equipo de trabajo, reconociendo el buen desempeño y otorgando asesoria puntual en las áreas de oportunidad de su equipo de trabajo.\", \"nResp\": \"3\" }]");				
		*/
		EncuestaDto encDto = new EncuestaDto();
		encDto.setIdEmpresaConf(IDCONF);
		encDto.setIdUsuario("2");
		encDto.setIdEvaluacion("1");
		
		//Respuestas
		List<ReactivoDto> respuestas = new ArrayList<ReactivoDto>();
		respuestas.add(new ReactivoDto("61","1","4"));
		respuestas.add(new ReactivoDto("62","2","1"));
		respuestas.add(new ReactivoDto("63","3","3"));
		respuestas.add(new ReactivoDto("64","4","4"));
		respuestas.add(new ReactivoDto("65","5","3"));
		respuestas.add(new ReactivoDto("66","6","2"));
		respuestas.add(new ReactivoDto("67","7","2"));
		respuestas.add(new ReactivoDto("68","8","1"));
		respuestas.add(new ReactivoDto("69","9","3"));
		//
		encDto.setRespuestas(respuestas);
		
		
		String jSon = getJsonFromService(gson.toJson(encDto), URI_ENCUESTA+METHOD_UPDATE );	
		return jSon; 
	}
	
	public static String encuestaCuestionario() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put("idEmpresaConf", IDCONF );
		
		jsCont.put("idEvaluacion", "1");
		
		
		String jSon = getJsonFromService(jsCont.toString(), URI_ENCUESTA+METHOD_QUESTIONARY );	
		return jSon; 
	}
}
