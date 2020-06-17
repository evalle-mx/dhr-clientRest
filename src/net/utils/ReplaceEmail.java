package net.utils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
/**
 * Clase que genera un archivo SQL donde se actualiza correo electrónico con un dominio de prueba (@dhrtest.net) 
 *
 * @author dothr
 *
 */
public class ReplaceEmail {
	
	private static final String changeDomain = "@dhrtest.net";	// @dhrtest.net | @clicktoday.com.mx
//			"@dhrtest.net";
	static final String inPath = "/home/netto/Documents/TMP/emailsUser.txt"; /* documento desde query con idPersona,email, nombre */
	//"/home/dothr/workspace/ClientRest/files/emailsUser.txt";
	static final String outPath = "/home/netto/Documents/TMP/updateEmail.sql"; /* Documento sql de actualizacion */
	
	public static void main(String[] args) {
		updateEmails();
	}
	
	
	/**
	 * Regresa un correo Dummy con el mismo username pero dominio <b>dhrtest.net</b>
	 * @param email
	 * @return
	 */
	protected static String replaceDomain(String email){
		String username = email.substring(0, email.indexOf("@"));		
		return username+changeDomain;
	}
	
	/**
	 * Lee un archivo con idPersona;email para reemplazar el dominio del correo por un 
	 * dominio propio [@dhrtest.net], y genera un archivo SQL con update's para ejecutar en Base de Datos
	 */
	public static void updateEmails(){
		
		StringBuilder sbSql = new StringBuilder("/*  UPDATE de correos electrónicos para prueba */\n\n");
		
		
		List<String> lsLinea = ClientRestUtily.getLinesFile(inPath), lsTokens;
		System.out.println("lsLinea.size: " + lsLinea.size());
		Iterator<String> itLinea = lsLinea.iterator();
		String linea, idPersona, email, emailTest;
		
		while(itLinea.hasNext()){
			linea = itLinea.next();
			System.out.println("linea: " + linea );
			lsTokens = Arrays.asList(linea.split("\\s*;\\s*"));
			idPersona = lsTokens.get(0);
			email = lsTokens.get(1);
			
			emailTest = replaceDomain(email);
			sbSql.append("UPDATE persona SET email = '").append(emailTest).append("' WHERE id_persona=")
			.append(idPersona).append(";").append(" -- ").append(email).append("\n");
//			System.out.println("idPersona: " + idPersona + ", email: " + email + ", emailTest: "+emailTest);
		}
		System.out.println(sbSql);
		
		ClientRestUtily.writeStringInFile(outPath, 
				sbSql.toString(), false);
	}

}
