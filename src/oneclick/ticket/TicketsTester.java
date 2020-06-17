package oneclick.ticket;

import org.json.JSONObject;

import netto.AppTester;

public class TicketsTester extends AppTester {

	public final static String SERV_TICKETS = "/module/tickets";
	
	
	public static void main(String[] args) {
		try {
			getTickets();
//			createTicket();
//			readTicket();
//			updateTicket();
//			deleteTicket();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static String getTickets() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put("idUsuario", "89");		//199 
//		jsCont.put("idAdmin", "199");	//199 | 1
		
//		jsCont.put("idTipoTicket", "1");
//		jsCont.put("idTipoEstatus", "1");
//		jsCont.put("idTecnicoAtencion", "1");
		
		String jSon = getJsonFromService(jsCont.toString(), SERV_TICKETS+METHOD_GET );	
		return jSon; 
	}
	
	public static String createTicket() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put("idUsuario", "99");
		jsCont.put("idTipoTicket", "1");
		jsCont.put("idTipoEstatus", "1");	//Default debe ser 1, pero permite crearla con cualquier estado (de la tabla)
		jsCont.put("descripcion", "AQUI PONER LA DESCRIPCION DEL Ticket");
		jsCont.put("comentario", "Prueba de Inserción");
		
		String jSon = getJsonFromService(jsCont.toString(), SERV_TICKETS+METHOD_CREATE );	
		return jSon; 
	}
	
	public static String readTicket() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put("idUsuario", "199");		//idUsuario | idAdmin
//		jsCont.put("idAdmin", "199");
		jsCont.put("idTicket", "2");
		
		String jSon = getJsonFromService(jsCont.toString(), SERV_TICKETS+METHOD_READ );	
		return jSon; 
	}
	
	public static String updateTicket() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put("idTicket", "2");
		jsCont.put("idTipoEstatus", "1");
		jsCont.put("idTipoEstatus", "1");
		
		String jSon = getJsonFromService(jsCont.toString(), SERV_TICKETS+METHOD_UPDATE );	
		return jSon; 
	}
	
	public static String deleteTicket() throws Exception {
		JSONObject jsCont = new JSONObject();
		jsCont.put("idTicket", "2");
		
		String jSon = getJsonFromService(jsCont.toString(), SERV_TICKETS+METHOD_DELETE );	
		return jSon; 
	}
}
