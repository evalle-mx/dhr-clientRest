package net.dothr.transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import net.dothr.MainAppTester;

@SuppressWarnings("unused")
public class DocClasificacionTester extends MainAppTester {

	private static final String IDCONF = IDCONF_DOTHR;
	public final static String URI_DOCS_CLASS="/module/classify";
	public final static String URI_DOCS_LOADTOKENS="/loadTokens";
	public final static String URI_DOCS_CLASSIFYBYLOT="/classifyByLot";
	
	public final static String URI_TASK="/module/task";
	public final static String URI_TASK_SYNC_CLASS_DOCS="/syncClassDocs";
	public final static String URI_TASK_RECLASSIFICATION="/reclassification";
	
	/** ***********************************************************************************
	 *  **************************    M A I N    ******************************************
	 *  ***********************************************************************************
	 * @param args
	 */
	public static void main(String[] args) {
		try {
//			synchronizeDocClass();	//No esta definido en DEMO
			pruebaGet();
//			pruebaUpdate();
//			pruebaUpdateMultiple();	//Verificar salida real
			
//			pruebaLoadTokens();
//			autoClasificarDocs("2");
			
			
//			testTOList();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Prueba el servicio que clasifica de manera automatica
	 * @throws Exception
	 */
	public static void autoClasificarDocs(String estatusTope) throws Exception{
		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, IDSELEX);
		json.put("estatusClasificacion", estatusTope);
		json.put("aplicaModelo", "0");
		json.put("idTipoDocumento", "1");
		
		String jSon = getJsonFromService(json.toString(), URI_DOCS_CLASS+URI_DOCS_CLASSIFYBYLOT );
	}
	
	/**
	 * Encargado de sincronizar los documentos cargados en Solr con los registros en la tabla DOCUMENTO_CLASIFICACION
	 * @throws Exception
	 */
	public static void synchronizeDocClass() throws Exception{
		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, IDCONF);
		json.put(P_JSON_PERSONA, "2");
		
		String jSon = getJsonFromService(json.toString(), URI_TASK+URI_TASK_SYNC_CLASS_DOCS );
	}
	
	
	/**
	 * Prueba para leer lista de Documentos clasificados
	 * @throws Exception 
	 */
	public static void pruebaGet() throws Exception{	
		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, IDSELEX);
		
//		json.put("sincronizado", "false");	//Por default solo trae los NO sincronizados
//		json.put("automatico", "true");
//		json.put("idArea", "2");
//		json.put("estatusClasificacion", "5");
//		json.put("confirmada", "false"); //NO funciona actualmente, solo trae confirmados de origen
		
		
		String jSon = getJsonFromService(json.toString(), URI_DOCS_CLASS+URI_GET );
	}
	
	/**
	 * Prueba para actualizar uno o varios documentos Clasificados
	 * @throws Exception 
	 */
	public static void pruebaUpdateMultiple() throws Exception{
//		String uri_operation="update";
		
		JSONArray jsArr = new JSONArray();
		JSONObject json = new JSONObject();
		
		json.put("idDocumentoClasificacion", "2");
		json.put("area", "5");
		json.put("categoria", "8,11,58");
		json.put("estatusClasificacion", "1");
		
		jsArr.put(json);
		System.out.println(jsArr.toString());
		String jSon = getJsonFromService(jsArr.toString(), URI_DOCS_CLASS+URI_UPDATE );
	}
	
	
	public static void pruebaUpdate() throws Exception {
		JSONObject json = new JSONObject();
		
		json.put(P_JSON_IDCONF, IDSELEX);
		json.put("idDocumentoClasificacion", "103");
		json.put("idArea", "19");
		json.put("categoria", "5,19");
		json.put("estatusClasificacion", "3");
		
		String jSon = getJsonFromService(json.toString(), URI_DOCS_CLASS+URI_UPDATE );
	}
	
	/**
	 * Prueba para leer lista de Documentos clasificados
	 * @throws Exception 
	 */
	public static void pruebaLoadTokens() throws Exception{	
		JSONObject json = new JSONObject();
		json.put(P_JSON_IDCONF, IDCONF);
		json.put("modo", "3");
		json.put("docType", "1");
		json.put("jsonTokens", getPares3());
		json.put("numeroDocs", "15");
		json.put("desviacion", "100");
		json.put("idArea", "62");
//		json.put("seedDir", "NoDir");
		String jSon = getJsonFromService(json.toString(), URI_DOCS_CLASS+URI_DOCS_LOADTOKENS );
	}
	
	/**
	 * Crea la cadena de Pares [token, ponderacion] que se enviaran en el Json de Request
	 * @return
	 */
	private static String getPares3(){
		String stPares="";
		JSONObject jsPar = new JSONObject();
		try{
			jsPar.put("Java", 10);
			jsPar.put("Oracle", 15);
			jsPar.put("PHP", 8);
			jsPar.put("HTML", 8);
			jsPar.put("Visual Basic", 8);
			stPares = jsPar.toString();
		}catch (Exception e){
			e.printStackTrace();
		}
		return stPares;
	}
	
	
	/* ********************************************************************************** */
	
	private static Object getLsString(){
		String lsString = "[9,-2.3860799682693896,0, 10,-1.0541476666758314,0, 7,-0.41208092405265784,0, 3,-0.30299052893947803,0, 19,-0.24782141296907673,0, 1,-0.0066290692400128885,0, 20,0.3493523711288877,0, 22,0.5083800257921203,0, 23,0.5094290716343627,0, 6,0.737947609573929,0, 5,1.0257187967219965,0, 21,1.2789216952951548,0]"; 
				//1)"[19,-1.4727897904,0, 20,-1.4603986784487,0, 9,-1.4008555160876137,0, 21,-0.4850312024296642,0, 1,0.03879957066340323,0, 22,0.1939471461180746,0, 3,0.4242671246213901,0, 10,0.43286296188525947,0, 23,0.5211293820680848,0, 7,0.6335797705911999,0, 6,1.0559611423311341,1, 5,1.5185280890874002,1]";
		return lsString;
	}
	
	private static void testTOList() throws Exception {
		Integer tope = new Integer("0"); // AutoClasifica cuando preClasificacion sea superior o igual al tope
		Object objeto = getLsString();
		String ls = (String) objeto;
		String areaFinal = null;
		List<String> lsData = Arrays.asList(ls.replace("[", "").replace("]", "").split("\\s*,\\s*"));
				//(List<String>)objeto; //Fallo
		
		System.out.println("lsData.size " + lsData.size());
		if(lsData!=null && !lsData.isEmpty()){
			System.out.println("div: " + (lsData.size()/3));
			int mod = lsData.size()%3;
			int dtIndx = 0;
			
			System.out.println("mod: " + mod + "\n ******** \n");
			if(mod==0){
				String area;
				BigDecimal bdCalifica;
				Integer preCla;
				
				List<ClasificacionVo> lsClasVo = new ArrayList<ClasificacionVo>();
				Iterator<String> itSt = lsData.iterator();
				ClasificacionVo vo = null; 
				DocClasificacionTester test = new DocClasificacionTester();
				while(itSt.hasNext()){
					
					if(dtIndx == 0){
						area = itSt.next();
						vo = test.new ClasificacionVo(area);
//						System.out.println("Area:"+area);						
						dtIndx++;
					}else if(dtIndx==1){
						bdCalifica = new BigDecimal(itSt.next());
//						System.out.println("Calificacion:"+bdCalifica);
						vo.setBdCalifica(bdCalifica);
						dtIndx++;
					}else {
						preCla = Integer.parseInt(itSt.next());
//						System.out.println("PreClasificacion:"+preCla);
						dtIndx=0;
//						System.out.println();
						vo.setPreCla(preCla);
						
						if(preCla >= tope ){
							lsClasVo.add(vo);
						}
						
						area= null;
						bdCalifica = null;
						preCla = null;
						vo = null;						
					}
				}
				if(!lsClasVo.isEmpty()){
					ClasificacionVo voTmp;
					if(lsClasVo.size()==1){
						System.out.println("Solo hay un resultado, se toma por default");
						voTmp = lsClasVo.get(0);
					}else{
						Iterator<ClasificacionVo> itVo = lsClasVo.iterator();
						System.out.println("Se iteran " + lsClasVo.size() +" resultados: \n" + lsClasVo);
//						int x = 1;
						voTmp = test.new ClasificacionVo(null, new BigDecimal("0"), new Integer("-1"));
						while(itVo.hasNext()){
							voTmp.setBigger( itVo.next() );
							//System.out.println("Vo"+x+"> Area: "+tmp.getArea()+ ", Calificacion: "+tmp.getBdCalifica() + ", PreCla: "+tmp.getPreCla());
//							x++;
						}
					}
					System.out.println("voTmp " + voTmp);
					areaFinal = voTmp.getArea();
				}else{
					System.out.println("El arreglo no contiene valores preclasificados aceptables (" + tope + ")");
				}
			}else{
				System.out.println("No es un arreglo de tercias valido");
			}
			
		}
		System.out.println(areaFinal!=null?(">>>>>>   AreaFinal: " + areaFinal):">>>>>>   No fue posible clasificar " );
	}
	
	class ClasificacionVo
	{
	   private String area;
	   private BigDecimal bdCalifica;
	   private Integer preCla;
	   
	   ClasificacionVo(String area) {
		   this.area = area;
	   }
	   
	   ClasificacionVo (String area,  BigDecimal bdCalifica, Integer preCla)
	   {
	      this.area = area;
	      this.bdCalifica = bdCalifica;
	      this.preCla = preCla;
	   }
	   
	   public void setBigger(ClasificacionVo vo){
		   if(vo!=null && this.bdCalifica!=null && this.preCla!=null){
			   if(vo.preCla!=null && vo.preCla>=this.preCla){
				   //System.out.println("vo.bdCalifica "+vo.bdCalifica +" VS this.bdCalifica: " + this.bdCalifica + " (res: " + vo.bdCalifica.compareTo(this.bdCalifica));
				   if(vo.bdCalifica!=null && vo.bdCalifica.compareTo(this.bdCalifica) == 1 ){
//					   System.out.println("el nuevo es mayor que el actual");
					   this.area = vo.getArea();
					   this.bdCalifica = vo.getBdCalifica();
					   this.preCla = vo.getPreCla();
				   }
			   }
		   }
	   }

		public String getArea() {
			return area;
		}
	
		public void setArea(String area) {
			this.area = area;
		}
	
		public BigDecimal getBdCalifica() {
			return bdCalifica;
		}
	
		public void setBdCalifica(BigDecimal bdCalifica) {
			this.bdCalifica = bdCalifica;
		}
	
		public Integer getPreCla() {
			return preCla;
		}
	
		public void setPreCla(Integer preCla) {
			this.preCla = preCla;
		}
		
		public String toString(){
			return "[area:" + this.getArea() + ", bdCalifica:"+ this.getBdCalifica() + ", preCla:"+this.getPreCla() + "]"; 
		}
	   
	}

}
