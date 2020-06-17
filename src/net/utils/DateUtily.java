package net.utils;

/*
 * HELP:
 * https://help.gooddata.com/cloudconnect/manual/date-and-time-format.html
 */
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.log4j.Logger;

public class DateUtily {

	private static Logger log4j = Logger.getLogger( DateUtily.class );
	
	private static TimeZone TIMEZONE = TimeZone.getTimeZone("Mexico/General");
	private static Locale LOCALE = new Locale("es","MX");
	private static final String DEFAULT_PATTERN_DATE = "dd/MM/yyyy";
	private final static int MULT_MILISEG_A_DIAS =(24 * 60 * 60 * 1000);
	
	 /**
     * Comprueba si la fecha formado por los argumentos es válida.
     * @param a anio 
     * @param m mes
     * @param d día
     * @return cierto si la fecha es válida, esto es,
     */
	public static boolean esFechaValida(int anioBase,String anio, String mes, String dia) {	
		int inAnio=anio == null ? anioBase:Integer.parseInt(anio);
		int inMes=mes == null ? (getCalendar(new Date()).get(Calendar.MONTH) + 1):Integer.parseInt(mes);
		int inDia=dia == null ? getCalendar(new Date()).get(Calendar.DATE):Integer.parseInt(dia);
		log4j.info("<esFechaValida> inAnio/inMes/inDia :" + inAnio + "/" + inMes + "/" + inDia);
        return isValidDate(String.valueOf(inDia).concat("/").concat(String.valueOf(inMes)).concat("/").
				 		   concat(String.valueOf(inAnio)), DEFAULT_PATTERN_DATE);
    }
    
	
	/**
     * Verifica que el parametro sea una fecha válida  
	 * @param sDate 
	 * @param pattern, i.e. dd/MM/yyyy
     * @return boolean
     */
    public static boolean isValidDate(String sDate,String pattern){
    	boolean resp=true;
    	try{
			SimpleDateFormat sdf=new SimpleDateFormat(pattern);
			sdf.setLenient(false);
			sdf.parse(sDate);
    		}catch(ParseException e){
    			resp=false;
    		}
    		log4j.debug(" &&  isValidDate() -> sDate:"+sDate+ " valido="+resp);
    		return resp;
    	}
	
	/**
	 * Metodo que compara dos fechas y  regresa
	 * 1: date1 > date2, -1: date1 < date2 y 0: equals
	 * @param date1
	 * @param date2 
	 * @return integer
	 */
	public static int compareDt1Dt2(Date date1, Date date2){
		return date1.compareTo(date2);
	}
	
	/**
	 * Regresa una fecha correspondiente a los parametros asignados
	 * @param anioBase, año propuesto
	 * @param anio, año
	 * @param mes, 0-11
	 * @param dia, 1-31
	 * @param hora, 1-60
	 * @param minuto, 1-60
	 * @param segundo,1-60
	 * @return  fecha correspondiente
	 */
	public static Date setDate(int anioBase, String anio, String mes, 
								String dia,String hora, String minuto, String segundo ){
		int inAnio=anio == null ? anioBase:Integer.parseInt(anio);
		int inMes=mes == null ? getCalendar(new Date()).get(Calendar.MONTH)+1:Integer.parseInt(mes);
		int inDia=dia == null ? getCalendar(new Date()).get(Calendar.DATE):Integer.parseInt(dia);
		log4j.debug(" setDate() -----------> "+inAnio+"/"+inMes+"/"+inDia+" "+hora+":"+minuto+" "+segundo);
		return setDateDirect(inAnio, inMes, inDia,Integer.parseInt(hora),
				Integer.parseInt(minuto),Integer.parseInt(segundo));
	}
	
	/**
	 * Regresa una fecha correspondiente a los parametros asignados
	 * @param anio, año
	 * @param mes, 0-11
	 * @param dia, 1-31
	 * @param hora, 1-60
	 * @param minuto, 1-60
	 * @param segundo,1-60
	 * @return  fecha correspondiente
	 */
	public static Date setDateDirect( int anio, int mes, 
			int dia,int hora, int minuto, int segundo ){
		Calendar cal = Calendar.getInstance(TIMEZONE, LOCALE);
		cal.clear();
		cal.set(anio, mes - 1, dia,hora,minuto,segundo);
		log4j.debug("setDateDirect() --> "+cal.getTime().getTime());
		return cal.getTime();
	}
	
	/**
	 * Regrea un objeto Calendar dado un objeto date
	 * @param date
	 * @return
	 */
	public static Calendar getCalendar(Date date){				 		
		 Calendar cal=Calendar.getInstance(TIMEZONE, LOCALE);
		 cal.setTime(date);
		 return cal;
	}
	
	/**
	 * returns the number of days Between 2 dates
	 * @param dt1
	 * @param dt2
	 * @return
	 */
	public static long daysBetween(Date dIni, Date dFin){
		double days =lBetween(dIni, dFin, MULT_MILISEG_A_DIAS); 
	    return (new Double(days)).longValue();
	}
	/**
	 * Regresa fecha solicitada con patron asignado
	 * @param d (Date)
	 * @param pattern (String) i.e. dd/MM/yyyy
	 * @return String
	 */
	public static String thisDateFormated(Date d, String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(d);
	}
	
	/**
	 * Regresa la diferencia entre dos fechas
	 * @param dt1, primera fecha
	 * @param dt2, ultima fecha
	 * @param divider, este valor se le divide a la diferencia de las fechas
	 * @return
	 */
	public static double lBetween(Date dt1, Date dt2, long divider){
		double days = ((double)(dt2.getTime() - dt1.getTime()) / divider );
		return redondear(days, 0,RoundingMode.HALF_UP);
	}
	
	/**
	 * Regresa la fecha actual con: Horas, minutos y segundos, en cero 
	 * @return Date
	 */
	public static Date getTodayZero(){		
		Calendar fechaCal = Calendar.getInstance(TIMEZONE, LOCALE);
		fechaCal.set(Calendar.HOUR_OF_DAY, 0);
		fechaCal.set(Calendar.MINUTE, 0);
		fechaCal.set(Calendar.SECOND, 0);
		fechaCal.set(Calendar.MILLISECOND, 0);
		return fechaCal.getTime();
	}
	
	/**
	 * Regresa una fecha dada, en un string, dependiendo del formato
	 * @param fecha, fecha a convertir
	 * @param pattern, formato asigando
	 * @return la fecha en string
	 */
	public static String date2String(Date fecha, String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(fecha);
	}

	/**
	 * Convierte un string a fecha
	 * @param String
	 * @returns Date
	 * @throws Exception 
	 * @throws Exception 
	 */
	public static Date string2Date(String dateString, String pattern) throws Exception{
		Calendar calendar = getCalendar();
		calendar.setTime(new SimpleDateFormat(pattern).parse(dateString));
		
		return calendar.getTime();
	}
	/**
	 * Regrea un objeto Calendar
	 * @param date
	 * @return
	 */
	public static Calendar getCalendar(){				 		
		 Calendar cal=Calendar.getInstance(TIMEZONE, LOCALE);
		 return cal;
	}
	
	/**
	 * Regresa la edad de la fecha de nacimiento
	 * @param fecha de nacimiento
	 * @param format, es el formato de la  fecha
	 * @return
	 */
	public static Integer calcularEdad(String fecha, String format) {
		try {
//			log4j.debug("%$%/ fecha "+fecha);
			Calendar birth =Calendar.getInstance(TIMEZONE, LOCALE);
			Calendar today =Calendar.getInstance(TIMEZONE, LOCALE);
			Integer age=0;
			int factor=0;
			Date birthDate=new SimpleDateFormat(format).parse(fecha);
			Date currentDate=new Date(); //current date
			birth.setTime(birthDate);
			today.setTime(currentDate);
			if (today.get(Calendar.MONTH) <= birth.get(Calendar.MONTH)) {
				if (today.get(Calendar.MONTH) == birth.get(Calendar.MONTH)) {	
					if (today.get(Calendar.DATE) > birth.get(Calendar.DATE)) {
						factor = -1; //Aun no celebra su cumpleaños
					}
				} else {
					factor = -1; //Aun no celebra su cumpleaños
				}
			}
			age=(today.get(Calendar.YEAR)-birth.get(Calendar.YEAR))+factor;
			return age;
		} catch (ParseException e) {
			log4j.error(e.getMessage());
			e.printStackTrace();
			return -1;
		}
	}
	
	/**
	 * Regresa una fecha en base a parametros de entrada, de manera que año debe ser cadena de 4 caracteres,<br>
	 * y dia puede ser null, tomandose por default el 1er dia del mes.<br>
	 * <b>Si alguno de los 3 parametros es invalido, regresa <i>null</i> </b>
	 * @param anio i.e. 1999
	 * @param mes i.e. 12
	 * @param dia i.e. 25
	 * @return Date <u>i.e.</u> Sat Dec 25 00:00:00 CST 1999
	 */
	public static Date creaFecha(String anio, String mes, String dia){
		Date fecha = null;
		if(anio!=null  && !anio.trim().equals("") && anio.trim().length()==4 ){
			if(mes!=null  && !mes.trim().equals("")){
				if(dia==null  || dia.trim().equals("")){
					dia="1"; /*Dia en fecha por default*/
				}
				try{
					fecha = setDateDirect(Integer.parseInt(anio), Integer.parseInt(mes), Integer.parseInt(dia), 0, 0, 0);
				}catch (Exception e) {
					log4j.error("<DateUtily> Error al convertir parametros en Enteros para procesar fecha ("+
							anio+"/"+mes+"/"+dia+")",e);
					fecha = null;
				}			
			}
		}
		
		return fecha;
	}
	
	/**
	 * Obtiene la fecha diferida entre el origen y los diasDIf
	 * @param fechaOrigen
	 * @param diasDif +d = fecha+dias | -d = fecha-dias
	 * @return
	 */
	public static Date getDateFrom(Date fechaOrigen, int diasDif){
		 Calendar cal=Calendar.getInstance(TIMEZONE, LOCALE);
		 cal.setTime(fechaOrigen);
		 cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR)+diasDif);
		 return cal.getTime();
	}
	
	
	/**
	 * Redondea un numero decimal de tipo double
	 * @param numero donde se aplica el redondeo
	 * @param digitos, numero de digitos a redondear
	 * @param roundingMode, modo de redondeo
	 * @return un numero redondeado
	 */
	public static double redondear(double numero, int digitos, RoundingMode roundingMode) {
		 	return new BigDecimal(String.valueOf(numero)).
						setScale(digitos,roundingMode).doubleValue();
	}

	public static Long getDateInLong() {
		Date date = new Date();
		return date.getTime();
	}
	
	/**
	 * Temporal para obtener el Més en idioma Español
	 * @param mes
	 * @return
	 */
	public static String mesLatino(int mes){
		String stMes = "";
		switch (mes) {
		case 1:
			stMes = "Enero";
			break;	
		case 2:
			stMes = "Febrero";
			break;
		case 3:
			stMes = "Marzo";
			break;
		case 4:
			stMes = "Abril";
			break;
		case 5:
			stMes = "Mayo";
			break;
		case 6:
			stMes = "Junio";
			break;
		case 7:
			stMes = "Julio";
			break;
		case 8:
			stMes = "Agosto";
			break;
		case 9:
			stMes = "Septiembre";
			break;
		case 10:
			stMes = "Octubre";
			break;
		case 11:
			stMes = "Noviembre";
			break;
		case 12:
			stMes = "Diciembre";
			break;
		}
		return stMes;
	}
	
	
	public static String fechaLatino(Date fecha){
		Calendar gregDate = new GregorianCalendar();
		gregDate.setTime(fecha);
		
//		System.out.println("gregDate -> "+gregDate);
//		System.out.println("year   -> "+gregDate.get(Calendar.YEAR));
//		System.out.println("month  -> "+gregDate.get(Calendar.MONTH));
//		System.out.println("dom    -> "+gregDate.get(Calendar.DAY_OF_MONTH));
//		System.out.println("dow    -> "+gregDate.get(Calendar.DAY_OF_WEEK));
//		System.out.println("hour   -> "+gregDate.get(Calendar.HOUR));
//		System.out.println("minute -> "+gregDate.get(Calendar.MINUTE));
//		System.out.println("second -> "+gregDate.get(Calendar.SECOND));
//		System.out.println("milli  -> "+gregDate.get(Calendar.MILLISECOND));
//		System.out.println("ampm   -> "+gregDate.get(Calendar.AM_PM));
//		System.out.println("hod    -> "+gregDate.get(Calendar.HOUR_OF_DAY));
		
		return mesLatino(gregDate.get(Calendar.MONTH))
				+" "+gregDate.get(Calendar.DAY_OF_MONTH)
				+", "+gregDate.get(Calendar.YEAR);
	}
	
	
//	public static void main(String[] args) {
//		String fechaLt = fechaLatino(new Date());
//		System.out.println(fechaLt);
//	}
}
