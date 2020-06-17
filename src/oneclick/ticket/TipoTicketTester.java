package oneclick.ticket;

import org.json.JSONObject;

import netto.AppTester;

public class TipoTicketTester extends AppTester {

	public final static String SERV_TIPOTICKETS = "/module/tickettype";
	
	
	public static void main(String[] args) {
		try {
			getTipoTickets();
//			createTipoTicket();
//			updTipoTicket();
//			delTipoTicket();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static String getTipoTickets() throws Exception {
		JSONObject jsCont = new JSONObject();
//		jsCont.put("idTipoSoporte", "1");
		
		String jSon = getJsonFromService(jsCont.toString(), SERV_TIPOTICKETS+METHOD_GET );	
		return jSon; 
	}
	
	public static String createTipoTicket() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put("idTipoSoporte", "1");
		jsCont.put("etiqueta", "PrUeBas");
		jsCont.put("descripcion", "create registro");
		
		String jSon = getJsonFromService(jsCont.toString(), SERV_TIPOTICKETS+METHOD_CREATE );	
		return jSon; 
	}
	
	public static String updTipoTicket() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put("idTipoTicket", "21");
		jsCont.put("etiqueta", "PrUeBas dos");
		jsCont.put("idTipoSoporte", "2");
		jsCont.put("descripcion", "UPDATE registro");
		
		String jSon = getJsonFromService(jsCont.toString(), SERV_TIPOTICKETS+METHOD_UPDATE );	
		return jSon; 
	}
	
	public static String delTipoTicket() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put("idTipoTicket", "21");
		
		String jSon = getJsonFromService(jsCont.toString(), SERV_TIPOTICKETS+METHOD_DELETE );	
		return jSon; 
	}
}
