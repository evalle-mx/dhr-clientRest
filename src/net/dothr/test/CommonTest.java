package net.dothr.test;

import java.util.ArrayList;
import java.util.Iterator;

import net.utils.ClientRestUtily;

public class CommonTest {
	
	private static final String pathTxt = "/home/dothr/JsonUI/mails.txt";
	
	public static void main(String[] args) {
		try {
//			mail2UserNs();
//			checkDuplicados();
			numerador();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Simple desplegado de números en consola
	 */
	public static void numerador(){
		int inicio = 1, fin = 760;
		for(int x=inicio;x<=fin;x++){
			System.out.println(x);
		}
	}
	
	
	public static void mail2UserNs() throws Exception {
		System.out.println("mail2UserNs");
		StringBuilder sb = new StringBuilder();
		String email, username;
		Iterator<String> itFile = ClientRestUtily.readListaTxt(pathTxt).iterator();
		while(itFile.hasNext()){
			email = itFile.next();
			username = email.substring(0, email.indexOf("@"));
			System.out.println(username);
			sb.append(username).append(".json").append("\n");
		}
		ClientRestUtily.writeStringInFile("/home/dothr/JsonUI/usernames.txt", sb.toString(), false);
	}
	
	public static void checkDuplicados() throws Exception {
		System.out.println("checkDuplicados");
		ArrayList<String> lsUserNames = new ArrayList<String>();
		Iterator<String> itFile = ClientRestUtily.readListaTxt("/home/dothr/JsonUI/PersonaRecreate/curriculumManagement/x.Selex2017/__lsCurriculumManagement.txt").iterator();
		String userNameFile, username, userNameLs;
		boolean duplicado = false;
		int nDuplicados = 0;
		while(itFile.hasNext()){
			userNameFile = itFile.next();
			username = userNameFile.substring(0, userNameFile.indexOf(".json"));
			for(int x=0; x<lsUserNames.size();x++){
				userNameLs = lsUserNames.get(x);
//				System.out.println("comparando "+username +" /"+userNameLs);
				if(username.equals(userNameLs)){
					//System.out.println(""+username +" == "+userNameLs);
					duplicado = true;
					nDuplicados++;
				}
			}
			
			if(duplicado){
				System.out.println(username );
			}else{
				lsUserNames.add(username);
			}
			duplicado = false;
		}
		
		System.out.println("# duplicados: " + nDuplicados);
	}

}
