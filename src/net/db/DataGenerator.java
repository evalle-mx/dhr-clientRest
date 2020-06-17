package net.db;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import net.utils.ConnectionBD;
/**
 * Clase muestra para acceder a la información de la base de Datos XE
 * @author dothr
 *
 */
public class DataGenerator {

	protected static ConnectionBD conn;
	
	/**
	 * Realiza una consulta a la tabla uriCodes y regresa el resultado en un StringBuilder 
	 * (texto plano), y posteriormente procesaro o exportarlo a un txt o un csv
	 * @return
	 */
	protected static StringBuilder readPermiso(){
		String sqlUricode = "SELECT * FROM permiso ", separator = ",";
		StringBuilder stb = new StringBuilder();
		try{
			conn.getDbConn();
			
			ResultSet rs = conn.getQuerySet(sqlUricode);
			ResultSetMetaData rsmd = rs.getMetaData();
			if(rs!= null ){
				int columnas = rsmd.getColumnCount(), iCol = 0;
				//List<String> lsColumnName = new ArrayList<String>();
				for(iCol = 0; iCol<columnas; iCol++){
					stb.append(rsmd.getColumnName(iCol+1)).append(iCol==columnas-1?"":separator);
				}
				stb.append("\n");
				while (rs.next()){
					for(iCol=0; iCol<columnas; iCol++){
						stb.append(rs.getString(iCol+1)).append(iCol==columnas-1?"":separator);
					}
					stb.append("\n");
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			stb.append("<EXCEPTION>").append(e.getMessage());
		}
		return stb;
	}
	
	/**
	 * Realiza una consulta a la tabla SOLICITADA y regresa el resultado en un StringBuilder 
	 * (texto plano), y posteriormente procesarlo o exportarlo a un txt o un csv
	 * @return
	 */
	public static StringBuilder getTableContent(String tableName){	
		StringBuilder stb = new StringBuilder("/* Se muestra la información de la tabla: ")
		.append(tableName).append(" */ \n\n");
		String sqlRead = "SELECT * FROM "+tableName+" ", separator = ",";
		int iRows = 0;
		try{
			conn.getDbConn();
			
			ResultSet rs = conn.getQuerySet(sqlRead);
			ResultSetMetaData rsmd = rs.getMetaData();
			if(rs!= null ){
				int columnas = rsmd.getColumnCount(), iCol = 0;
				//List<String> lsColumnName = new ArrayList<String>();
				for(iCol = 0; iCol<columnas; iCol++){
					stb.append(rsmd.getColumnName(iCol+1)).append(iCol==columnas-1?"":separator);
				}
				stb.append("\n");
				while (rs.next()){
					for(iCol=0; iCol<columnas; iCol++){
						stb.append(rs.getString(iCol+1)).append(iCol==columnas-1?"":separator);
					}
					stb.append("\n");
					iRows++;
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			stb.append("<EXCEPTION>").append(e.getMessage());
		}
		stb.append("\n\n /* Se obtuvieron "+iRows+" resultados */");
		return stb;
	}
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(conn==null){
			conn = new ConnectionBD(); 
		}
		String tableName = "permiso";
		StringBuilder sbTable = getTableContent(tableName);//generaPersonaPwd();
		System.out.println(sbTable.toString());
		
		
		conn.closeConnection();
	}

}
