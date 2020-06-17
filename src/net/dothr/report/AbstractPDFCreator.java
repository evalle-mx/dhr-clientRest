package net.dothr.report;

import com.google.gson.Gson;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;

import java.awt.Color;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import net.tce.dto.CompensacionDto;
import net.tce.dto.CurriculumDto;
import net.tce.util.Constante;
import net.utils.ClientRestUtily;
import net.utils.DateUtily;
import net.utils.NumericUtily;

public abstract class AbstractPDFCreator {

	/* Tamaño de Pagina */
	protected static final Rectangle tamPagDfault = PageSize.A4;	//PageSize.A4.rotate()); /* Horizontal */
	protected static final float pagWidthDef = 570F;
	
	protected static final Font fTitulo = FontFactory.getFont(
			FontFactory.HELVETICA_BOLD, 
			16, 
			Font.NORMAL, 
			new Color(0x0780da)
			);
	protected static final Font fSubTitulo = FontFactory.getFont(
			FontFactory.HELVETICA_BOLD, 
			10, 
			Font.NORMAL, 
			new Color(0x151515)
			);
	protected static final Font fTextoBold = FontFactory.getFont(
			FontFactory.HELVETICA, 
			8, 
			Font.BOLD, 
			new Color(0x424242)
			);
	protected static final Font fTexto = FontFactory.getFont(
			FontFactory.HELVETICA, 
			8, 
			Font.NORMAL, 
			new Color(0x424242)
			);
	
	
	/* ***** auxiliares   ******* */
	private static Gson gson;
	/**
	 * Agrega los Metadados (titulo, fecha, autor) del archivo físico
	 * @param document
	 */
	protected void addMetaData(Document document, String nombreDoc) {
        document.addTitle(nombreDoc);
//        document.addSubject("Using iText");
        document.addKeywords("Formato, PDF, iText");
        document.addAuthor("DotHR-PIRH, SELEX");
        document.addCreator("Mónica Quintero");
    }
	
	/**
	 * Agrega n saltos de linea al Parrafo
	 * @param paragraph
	 * @param number
	 */
	protected static void addEmptyLine(Paragraph paragraph, int nSaltos) {
        for (int i = 0; i < nSaltos; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
	
	protected Paragraph emptyParag(int nSaltos){
		Paragraph paragraph = new Paragraph();
		for (int i = 0; i < nSaltos; i++) {
            paragraph.add(new Paragraph(" "));
        }
		return paragraph;
	}
	
	/**
	 * convierte una cadena simple en formato moneda
	 * @param stCandidad
	 * @return
	 */
	protected String getCurrencyFormated(String stCandidad) {
		String formated;
		if(stCandidad==null){
			stCandidad = "0";
		}
		Double numero = Double.parseDouble(stCandidad);
		formated= "$ "+NumericUtily.formatedNumber(numero, "###,###,###.00");
		return formated;
	}
	
	/**
	 * Convierte 1/0 a si/No 
	 * @param stValue
	 * @return
	 */
	protected String siNo(Object stValue){
		if(stValue==null || !stValue.equals("1")){
			return "No";
		}else{
			return "Sí";
		}
	}
	
	/**
	 * Da formato a la fecha
	 * @param stFecha
	 * @return
	 */
	protected String dateFormated(String stFecha){
		if(stFecha==null){
			return "";
		}
		else{
			String fechaForm = "";
			try {
				Date fecha = DateUtily.string2Date(stFecha, Constante.DATE_FORMAT_JAVA);
				fechaForm = DateUtily.fechaLatino(fecha);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return fechaForm;
		}
	}
	
	protected String sinDef(String value){
		if(value==null || value.trim().equals("")){
			return "-Sin Definir-";
		}
		return value;
	}
	
	protected String getFechaRang(String anIni, String anFin, String act){
		if(anIni==null){
			anIni = "";
		}
		if(anFin==null ){
			if(act!=null && act.equals("1")){
				anFin = "Actual";
			}else{
				anFin = "";
			}
		}
		return anIni+" - "+anFin;
	}
	
	protected String notNull(String value){
		if(value == null){
			value = "";
		}
		return value;
	}
	
	public static JSONObject getJsonObject(String fileName, String fileDir) throws Exception {
		String stPersona = ClientRestUtily.getJsonFile(fileName, fileDir);
		JSONArray jsResp = new JSONArray(stPersona);
		return jsResp.getJSONObject(0);
	}
	
	public static CurriculumDto getCvDto (JSONObject jsonPersona){
		gson = new Gson();
		CurriculumDto dto = new CurriculumDto();
		dto = gson.fromJson(jsonPersona.toString(), CurriculumDto.class);
		return dto;
	}
	public static CompensacionDto getDto (JSONObject jsonCompensa){
		gson = new Gson();
		CompensacionDto dto = new CompensacionDto();
		dto = gson.fromJson(jsonCompensa.toString(), CompensacionDto.class);
		return dto;
	}
}
