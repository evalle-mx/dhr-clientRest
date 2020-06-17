package net.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Clase con metodos estaticos que proporcionan utilidades para generacion, manipulacion y ordenamiento
 * de Numeros, patrones y ordenamiento
 * @author EAVV
 * 
 */
//public class NumericUtily {
public class NumericUtily extends NumberToLetterConverter{

//	public static boolean isNumeric(String cadena){
//		java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(".*[^0-9].*");
//		boolean match = pattern.matcher(cadena).matches();
//		System.out.println("match: " + match );
//		return false;
//	}
	
	/**
	 * Genera un numero entero aleatorio menor o igual al maximo definido
	 * @param maximo
	 * @return integer
	 * @author EAVV
	 */
	public static int getAleatorio(int maximo){
		int numeroAleatorio = (int) (Math.random()*maximo);
		return numeroAleatorio;
	}
	
	/**
	 * Genera un numero aleatorio con la longitud determinada
	 * @param longitud
	 * @return
	 * @author EAVV
	 */
	public static String genAleatLong(int longitud){
		String numero = "";
		for(int i=0;i<longitud;i++){
			numero+=(getAleatorio(10)-1);
		}
		return numero;
	}
	
	/**
	 * Valida si es valor es factible de ser long
	 * @param value
	 * @return boolean
	 * @author EAVV
	 */
	public static boolean isLongable(String value) {
		try {			
			Long.parseLong(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	/**
	 * Ordena un arreglo de enteros Ascendentemente
	 * @param arrComb
	 * @return
	 */
	public static int[] ordenaArrayInt( int[] arrComb){
		for(int i=0; i<arrComb.length;i++){
			int temp = -1;
			for(int j=0; j<arrComb.length;j++){
				if(arrComb[i]<arrComb[j]){
					temp = arrComb[i];
					arrComb[i] = arrComb[j];
					arrComb[j] = temp;
					
				}
			}
		}
		return arrComb;
	}
	
	/**
	 * Valida si el valor ya existe en el arreglo
	 * @param arrComb
	 * @param value
	 * @param index
	 * @return
	 * @author EAVV
	 */
	public static boolean unicInArray(int[] arrComb, int value, int index){
		for(int i=0; i<=index; i++){
			if(arrComb[i] == value){
				return false;
			}
		}
		return true;
	}
	
	
	/**
	 * 
	 * @param numInput
	 * @param maximo
	 * @return
	 */
	public static boolean matchDecimal(String numInput, String maximo){
		
		if(numInput.indexOf(".")!=-1){
			if(numInput.indexOf(".") != numInput.lastIndexOf(".")){
				return false;
			}
		}
		double numDouble = 0;
		double maxDouble = 0;
		try{
			numDouble = Double.parseDouble(numInput);
			maxDouble = Double.parseDouble(maximo);	
		}catch (NumberFormatException e) {
			return false;
		}		
		if(numDouble > maxDouble){
			System.out.println(numInput + " es mayor que " + maxDouble);
			return false;
		}		
		return true;
	}
	
	/**
	 * Convierte el objeto (si es Numerico) a su valor entero
	 * @param obj
	 * @return
	 * @author EAVV
	 */
	public static int getIntValueObj(Object obj){		 
		 if(obj instanceof BigDecimal){			 
			 return ((BigDecimal) obj).intValue();
		 }
		 else if(obj instanceof Integer){			 
			 return ((Integer) obj).intValue();
		 }
		 else if(obj instanceof Double){			 
			 return ((Double) obj).intValue();
		 }
		 else if(obj instanceof Long){			 
			 return ((Long) obj).intValue();
		 }
		 else if(obj instanceof Float){			 
			 return ((Float) obj).intValue();
		 }
		 else {			 
			 return (new Integer((String)obj)).intValue(); 
		 }
	 }
	
	 /**
	   * Helper para transformar Objeto number en su double
	   * @param obj
	   * @return
	   * @author EAVV
	   */
	  public static double getDoubValueObj(Object obj){		 
			 if(obj instanceof BigDecimal){			 
				 return ((BigDecimal) obj).doubleValue();
			 }
			 else if(obj instanceof Integer){			 
				 return ((Integer) obj).doubleValue();
			 }
			 else if(obj instanceof Double){			 
				 return ((Double) obj).doubleValue();
			 }
			 else if(obj instanceof Long){			 
				 return ((Long) obj).doubleValue();
			 }
			 else if(obj instanceof Float){			 
				 return ((Float) obj).doubleValue();
			 }
			 else {
				 if(obj instanceof Number){
					 return (new Double((String)obj)).doubleValue();
				 }else{
					 System.out.println("no Es Number");
					 Double dbObj = (Double) obj;
					 return dbObj.doubleValue();
				 }
			 } 
		 }
	  
	  /**
	   * Metodo para dar formato y evitar el Infinito o NaN en Doubles
	   * @param objeto
	   * @param numberFormat
	   * @return
	   */
		
		public String doubleNF_Formated(Double dbObj, int maxFraccDigs, int minFraccDigs){
			String doubleForm = "";
			
			if(dbObj.isNaN()){
				return doubleForm;
			}else{
				NumberFormat numberFormat = NumberFormat.getInstance();
				numberFormat.setMaximumFractionDigits(maxFraccDigs);
				numberFormat.setMinimumFractionDigits(minFraccDigs);
				
					if (!Double.isInfinite(dbObj) &&
							!Double.isNaN(dbObj) ) {
							doubleForm = numberFormat.format(dbObj);
						}
					return doubleForm;
			}
		}
	  
	  
	  /**
	   * 
	   * @param dato
	   * @return
	   */
	  private static BigDecimal convierteDato(String dato) {
          BigDecimal resultado = new BigDecimal(0);
          if (dato.equals("")) {
                return resultado;
          }
          try {
                // Tenemos que quitar las comas
                DecimalFormat df = new DecimalFormat();
                char coma = df.getDecimalFormatSymbols().getGroupingSeparator();
                int posComa = dato.indexOf(coma);
                while (posComa >= 0) {
                     if (posComa > 0) {
                           dato = dato.substring(0, posComa) + dato.substring(posComa + 1);
                     } else {
                           dato = dato.substring(posComa + 1);
                     }
                     posComa = dato.indexOf(coma);
                }
                resultado = new BigDecimal(dato);
          } catch (Exception e) {
                // OJO: Esto no debe pasar nunca puesto que la entrada est� validada
          }
          return resultado;
    }
	  
	  
	  public static String formatedNumber(double number, String format){
		  String formatedNumber = String.format("%.2f",number);
		  if(format!=null && format.trim().length()>0){
			  try{
				  NumberFormat formatter = new DecimalFormat(format);
				  formatedNumber = formatter.format(number);
			  }catch (Exception e) {
				e.printStackTrace();
			} 
		  }
		  
		  return formatedNumber;
	  }
	  
	  
	  public static String double2Money(double numero){
		  String resp = " ";//"$ "
		  resp+=formatedNumber(numero, "###,###,###.00");
		  return resp;
	  }

		  
	 
	  public static void main(String[] args) {
		System.out.println(double2Money(125123999.1));//-->$ 125,123,999.10
//		  isNumeric("123");
	}
	  
	  
}
