package net.dothr.report;

import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import net.db.QueryXe;
import net.dothr.MainAppTester;
import net.utils.ClientRestUtily;
import net.utils.ConnectionBD;
import net.utils.ConstantesREST;
import net.utils.DateUtily;
/**
 * Clase empleada para generar archivos csv con el reporte de usuarios según su estatus de inscripcion
 * @author dothr
 *
 */
public class UserDothrReporter extends MainAppTester {
	/*
	 * Procedimiento 
	 * 
	 * 1. Hacer un Backup de RDS-xe
	 * 
	 * 2. Cerrar la conexión con RDS
	 * 
	 * 3. Crear/Actualizar un esquema de Postgre Local llamado xeAWS (Donde apunta esta clase)
	 * 
	 * 4. Ejecutar el main para obtener 4 csv's en la ruta de salida [CSV_PATH]
	 * 
	 * 5. Agregar estos en un excel (xls)
	 */
	static Logger log4j = Logger.getLogger( UserDothrReporter.class );	
	protected static ConnectionBD conn;
	
	private static boolean inscrito = false, activonull = false, activoData = false, publicado =false;
	private static int nInscrito=0, nActivoNull=0, nActivoData=0, nPublicado=0;
	
	
	private static StringBuilder sbActivoNullParam;
	private static final String CSV_PATH = "/home/dothr/Documents/SELEX/RDS-Reporte/"; 
			//ConstantesREST.JSON_HOME+"Report/";
	private static final String WBRESOURCE = ConstantesREST.WEB_TRANSACT_AWS; //REPLICAAWS
	
	
	public static void main(String[] args) {		
		
		try{
			conn = new ConnectionBD(WBRESOURCE); 
			conn.getDbConn();
			String PRENOM = "AWS";
			log4j.debug("INICIO DE PROCESO ");
			ClientRestUtily.writeStringInFile(CSV_PATH+PRENOM+"1.SelexInscritos.csv", getInscritos().toString(), false);
			if(inscrito){
				ClientRestUtily.writeStringInFile(CSV_PATH+PRENOM+"2.SelexActivosNull.csv", getActivoNull().toString(), false);
				if(activonull){
					ClientRestUtily.writeStringInFile(CSV_PATH+PRENOM+"3.SelexActivosData.csv", getActivoData().toString(), false);
					if(activoData){
						ClientRestUtily.writeStringInFile(CSV_PATH+PRENOM+"4.SelexPublicado.csv", getPublicado().toString(), false);
						if(publicado){
							log4j.debug("Todos los reportes se generaron en \n" + CSV_PATH + "* \n" +
									textoCorreo()									
									);
						}
					}
				}
			}
			log4j.debug(" \n FIN DE PROCESO ");
		}catch (Exception e){
			e.printStackTrace();
			log4j.fatal("Excepción ");
		}//*/ 
		
		//System.out.println(QueryXe.SQL_3_ACTIVOS_NULOS);
	}
	
	/**
	 * genera una fecha con dias menos a la actual para usar en filtro de fecha
	 * @param diasMenos
	 * @return
	 */
	protected static String fechaFiltro(int diasMenos){
		Date fechaDias = DateUtily.getDateFrom(new Date(),(diasMenos*-1));
//		log4j.debug("fecha nueva: " + fechaDias );
		String fDate =date2String(fechaDias, "yyyy-MM-dd 00:00:00:001"); 
		return fDate;
	}
	
	
	/**
	 * Genera csv de Inscritos
	 * @return
	 */
	protected static StringBuilder getInscritos() {
		StringBuilder sbInscritos = new StringBuilder();
		try{
			//1. Pasar Inactivos con datos a Activos
			log4j.debug("<getInscritos> 1. Pasar Inactivos con datos a Activos");
			conn.updateDataBase(QueryXe.SQL_0_SET_ACTIVOS_DE_INSCRITOS.toString());
			
			//2. Obtener usuarios inactivos
			log4j.debug("<getInscritos> 2. Obtener Usuarios con estatus 1 (Inscritos sin Validar) ");
			
			ResultSet rs = conn.getQuerySet(QueryXe.SQL_1_INSCRITOS.toString());
			if(rs!= null ){			
				while (rs.next()){
					sbInscritos.append(rs.getLong(1))
					.append(",").append(rs.getString(2))
					.append(",").append(rs.getString(3))
					.append(",").append(rs.getString(4))

					.append("\n");
					nInscrito++;
				}
			}
			
			inscrito = true;
			
		}catch (Exception e){
			log4j.fatal("Excepcion en Inscritos ", e);
		}
		
		return sbInscritos;
	}
	
	/**
	 * Obtiene listado de Activos sin Datos
	 * @return
	 */
	protected static StringBuilder getActivoNull() {
		StringBuilder sbActivonull = new StringBuilder();
		try{
			// Obtener usuarios Activos (Datos elementales nulos), del resultado se deriva el siguiente QUery
			log4j.debug("<getActivoNull> 3. Usuarios que Validaron pero no accedieron a sistema ");
			
			ResultSet rs = conn.getQuerySet(QueryXe.SQL_2_ACTIVOS_NULOS.toString());
			if(rs!= null ){	
				sbActivoNullParam = new StringBuilder();
				while (rs.next()){
					sbActivonull.append(rs.getLong(1))
					.append(",").append(rs.getString(2))
					.append(",").append(rs.getString(3))
					.append(",").append(rs.getString(4))

					.append("\n");
					sbActivoNullParam.append(",").append(rs.getLong(1));
					nActivoNull++;
				}
			}
			
			activonull = true;
			
		}catch (Exception e){
			log4j.fatal("Excepcion en ActivoNull ", e);
		}
		
		return sbActivonull;
	}
	
	/**
	 * Obtiene listado de Activos con Datos
	 * @return
	 */
	protected static StringBuilder getActivoData() {
		StringBuilder sbActivoData = new StringBuilder();
		try{
			// Obtener usuarios Activos 
			log4j.debug("<getActivoData> 4. Usuarios que accedieron y tienen datos capturados pero no han Publicado");
			
			ResultSet rs = conn.getQuerySet(QueryXe.SQL_3_ACTIVOS_PORPUBLICAR.toString().replace("<idPersonas>", sbActivoNullParam));
			if(rs!= null ){			
				while (rs.next()){
					sbActivoData.append(rs.getLong(1))
					.append(",").append(rs.getString(2))
					.append(",").append(rs.getString(3))
					.append(",").append(rs.getString(4))
					.append(",").append(rs.getString(5))
					.append("\n");
					nActivoData++;
				}
			}
			
			activoData = true;
			
		}catch (Exception e){
			log4j.fatal("<getActivoData> Excepcion en Activos ", e);
		}
		
		return sbActivoData;
	}
	
	
	/**
	 * Obtiene listado de Publicados
	 * @return
	 */
	protected static StringBuilder getPublicado() {
		StringBuilder sbPublicado = new StringBuilder();
		try{
			// Obtener usuarios Activos (Datos elementales nulos), del resultado se deriva el siguiente QUery
			log4j.debug("<getPublicado> 5. Usuarios Publicados correctamente ");
			
			ResultSet rs = conn.getQuerySet(QueryXe.SQL_4_PUBLICADOS.toString());
			if(rs!= null ){			
				while (rs.next()){
					sbPublicado.append(rs.getLong(1))
					.append(",").append(rs.getString(2))
					.append(",").append(rs.getString(3))
					.append(",").append(rs.getString(4))
					.append(",").append(rs.getString(5))
					.append("\n");
					nPublicado++;
				}
			}
			publicado = true;
			
		}catch (Exception e){
			log4j.fatal("<getPublicado> Excepcion en ActivoNull ", e);
		}
		
		return sbPublicado;
	}

	
	/**
	 * Formatea la información para copiar directo al mensaje de correo
	 * @return
	 */
	private static String textoCorreo(){
		StringBuilder sb = new StringBuilder();
		
		Calendar cal = DateUtily.getCalendar(new Date());
		int inMes= cal.get(Calendar.MONTH)+1;		
				//mes == null ? getCalendar(new Date()).get(Calendar.MONTH)+1:Integer.parseInt(mes);
		int inDia=cal.get(Calendar.DATE);
				//dia == null ? getCalendar(new Date()).get(Calendar.DATE):Integer.parseInt(dia);
		String horamin = DateUtily.date2String(new Date(), "HH:mm");
		
		//log4j.debug("dia: "+inDia + " mes: " + DateUtily.mesLatino(inMes) + " ("+ inMes +") horamin: " + horamin );
		
		sb.append("\n >>>>   Relación usuarios ")
		.append(DateUtily.mesLatino(inMes)).append(" ").append(inDia).append(" <<<< \n\n");//"Agosto 9"
		
		sb.append("Hola, buen día:\n\n")
		.append("Anexo el archivo de relación de Usuarios registrados en el sistema, al día ")
		.append(inDia).append(" de ").append(DateUtily.mesLatino(inMes)).append(" a las ").append(horamin).append(" hrs. ").append("\n")
		.append("Clasificados en pestañas por su estatus en Inscritos, Activos (null)")
		.append(", Activos2 (Con Datos) y Publicados, cuya descripción se incluye en cada apartado.\n\n");

		sb.append("Los Usuarios se Clasifican de la siguiente manera")
		.append("\nRegistrados en Sistema:\t ").append( (nInscrito+nActivoNull+nActivoData+nPublicado) )
				.append("\n  Inscritos: \t  ").append(nInscrito)
				.append("\n  Activos Null:   ").append(nActivoNull).append(" (Sin datos capturados) ")
				.append("\n  Activos Data:   ").append(nActivoData).append(" (Datos incompletos) ")
				.append("\n  Publicados: \t  ").append(nPublicado)
				//.append("\n Total: ").append((nInscrito+nActivoNull+nActivoData+nPublicado) )
				;
		return sb.toString();
	}
}
