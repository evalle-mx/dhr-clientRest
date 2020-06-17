package net.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Clase que permite insertar archivos binarios a la base de datos 
 * Metodos pertenecientes a procesos de Contenido (archivos Binarios)
 * utilizando JDBC 
 * @author dothr
 *
 */
public class ContenidoGenerator extends DBAccess {

/**
 * Obtiene el último id de la tabla contenido
 * @return
 */
public int maxIdContenido() {
	int max = -1;
	String sql = "select MAX(id_contenido) from CONTENIDO ";
	try{
		ResultSet rs = getQuerySet(sql);
		if(rs!= null ){
			while (rs.next()){
				max = rs.getInt(1);
				//System.out.println(max);
			}
		}
	}catch  (Exception e) {
		e.printStackTrace();
	}
	
	
	return max;
}

/**
 * Inserta una nueva imagen en la tabla XE.CONTENIDO
 * @param idContenido
 * @param idEntidad
 * @param idTipoContenido
 * @param pathFile
 * @param description
 * @param type (0->empresa, 1->Persona)
 */
public void insertBlob(int idEntidad, int idTipoContenido, String pathFile, String description, int type) {
	int idGet = -1;
//	String SQLCommand = "INSERT INTO CUSTOMER cust_id,title,firstname,lastname) " +
//			"VALUES(CUST_SEQUENCE.NEXTVAL,'" +  title + "','" + firstname + "','" + lastname + "')" ;
	System.out.println("idEntidad: "+idEntidad);
	String statement = "insert into CONTENIDO (id_contenido, ID_PERSONA, ID_TIPO_CONTENIDO_ARCHIVO,FECHA_CARGA, CONTENIDO, DESCRIPCION) ";
	if(type==1){//persona
		statement = "insert into CONTENIDO (id_contenido, ID_PERSONA, ID_TIPO_CONTENIDO_ARCHIVO,FECHA_CARGA, CONTENIDO, DESCRIPCION) ";
	}else if(type==0){//EMpresa
		statement = "insert into CONTENIDO (id_contenido, ID_EMPRESA, ID_TIPO_CONTENIDO_ARCHIVO,FECHA_CARGA, CONTENIDO, DESCRIPCION) ";
	}
	try{
		PreparedStatement pstmt = connection.prepareStatement( statement +
				" values (SEQ_CONTENIDO.NEXTVAL, ?, ?, ?, ?, ?)");
		File blobFile = new File(pathFile);
		InputStream in = new FileInputStream(blobFile);
		@SuppressWarnings("deprecation")
		java.sql.Date dt = new Date(2014, 5, 25);
//		pstmt.setInt(1, idContenido);
		pstmt.setInt(1, idEntidad);
		pstmt.setInt(2, idTipoContenido);
		pstmt.setDate(3, dt);
		pstmt.setBinaryStream(4, in, (int)blobFile.length());
		pstmt.setString(5, description!=null?description:" ");
		idGet = pstmt.executeUpdate();
		System.out.println("Inserción ejecutada con exito, resultado " + idGet);
				//conn.commit();
	}catch (Exception e) {
		e.printStackTrace();
	}
}



/**
 * 
 * @param args
 */
    public static void main(String[] args) {
    	ContenidoGenerator conn = new ContenidoGenerator();
		conn.getDbConn();
		
		/*/ 1)Test Connection
		conn.testDriver();
		conn.closeConnection(); //*/
		//*/ 5) Obtiene maximo idContenido
		System.out.println("Ultimo idRegistrado:" + conn.maxIdContenido() +"");//*/
		
		//*/ 4) Insert Blob
		int idPersona = 16;
		int idTipo = 5; //5->10.Imagen jpeg
		//conn.insertBlob(88, idPersona, idTipo, "/home/tce/Documents/bien256.jpg", "FotoPerfil-CR-Bien256",1);
		conn.insertBlob(idPersona, idTipo, "/home/tce/Documents/bien256.jpg", "FotoPerfil-CR-Bien256",1);
		conn.closeConnection();
			//*/
	}

}
