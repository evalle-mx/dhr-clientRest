package net.utils;
/**
 * Clase Generica que realiza conexiones a Base de Datos
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class ConnectionBD {
	
	private Connection connection = null;
	/* Parametros de conexión*/
    private String sDriver;
    private String sUrlConnection;
    private String sUser;
    private String sPassword;
    private String sDBaseName;
    
    /* objetos auxiliares */
    private Statement statementDB;
    private ResultSet rsDataBase;
    
    
    public ConnectionBD()
    {        
          sDriver = "";
          sUrlConnection = "localhost";	//localhost | ec2-52-2-148-215.compute-1.amazonaws.com | dothrinstance.cclfhplprrct.us-east-1.rds.amazonaws.com
//          sPort = "5432";
          sUser = "dothr"; 
          sPassword = "tc34dm1n";    	
          sDBaseName = "xe";
          connection = null;
          statementDB = null;
          rsDataBase = null;//resultado Query
    }
    
    public ConnectionBD(String webResource){
    	System.out.println("Conectando a BD en "+webResource);
    	if(webResource.equals(ConstantesREST.WEB_TRANSACT_LOCAL)){ //Apuntando a Localhost (AppTransactional)
    		sUrlConnection = "localhost";
            sDBaseName = "xe";
    	}
    	else if(webResource.equals(ConstantesREST.WEB_TRANSACT_AWS)){ ////Apuntando a AWS (AppTransactional)
    		sUrlConnection = "dothrinstance.cclfhplprrct.us-east-1.rds.amazonaws.com";  	
            sDBaseName = "xe";
    		System.err.println("<ConnectionBD 52> Descomentar para Utilizar en AWS (RDS)");
    	}
    	else if(webResource.equals("REPLICAAWS")){ ////Apuntando a REPLICA DE RDS
    		System.err.println("<ConnectionBD> Apuntando a REPLICA DE RDS-AWS (localhost:xeAWS )");
    		sUrlConnection = "localhost";
    		sDBaseName = "xeAWS";
    	}
    	else if(webResource.equals(ConstantesREST.OCTAVIO_APP)){ ////Apuntando a OCTAVIO (AppTransactional)
    		System.err.println("<ConnectionBD> Apuntando a OCTAVIO_APP");
    		sUrlConnection = ConstantesREST.IP_OCTAVIO;
    		sDBaseName = "xe";
    	}

    	else if(webResource.equals(ConstantesREST.HPSERVER_APP)){ ////Apuntando a HPServer (AppTransactional)
    		System.err.println("<ConnectionBD> Apuntando a HPSERVER_APP");
    		sUrlConnection = ConstantesREST.IP_HPSERVER;
    		sDBaseName = "xe";
    	}
    	sUser = "dothr"; 
        sPassword = "tc34dm1n";
    }
    
    
    public void testDriver() {//throws DataBaseException{
      	System.out.println("...............Probando el Driver");
      	sDriver = "org.postgresql.Driver";
      	try
    	{
      		System.out.println("Driver "+ sDriver);
    	   Class.forName( sDriver );
    	   System.out.println("ok!!!");
    	}catch (ClassNotFoundException e) {
            // Could not find the database driver
    		 e.printStackTrace();
              System.out.println("failClass");//*/
      	}catch (Exception e)
    	{
    	   e.printStackTrace();
    	}
    	System.out.println("Termina TestDriver...............");
      }
    
    
    public boolean getDbConn( ){//String jdbcip, String jdbcport, String sid) {
		String url = "";
		//Connection conn;
//		int enerror=0;
		try {
			//url = "jdbc:oracle:thin:@"+ sUrlConnection+":"+sPort+":"+sDBaseName;
//			DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());
			url = "jdbc:postgresql://"+sUrlConnection+"/"+sDBaseName+"?characterEncoding=utf8";
			DriverManager.registerDriver (new org.postgresql.Driver());
			//conn = DriverManager.getConnection(url,sUser,sPassword);
			//conn.setAutoCommit(false);
			connection = DriverManager.getConnection(url,sUser,sPassword);
			connection.setAutoCommit(false);
			statementDB = connection.createStatement();
			//System.out.println("Conexion obtenida");
			
			
			return true;
		} catch (SQLException sqlex) {
			System.out.println("Error al conectar a JDBC");
			System.out.println("Error:" + sqlex);
			return false;
		}
		//return false;
	}
    
    public ResultSet getQuerySet(String sQuery)throws Exception
    {
      //System.out.println("El Query solicitado es: "+sQuery + "\n\n);
    	try
    	{
    	   rsDataBase = statementDB.executeQuery(sQuery);
    	   return rsDataBase;
        }
        catch(SQLException sqle)
        {
           throw new Exception(sqle.getMessage());
        }
    }

public void updateDataBase(String sQuery)throws Exception
    {
      //System.out.println("El Update solicitado es: "+sQuery);
    	try
    	{
    		connection.setAutoCommit(true);
    	   statementDB.executeUpdate(sQuery);
    	   //connection.commit();
        }
        catch(SQLException sqle)
        {  
           try
           {
              connection.rollback();	
              throw new Exception(sqle.getMessage());
           }
           catch(SQLException sqlex)
           {
              throw new Exception(sqlex.getMessage());
           }
        }
    }

public void updateDataBaseNoCommit(String sQuery)throws Exception
{
  //System.out.println("El Update solicitado es: "+sQuery);
	try
	{
		connection.setAutoCommit(false);
	    statementDB.executeUpdate(sQuery);
	   //connection.commit();
    }
    catch(SQLException sqle)
    {  
       try
       {
          connection.rollback();	
          throw new Exception(sqle.getMessage());
       }
       catch(SQLException sqlex)
       {
          throw new Exception(sqlex.getMessage());
       }
    }
}
    
public void closeConnection()
{
    try
    {
    	if(statementDB!=null){
    		statementDB.close();
    	}
    	if(connection!=null){
    		connection.close();
    	}
      // System.out.println("Connection close");
    }
    catch(SQLException sqle)
    {
       System.out.println(sqle);	           
    }
}

public void commitQuery() throws SQLException{
	connection.commit();
}

/**
 * Ejemplo para leer la tabla de Persona en PostgreSQL
 * @param conn
 */
public static void testReadPersonas(ConnectionBD conn){
	try{
		String sql = "select * From Persona p " 
					+ " where p.id_persona < 15"
				;
		ResultSet rs = conn.getQuerySet(sql);
		ResultSetMetaData rsmd = rs.getMetaData();
		if(rs!= null ){
			int columnas = rsmd.getColumnCount();
			List<String> lsColumnName = new ArrayList<String>();
			for(int iCol = 0; iCol<columnas; iCol++){
				lsColumnName.add(iCol,rsmd.getColumnName(iCol+1));
			}
			System.out.println("lsColumnName= " + lsColumnName );
			while (rs.next()){
				StringBuilder st = new StringBuilder();
				for(int i=0; i<columnas; i++){
					st.append(rs.getString(i+1)).append("|");
				}
				String imp = st.toString();
				System.out.println(imp.substring(0,imp.length()-1));	
			}
		}
	}catch (Exception e) {
		e.printStackTrace();
	}
	finally{
		conn.closeConnection();
	}
}


/**
 * 
 * @param args
 */
    public static void main(String[] args) {
		ConnectionBD conn = new ConnectionBD(ConstantesREST.WEB_TRANSACT_LOCAL);
		conn.getDbConn();
		
		/*/ 1)Test Connection
		conn.testDriver();
		conn.closeConnection(); //*/		
		
		// 3) Query en otro metodo
		testReadPersonas(conn); 
		//*/
		/*  2 ) Query's
		try{
			int nRegistros = 0;
			String sql = "select * From TELEFONOS t";
			ResultSet rs = conn.getQuerySet(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			if(rs!= null ){
				int columnas = rsmd.getColumnCount();
				while (rs.next()){
					StringBuilder st = new StringBuilder();
					for(int i=0; i<columnas; i++){
						st.append(rs.getString(i+1)).append("|");
					}
					String imp = st.toString();
					System.out.println(imp.substring(0,imp.length()-1));					
//					System.out.print("CVE_FONDO: " + rs.getString("CVE_FONDO"));
//					System.out.print(", CVE_CUENTA_MAYOR: " + rs.getString("CVE_CUENTA_MAYOR"));
					nRegistros++;
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			conn.closeConnection();
		}
		System.out.println("total de registros: " + nRegistros); //*/
		
	}

}