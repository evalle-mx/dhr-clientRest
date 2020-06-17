package net.dothr.recreate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import net.dothr.fromjson.PosicionFromJson;
import net.utils.ClientRestUtily;

public class PosicionFullFromJson extends PosicionFromJson {
	
	private static Logger log4j = Logger.getLogger( PosicionFullFromJson.class );
//	public final static String URI_POSICION_HABILIDAD = "/module/positionSkill";
	public final static String PATH_REPORTE = "/home/dothr/workspace/ClientRest/files/out/Rep.PositionFullFromJson.txt";
	

	public static void main(String[] args) {
		StringBuilder sb;
		try {
			//1.Crear Posiciones
			sb = procesaMultiplePosicion();
			
			ClientRestUtily.writeStringInFile(PATH_REPORTE, sb.toString(), false);
			log4j.debug("Se escribio resultado de PosicionFull en \n" + PATH_REPORTE );
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
		
	
	
	
	 /** Procesa una lista de Archivos de Posicion, para reCrear datos en la aplicación
		 * @return
		 * @throws Exception
		 */
		public static StringBuilder procesaMultiplePosicion() throws Exception {
			StringBuilder sbMain = new StringBuilder();
			List<String> lsPosiciones = getLsPosiciones();
			Iterator<String> itPosicion = lsPosiciones.iterator();
			String archivoPosicion = null;
			int iFiles = 0;
			while(itPosicion.hasNext()){
				archivoPosicion = itPosicion.next();
				sbMain.append("\n/* >>>>>>>>>>>>>>>>>>>   PROCESO PARA " + archivoPosicion + " <<<<<<<<<<<<<<<<<< */\n");
				sbMain.append(procesaPosicionFile(archivoPosicion)).append("\n");
				iFiles++;
			}
			sbMain.append("\n Fin de Proceso sin Errores, ").append(iFiles).append(" Procesados.\n ");
			return sbMain;
		}
	
		
		/**
		 * Genera la lista de archivos Json-Posicion a persistir en la Aplicación
		 * @return
		 */
		private static List<String> getLsPosiciones(){
			List<String> lsPos = new ArrayList<String>();
			
			lsPos.add("read-5.json");
			lsPos.add("read-6.json");
			lsPos.add("read-7.json");
			lsPos.add("read-8.json");
			lsPos.add("read-9.json");
			
			return lsPos;
		}		
		
}
