package net.mock.json;

import java.util.Iterator;
import java.util.List;

import net.utils.ClientRestUtily;

public class TempListasCompara extends RecreateXEJson {
	
	private static void compara () throws Exception {
		System.out.println("<compara>");
		String dir = "/home/dothr/Documents/SELEX/JsonUI/PersonaRecreate/curriculumManagement/";
		Iterator<String> itEmail = listFromFile(dir,"buscando.txt").iterator();
				
		List<String> lsLinea = listFromFile(
				dir,
				"fileNames_6.SelexP52017.csv");
		String linea, email1, email2;
		String [] tokens;
		Iterator<String> itLinea;
		StringBuilder sb = new StringBuilder();
		while(itEmail.hasNext()) {
			email1 = itEmail.next();
			System.out.println("Buscando "+email1);
			itLinea = lsLinea.iterator();
			while(itLinea.hasNext()) {
				linea = itLinea.next();
				tokens = linea.split("\\s*;\\s*");
				email2 = tokens[0];
				//System.out.println(email);
				if(email1.equals(email2)) {
					System.out.println("Se encontro " + email1 );
					sb.append(linea).append("\n");
					break;
				}
			}
		}
		
		ClientRestUtily.writeStringInFile(dir+"info.csv", 
				sb.toString(), false);
	}
	
	
	private static void compara2() throws Exception {
		System.out.println("<compara2>");
		String dir = "/home/dothr/Documents/SELEX/JsonUI/PersonaRecreate/curriculumManagement/";
		List<String> lsLineas = listFromFile(dir,"archivosNoListados.csv"); //Lista de no agregadas en XE
		List<String> lista1 = listFromFile(dir,"listaPx.txt");//Lista de p3.Varios
		Iterator<String> itLinea = lsLineas.iterator(), itEmail;
		String linea, email, emailp4;
		String [] tokens;
		boolean encontrado = false;
		StringBuilder sb = new StringBuilder();
		while(itLinea.hasNext()) {
			encontrado = false;
			emailp4 = "";
			linea = itLinea.next();
			tokens = linea.split("\\s*;\\s*");
			email = tokens[0];
			itEmail = lista1.iterator();
//			System.out.println("Buscando "+email);
			while(itEmail.hasNext()) {
				emailp4 = itEmail.next();
				if(email.equals(emailp4)) {
					encontrado = true;
					break;
				}
			}
			
			if(!encontrado) {
				System.out.println("No esta en p4.Varios");
				sb.append(linea).append("\n");
			}
			ClientRestUtily.writeStringInFile(dir+"noEnXE.csv", 
					sb.toString(), false);
		}
		
	}

	
	public static void generaCopyComm() throws Exception {
		String fromDir = "cp <DIR>6.SelexP52017/";
		String toDir = " <DIR>xeAll/incomp/";
		String dir = "/home/dothr/Documents/SELEX/JsonUI/PersonaRecreate/curriculumManagement/";
		
		StringBuilder sb = new StringBuilder("#Comando de copia \n");
		Iterator<String> itLineas = listFromFile(dir,"moverList.txt").iterator(); //Lista de no agregadas en XE
		String fileName;
		while(itLineas.hasNext()) {
			fileName = itLineas.next();
			//System.out.println(fileName);
			sb.append(fromDir.replace("<DIR>", dir) ).append(fileName)
			.append(toDir.replace("<DIR>", dir)).append(fileName).append("\n");
		}
		
		ClientRestUtily.writeStringInFile(dir+"mover.sh", 
				sb.toString(), false);
		
	}
	
	public static void main(String[] args) {
		try {
//			compara();
//			compara2();
			generaCopyComm();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
