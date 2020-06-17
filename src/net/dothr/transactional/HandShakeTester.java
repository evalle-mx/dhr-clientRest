package net.dothr.transactional;

import org.json.JSONObject;

import net.dothr.MainAppTester;

public class HandShakeTester extends MainAppTester {

	private static final String IDCONF = IDCONF_DOTHR;
	public final static String URI_HANDSHAKE ="/module/handshake";
	
	private final static String OP_VISTO = "1";
	private final static String OP_INVITACION = "2";
	private final static String OP_RECHAZO = "3";
	private final static String OP_ACEPTAR = "4";
	private final static String OP_NO_ACEPTAR="5";
	private final static String OP_REINVITAR = "7";
	
	
	public static void pruebaCreateHS_Visto() throws Exception{
		//TODO verificar idEstatus por default si es creado o Activo
		JSONObject json = new JSONObject();
		json.put("idConf", IDCONF);
		json.put("idPosicion", "13");
		json.put("idCandidato", "251");
		json.put("operacion", OP_VISTO); // OP_VISTO | OP_INVITACION | OP_RECHAZO => (contratante->candidato)
		
		String jSon = getJsonFromService(json.toString(), URI_HANDSHAKE+URI_CREATE );
	}
	
	public static void pruebaCreateHS_Invitacion() throws Exception{
		//TODO verificar idEstatus por default si es creado o Activo
		JSONObject json = new JSONObject();
		json.put("idConf", IDCONF);
		json.put("idPosicion", "13");
		json.put("idCandidato", "251");
		json.put("operacion", OP_INVITACION); // OP_VISTO | OP_INVITACION | OP_RECHAZO => (contratante->candidato)
		
		String jSon = getJsonFromService(json.toString(), URI_HANDSHAKE+URI_CREATE );
	}
	
	public static void pruebaCreateHS_Rechazo() throws Exception{
		//TODO verificar idEstatus por default si es creado o Activo
		JSONObject json = new JSONObject();
		json.put("idConf", IDCONF);
		json.put("idPosicion", "13");
		json.put("idCandidato", "251");
		json.put("operacion", OP_RECHAZO); // OP_VISTO(1) | OP_INVITACION(2) | OP_RECHAZO(3) => (contratante->candidato)
		json.put("motivoRechazo", "[1,2,3]");
		json.put("otros","Detalle de rechazo especifico");
		
		String jSon = getJsonFromService(json.toString(), URI_HANDSHAKE+URI_CREATE );
	}
	
	public static void pruebaCreateHS_NoInteresado() throws Exception{
		//TODO verificar idEstatus por default si es creado o Activo
		JSONObject json = new JSONObject();
		json.put("idEmpresaConf", IDCONF);
		
		json.put("idPosicion", "13");
		json.put("idCandidato", "251");
		//json.put("operacion", OP_NO_ACEPTAR ); // OP_VISTO(1) | OP_INVITACION(2) | OP_RECHAZO(3) => (contratante->candidato)
		json.put("claveEvento", "HANDCHECK_RECHAZO");
		json.put("motivoRechazo", "7,8,9");
//		json.put("otros","Detalle de rechazo especifico");
		
		String jSon = getJsonFromService(json.toString(), URI_HANDSHAKE+URI_CREATE );
	}
	
	public static void pruebaCreateHandShake(String tipoHs) throws Exception {
		if(tipoHs.equals(OP_VISTO)){
			pruebaCreateHS_Visto();
		}
		if(tipoHs.equals(OP_INVITACION)){
			pruebaCreateHS_Invitacion();
		}
		if(tipoHs.equals(OP_RECHAZO)){
			pruebaCreateHS_Rechazo();
		}
		if(tipoHs.equals(OP_NO_ACEPTAR)){
			pruebaCreateHS_NoInteresado();
		}
		/* name, value */
	}

	public static void main(String[] args) {
		try {
			pruebaCreateHandShake(OP_NO_ACEPTAR);
			
		} catch (Exception e) { e.printStackTrace(); }
	}
}
