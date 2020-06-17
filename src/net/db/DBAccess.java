package net.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * Clase para conectarse a la base de datos, independiente del común net.utils.ConnectionBD
 * para utilizarse de manera directa en este paquete
 * @author dothr
 *
 */
public class DBAccess {

	protected Connection connection = null;
    private String sDriver;
    private String sUser;
    private String sPassword;
    private String sDBaseName;

    private Statement statementDB;
    private ResultSet rsDataBase;
    
    public DBAccess() {
    	sDriver = "";
        sUser = "dothr"; 
        sPassword = "tc34dm1n";    	
        sDBaseName = "xe";
        connection = null;
        statementDB = null;
        rsDataBase = null;//resultado Query
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
			url = "jdbc:postgresql://localhost/"+sDBaseName;
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
    
	public void closeConnection()
	{
	    try
	    {
	       statementDB.close();
	       connection.close();
	      // System.out.println("Connection close");
	    }
	    catch(SQLException sqle)
	    {
	       System.out.println(sqle);	           
	    }
	}
}
