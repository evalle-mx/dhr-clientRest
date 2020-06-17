package oneclick.ticket;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.google.gson.Gson;

import netto.AppTester;
import oneclick.ticket.dto.CatalogoDto;

public class CatalogoTester extends AppTester {
	
	public final static String SERV_CATALOG = "/admin/catalog";
	public final static String METHOD_FULL_GET = "/fullGet";
	
	public final static String SERV_CAT_AREA = "/admin/area";
	public final static String SERV_CAT_ROL = "/admin/rol";
	public final static String SERV_CAT_TIPOSOPORTE = "/admin/tipoSoporte";
	public final static String SERV_CAT_ESTATUS = "/admin/estatus";
	
	
	public static void main(String[] args) {
		try {
//			getCatalogo();
			getFullCatalogos();
//			getAreas();
//			getCat(SERV_CAT_TIPOSOPORTE);
			
//			createCat("area");
//			readCat("area");
//			updateCat("area");
//			deleteCat("area");
			
//			createCat("rol");
//			readCat("rol");
//			updateCat("rol");
//			deleteCat("rol");
			
//			createCat("tipoSoporte");
//			readCat("tipoSoporte");
//			updateCat("tipoSoporte");
//			deleteCat("tipoSoporte");
			
//			createCat("estatus");
//			readCat("estatus");
//			updateCat("estatus");
//			deleteCat("estatus");
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Obtiene la lista de un catalogo ya predefinido en el DataCache CATALOG.G
	 * @return
	 * @throws Exception
	 */
	public static String getCatalogo() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put("nombre", "Area");	// Area, Rol, TipoEstatus, TipoSoporte
		jsCont.put("descripcion", "descripcion");//Si se agrega este valor devuelve campo descripcion
		
		String jSon = getJsonFromService(jsCont.toString(), SERV_CATALOG+METHOD_GET );	
		return jSon; 
	}
	

	
	public static String getCat(String uriRoot) throws Exception {
		JSONObject jsCont = new JSONObject();
		String jSon = getJsonFromService(jsCont.toString(), uriRoot+METHOD_GET );	
		return jSon;
	}
	
	public static String getAreas() throws Exception {
		
		String jSon = getCat(SERV_CAT_AREA);	
		return jSon; 
	}
	
	public static String createCat(String catalogo) throws Exception{
		JSONObject jsCont = new JSONObject();
		jsCont.put("etiqueta", "prueba");
		jsCont.put("descripcion", "Registro insertado en "+"/admin/"+catalogo);
		String jSon = getJsonFromService(jsCont.toString(), "/admin/"+catalogo+METHOD_CREATE );	
		
		return jSon;
	}
	
	public static String readCat(String catalogo) throws Exception {
		JSONObject jsCont = new JSONObject();
		
		String jSon = null;
		if(catalogo.equals("area")){
			jsCont.put("idArea", "5");
		}
		else if(catalogo.equals("rol")){
			jsCont.put("idRol", "5");
		} else if(catalogo.equals("tipoSoporte")){
			jsCont.put("idTipoSoporte", "3");
		}else if(catalogo.equals("estatus")){
			jsCont.put("idTipoEstatus", "6");
		}
		else {
			return null;
		}
			
		jSon = getJsonFromService(jsCont.toString(), "/admin/"+catalogo+METHOD_READ );
		return jSon;
	}
	
	public static String updateCat(String catalogo) throws Exception {
		JSONObject jsCont = new JSONObject();
		
		jsCont.put("etiqueta", "Testing upd");
		String jSon = null;
		if(catalogo.equals("area")){
			jsCont.put("idArea", "5");
		}
		else if(catalogo.equals("rol")){
			jsCont.put("idRol", "5");
		} else if(catalogo.equals("tipoSoporte")){
			jsCont.put("idTipoSoporte", "3");
		}else if(catalogo.equals("estatus")){
			jsCont.put("idTipoEstatus", "6");
		}
		else {
			return null;
		}
			
		jSon = getJsonFromService(jsCont.toString(), "/admin/"+catalogo+METHOD_UPDATE );
		return jSon;
	}
	
	
	public static String deleteCat(String catalogo) throws Exception {
		JSONObject jsCont = new JSONObject();
		
		String jSon = null;
		if(catalogo.equals("area")){
			jsCont.put("idArea", "7");
		}
		else if(catalogo.equals("rol")){
			jsCont.put("idRol", "5");
		} else if(catalogo.equals("tipoSoporte")){
			jsCont.put("idTipoSoporte", "3");
		}else if(catalogo.equals("estatus")){
			jsCont.put("idTipoEstatus", "6");
		}
		else {
			return null;
		}
			
		jSon = getJsonFromService(jsCont.toString(), "/admin/"+catalogo+METHOD_DELETE );
		return jSon;
	}
	
	/**
	 * Obtiene el listado de catalogo con valores (GEnera JSON de catalogo) CATALOG.FG
	 * @return
	 * @throws Exception
	 */
	public static String getFullCatalogos() throws Exception {
		CatalogoDto catDto = new CatalogoDto();
		gson = new Gson();
		
		List<String> lsNombres = new ArrayList<String>();
		/* puros */
//		lsNombres.add("Area");lsNombres.add("Rol");lsNombres.add("TipoEstatus");lsNombres.add("TipoSoporte");
		/* vivos */
		lsNombres.add("Sucursal");lsNombres.add("TipoTicket");lsNombres.add("TecnicoAtencion");
		catDto.setLsNombre(lsNombres);
		catDto.setDescripcion("1");
		
		String jSon = getJsonFromService(gson.toJson(catDto), SERV_CATALOG+METHOD_FULL_GET );
		return jSon; 
	}

}
