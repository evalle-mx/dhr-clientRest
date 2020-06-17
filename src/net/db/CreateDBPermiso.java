package net.db;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.utils.ClientRestUtily;
/**
 * Clase que lee un archivo Matriz para generar los nuevos csv a colocar en la carpeta Load del 
 * script de postgresql
 * @author dothr
 *
 */
public class CreateDBPermiso {

	//Inputs (archivos de entrada)
	private static final String ROOTPATH = "/home/dothr/workspace/ClientRest/files/";
	private static final String pathCSVMatriz = ROOTPATH+"RolesPermiso.csv";//Se genera manualmente a partir de la matriz
	//Outputs (Archivos generados para Mock y BD)
	private static final String pathCSVOUT = ROOTPATH+"csvOut/";
	private static final String pathCSVPermiso = pathCSVOUT+"permiso.csv";
	private static final String pathCSVRolPermiso = pathCSVOUT+"rol_permiso.csv";
	private static final String pathSQL = ROOTPATH+"sql/insertsPermiso.sql";
	
	
	/* Queries SQL*/
	private static final String INS_PERMISO = "INSERT INTO permiso "+
	"(id_permiso, id_permiso_relacionada, contexto, valor, descripcion, id_tipo_permiso) VALUES \n";	
	private static final String INS_ROLPERMISO = "INSERT INTO rol_permiso "+
	"(id_rol_permiso,id_rol,id_permiso,activo) VALUES \n";
	
	
	protected static String[] idRols = {"1","2","3","4","5"};
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
//		numerador();
		
		
		/* 1.- Agregar los nuevos uriCodes/menu en el archivo Roles_Permisos.xls*/
		/* 2.- A partir de Roles_Permisos.xls, crear/reemplazar datos en inRolesPErm.csv (CAMBIAR ',' por ';') */
		/* 3.- Ejecutar reordenaMatriz para generar Roles_Permisos.csv  */
//		reordenaMatriz();
		/* 4.- Ejecutar generaCsvsDeMatriz para Generar archivos CSV */
		generaCsvsDeMatriz();
		
		
		
		/* ******************************* DEPRECADO ************* */
		//Paso 0: Hacer las modificaciones requeridas en la Matriz de Roles_Permisos.xls para agregar o renombrar permisos, y usuarios  
		//Paso 1: Copiar datos de Roles_Permisos.xls a RolesPermiso.csv (Separador ';' )
		//**Paso 2: Generar archivos CSV para TMock (y paso3)
//		generaCsvsDeMatriz();
		/* ***************************************** */
		
		/*
		 *  *Para reinsertar los datos en la base sin reiniciar: *
		 * 	> DELETE from rol_permiso; DELETE from permiso;
		 * 	cd /home/dothr/workspace/ClientRest/files/csvOut/
		 * 	psql xe -U dothr -p 5432 -h localhost -c "\copy permiso from 'permiso.csv' with DELIMITER ';' NULL AS ''"
		 * 	psql xe -U dothr -p 5432 -h localhost -c "\copy rol_permiso from 'rol_permiso.csv' with DELIMITER ';' NULL AS ''"
		 */
		
//		genSQLInserts();
	}
	
	public static void numerador(){
		int max = 177;
		for(int x=1;x<max;x++){
			System.out.println(x);
		}
	}
	
	
	public static void reordenaMatriz(){
		StringBuilder csvOut = new StringBuilder();
		
		String inputCsv = "/home/dothr/workspace/ClientRest/files/inRolesPErm.csv";//Csv que contiene: Contexto,Valor,descripcion,idTipoPermiso,X,1,2,3,4
		//obtenemos lineas de archivo
		List<String> lsRows = ClientRestUtily.getLinesFile(inputCsv);
		if(!lsRows.isEmpty()){
			//List<String> cols;
			String[] cols;
			Iterator<String> itRow = lsRows.iterator();
			String linea, contexto, valor, descripcion, idTipoPermiso;
			/*para Tipo Menu */
			String[] subFolders;
			String fPadre = "";
			// <fPadre(Contexto), indicePermiso>
			HashMap<String, Integer> mapaMenu = new HashMap<String, Integer>();
			
			int iColIn = 0, indicePermiso=1;
			Integer indiceRel;
			while(itRow.hasNext()){
				linea = itRow.next();
//				System.out.println(linea);
				cols = linea.split("\\s*;\\s*");
//				System.out.println(cols.length);
				contexto = cols[iColIn];//0
				valor = cols[iColIn+1];//1
				descripcion= cols[iColIn+2];//2
				idTipoPermiso = cols[iColIn+3];//3
				indiceRel = null;
				//En adelante (4,5,6,7,8...)son espacio y permisos, que no se emplean en algoritmo
				
				/*  Permiso Uricode */
				if(idTipoPermiso.equals("1")){
					//solo se Enumera y se agrega al builder 
					csvOut.append(indicePermiso).append(";;").append(linea);
				}
				/*  Permiso Menu */
				else if(idTipoPermiso.equals("2")){					
					//Se valida si descripción tiene diagonales (subcarpeta)
					subFolders = descripcion.split("/");
					//<fPadre(Contexto), indicePermiso>
//					System.out.println(contexto + ", subFolders.length: "+subFolders.length );
					
					if(subFolders.length>2){						
						fPadre = subFolders[subFolders.length-2]; //fPadre 
						indiceRel = mapaMenu.get(fPadre);						
						csvOut.append(indicePermiso).append(";").append(indiceRel==null?"":indiceRel).append(";").append(linea);
						//Si valor tiene dato, es hoja, sino es rama
//						System.out.println("valor: "+valor + " .length "+valor.length());
						if(valor.length()<1){
							System.out.println("Se guarda en MapaMenu <"+contexto+","+indicePermiso+">");
							mapaMenu.put(contexto, indicePermiso);
						}
					}
					else{//Es raiz ('' / 'Hoja')
						System.out.println("Se guarda en MapaMenu <"+contexto+","+indicePermiso+">");
						mapaMenu.put(contexto, indicePermiso);
						csvOut.append(indicePermiso).append(";;").append(linea);
					}
				}
				indicePermiso++;
				csvOut.append("\n");
			}
			System.out.println("Se crea nuevo archivo de RolesPermiso:\n"+pathCSVMatriz);
			ClientRestUtily.writeStringInFile(pathCSVMatriz, csvOut.toString(), false);
		}
		
		
	}
	
	/**
	 * Genera los archivos permiso.csv y rol_permiso.csv para insertar en XE
	 * a partir del archivo Matriz (RolesPermiso.csv), que contiene permisos+roles
	 */
	public static void generaCsvsDeMatriz(){
		StringBuilder sbPermiso = new StringBuilder(), sbRolPerm = new StringBuilder();
		List<String> lsRows = ClientRestUtily.getLinesFile(pathCSVMatriz);
		Iterator<String> itRow;
		List<String> cols=null;
			//roles base del sistema (adm, adOp, cand, cont)
		String row;
		if(lsRows!=null && lsRows.size()>0){
			
			//1. Iteración para generar permiso.csv
			itRow = lsRows.iterator();
			int colsRol = 0;
			while(itRow.hasNext()){
				row = itRow.next();
				if(row.indexOf(",")!=-1){
					row = row.replaceAll(",",";");
				}
				cols = Arrays.asList(row.split("\\s*;\\s*"));
				System.out.println("cols: " + cols );
				sbPermiso.append("")
				.append(cols.get(0)).append(";")	//id_permiso
				.append(cols.get(1)).append(";")	//id_permiso_relacionada
				.append(cols.get(2)).append(";")	//contexto
				.append(cols.get(3)).append(";")	//valor
				.append(cols.get(4)).append(";")	//descripcion
				.append(cols.get(5)).append("\n");	//id_tipo_permiso
			}
			System.out.println("cols.size()" + cols.size() );
			colsRol = cols.size()-7;
			ClientRestUtily.writeStringInFile(pathCSVPermiso, sbPermiso.toString(), false);
			System.out.println("Se generó correctamente archivo:"+pathCSVPermiso); //permiso.csv
			
			//2. X iteraciones por cada Columna de rol (col >6)
			if(colsRol>=idRols.length){
				int indiceRolPer = 1;
				System.out.println("roles: "+idRols);
				for(int x=0;x<idRols.length;x++){
					System.out.println("se para rol "+ idRols[x] + "");
					itRow = lsRows.iterator();
					while(itRow.hasNext()){
						row = itRow.next();
						cols = Arrays.asList(row.split("\\s*;\\s*"));
						sbRolPerm.append(indiceRolPer==1?"":"\n")
						.append(indiceRolPer).append(";")	//id_rol_permiso
						.append(idRols[x]).append(";")		//id_rol
						.append(cols.get(0)).append(";")	//id_permiso
						.append(cols.get(7+x).equals("t")?"t":"f");	//activo (t/f)
						indiceRolPer++;
					}
				}
				ClientRestUtily.writeStringInFile(pathCSVRolPermiso, sbRolPerm.toString(), false);
				System.out.println("Se generó correctamente archivo:"+pathCSVRolPermiso);//rol_permiso.csv
				
				System.out.println("\nPara reinsertar los datos en la base sin reiniciar: \n"+
						"> DELETE from rol_permiso; DELETE from permiso; \n"+
						"cd "+pathCSVOUT+" \n"+
						"psql xe -U dothr -p 5432 -h localhost -c \"\\copy permiso from 'permiso.csv' with DELIMITER ';' NULL AS ''\" \n"+
						"psql xe -U dothr -p 5432 -h localhost -c \"\\copy rol_permiso from 'rol_permiso.csv' with DELIMITER ';' NULL AS ''\" \n");
			}			
		}else{
			System.err.println("EL archivo no es correcto");
		}
	}
	
	
	/* ********************************************************************** */
	/* ************  GENERA CODIGO SQL A PARTIR DE ARCHIVOS CSV  ******************* */
	/* ********************************************************************** */
	
	/**
	 * utiliza metodos para obtener inserts de rol_permiso y permiso
	 */
	public static void genSQLInserts(){
		StringBuilder sbQuery = new StringBuilder();
		
		
		sbQuery.append("DELETE FROM rol_permiso;\nDELETE FROM permiso; \n\n");
		System.out.println("Procesando permiso.csv");
		sbQuery.append("\n/*  DELETE/INSERT para permiso */ \n");
		sbQuery.append(insertsPermiso());
		
		System.out.println("Procesando rol_permiso.csv");
		sbQuery.append("\n/*  DELETE/INSERT para rol permiso */ \n");
		sbQuery.append(insertsRolPermiso());
		
		System.out.println("Escribiendo Archivo");
		ClientRestUtily.writeStringInFile(pathSQL, sbQuery.toString(), false);
		System.out.println("Se escribio el archivo SQL: " + pathSQL );
	}
	
	/**
	 * Lee el archivo rol_permiso.csv y generá los inserts
	 */
	public static StringBuilder insertsRolPermiso(){
		StringBuilder sbQuery = new StringBuilder(INS_ROLPERMISO);
		List<String> lsRows = ClientRestUtily.getLinesFile(pathCSVRolPermiso);
		if(lsRows!=null && lsRows.size()>0){
			Iterator<String> itReg = lsRows.iterator();
			List<String> cols;			
			String row;
			while(itReg.hasNext()){
				row = itReg.next();
				cols = Arrays.asList(row.split("\\s*;\\s*"));
				//System.out.println("cols: " + cols );
				sbQuery.append("(")
				.append(cols.get(0)).append(",")
				.append(cols.get(1)).append(",")
				.append(cols.get(2)).append(",")
				.append(cols.get(3).equals("t")?"true":"false").append(")")
				.append(itReg.hasNext()?",":";").append("\n");				
			}
		}
		//System.out.println(sbQuery);
		return sbQuery;
	}
	
	/**
	 * Lee el archivo Permiso.csv y generá los inserts
	 */
	public static StringBuilder insertsPermiso(){
		StringBuilder sbQuery = new StringBuilder(INS_PERMISO);
		List<String> lsRows = ClientRestUtily.getLinesFile(pathCSVPermiso);
		if(lsRows!=null && lsRows.size()>0){
			Iterator<String> itReg = lsRows.iterator();
			List<String> cols;			
			String row;
			while(itReg.hasNext()){
				row = itReg.next();
				cols = Arrays.asList(row.split("\\s*;\\s*"));
				//System.out.println("cols: " + cols );
				sbQuery.append("(")
				.append(cols.get(0)).append(",")
				.append(cols.get(1).equals("")?"null":cols.get(1)).append(",")
				.append("'").append(cols.get(2)).append("',")
				.append("'").append(cols.get(3)).append("',")
				.append("'").append(cols.get(4)).append("',")
				.append(cols.get(5)).append(")")
				.append(itReg.hasNext()?",":";").append("\n");				
			}
		}
		//System.out.println(sbQuery);
		return sbQuery;
	}
}
