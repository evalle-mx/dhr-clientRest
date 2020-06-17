package net.dothr.fromjson;

import java.sql.ResultSet;

import net.dothr.MainAppTester;
import net.utils.ClientRestUtily;
import net.utils.ConnectionBD;
import net.utils.ConstantesREST;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Clase privada desarrollada TEMPORALMENTE para leer un excel (aún no implementado) donde se relaciona email con area
 * leer la Base de datos para obtener idPersona
 * 
 * generar un JSON de email-idPersona-area.
 * <br>
 * Y en un segundo metodo, generar un archivo SQL con Inserts a la tabla Area_Persona con los id's correspondientes
 * 
 * @author Netto
 *
 */
public class SetAreaPersona extends MainAppTester{

	static Logger log4j = Logger.getLogger( SetAreaPersona.class );
			
	protected static ConnectionBD conn;
	private static final String sqlSelIdPersona = "SELECT id_persona FROM persona WHERE email = '<correo>';"; 
	private static final String sqlInsertAreaPersona = "INSERT INTO AREA_PERSONA (id_area_persona, id_area, id_persona)" +
			" VALUES (<consecutivo>,<idArea>,<idPersona>);";
	
	private static final String JSARRAY_FILE = "JSON.Candidatos.json";
	private static final String PATH_JSONARRAY = ConstantesREST.JSON_HOME+"PersonaRecreate/";
	private static final String PATH_SQL = ConstantesREST.JSON_HOME+"PersonaRecreate/SQL.InsertAreaPersona.sql";
	
	
	public static void main(String[] args) {
		//setIdPersona2Json();
		insertAreaPersona();
	}
	
	
	protected static void insertAreaPersona(){
		JSONArray jsClasif = new JSONArray();
		JSONObject json;
		int consecutivo = 1;
		StringBuilder sb = new StringBuilder();
		
		try{
			String jsFile = ClientRestUtily.getJsonFile(JSARRAY_FILE, PATH_JSONARRAY);
			String idPersona, idArea, updQuery;
			Long lIdArea;
			log4j.debug(jsFile);
			jsClasif = new JSONArray(jsFile);
			log4j.debug(jsClasif);
			
			
			conn = new ConnectionBD(WEB_RESOURCE); 
			conn.getDbConn();
			
			for(int x=0;x<jsClasif.length();x++){
				json = jsClasif.getJSONObject(x);
				
				idPersona = json.getString("idPersona");
				lIdArea = json.getLong("idArea");
				idArea = String.valueOf(lIdArea);
				log4j.debug(json);
				//<consecutivo>,<idArea>,<idPersona>
				updQuery = sqlInsertAreaPersona.replace("<consecutivo>", String.valueOf(consecutivo) )
						.replace("<idArea>", idArea).replace("<idPersona>", idPersona);
				log4j.debug(updQuery);
				sb.append(updQuery).append("\n");
				//conn.updateDataBase(updQuery);
				consecutivo++;
			}
			sb.append("\n--SELECT last_value FROM SEQ_AREA_PERSONA;\n ALTER SEQUENCE SEQ_AREA_PERSONA RESTART WITH ").append(consecutivo).append(";\n\n ");
			
			conn.closeConnection();
		}catch (Exception e){
			e.printStackTrace();
		}
		ClientRestUtily.writeStringInFile(PATH_SQL, sb.toString(), false);
		log4j.debug("SQL generado: "+PATH_SQL );
	}
	
	protected static void setIdPersona2Json(){
		JSONArray jsClasif = new JSONArray();
		JSONObject json;
		String email, idPersona;
		try{
			conn = new ConnectionBD(WEB_RESOURCE); 
			conn.getDbConn();
			
			jsClasif = getJsArr();
			for(int x=0;x<jsClasif.length();x++){
				json = jsClasif.getJSONObject(x);
				email = json.getString("email");
				log4j.debug(json.toString());
				idPersona = getIdFromEmail(email);
				json.put("idPersona", idPersona);				
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			conn.closeConnection();
		}
		
		ClientRestUtily.writeStringInFile(PATH_JSONARRAY+JSARRAY_FILE, jsClasif.toString(), false );
	}
	
	

	
	private static String getIdFromEmail(String email){
		String idPersona=null;
		try {
			ResultSet rs = conn.getQuerySet(sqlSelIdPersona.replace("<correo>", email));
			if(rs.next()){
				idPersona = String.valueOf( rs.getLong(1) );
			}
		} catch (Exception e) {
			e.printStackTrace();
			//log4j.fatal("<error> Obtener el ultimo valor de la secuencia " + sequenceName );
			idPersona = null;
		}
		
		return idPersona;
	}
	
	
	protected static JSONArray getJsArr() {
		JSONArray jsOut = new JSONArray();
		JSONObject json = new JSONObject();
		
		 try {
			json.put("email","58862230@prodigy.net.mx");
			 json.put("idArea",17);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","aaron_aguilar2727@hotmail.com");
			 json.put("idArea",23);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","acarrillo_n_@hotmail.com");
			 json.put("idArea",23);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","acollantesr@hotmail.com");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","agarciac70@gmail.com");
			 json.put("idArea",1);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","a_jimenezq@hotmail.com");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","alekperez79@hotmail.com");
			 json.put("idArea",7);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","alfredocarrillo64@hotmail.com");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","amtzlpz1973@hotmail.com");
			 json.put("idArea",1);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","ar.go.ma@outlook.com");
			 json.put("idArea",7);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","arnulfperez083@gmail.com");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","asanchez201274@live.com.mx");
			 json.put("idArea",7);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","axdireccion@gmail.com");
			 json.put("idArea",23);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","balamgab@yahoo.com.mx");
			 json.put("idArea",1);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","brunoro99@yahoo.com");
			 json.put("idArea",23);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","car1046@hotmail.com");
			 json.put("idArea",23);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","carlos700201@yahoo.com.mx");
			 json.put("idArea",1);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","carlosaranda60@hotmail.com");
			 json.put("idArea",9);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","carlos.saldana@itelcel.com");
			 json.put("idArea",21);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","c_enriquezr@outlook.com");
			 json.put("idArea",3);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","cesarpcarlos@hotmail.com");
			 json.put("idArea",23);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","chelo.1974@hotmail.com");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","christian.jimenez.rh@gmail.com");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","corina.vargas@gmail.com");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","cosmesalvador@hotmail.com");
			 json.put("idArea",1);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","cruzarma62@gmail.com");
			 json.put("idArea",1);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","cs.joel@gmail.com");
			 json.put("idArea",21);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","csuarezl1973@gmail.com");
			 json.put("idArea",23);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","danielms888@yahoo.com.mx");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","david.constante@hotmail.com");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","davidmab85@hotmail.com");
			 json.put("idArea",1);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","dj.lopezp@gmail.com");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","donajigo@gmail.com");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","edgar.gtorres@hotmail.com");
			 json.put("idArea",7);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","edurom69@hotmail.com");
			 json.put("idArea",23);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","eleazar.esther@gmail.com");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","elisardzmz@gmail.com");
			 json.put("idArea",23);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","emiranda.28@hotmail.com");
			 json.put("idArea",9);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","enriquecarrilloflores@hotmail.com");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","enriquev@evheadhunter.com.mx");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","erika_arguetap@hotmail.com");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","ernesto_garcia_tapia@hotmail.com");
			 json.put("idArea",1);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","eservin2@hotmail.com");
			 json.put("idArea",23);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","farhidcime@gmail.com");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","felbarrios007@gmail.com");
			 json.put("idArea",1);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","felipechiwaviles@hotmail.com");
			 json.put("idArea",4);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","fernando.gomez.iglesias@gmail.com");
			 json.put("idArea",1);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","ficoazcorra20@icloud.com");
			 json.put("idArea",1);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","fiilibusterosport@yahoo.com.mx");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","fqperez@hotmail.com");
			 json.put("idArea",23);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","francisco_melendez54@yahoo.com.mx");
			 json.put("idArea",23);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","futole@hotmail.com");
			 json.put("idArea",1);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","fxmr61@yahoo.com.mx");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","garciajncrls@hotmail.com");
			 json.put("idArea",1);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","gbrionesv@hotmail.com");
			 json.put("idArea",1);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","gcervantesmm@gmail.com");
			 json.put("idArea",22);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","gc.mfer@gmail.com");
			 json.put("idArea",7);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","gerardo1609@hotmail.com");
			 json.put("idArea",1);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","geroti@hotmail.com");
			 json.put("idArea",1);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","geyvol.lopez@gmail.com");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","g.galvezh@hotmail.com");
			 json.put("idArea",1);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","gilemmmc@hotmail.com");
			 json.put("idArea",7);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","gmedra27@hotmail.com");
			 json.put("idArea",7);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","gromeroc75@gmail.com");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","gsmorton@hotmail.com");
			 json.put("idArea",23);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","guadalupe.mendoza.plata@gmail.com");
			 json.put("idArea",1);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","guillermo.aleman1@hotmail.com");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","guillermovm1968@yahoo.com.mx");
			 json.put("idArea",11);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","hazaaldana@hotmail.com");
			 json.put("idArea",3);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","hchagoya5@gmail.com");
			 json.put("idArea",7);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","howmar83@hotmail.com");
			 json.put("idArea",23);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","hugo.alarcong@live.com.mx");
			 json.put("idArea",23);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","humberto.ortiz.flores@gmail.com");
			 json.put("idArea",7);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","ingeniero_olivera@yahoo.com.mx");
			 json.put("idArea",7);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","ing-francisco2011@hotmail.com");
			 json.put("idArea",1);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","iscdmorales@gmail.com");
			 json.put("idArea",7);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","ivankendo@hotmail.com");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","ivanrojo_02@yahoo.com.mx");
			 json.put("idArea",1);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","jaime_ocampo13@hotmail.com");
			 json.put("idArea",17);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","jarlmir@hotmail.com");
			 json.put("idArea",7);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","jcaaus@yahoo.com");
			 json.put("idArea",9);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","jc_garzaro77@yahoo.com");
			 json.put("idArea",7);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","jcmadrig@hotmail.com");
			 json.put("idArea",1);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","jcmendoz191@hotmail.com");
			 json.put("idArea",7);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","jcu258@hotmail.com");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","jelias.resendizv@gmail.com");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","JERICKG@HOTMAIL.COM");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","jes180968@hotmail.com");
			 json.put("idArea",4);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","jessica.ortiz@grupoalx.com");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","jesus_251290@hotmail.com");
			 json.put("idArea",15);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","jesusdela_rosa@hotmail.com");
			 json.put("idArea",18);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","jesus.espejel.hernandez@gmail.com");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","jesusgomezcruz@hotmail.com");
			 json.put("idArea",7);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","jevar.ram@gmail.com");
			 json.put("idArea",7);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","jfgomezguerra@hotmail.com");
			 json.put("idArea",7);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","jlcanino@hotmail.com");
			 json.put("idArea",7);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","jluis0409@hotmail.com");
			 json.put("idArea",1);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","jmoncada65@hotmail.com");
			 json.put("idArea",9);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","joestradam_0911@hotmail.com");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","jomamtz2000@yahoo.com.mx");
			 json.put("idArea",7);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","jordymtz@yahoo.com");
			 json.put("idArea",23);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","jorge.rodriguezelizondo@gmail.com");
			 json.put("idArea",17);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","joseluis.davilas@hotmail.com");
			 json.put("idArea",7);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","josuearbaiza@gmail.com");
			 json.put("idArea",7);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","jrdzes@gmail.com");
			 json.put("idArea",9);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","jtrevillo@msn.com");
			 json.put("idArea",7);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","juanchemontedaw@hotmail.com");
			 json.put("idArea",1);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","juanc_serrano_e@yahoo.com.mx");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","j.w.gutierrez@hotmail.com");
			 json.put("idArea",15);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","jxchavezc@gmail.com");
			 json.put("idArea",5);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","karlaesquivel74@outlook.com");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","karla.victoria@hotmail.com");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","kristelsantiagog@gmail.com");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","lizpinaortega@gmail.com");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","lopezleticia465@yahoo.com.mx");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","lorenzovaladez@yahoo.com.mx");
			 json.put("idArea",7);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","lsosa65@hotmail.com");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","luispascacio@hotmail.com");
			 json.put("idArea",4);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","macorinapuyana@gmail.com");
			 json.put("idArea",1);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","manueldepuebla@hotmail.com");
			 json.put("idArea",23);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","maoryve@gmail.com");
			 json.put("idArea",7);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","marcial.viveros@live.com.mx");
			 json.put("idArea",4);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","mariotrevino68@gmail.com");
			 json.put("idArea",23);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","marisol.mmtz@hotmail.com");
			 json.put("idArea",7);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","martias2@hotmail.com");
			 json.put("idArea",21);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","martinperez_@hotmail.com");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","matiws_018@hotmail.com");
			 json.put("idArea",23);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","max_alarkon11@hotmail.com");
			 json.put("idArea",23);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","mayteiveth.perez@gmail.com");
			 json.put("idArea",7);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","mbd83@hotmail.com");
			 json.put("idArea",1);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","merced.hernandezjuarez@hotmail.com");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","mespinosa_monroy@hotmail.com");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","midario_23@yahoo.com.mx");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","mireromanf@yahoo.com.mx");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","miruizv@hotmail.com");
			 json.put("idArea",21);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","m_mazon_m@hotmail.com");
			 json.put("idArea",23);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","mmreys@hotmail.com");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","moika_wpa@hotmail.com");
			 json.put("idArea",1);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","mpeimbert@gmail.com");
			 json.put("idArea",18);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","mrico10@msn.com");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","mvaca220173@gmail.com");
			 json.put("idArea",1);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","my-chi-vis@hotmail.com");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","nanis99@yahoo.com");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","nataliagbaena@gmail.com");
			 json.put("idArea",3);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","norberto_abarca@msn.com");
			 json.put("idArea",7);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","norma_acr@hotmail.com");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","npintador@hotmail.com");
			 json.put("idArea",7);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","nso.acastaneda@gmail.com");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","obrogelio@hotmail.com");
			 json.put("idArea",7);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","olivares.abraham@yahoo.com.mx");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","omar9garcia@hotmail.com");
			 json.put("idArea",18);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","omar.romero755@gmail.com");
			 json.put("idArea",9);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","ontiveros.gabriel@hotmail.com");
			 json.put("idArea",1);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","orlando.garcia0@gmail.com");
			 json.put("idArea",7);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","ortizgo21@hotmail.com");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","oscarbarrazaperalta@hotmail.com");
			 json.put("idArea",1);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","pacooa22@hotmail.com");
			 json.put("idArea",7);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","pancho.angel@hotmail.com");
			 json.put("idArea",10);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","paobar2911@gmail.com");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","patrick_comerford@yahoo.com.mx");
			 json.put("idArea",9);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","paulettifernando@gmail.com");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","pepe_cantero02@hotmail.com");
			 json.put("idArea",7);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","pepecortes@msn.com");
			 json.put("idArea",1);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","pepe.gonzalezfigueroa@gmail.com");
			 json.put("idArea",9);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","pherdezs@hotmail.com");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","plinioriv@gmail.com");
			 json.put("idArea",23);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","psgarcia71@hotmail.com");
			 json.put("idArea",9);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","psyed14@hotmail.com");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","pzuviri@hotmail.com");
			 json.put("idArea",4);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","qcrick@gmail.com");
			 json.put("idArea",7);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","rafael_alvarez_aguilar@hotmail.com");
			 json.put("idArea",21);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","rafael.lll.ramirez@gmail.com");
			 json.put("idArea",23);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","rafaelpablosc@gmail.com");
			 json.put("idArea",15);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","ramalijo@gmail.com");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","raularrgon77@gmail.com");
			 json.put("idArea",23);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","ray_sg@hotmail.com");
			 json.put("idArea",23);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","rcruzbustamante@gmail.com");
			 json.put("idArea",7);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","rene.gomez.de.segura@gmail.com");
			 json.put("idArea",12);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","reneoceguera_78@yahoo.com.mx");
			 json.put("idArea",23);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","reygarciap@gmail.com");
			 json.put("idArea",1);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","RGMALA@HOTMAIL.COM");
			 json.put("idArea",9);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","rllano1980@gmail.com");
			 json.put("idArea",10);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","rlopez12486@hotmail.com");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","robertoprocelmagaldi@gmail.com");
			 json.put("idArea",1);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","rodriguez_jesus@hotmail.com");
			 json.put("idArea",7);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","rolandoflores_c@hotmail.com");
			 json.put("idArea",7);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","rolando.orozco.mendieta@gmail.com");
			 json.put("idArea",9);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","rola.pamo@gmail.com");
			 json.put("idArea",7);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","rpguzmanunibe@yahoo.com.mx");
			 json.put("idArea",1);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","rrvilla_1@hotmail.com");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","sainzdiego1978@gmail.com");
			 json.put("idArea",1);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","sanferleo80@gmail.com");
			 json.put("idArea",23);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","saurinact@gmail.com");
			 json.put("idArea",7);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","segysa-passt@outlook.com");
			 json.put("idArea",22);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","selex1@selex.com.mx");
			 json.put("idArea",22);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","sergiokuri@outlook.com");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","silvapugamanuel@gmail.com");
			 json.put("idArea",23);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","sozapatac@yahoo.es");
			 json.put("idArea",9);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","t.diazdelapena@gmail.com");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","terresliz@hotmail.com");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","tizoc-ml@hotmail.com");
			 json.put("idArea",7);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","torreblanca98@hotmail.com");
			 json.put("idArea",7);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","valery17_delao@hotmail.com");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","victorlopez_23@hotmail.com");
			 json.put("idArea",19);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","victorm.gutierrezj58@gmail.com");
			 json.put("idArea",1);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","viictor.calderon@live.com.mx");
			 json.put("idArea",1);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","vsdelgado@live.com.mx");
			 json.put("idArea",17);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","waco_v@hotmail.com");
			 json.put("idArea",7);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","walter.wm619@gmail.com");
			 json.put("idArea",7);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","ww4ur1c10@gmail.com");
			 json.put("idArea",9);
			 jsOut.put(json);

			 json= new JSONObject();
			 json.put("email","zury_op@hotmail.com");
			 json.put("idArea",10);
			 jsOut.put(json);	
			 
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsOut;
	}
	
	
}
