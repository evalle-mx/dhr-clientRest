package net.dothr.test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EvaluaCandidato {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			testEval();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	protected static void testEval() throws Exception {
		String certPosicion = "Human Resource Business Professional (HRBP)";
		JSONArray jsCerts = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("idGrado", "1");
		json.put("tituloCert", "ADMINISTRACION DE RECURSOS");
		jsCerts.put(json);
		json = new JSONObject();
		json.put("idGrado", "1");
		json.put("tituloCert", "J2EE");
		jsCerts.put(json);
		json = new JSONObject();
		json.put("idGrado", "1");
		//json.put("tituloCert", "Human Resource Business Professional (HRBP)"); //1.Exacto
//		json.put("tituloCert", "Profesional de Negocios de Recursos Humanos HRBP"); //2.Acrónimo
//		json.put("tituloCert", "Human Resource  B P"); //3. Tokens
		json.put("tituloCert", "HUMAN RESOURCE B P"); //3. Tokens Mayusculas, Obtiene 8 al caer en Acrónimo
		//json.put("tituloCert", "Profesional de Negocios de Recursos Humanos"); //4. Traducida
		jsCerts.put(json);
		int res = evalCert(certPosicion, jsCerts);
		System.out.println("RES: " + res);
	}
	
	private static int evalCert(String certPosicion, JSONArray jsCerts) throws Exception{
		JSONObject jsonCert;
		int res =0;
		String certPersona;
		System.out.println("Se evaluan "+ jsCerts.length() +" certificaciones-Persona\n\n" );
		for(int x=0; x<jsCerts.length(); x++){
			jsonCert = jsCerts.getJSONObject(x);
			certPersona=jsonCert.getString("tituloCert");
			System.out.println("certPersona: "+ certPersona);
			//1. Si es exactamente igual
			if(procTitulo(certPosicion).equals(procTitulo(certPersona))){
				System.out.println("Coincidencia Exacta");
				res=10;
			}
			else{
				List<String> lsTokens = Arrays.asList(certPersona.split("\\s* \\s*"));
				System.out.println("lsTokens.size(): "+lsTokens.size());
				Iterator<String> itTok = lsTokens.iterator();
				String token;
				Matcher mat;
				while(itTok.hasNext() && res == 0){
					token = itTok.next().replace("(", "").replace(")", "");
					System.out.println("token: -" + token+"-");
					mat = Pattern.compile("[A-Z]{3,}"). matcher(token);
					if(mat.matches()){ //Es Acrónimo
						System.out.println("Se busca el acronimo -"+token + "- en: "+ procTitulo(certPosicion));
						if(procTitulo(certPosicion).indexOf(token)!=-1){
							System.out.println("Se encontro el acrónimo "+token + " en certPosicion");
							res=8;
						}
					}
				}
				
				if(res==0){//Si sigue sin coincidir, se busca coincidencia de multiples tokens
					itTok = Arrays.asList(procTitulo(certPersona).split("\\s* \\s*")).iterator();
					int iToksPos = Arrays.asList(certPosicion.split("\\s* \\s*")).size();
					int coinc = 0, estad=0;
					while(itTok.hasNext()){
						token = itTok.next();
						if(token.replace(".", "").length()>2){//Para eliminar el Solo Mayusculas
							System.out.println("Se buscan coincidencia del token "+token +" \t en "+ procTitulo(certPosicion));
							if(procTitulo(certPosicion).indexOf(token)!=-1){
							 coinc++;
							}
						}
					}
					estad = (iToksPos/2); //Al menos la mitad de las palabras estan contenidas
					System.out.println( "coinc: "+coinc + ", toksPos/2="+estad +" => "+(coinc>=estad)) ;
					if(coinc>=estad){
						res=6;
					}
				}
				
			}
			//
			System.out.println("*****");
		}
		
		return res;
	}
	
	private static String procTitulo(String titulo){
		if(titulo!=null){
			titulo = titulo.toUpperCase();
			titulo = titulo.replace(" DE ", " ").replace(" PARA ", " ").replace(" CON ", " ").replace("(", "").replace(")", "");
			titulo = titulo.replace("Á", "A").replace("É", "E").replace("Í", "I").replace("Ó", "O").replace("Ú", "U");
			
			titulo = titulo.replace("  ", " ");
		}
		return titulo;
	}

}
