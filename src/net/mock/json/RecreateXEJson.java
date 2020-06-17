package net.mock.json;

import java.util.Iterator;

import org.apache.commons.lang.WordUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import net.dothr.recreate.MainRecreate;
import net.utils.ClientRestUtily;

public class RecreateXEJson extends MainRecreate {
	
	private static Logger log4j = Logger.getLogger( RecreateXEJson.class );

	private static JSONObject jsonPersona;
	private static String fileName;
	private static StringBuilder sb;
	
	//ConstantesREST.JSON_HOME
	private static String jsonPath = "/home/dothr/Documents/SELEX/JsonUI/PersonaRecreate/curriculumManagement/";
	
	
//	private static void revisaMultipleDir() throws Exception {
//		log4j.debug("<revisaMultipleDir>");
//		String[] carpetas = {"1.jul-2015","2.AWS(P.RH)","3.Selex(P.Disen)","4.SelexP32016","5.SelexP42017","6.SelexP52017"};
//		for(String carpeta : carpetas) {
//			System.out.println("revisando: "+carpeta);
//			revisaJsonDir(carpeta);
//		}
//	}
	
	/**
	 * Obtiene los jsons en lista y valida la pre-publicación
	 * @param carpeta
	 * @throws Exception
	 */
	private static void revisaJsonDir(String carpeta, String nombreLista) throws Exception {
		log4j.debug("<readJsonPer>");
		String outCsvAreaPersona = "/home/dothr/Documents/SELEX/JsonUI/PersonaRecreate/curriculumManagement/jsonRevisado_<CARP>.csv";
		String jsonDir = jsonPath+carpeta+"/";
		
		String email, nombreComp, faltantes;
		Iterator<String> itFileName = listFromFile(jsonDir, nombreLista).iterator();
		sb = new StringBuilder();
		sb.append("EMAIL;NOMBRE_COMPLETO;FILENAME;FALTANTES\n");//Comentar para quitar encabezado
		StringBuilder sbIncom = new StringBuilder();
		
		sb.append("#### Completos [").append(carpeta).append("] ##### \n");
//		int indice = 1;
		while(itFileName.hasNext()) {
			fileName = itFileName.next();
			//System.out.println(fileName);
			if(!fileName.startsWith("#")) {
				jsonPersona = getJsonObject(fileName, jsonDir);
				revisaPersona(jsonPersona);
				email = jsonPersona.getString("email");
				nombreComp = 
					( jsonPersona.has("apellidoPaterno")?jsonPersona.getString("apellidoPaterno"):"")
					+( jsonPersona.has("apellidoMaterno")?" "+jsonPersona.getString("apellidoMaterno"):"")
					+( jsonPersona.has("nombre")?" "+jsonPersona.getString("nombre"):"");
				
				if(jsonPersona.has("faltantes")) {
					faltantes =jsonPersona.getString("faltantes");
					sbIncom.append(email).append(";")
					.append(nombreComp.length()>1?WordUtils.capitalizeFully(nombreComp):"##SIN NOMBRE##").append(";")
					.append(fileName).append(";")				
					.append(faltantes).append("\n");
				}
				else {
					faltantes = "";
					sb.append(email).append(";")
					.append(nombreComp.length()>1?WordUtils.capitalizeFully(nombreComp):"##SIN NOMBRE##").append(";")
					.append(fileName).append(";")				
					.append(faltantes).append("\n");
				}
				
			}
			else {
				System.out.println(fileName);
			}
		}
		sb.append("#### Incompletos [").append(carpeta).append("] ###\n").append(sbIncom);
		//Generar archivo csv con datos
		ClientRestUtily.writeStringInFile(outCsvAreaPersona.replace("<CARP>", carpeta), 
				sb.toString(), false);
		log4j.debug("<readJsonPer> archivo generado: \n"+outCsvAreaPersona.replace("<CARP>", carpeta));
	}
	
	
	
	public static void main(String[] args) {
		try {
			revisaJsonDir("xeAll", "listaReadAll.txt");
//			revisaMultipleDir();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
