package net.dothr.report;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import net.dothr.MainAppTester;
import net.tce.dto.ConverterJsonDto;
import net.tce.dto.CurriculumDto;
import net.tce.dto.FileDto;
import net.utils.ClientRestUtily;
import net.utils.ConstantesREST;

public class CvPersonaPdfTester extends MainAppTester {

	private static Logger log4j = Logger.getLogger( CvPersonaPdfTester.class );
	
	private static final String CV_JSON_DIR = ConstantesREST.JSON_HOME+
			"module/curriculumManagement/";	
	private static final String OutPath = "/home/netto/app/webServer/repository/docs/X/";
	
	public static final String paramsReq = "imgp,nom,dir,sal,gen,cont,edoc,cdom,dhora,dviaj";
	
	public static void main(String[] args) {
		test();
	}
	
	protected static void test(){
		try{
			if(ClientRestUtily.createDirIfNotExist(OutPath, true)) {
				//CvPersonaPdf pdfMaker = new CvPersonaPdf();
				CvPersonaPdf2 pdfMaker = new CvPersonaPdf2();
				String jsonFileName = "read-45.json";	// 	read-45.json | eservin2.json
				CurriculumDto dtoCV = getDtoCV(jsonFileName);
				
				JSONObject jsParams = new JSONObject();
				jsParams.put("imgp", 1);	//ImagenPerfil
				jsParams.put("nom", 1);	//Nombre
				jsParams.put("dir", 1);	//Dirección
				jsParams.put("sal", 1);	//rangoSalarial
				jsParams.put("gen", 1);	//Genero
				jsParams.put("cont", 1);	//Contactos
				jsParams.put("edoc", 1);	//EstadoCivil
				jsParams.put("cdom", 1);	//CambioDom
				jsParams.put("dhora", 1);	//DispHorario
				jsParams.put("dviaj", 1);	//DispViajar
				
				FileDto dtoF = new FileDto(); 
				String filePdfName = "CV-E"+ dtoCV.getIdEmpresaConf()+"P"+dtoCV.getIdPersona()
						//getHexaNameCV(dtoCV.getNombre(), dtoCV.getApellidoPaterno(), dtoCV.getApellidoMaterno(), jsParams.toString())
						+".pdf";
				
				dtoF.setFileDescripcion(filePdfName);		
				dtoF.setRepParams(jsParams.toString());
						//"{nom : 1, dir : 1, sal : 1, gen : 1, cont  : 1, edoc  : 1, cdom  : 1, dhora : 1, dviaj : 1}");
				log4j.debug("FileName: \n"+filePdfName );
				setPermissCVDto(dtoCV, dtoF.getRepParams());
				
				dtoF= pdfMaker.createPdfDotHR(filePdfName, dtoCV);
				log4j.debug("\n Documento creado: \n" + (dtoF.getUrl()!=null?dtoF.getUrl():dtoF.getMessage()) );
			}
		}catch (Exception e){
			e.printStackTrace();
			log4j.fatal("Error: "+e.getMessage(), e);
		}
	}
	
	/**
	 * Elimina información dependiendo los permisos en Parámetros
	 * (Se utiliza en ServiceRest)
	 * @param dtoCV
	 * @param stParams
	 * @throws JSONException 
	 */
	public static void setPermissCVDto(CurriculumDto dtoCV, String stParams) throws JSONException{
		List<String> lsReqParam;
		JSONObject jsonParms, jsonDefPs= new JSONObject();
//		log4j.debug("=====>>  dtoCV.nombre " + dtoCV.getNombre());
//		log4j.debug("=====>>  stParams " + stParams );
		
		if(stParams==null){
			stParams="";
		}
		try{
			lsReqParam = Arrays.asList(paramsReq.split("\\s*,\\s*"));
			jsonParms = new JSONObject(stParams);
			String param;
			
			for(int x=0;x<lsReqParam.size();x++){
				param = lsReqParam.get(x);
				if(jsonParms.length()>0 && jsonParms.has(param)){
					if(jsonParms.getInt(param)!=1){
						jsonDefPs.put(param, 0);
					}else{
						jsonDefPs.put(param, 1);
					}
				}else{
					jsonDefPs.put(param, 0);
				}
			}
			
			if(jsonDefPs.getInt("imgp")==0){	//ImagenPerfil
				dtoCV.setImgPerfil(null);
			}
			System.out.println("jsonDefPs.getInt(\"nom\") " + jsonDefPs.getInt("nom"));
			if(jsonDefPs.getInt("nom")==0){	//Nombre
				dtoCV.setNombre(null);
				dtoCV.setApellidoPaterno(null);
				dtoCV.setApellidoMaterno(null);
			}
			if(jsonDefPs.getInt("dir")==0){	//Dirección
				dtoCV.setLocalizacion(null);
			}
			if(jsonDefPs.getInt("sal")==0){	//rangoSalarial
				dtoCV.setSalarioMin(null);
				dtoCV.setSalarioMax(null);
			}
			if(jsonDefPs.getInt("gen")==0){	//Genero
				dtoCV.setIdTipoGenero(null);
				dtoCV.setLbGenero(null);
			}
			if(jsonDefPs.getInt("cont")==0){	//Contactos
				dtoCV.setContacto(null);
			}
			if(jsonDefPs.getInt("edoc")==0){	//EstadoCivil
				dtoCV.setIdEstadoCivil(null);
				dtoCV.setLbEstadoCivil(null);
			}
			if(jsonDefPs.getInt("cdom")==0){	//CambioDom
				dtoCV.setCambioDomicilio(null);				
			}
			if(jsonDefPs.getInt("dhora")==0){	//DispHorario
				dtoCV.setDisponibilidadHorario(null);				
			}
			if(jsonDefPs.getInt("dviaj")==0){	//DispViajar
				dtoCV.setIdTipoDispViajar(null);
				dtoCV.setLbDispViajar(null);
			}

			
		}catch (Exception e){
			log4j.fatal("Error al leer Parametros, el reporte queda en Información no Restringida", e);
			dtoCV.setImgPerfil(null);
			dtoCV.setNombre(null);
			dtoCV.setApellidoPaterno(null);
			dtoCV.setApellidoMaterno(null);
			dtoCV.setSalarioMin(null);
			dtoCV.setSalarioMax(null);
			dtoCV.setIdTipoGenero(null);
			dtoCV.setLbGenero(null);
			dtoCV.setContacto(null);
			dtoCV.setIdEstadoCivil(null);
			dtoCV.setLbEstadoCivil(null);
			dtoCV.setCambioDomicilio(null);
			dtoCV.setDisponibilidadHorario(null);
			dtoCV.setIdTipoDispViajar(null);
			dtoCV.setLbDispViajar(null);
		}
	}
	

	
	
	/**
	 * Genera una cadena con un nombre utilizando un Json de entrada
	 * @param dto
	 * @param ext
	 * @return
	 */
	private static String getHexaNameCV(String nombre, String apPat, String apMat, String rpParams){
		List<String> lsReqParam = Arrays.asList(paramsReq.split("\\s*,\\s*"));
		StringBuilder sb = new StringBuilder();
		String binaryStr = "";
		log4j.debug("lsReqParam: "+lsReqParam);
		try {
			if(nombre!=null){
				sb.append(nombre.substring(0,1));
			}
			if(apPat!=null){
				sb.append(apPat.length()>7?apPat.substring(0,7):apPat);
			}
			if(apMat!=null){
				sb.append(apMat.substring(0,1));
			}			
			
			JSONObject jsonParms = new JSONObject(rpParams);
			log4j.debug("jsonParms: "+jsonParms.toString());
			jsonParms = new JSONObject();
			int iv = 0;
			String param;
			for(int x=0;x<lsReqParam.size();x++){
				iv = 0;
				param = lsReqParam.get(x);
				log4j.debug("param: >"+param+"< jsonParms.has("+param+")? "+jsonParms.has(param) );
				if(jsonParms.has(param)){
					log4j.debug(">"+param +" en jsonParms");
					iv = jsonParms.getInt(param);
				}
				binaryStr = iv+binaryStr;
			}
			
		} catch (Exception e) {
			log4j.fatal(e);
			// e.printStackTrace();
			for(int x=0;x<lsReqParam.size();x++){
				binaryStr = 0+binaryStr;
			}
		}
		int decimal = Integer.parseInt(binaryStr, 2);
		String hexStr = Integer.toString(decimal, 16);
		
		//log4j.debug("\n binaryStr: " + binaryStr + "\n hexStr: " + hexStr.toUpperCase() );
		
		return sb.toString()+hexStr.toUpperCase();
	}
	

	/**
	 * Obtiene un CV-DTO desde un Archivo Json (usando un Convertidor) 
	 * @param jsonFileName
	 * @return
	 * @throws Exception
	 */
	private static CurriculumDto getDtoCV(String jsonFileName) throws Exception {		
		JSONObject jsonPersona = getJsonObject(jsonFileName, CV_JSON_DIR);

		return AbstractPDFCreator.getCvDto(jsonPersona);
		//return ConverterJsonDto.cvDto(jsonPersona);
	}
}
