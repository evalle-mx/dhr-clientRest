package net.db;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.utils.ClientRestUtily;

public class AreaP_Csv2Sql {

	
	private static final String sqlInsertAreaPersona = "INSERT INTO AREA_PERSONA (id_area_persona, id_area, id_persona)" +
			" VALUES (<consecutivo>,<idArea>,<idPersona>);";
	private static final String sqlInsertDocumentoCls = "INSERT INTO DOCUMENTO_CLASIFICACION " +
			" (id_documento_clasificacion, id_area , id_tipo_documento, id_persona, id_perfil, id_empresa, id_posicion" +
			", identificador_doc, estatus_clasificacion, version_modelo, texto_clasificacion, categoria, " +
			" categorias_analisis, id_persona1) " +
			" VALUES (<consecutivo>,null,1,<idPersona>,null,null,null,<consecutivo>,2,0,null,'<categoria>'," +
			"'[8,-1.438279711007527,0, 16,-1.309714988441035,0, 1,-0.9238753711040113,0, 15,-0.8655326183958417,0, 19,-0.33239926511509055,0, 5,-0.13870045974094874,0, 17,0.2324105234878129,0, 18,0.2967299305236279,0, 9,0.88064969134947,0, 6,0.9717467759966579,0, 3,1.02 (...)',null );";
	
	
	
	private static final String pathOut = "/home/dothr/JsonUI/PersonaRecreate/curriculumManagement/_sqlInsert.sql";
	
	public static void main(String[] args) {
		String csvPath = "/home/dothr/Documents/TCE/Pruebas/KO-Certificacion/";
//		createSQL_AP(1, csvPath);		//int consecutivo
//		createSQL_DC(1, csvPath);
		
		compareEmail_AP(csvPath);
	}
	
	/**
	 * Procesa un archivo de Csv conteniendo las areas por IdPersona y genera un archivo sql con Inserts
	 *  correspondientes EN AREA PERSONA
	 * <br>
	 * <b>Formato</b>: idPersona;idArea,idArea,idArea
	 */
	public static void createSQL_AP(long consecutivo, String csvPath){
		StringBuilder sbSql = new StringBuilder("/* SQL documento Clasificacion [Ing en reversa] */\n\n");
		List<String> lsLinea = ClientRestUtily.readListaTxt(csvPath+"Ap-Insert.csv");
		
		Iterator<String> itLinea = lsLinea.iterator();
		String linea, sql; 
		String stArea, idPersona, idArea;
		List<String> items, areas;
//		int consecutivo = 1;
		System.out.println("SQL: ");
		while(itLinea.hasNext()){
			linea = itLinea.next();
//			System.out.println(linea);
			items = Arrays.asList(linea.split("\\s*;\\s*"));
			idPersona = items.get(0);
			stArea = items.get(1);
			if(stArea!=null && stArea.length()>0){
				areas = Arrays.asList(stArea.split("\\s*,\\s*"));
				for(int x=0; x<areas.size();x++){
					idArea=areas.get(x);
					sql = sqlInsertAreaPersona.replace("<consecutivo>", String.valueOf(consecutivo) )
							.replace("<idArea>", idArea).replace("<idPersona>", idPersona);
							consecutivo++;
//							System.out.println(sql);
							sbSql.append(sql).append("\n");		
				}
			}
		}
		sbSql.append("\n SELECT last_value FROM SEQ_AREA_PERSONA;\n ALTER SEQUENCE SEQ_AREA_PERSONA RESTART WITH ")
		.append(consecutivo).append(";\n\n ");
		ClientRestUtily.writeStringInFile(pathOut, sbSql.toString(), false);
	}
	
	/**
	 * Procesa un archivo de Csv conteniendo las areas por IdPersona y genera un archivo sql con Inserts
	 * correspondientes en DOCUMENTO CLASIFICACION
	 * <br>
	 * <b>Formato</b>: idPersona;idArea,idArea,idArea
	 */
	public static void createSQL_DC(long consecutivo, String csvPath){
		StringBuilder sbSql = new StringBuilder("/* SQL documento Clasificacion [Ing en reversa] */\n\n");
		List<String> lsLinea = ClientRestUtily.readListaTxt(csvPath+"Ap-Insert.csv");
		
		Iterator<String> itLinea = lsLinea.iterator();
		String linea, sql; 
		String email, stCategoria, idPersona;
		List<String> items;
//		int consecutivo = 1;
		System.out.println("SQL: ");
		while(itLinea.hasNext()){
			linea = itLinea.next();
//			System.out.println(linea);
			items = Arrays.asList(linea.split("\\s*;\\s*"));
			email = items.get(0);
			System.out.println(consecutivo + ": " + email );
			idPersona = items.get(1);
			stCategoria = items.get(2);
			sql = sqlInsertDocumentoCls.replace("<consecutivo>", String.valueOf(consecutivo) )
					.replace("<categoria>", stCategoria).replace("<idPersona>", idPersona);
			consecutivo++;
			sbSql.append(sql).append("\n");			
		}
		sbSql.append("\n ALTER SEQUENCE SEQ_DOCUMENTO_CLASIFICACION RESTART WITH ")
		.append(consecutivo).append(";\n\n ");
		
		System.out.println(sbSql);
		ClientRestUtily.writeStringInFile(pathOut, sbSql.toString(), false);
	}
	
	
	/**
	 * Compara la lista de correos con la lista de clasificados (Areas asignadas)
	 * 
	 * @param csvPath
	 */
	private static void compareEmail_AP(String csvPath){
		StringBuilder sb = new StringBuilder();
		
		List<String> lsEmails = 		//Se genera a partir del listado de personas en BD, [<JsonUI>/XE.xls]
				ClientRestUtily.readListaTxt("/home/dothr/Documents/TCE/Pruebas/t_emails.txt");
		List<String> lsEmAp = 			// Se genera a partir del listado de personas Clasificadas [.../TCE/ClasificadosPrueba3.xls]
				ClientRestUtily.readListaTxt("/home/dothr/Documents/TCE/Pruebas/t_ap.txt");
		
		Iterator<String> itEmails = lsEmails.iterator();
		String email, email2, stAreas;
		while(itEmails.hasNext()){
			email = itEmails.next();			
			stAreas=null;
			
			Iterator<String> itEmAp = lsEmAp.iterator();
			while(itEmAp.hasNext()){
				List<String> lsData = Arrays.asList(itEmAp.next().split("\\s*;\\s*"));
//				System.out.println(lsData);
				email2 = lsData.get(0);
				if(lsData.size()>1 && email.equals(email2)){
					stAreas = lsData.get(1);
					System.out.println("Se encontro email con area Persona "+email2 +" y areas: "+stAreas);
				}
			}
			
			//stAreas = "A,B";
			if(stAreas!=null){
				sb.append(email).append(";").append(stAreas).append(";").append("\n");
			}
			else{
				sb.append(email).append(";;").append("\n");
			}			
		}
		System.out.println(sb);
//		ClientRestUtily.writeStringInFile(csvPath+"_AreaPersona.csv", sb.toString(), false);
		ClientRestUtily.writeStringInFile("/home/dothr/Documents/TCE/Pruebas/T_Email_APers.csv", sb.toString(), false);
	}
}
