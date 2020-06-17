package net.dothr.recreate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import net.utils.ClientRestUtily;

public abstract class MainRecreate {
	
	private static StringBuilder sb;
	//1. Carga de Personas
	
	//2. Agregar Area Persona	
	private static final String inPathAreaPersona = "/home/dothr/Documents/SELEX/Pruebas/Pre-Candidatos/areaPersona.csv";
	protected static final String logSetAreaPersona = "/home/dothr/workspace/ClientRest/files/out/areaPersonaLoad.txt";
	
	
	/**
	 * Lista los archivos incluidos en el archivo lista en cada carpeta <b>listaRead.txt</b>
	 * <br><i> Si no existe llega vacio</i>
	 * @return
	 */
	protected static List<String> listFromFile(String path, String fileName){		
		//return ClientRestUtily.readListaTxt("./JsonUI/PersonaRecreate/curriculumManagement/listaRead.txt");
		return ClientRestUtily.readListaTxt(path+fileName);
	}
	
	/**
	 * Metodo común para obtener directamente el (1er) Objeto JSON a partir de un String.json, 
	 * <br> <b> el Formato del archivo .json debe ser [{}]</b>
	 * <br> utilizado por: <ul><li>Persona</li></ul>
	 * @param fileName
	 * @param fileDir
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getJsonObject(String fileName, String fileDir) throws Exception {
		String stPersona = ClientRestUtily.getJsonFile(fileName, fileDir);
		JSONArray jsResp = new JSONArray(stPersona);
//		JSONObject jsPersona = jsResp.getJSONObject(0);		
		return jsResp.getJSONObject(0);
	}

	/**
	 * PROTOTIPO para validar datos de publicacion
	 * TODO completarlo por reflexion 
	 * @param jsonPersona
	 * @throws Exception
	 */
	protected static void revisaPersona(JSONObject jsonPersona) throws Exception {
		String campos = "nombre,apellidoPaterno,contacto,localizacion,escolaridad,experienciaLaboral,idTipoGenero,idEstadoCivil,idTipoDispViajar,salarioMin,salarioMax";		
		List lsParam = Arrays.asList(campos.split("\\s*,\\s*")), faltantes = new ArrayList<String>();
		Iterator<String> itParam = lsParam.iterator();
		String parametro;
		while(itParam.hasNext()) {
			parametro = itParam.next();
//			System.out.println("buscando "+parametro+" en jsonPersona");
			if(!jsonPersona.has(parametro)) {
//				System.out.println("falta "+ parametro);
				faltantes.add(parametro);
			}
		}
		
		if(!faltantes.isEmpty()) {
			sb = new StringBuilder();
			itParam = faltantes.iterator();
			while(itParam.hasNext()) {
				parametro = itParam.next();
				sb.append(parametro).append(itParam.hasNext()?"/":"" );
			}
			jsonPersona.put("faltantes", sb.toString());
		}
//		return jsonPersona;
	}
}
