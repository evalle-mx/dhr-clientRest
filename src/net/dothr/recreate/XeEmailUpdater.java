package net.dothr.recreate;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import net.utils.ClientRestUtily;
import net.utils.ConnectionBD;
import net.utils.ConstantesREST;

/**
 * Esta clase hace una consulta a XE para obtener correo, nombre de persona,
 * para generar  un archivo sql para actualizar correos con @dothr.test, asi como nombre Camelizado
 * (Opcionalmente hacerlo en el momento con una bandera)
 * @author dothr
 *
 */
public class XeEmailUpdater {

	private ConnectionBD conn;
	private static final String REPORTE_PATH = "/home/dothr/workspace/ClientRest/files/out/"
			+"R.EmailUpdater.txt";
	private static final String SQL_FILE_PATH = "/home/dothr/workspace/ClientRest/files/out/"+
			"UPD.EmailPersona.sql";
	private static final String SQL_REVERSE_FILE_PATH = "/home/dothr/workspace/ClientRest/files/out/"+
			"UPD.Reverse-EmailPersona.sql";
	
	private static final String idPersonaInicio = "45";	// 45 | 184
	private static final boolean updEnCaliente = false; //Reemplaza el correo en tiempo de ejecución
	
	private static final String WEB_RESOURCE = ConstantesREST.WEB_TRANSACT_LOCAL;
	private static final String TEST_EMAIL_DOMAIN = "@dhrtest.net";
	
	private static final String QUERY_PERSONA_EMAIL =
			"SELECT id_persona, email, nombre, apellido_paterno, apellido_materno "
			+ "FROM persona WHERE id_persona > "+idPersonaInicio+" ORDER BY id_persona";
	private static final String QUERY_COUNT_USERNAME = "SELECT count(*) FROM persona where email like '<userName>@%' ;";
	
	private static final String UPD_EMAIL = "UPDATE persona SET email = '<emailTest>', nombre = '<nomCamel>'"
			+ ", apellido_paterno = '<apPaterno>', apellido_materno = '<apMaterno>' "
			+ " WHERE id_persona = <idPersona>; /*<emailAnt>*/";
	private static final String UPD_REV_EMAIL1 = "UPDATE persona SET email = '<emailOld>'"
			+ " WHERE email = '<emailTest>';";
	
	private StringBuilder sbRep, sbSql;
	
	
	public static void main(String[] args) {
		try {
			XeEmailUpdater xeUpd = new XeEmailUpdater();
			xeUpd.updaterEmail();
//			xeUpd.reverseSql();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Cuenta los correos que tienen un mismo username
	 * <b> count = 0</b> NO existe userName en tabla persona
	 * <b> count = 1</b> detectando el userName de parámetro
	 * <b> count > 1</b> Duplicidad del userName
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	private Long countUserName(String userName) throws Exception {
		Long cuenta;
		ResultSet rs = conn.getQuerySet(QUERY_COUNT_USERNAME.replace("<userName>", userName));
		if(rs.next()){
			cuenta = rs.getLong(1);
		}
		else{
			cuenta = new Long(0);
		}
		return cuenta;
	}
	
	/**
	 * Método principal para generar Query's de update y un archivo reporte
	 * @throws Exception
	 */
	protected void updaterEmail() throws Exception {
		conn = new ConnectionBD(WEB_RESOURCE);
		
		List<UsuarioDto> lsUsuario;
		ResultSet rs;
		String email, nombre, apPaterno, apMaterno, userName, updQuery;
		Long idPersona, cuenta;
		sbRep = new StringBuilder();
		
		sbRep.append("###  Conectando con XE \n");
		conn.getDbConn();
		sbRep.append("###  1: Query para Obtener Lista: ").append(QUERY_PERSONA_EMAIL).append(" \n");
		rs = conn.getQuerySet(QUERY_PERSONA_EMAIL);
		//1.Listar personas en Sistema
		if(rs!= null ){
			int nPersona = 0;
			lsUsuario = new ArrayList<XeEmailUpdater.UsuarioDto>();
			while (rs.next()){
				idPersona = rs.getLong(1);
				email = rs.getString(2);
				nombre = rs.getString(3)!=null?rs.getString(3):"";
				apPaterno = rs.getString(4)!=null?rs.getString(4):"";
				apMaterno = rs.getString(5)!=null?rs.getString(5):"";
				lsUsuario.add( new UsuarioDto(idPersona, email, nombre, apPaterno, apMaterno) );
				nPersona++;
			}
			rs.close();
			sbRep.append("# SE obtuvieron ").append(nPersona).append(" resultados (personas)\n");
			System.out.println(" Se procesan "+nPersona+" registros ");
			//2.Iterar lista
			if(!lsUsuario.isEmpty()){
				sbSql = new StringBuilder("/* QUERY DE ACTUALIZACIÓN DE CORREOS Y NOMBRE */\n");
				sbRep.append("idPersona;nombre;emailAnt;emailTest\n");
				UsuarioDto userDto;
				ListIterator<UsuarioDto> userIt = lsUsuario.listIterator();
				while(userIt.hasNext()){
					userDto = userIt.next();

					userName = userDto.getEmail().substring(0, userDto.getEmail().indexOf("@"));
					userDto.setUserName(userName);
					cuenta = countUserName(userName);
					
					System.out.println("userDto: " + userDto.toString2());
					if(cuenta>1){
						System.out.println("userName repetido: "+userName+", se añade indice ."+cuenta);
						userDto.setEmailTest( userName+"."+cuenta+TEST_EMAIL_DOMAIN);
					}else{
						userDto.setEmailTest( userName+TEST_EMAIL_DOMAIN );
					}
					updQuery = UPD_EMAIL.replace("<emailTest>", userDto.getEmailTest().toLowerCase())
							.replace("<nomCamel>", WordUtils.capitalizeFully(userDto.getNombre()))
							.replace("<apPaterno>", WordUtils.capitalizeFully(userDto.getApPaterno()))
							.replace("<apMaterno>", WordUtils.capitalizeFully(userDto.getApMaterno()))
							.replace("<idPersona>", String.valueOf(userDto.getIdPersona()))
							.replace("<emailAnt>", userDto.getEmail());
//					System.out.println( userDto.toString2() );
					sbSql.append( updQuery ).append("\n");
					if(updEnCaliente){
						System.out.println("se Hace el update de inmediato:"+updQuery);
						conn.updateDataBase(updQuery);
					}
					sbRep.append(userDto.getIdPersona()).append(";").append(userDto.getNombreCompleto()).append(";")
						.append(userDto.getEmail()).append(";").append(userDto.getEmailTest()).append("\n");
				}
				
				ClientRestUtily.writeStringInFile(SQL_FILE_PATH, sbSql.toString(), false);
				System.out.println("Se escribio archivo SQL: \n" + SQL_FILE_PATH );
			}
		}
		sbRep.append("\n #Termina Iteración, cerrando conexión \n");
		conn.closeConnection();
		sbRep.append("#######  FIN DE PROCESO   ##### ");
		ClientRestUtily.writeStringInFile(REPORTE_PATH, sbRep.toString(), false);
		System.out.println("Se escribio archivo reporte: \n" + REPORTE_PATH );
	}
	
	/**
	 * Lee el archivo sql generado anteriormente, para procesar las lineas y generar un 
	 * archivo sql con update de email original
	 * @throws Exception
	 */
	protected void reverseSql() throws Exception {
		System.out.println("<reverseSql>");
		List<String> lineas = ClientRestUtily.readListaTxt(SQL_FILE_PATH);
		if(lineas!=null && lineas.size()>0){
			String linea,updQuery;
			String testEmail, oldEmail; 
			String tok1 = "email = '", tok2="', nombre", tok3 = "; /*", tok4="*/";
			sbSql = new StringBuilder("/* Archivo para revertir el cambio de correo (Escribir correo Original)*/\n");
			ListIterator<String> itLinea = lineas.listIterator();
			while(itLinea.hasNext()){
				linea = itLinea.next();
//				System.out.println("linea: " + linea);
				if(!linea.startsWith("/*") && !linea.startsWith("#") && linea.length()>1){
					testEmail =  linea.substring(linea.indexOf(tok1)+tok1.length(), linea.indexOf(tok2));
					oldEmail =  linea.substring(linea.indexOf(tok3)+tok3.length(), linea.indexOf(tok4));
					
					updQuery = UPD_REV_EMAIL1
							.replace("<emailTest>", testEmail)
							.replace("<emailOld>", oldEmail)
							;
					System.out.println( updQuery );
					sbSql.append( updQuery ).append("\n");
				}
				else{
					System.out.println("NO SE TOMA EN CUENTA: " + linea);
				}
			}

			ClientRestUtily.writeStringInFile(SQL_REVERSE_FILE_PATH, sbSql.toString(), false);
			System.out.println("Se escribio archivo "+SQL_REVERSE_FILE_PATH);
		}
	}
	
	/**
	 * Clase DTO auxiliar
	 * @author dothr
	 *
	 */
	class UsuarioDto {
		String email;
		String nombre;
		String apPaterno;
		String apMaterno;
		String nombreCompleto;
		String userName;
		String emailTest;
		Long idPersona;
		
		public UsuarioDto() {}
		
		public UsuarioDto(Long idPersona, String email, String nombre, String apPaterno, String apMaterno) {
			this.idPersona=idPersona;
			this.email=email;
			this.nombre=nombre;
			this.apPaterno=apPaterno;
			this.apMaterno=apMaterno;
		}
		
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getNombre() {
			return nombre;
		}
		public void setNombre(String nombre) {
			this.nombre = nombre;
		}
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		public String getEmailTest() {
			return emailTest;
		}
		public void setEmailTest(String emailTest) {
			this.emailTest = emailTest;
		}
		public Long getIdPersona() {
			return idPersona;
		}
		public void setIdPersona(Long idPersona) {
			this.idPersona = idPersona;
		}
		public String getApPaterno() {
			return apPaterno;
		}
		public void setApPaterno(String apPaterno) {
			this.apPaterno = apPaterno;
		}
		public String getApMaterno() {
			return apMaterno;
		}
		public void setApMaterno(String apMaterno) {
			this.apMaterno = apMaterno;
		}
		public String getNombreCompleto() {
			return (this.nombre!=null?this.nombre:"")+
					" "+(this.apPaterno!=null?this.apPaterno:"") + 
					" "+ (this.apMaterno!=null?this.apMaterno:"");
		}
		
		public String toString() {
		        return ReflectionToStringBuilder.toString(this);
//		    	return ReflectionToStringBuilder.toString(this).replace("=<null>", "=null");
		}
		
		public String toString2(){
			return "idPersona:"+this.idPersona+", email: "+this.email+", nombre: "+this.nombre
					+ ", nombreCompleto: "+getNombreCompleto()
					+ (this.userName!=null?", userName: "+this.userName:"") + (this.emailTest!=null?", emailTest: "+this.emailTest:"");
		}
	}
}
