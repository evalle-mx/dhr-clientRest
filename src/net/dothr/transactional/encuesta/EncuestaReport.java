package net.dothr.transactional.encuesta;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.utils.ClientRestUtily;

import org.json.JSONArray;
import org.json.JSONObject;

public class EncuestaReport extends EncuestaTester {

	private static final String IDCONF = IDCONF_DOTHR;
	
	private static final String ENCODE_ISO = "ISO-8859-1";
	
	private static final boolean reOrdenado = true;
	private static final String headerDef = "Reactivo 1;Reactivo 2;Reactivo 3;Reactivo 4;Reactivo 5;Reactivo 6;Reactivo 7;Reactivo 8;Reactivo 9";
	private static final String ordenResp = "6;9;7;4;5;1;3;8;2";//"1;2;3;4;5;6;7;8;9";
	private static final String headerOrder = "R.37 (6);R.38 (9);R.39 (7);R.40 (4);R.41 (5);R.42 (1);R.43 (3);R.44 (8);R.45 (2)";
//			"Reactivo 6;Reactivo 9;Reactivo 7;Reactivo 4;Reactivo 5;Reactivo 1;Reactivo 3;Reactivo 8;Reactivo 2";
	
	/*
	
	6.- Puede negociar de manera diplomática, así como de una forma directa y enérgica cuando se requiere.   3   (37)
	9.- Realiza de una manera equilibrada evaluación de su equipo de trabajo, reconociendo el buen desempeño y otorgando asesoría puntual en las áreas de oportunidad de su equipo de trabajo.   1   (38)
	7.- Cuenta con la capacidad para comunicar la misión, visión, valores y objetivos de la organización y motiva a su equipo a identificarlos y participar de ellos.   3   (39)
	4.- Describe las ventajas de sus propuestas a los demás y soluciona las diferencias con otras personas y equipos sin dañar las relaciones.  4  (40)
	5.- Es hábil para negociar efectivamente temas críticos, llegar a acuerdos en los que todas las partes ganen y sin perder el sentido del tiempo.   2  (41)
	1.- Puede establecer acuerdos duros y resolver las disputas equitativamente.   2  (42)
	3.- Sabe identificar eficazmente las fuentes de conflicto y conciliar las diferencias que surjan.   2  (43)
	8.- Identifica las oportunidades y problemas futuros y se anticipa generando planes de acción.  3  (44)
	2.- Actúa rápidamente para resolver problemas personales y previene su recurrencia.    2 (45)
	
	*/
	
	public static void main(String[] args) {
		try{
//			reportTest(null); //null (todos) | true (solo terminados) | false (sinTerminar)
//			resultados();
			
//			testGetEvals();
			
			reportEvals("evalResps0126.csv");
			
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	
	public static void reportEvals(String csvName) throws Exception {
		String stResp;
		JSONObject jsServReq;
		
		JSONArray jsEvaluados;
//		//Temp
//		JSONObject jsonEvaluado;
//		JSONArray jsRespuesta;

		/* 1 obtener listado de Evaluados (32) */
		jsServReq = new JSONObject();
		jsServReq.put(P_JSON_IDCONF, IDCONF );
		jsServReq.put("idUsuario", "2"); //Administrador (yo)
		jsServReq.put("modo", "2");
		stResp = getJsonFromService(jsServReq.toString(), URI_ENCUESTA+URI_GET );
		
		/* 1.b Convertir a arreglo de Evaluados */
		jsEvaluados = new JSONArray(stResp);
		System.out.println("# evaluados: " + jsEvaluados.length() );
//		for(int x=0; x<jsEvaluados.length();x++){
//			jsonEvaluado = jsEvaluados.getJSONObject(x);
//			System.out.println( (x+1) + "-" + jsonEvaluado.getString("evaluado") + 
//					"\t\t revisar \tEsta persona tiene "+jsonEvaluado.getString("total") + " personas que lo evalúan");
//		}
		StringBuilder sbReporte = getEvaluaciones(jsEvaluados);
		ClientRestUtily.writeStringInFile("/home/dothr/Documents/SELEX/Encuesta/"+csvName, sbReporte.toString(), false);
		
	}
	
	
	private static void testGetEvals() throws Exception {
		JSONObject json = new JSONObject();
		json.put("idEvaluado", "46");
		json.put("evaluado", "Jesus Fernando Hernandez Armendariz");
		JSONArray jsEvaluados = new JSONArray();
		jsEvaluados.put(json);
		
		StringBuilder sbReporte = getEvaluaciones(jsEvaluados);
		ClientRestUtily.writeStringInFile("/home/dothr/Documents/SELEX/Encuesta/evalResps.csv", sbReporte.toString(), false);
	}
	
	/**
	 * Obtiene las evaluaciones para cada Evaluado en base a su id (recorre una Lista de Id's)
	 * @throws Exception
	 */
	public static StringBuilder getEvaluaciones(JSONArray jsEvaluados) throws Exception {
		StringBuilder sbReporte = new StringBuilder();
		JSONObject jsReq, jsonEval;
		JSONArray jsResp, jsReactivo;
		
		
		String idEvaluado, evaluado, total;
		String jSon;
		Integer iTotal, iTerminado;
		String usuario, correlacion; //txtEvaluado
		
		
		
		for(int a=0; a<jsEvaluados.length(); a++){
			jsonEval = jsEvaluados.getJSONObject(a);
			idEvaluado = jsonEval.getString("idEvaluado");
			evaluado = jsonEval.getString("evaluado");
			total = jsonEval.getString("total");
			iTotal = new Integer(total);
			iTerminado = new Integer(0);
			sbReporte.append("Evaluado: (").append(idEvaluado).append(") ").append(evaluado).append("\n")
			//.append("Nombre;Perfil;Terminado;Reactivo 1;Reactivo 2;Reactivo 3;Reactivo 4;Reactivo 5;Reactivo 6;Reactivo 7;Reactivo 8;Reactivo 9\n");
			.append("Nombre;Perfil;Terminado;").append(reOrdenado?headerOrder:headerDef).append("\n");
						
			jsReq = new JSONObject();
			jsReq.put(P_JSON_IDCONF, IDCONF );
			jsReq.put("idEvaluado", idEvaluado); 
			jsReq.put("modo", "3");
			jSon = getJsonFromService(jsReq.toString(), URI_ENCUESTA+URI_GET );
			jsResp = new JSONArray(jSon);
						
			for(int x=0; x<jsResp.length();x++){
				jsonEval = jsResp.getJSONObject(x);
				usuario = jsonEval.getString("usuario");
				correlacion = jsonEval.getString("correlacion");
				sbReporte.append(usuario).append(";").append(correlacion).append(";");
				if(jsonEval.getString("terminado").equals("1")){
					System.out.println("TERMINADO: "+ jsonEval);
					sbReporte.append("TERMINADO");
					
					jsReactivo = jsonEval.getJSONArray("respuestas");
					//Respuestas
					sbReporte.append(getRespOrdenado(jsReactivo)).append("\n");
					iTerminado++;
				}
				else{
					System.out.println("INCOMPLETO: "+ jsonEval);
					sbReporte.append("incompleto;;;;;;;;;\n");
				}
			}
			sbReporte.append(";;").append(iTerminado).append(" de ").append(iTotal).append(";\n-----\n");
		}
		return sbReporte;
	}
	
	private static String getRespOrdenado(JSONArray jsReactivo) throws Exception {
		StringBuilder sb = new StringBuilder();
		JSONObject jsonReact;
		if(reOrdenado){
			//re-orden definido
			List<String> lsOrden = Arrays.asList( ordenResp.split("\\s*;\\s*") );
			Iterator<String> itOrden = lsOrden.iterator();
			String idReact, valor="x";
			while(itOrden.hasNext()){
				idReact = itOrden.next();
				for(int y=0;y<jsReactivo.length();y++){
					jsonReact = jsReactivo.getJSONObject(y);
					if(jsonReact.getString("idReactivo").equals(idReact)){
						valor = jsonReact.getString("valor");
					}
				}
				sb.append(";").append(valor);
//				sb.append(";").append(valor).append(" [").append(idReact).append("]");
			}
		}else{
			//Orden natural
			for(int y=0;y<jsReactivo.length();y++){
				jsonReact = jsReactivo.getJSONObject(y);
				System.out.println("\t"+jsonReact.toString());
				sb.append(";").append(jsonReact.getString("valor"));
//				sb.append(";").append(jsonReact.getString("valor")).append(" [").append(y+1).append("]");
			}
		}

		return sb.toString();
	}
	
	/**
	 * Obtiene las evaluaciones para cada Evaluado en base a su id (recorre una Lista de Id's)
	 * @throws Exception
	 */
	public static void resultados() throws Exception {
		StringBuilder sbReporte = new StringBuilder();
		JSONObject jsReq, jsonEval, jsonReact;
		JSONArray jsResp, jsReactivo;
		String idsEvaluados = "46", idEvaluado;
		String jSon;
		
		String usuario, correlacion; //txtEvaluado
		
		List<String> lsIdEvaluado = Arrays.asList( idsEvaluados.split("\\s*;\\s*") );
		Iterator<String> itIdEval = lsIdEvaluado.iterator();
		
		while(itIdEval.hasNext()){
			idEvaluado = itIdEval.next();
			sbReporte.append("Evaluado: ").append(idEvaluado).append("\n")
			.append("Nombre;Perfil;Terminado;Reactivo 1;Reactivo 2;Reactivo 3;Reactivo 4;Reactivo 5;Reactivo 6;Reactivo 7;Reactivo 8;Reactivo 9\n");
			
			jsReq = new JSONObject();
			jsReq.put(P_JSON_IDCONF, IDCONF );
			jsReq.put("idEvaluado", idEvaluado); 
			jsReq.put("modo", "3");
			jSon = getJsonFromService(jsReq.toString(), URI_ENCUESTA+URI_GET );
			jsResp = new JSONArray(jSon);
						
			for(int x=0; x<jsResp.length();x++){
				jsonEval = jsResp.getJSONObject(x);
				usuario = jsonEval.getString("usuario");
				correlacion = jsonEval.getString("correlacion");
				sbReporte.append(usuario).append(";").append(correlacion).append(";");
				if(jsonEval.getString("terminado").equals("1")){
					System.out.println("TERMINADO: "+ jsonEval);
					sbReporte.append("TERMINADO");
					
					jsReactivo = jsonEval.getJSONArray("respuestas");
					//Respuestas
					for(int y=0;y<jsReactivo.length();y++){
						jsonReact = jsReactivo.getJSONObject(y);
						System.out.println("\t"+jsonReact.toString());
						sbReporte.append(";").append(jsonReact.getString("valor"));
					}
					sbReporte.append("\n");
				}
				else{
					System.out.println("INCOMPLETO: "+ jsonEval);
					sbReporte.append("incompleto;;;;;;;;;\n");
				}
			}
		}
		ClientRestUtily.writeStringInFile("/home/dothr/Documents/SELEX/Encuesta/evalRep.csv", sbReporte.toString(), false);
	}

	public static void reportTest(Boolean bTerminado) throws Exception {
		StringBuilder sbReporte = new StringBuilder(), sbCsv = new StringBuilder();
		
		String idsUsuario = "46"; 
				//"10;11;12;13;14;15;16;17;18;19;20;21;22;23;24;25;26;27;28;29;30;31;32;33;34;35;36;37;38;39;40;41;42;43;44;45;46;47;48;49;50;51;52;53;54;55;56;57;58;59;60;61;62;63;64;65;66;67;68;69;70;71;72;73;74;75;76;77;78;79;80;81;82;83;84;85;86;87;88;89;90;91;92;93;94;95";
		List<String> lsIdUsuario = Arrays.asList( idsUsuario.split("\\s*;\\s*") );
		
		System.out.println("Tamaño: "+ lsIdUsuario.size() );
		Iterator<String> itIdUser = lsIdUsuario.iterator();
		String idUsuario;
		JSONObject jsReq, jsonEval;
		JSONArray jsResp;
		sbReporte.append("# de Evaluadores: ").append(lsIdUsuario.size()).append(bTerminado==null?"":bTerminado?" (Solo terminados)":"(solo SIn terminar)").append("\n");
		sbCsv.append("Evaluadores:;").append(lsIdUsuario.size()).append("\n");
		String evaluado, usuario, terminado, idEvaluado;
		
		while(itIdUser.hasNext()){
			idUsuario = itIdUser.next();
			//sbReporte.append("idUsuario:").append(idUsuario).append("\n");
			
			jsReq = new JSONObject();
			jsReq.put(P_JSON_IDCONF, IDCONF );
			jsReq.put("idUsuario", idUsuario); 
			jsReq.put("modo", "1"); /*=>Evaluaciones por usuario */
			String stResp = getJsonFromService(jsReq.toString(), URI_ENCUESTA+URI_GET );
			//sbReporte.append(stResp).append("\n");
			
			jsResp = new JSONArray(stResp);
			for(int x=0; x<jsResp.length();x++){
				jsonEval = jsResp.getJSONObject(x);
				//System.out.println(jsonEval);
				usuario = jsonEval.getString("usuario");
				evaluado = jsonEval.getString("evaluado");
				idEvaluado = jsonEval.getString("idEvaluado");
				terminado = jsonEval.getString("terminado");
				if(x==0){
					sbReporte.append("Nombre: ").append(usuario)
//					.append(" [").append(jsResp.length()).append(" evaluados] ")
					.append("\n");
					sbCsv//.append(idUsuario).append(";")
					.append(usuario).append(";\n");
				}
				if(bTerminado!=null){					
					if(bTerminado.booleanValue() && terminado.equals("1")){
						//sbReporte.append("\tevaluado: ").append(evaluado)
//						.append(", terminado")
						sbReporte.append("\t >>").append(evaluado)
						.append("\n");
					}else if(!bTerminado.booleanValue() && terminado.equals("0")){
						//sbReporte.append("\tevaluado: ").append(evaluado)
//						.append(", Sin terminar")
						sbReporte.append("\t >>").append(evaluado)
						.append("\n");
					}
				}
				else{
					sbReporte.append("\t>").append(evaluado)
					.append(terminado.equals("1")?" -Evaluado-":"")//.append(", terminado: ").append(terminado)
					.append("\n");
					sbCsv//.append(idEvaluado).append(";"
					.append(";").append(evaluado).append(terminado.equals("1")?";Terminado":";pendiente").append("\n");
				}
			}
			
		}
		
		System.out.println("\n\n\n"+sbReporte);
		ClientRestUtily.writeStringInFile("/home/dothr/Documents/SELEX/Encuesta/EvaluacionesRep.txt", sbReporte.toString(), ENCODE_ISO, false);
		ClientRestUtily.writeStringInFile("/home/dothr/Documents/SELEX/Encuesta/EvaluacionesRep.csv", sbCsv.toString(), ENCODE_ISO, false);
	}
}
