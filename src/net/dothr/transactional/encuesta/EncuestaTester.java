package net.dothr.transactional.encuesta;

//import org.apache.log4j.Logger;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import net.dothr.MainAppTester;
import net.utils.ClientRestUtily;

public class EncuestaTester extends MainAppTester {
	
	private static final String IDCONF = IDCONF_DOTHR;
	public final static String URI_ENCUESTA = "/module/encuesta";
	public final static String URI_QUEST="/questionary";
	
	private static final String ENCODE_ISO = "ISO-8859-1";
	

//	private static Logger log4j = Logger.getLogger( EncuestaTester.class );
	
	
	public static void main(String[] args) {
		try {
//			encuestaGet();
//			encuestaRead();
//			encuestaCuestionario();
//			encuestaUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String encuestaGet() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );
//		jsCont.put("idUsuario", "46"); jsCont.put("modo", "1"); /*=>Evaluaciones por usuario */		
		
//		jsCont.put("modo", "2"); /*=>Lista de Evaluados y resultados por evaluador */

		jsCont.put("idEvaluado", "10"); jsCont.put("modo", "3");/* Solo para modo 3 (Lista de Evaluadores por evaluado) */
		String jSon = getJsonFromService(jsCont.toString(), URI_ENCUESTA+URI_GET );	
		return jSon; 
	}
	
	public static String encuestaRead() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );
		
		jsCont.put("idUsuario", "1");
		jsCont.put("idEvaluacion", "2");
		
		String jSon = getJsonFromService(jsCont.toString(), URI_ENCUESTA+URI_READ );	
		return jSon; 
	}
	
	
	public static String encuestaUpdate() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );
		
		jsCont.put("idUsuario", "2");
		jsCont.put("idEvaluacion", "1");
		//TODO no es correcto el formato de respuestas, en APPUI.Dev js si funciona
		jsCont.put("respuestas", "[{ \"idReactivo\": \"5\", \"texto\": \"DUMMY Es hábil para negociar efectivamente temas críticos, llegar a acuerdos en los que todas las partes ganen y sin perder el sentido del tiempo.\", \"nResp\": \"3\" }, { \"idReactivo\": \"2\", \"texto\": \"Actúa rápidamente para resolver problemas personales y previene su recurrencia.\", \"nResp\": \"3\" }, { \"idReactivo\": \"3\", \"texto\": \"Sabe identificar eficazmente las fuentes de conflicto y conciliar las diferencias que surjan.\", \"nResp\": \"3\" }, { \"idReactivo\": \"1\", \"texto\": \"Pueden establecer acuerdos duros y resolver las disputas equitativamente.\", \"nResp\": \"3\" }, { \"idReactivo\": \"6\", \"texto\": \"Puede negociar de manera diplomática, así como de una forma directa y enérgica cuando se requiere.\", \"nResp\": \"3\" }, { \"idReactivo\": \"7\", \"texto\": \"Cuenta con la capacidad para comunicar la misión, visión, valores y objetivos de la organización y motiva a su equipo a identificarlas y participar de ellos.\", \"nResp\": \"3\" }, { \"idReactivo\": \"4\", \"texto\": \"Describe las ventajas de sus propuestas a los demás y soluciona las diferencias con otras personas y equipos sin dañar las relaciones.\", \"nResp\": \"3\" }, { \"idReactivo\": \"8\", \"texto\": \"Identifica las oportunidades y problemas futuros y se anticipa generando planes de acción.\", \"nResp\": \"3\" }, { \"idReactivo\": \"9\", \"texto\": \"Realiza de una manera equilibrada evaluación de su equipo de trabajo, reconociendo el buen desempeño y otorgando asesoria puntual en las áreas de oportunidad de su equipo de trabajo.\", \"nResp\": \"3\" }]");
				
		
		String jSon = getJsonFromService(jsCont.toString(), URI_ENCUESTA+URI_UPDATE );	
		return jSon; 
	}
	
	public static String encuestaCuestionario() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put(P_JSON_IDCONF, IDCONF );
		
		jsCont.put("idEvaluacion", "1");
		
		
		String jSon = getJsonFromService(jsCont.toString(), URI_ENCUESTA+URI_QUEST );	
		return jSon; 
	}
}
