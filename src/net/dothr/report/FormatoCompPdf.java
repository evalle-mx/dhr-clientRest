package net.dothr.report;

/**
 * Generador de archivos PDF para formato de Compensación, el cual se obtiene desde un archivo .json 
 * 
 */
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Iterator;

import net.dothr.transactional.assembler.CompensationAssembler;
import net.tce.dto.BonoDto;
import net.tce.dto.CompensacionDto;
import net.tce.dto.ContactInfoDto;
import net.tce.dto.SeguroDto;
import net.tce.dto.ValeDto;
import net.utils.ConstantesREST;

import org.json.JSONObject;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;
//import com.lowagie.text.Font;

public class FormatoCompPdf extends AbstractPDFCreator {

	private static String PDF_OUT_PATH = "/home/dothr/app/webServer/repository/docs/X/"+
			"formatoComp.pdf";
	
	private float comSpacBefore = 4f;
	private float comSpacAfter = 4f;
	
	public static void main(String[] args) {
		try {
			JSONObject jsonComp = CompensationAssembler.getJsonObject("read.json", 
					ConstantesREST.JSON_HOME+"module/compensation/");
			CompensacionDto fcDto = CompensationAssembler.getDto(jsonComp);
			System.out.println("fcDto: " + fcDto );			
			
			FormatoCompPdf formPdf = new FormatoCompPdf();
			//formPdf.generaPDF1(fcDto, PDF_OUT_PATH);//Información en una sola columna (saltos de linea)
			formPdf.generaPDF2(fcDto, PDF_OUT_PATH);//Información en DOs columnas 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Metodo de escritura en una sola columna de PDF
	 * @param fcDto
	 * @param rutaSalida
	 * @throws FileNotFoundException
	 * @throws DocumentException
	 */
	protected void generaPDF1(CompensacionDto fcDto, final String rutaSalida) 
			throws FileNotFoundException, DocumentException{
		Document document =  new Document(tamPagDfault);
        PdfWriter.getInstance(document, new FileOutputStream(rutaSalida));
        document.open();
        
        /* >>>>>>>>>>> ARMADO DEL DOCUMENTO  <<<<<<<<<<<<< */
        addMetaData(document, "Formato de Compensación "+ fcDto.getNombreCompleto() );       
        document.add(emptyParag(1));
        
//        Font font = fTitulo;	//FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
        Paragraph titulo = new Paragraph();
        titulo.setAlignment(Element.ALIGN_CENTER);
        Chunk chunk = new Chunk("INFORMACIÓN PERSONAL Y DE COMPENSACIONES \n", fTitulo);
        titulo.add(chunk);
        //Inserta Titulo principal
        document.add(titulo);
        LineSeparator ls = new LineSeparator();
        document.add(new Chunk(ls));        
        document.add(emptyParag(2));
        
        //Inserta Segmento de datos Personales
        document.add(genDatosPerson(fcDto));
        document.add(emptyParag(1));
        
        //Inserta Segmendo Beneficios Economicos
        document.add(genEconomicas(fcDto));
        document.add(emptyParag(1));
        
        //Inserta Segmento Prestaciones
        document.add(genPrestaciones(fcDto));
        document.add(emptyParag(1));
        
        //Inserta beneficios adicionales
        document.add(genBeneficios(fcDto));
        document.add(emptyParag(1));        
        
        /* >>>>>>>>>>> FIN DE CUERPO DEL ARMADO DE DOCUMENTO  <<<<<<<<<<<<< */
        //Cerrar documento
        document.close();
        
        System.out.println("Se genero archivo: "+rutaSalida);
		
	}
	
	protected void generaPDF2(CompensacionDto fcDto, final String rutaSalida) 
			throws FileNotFoundException, DocumentException{
		Document document =  new Document(tamPagDfault);
        PdfWriter.getInstance(document, new FileOutputStream(rutaSalida));
        document.open();
        
        /* >>>>>>>>>>> ARMADO DEL DOCUMENTO  <<<<<<<<<<<<< */
        
        addMetaData(document, "Formato de Compensación "+ fcDto.getNombreCompleto() );         
        document.add(emptyParag(1));
        
//        Font font = fTitulo;	//FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
        Paragraph titulo = new Paragraph();
        titulo.setAlignment(Element.ALIGN_CENTER);
        Chunk chunk = new Chunk("INFORMACIÓN PERSONAL Y DE COMPENSACIONES \n", fTitulo);
        titulo.add(chunk);
        //Inserta Titulo principal
        document.add(titulo);
        LineSeparator ls = new LineSeparator();
        document.add(new Chunk(ls));        
//        document.add(emptyParag(2));
        
        /* ****   TABLA PRINCIPAL   **** */
//        float[] columnWidths = {3, (float)10.25 };
        PdfPTable mainTable = new PdfPTable(2);
        mainTable.setWidthPercentage(100);
        mainTable.setTotalWidth(pagWidthDef);
        mainTable.setLockedWidth(true);
        PdfPCell cell, subCell1, subCell2;
        
        PdfPTable innerTable1 = new PdfPTable(1);
    	innerTable1.setSpacingAfter(4);		//setPadding(5);
        innerTable1.setWidthPercentage(100);
        
        /* Columna izquierda */
        subCell1 = new PdfPCell();
        subCell1.setBorderWidth(Rectangle.NO_BORDER);
      
        //Inserta Segmento de datos Personales
        subCell1.addElement(genDatosPerson(fcDto));
        subCell1.addElement(emptyParag(1));
        
//        //Inserta Segmendo Beneficios Economicos
//        subCell1.addElement(genEconomicas(fcDto));
//        subCell1.addElement(emptyParag(1));
        
        innerTable1.addCell(subCell1);
        subCell1 = new PdfPCell();
        subCell1.setBorderWidth(Rectangle.NO_BORDER);
        
//        //Inserta Segmento Prestaciones
//        subCell1.addElement(genPrestaciones(fcDto));
//        subCell1.addElement(emptyParag(1));
        
        //Inserta beneficios adicionales
        subCell1.addElement(genBeneficios(fcDto));
        subCell1.addElement(emptyParag(1));
        
        innerTable1.addCell(subCell1);
        
        cell = new PdfPCell(innerTable1);
        cell.setBorderWidth(0);
        mainTable.addCell(cell);
        
        PdfPTable innerTable2 = new PdfPTable(1);
        innerTable2.setSpacingAfter(2);		//setPadding(5);
        innerTable2.setWidthPercentage(100);
        
        /* Columna Derecha */
        subCell2 = new PdfPCell();
        subCell2.setBorderWidth(Rectangle.NO_BORDER);
        //Inserta Segmendo Beneficios Economicos
        subCell2.addElement(genEconomicas(fcDto));
        subCell2.addElement(emptyParag(1));
        
        innerTable2.addCell(subCell2);
        subCell2 = new PdfPCell();
        subCell2.setBorderWidth(Rectangle.NO_BORDER);
        
        //Inserta Segmento Prestaciones
        subCell2.addElement(genPrestaciones(fcDto));
        subCell2.addElement(emptyParag(1));
        
//        //Inserta beneficios adicionales
//        subCell2.addElement(genBeneficios(fcDto));
//        subCell2.addElement(emptyParag(1));
        
        innerTable2.addCell(subCell2);
        
        
        cell = new PdfPCell(innerTable2);
        cell.setBorderWidth(0);
        mainTable.addCell(cell);
        
        
        //Insertar tabla en documento
        document.add(mainTable);
        
        /* >>>>>>>>>>> FIN DE CUERPO DEL ARMADO DE DOCUMENTO  <<<<<<<<<<<<< */
        //Cerrar documento
        document.close();
        
        System.out.println("Se genero archivo: "+rutaSalida);
	}
	
	
	/* ********************************************************** */
	/* **************  GENERADORES DE PARRAFOS  ***************** */
	/* ********************************************************** */
	
	/** 
	 * Genera el parrafo para datos Personales
	 * @param fcDto
	 * @return
	 */
	private Paragraph genDatosPerson(CompensacionDto fcDto){
		Paragraph paragDos = new Paragraph("Datos Personales\n", fSubTitulo);
        paragDos.setSpacingBefore(comSpacBefore);
        paragDos.setSpacingAfter(comSpacAfter);
        
        paragDos.add(new Chunk("Nombre: ", fTextoBold));
        paragDos.add(new Chunk( fcDto.getNombreCompleto() +"\n", fTexto));
        paragDos.add(new Chunk("CURP: ", fTextoBold));
        paragDos.add(new Chunk( fcDto.getCurp() +"\n", fTexto));
        
        paragDos.add(new Chunk("Fecha de Nacimiento: ", fTextoBold));
        paragDos.add(new Chunk( dateFormated( fcDto.getFechaNacimiento() ) +"\n", fTexto));
        paragDos.add(new Chunk("Género: ", fTextoBold));
        paragDos.add(new Chunk( fcDto.getLbGenero() +"\n", fTexto));
        paragDos.add(new Chunk("Estado civil: ", fTextoBold));
        paragDos.add(new Chunk( fcDto.getLbEstadoCivil() +"\n", fTexto));
        if(fcDto.getNumeroHijos()!=null){
        	paragDos.add(new Chunk("Número de Hijos: ", fTextoBold));
            paragDos.add(new Chunk( fcDto.getNumeroHijos() +"\n", fTexto));
        }
        
        paragDos.add(new Chunk("País de origen: ", fTextoBold));
        paragDos.add(new Chunk( fcDto.getStPais() +"\n", fTexto));
        paragDos.add(new Chunk("Nombre: ", fTextoBold));
        paragDos.add(new Chunk( fcDto.getNombreCompleto() +"\n", fTexto));
        
        if(fcDto.getContactos()!=null && !fcDto.getContactos().isEmpty()){
			 ContactInfoDto contactDto;
			 Iterator<ContactInfoDto> itConts = fcDto.getContactos().iterator();
			 paragDos.add(new Chunk("Contactos: \n", fTextoBold));
			 while(itConts.hasNext()){
				 contactDto = itConts.next();
				 paragDos.add(new Chunk("\t - " + contactDto.getLbTipoContacto() + ": " + (contactDto.getNumero()!=null?contactDto.getNumero():contactDto.getContacto()!=null?contactDto.getContacto():"") +"\n", fTexto));
			 }
		 }
        
        return paragDos;
	}
	
	/**
	 * Genera el parrafo de Beneficios economicos
	 * @param fcDto
	 * @return
	 */
	private Paragraph genEconomicas(CompensacionDto fcDto){
		Paragraph paragDos = new Paragraph("Compensación económica\n", fSubTitulo);
        paragDos.setSpacingBefore(comSpacBefore);
        paragDos.setSpacingAfter(comSpacAfter);
        
        if(fcDto.getSueldo()!=null){
        	if(fcDto.getSueldo().getCantidad()!=null){
        		paragDos.add(new Chunk("Sueldo Base: ", fTextoBold));
                paragDos.add(new Chunk( getCurrencyFormated(fcDto.getSueldo().getCantidad()) +"\n", fTexto));
        	}
        	if(fcDto.getSueldo().getLbPeriodicidadSueldo()!=null){
                paragDos.add(new Chunk("Periodicidad:: ", fTextoBold));
                paragDos.add(new Chunk( fcDto.getSueldo().getLbPeriodicidadSueldo() +"\n", fTexto));        		
        	}
        }
       
        if(fcDto.getCantidadFondoAhorro()!=null){
        	paragDos.add(new Chunk("Fondo de Ahorro: ", fTextoBold));
            paragDos.add(new Chunk( getCurrencyFormated(fcDto.getCantidadFondoAhorro()) +"\n", fTexto));
        }
        if(fcDto.getVales()!=null && !fcDto.getVales().isEmpty()){
	    	ValeDto valedto;
	    	paragDos.add(new Chunk("Vales: \n", fTextoBold));
	    	Iterator<ValeDto> itVales = fcDto.getVales().iterator();
	    	while(itVales.hasNext()){
	    		valedto = itVales.next();
	    		if(valedto.getActivo()!=null && valedto.getActivo().equals("1")){
				    paragDos.add(new Chunk("\t- " + valedto.getLbTipoVale() 
				    		+ ", " + getCurrencyFormated(valedto.getCantidad()) +"\n", fTexto));
	    		}
	    	}
	    }
        if(fcDto.getOtro()!=null){
		    paragDos.add(new Chunk("Otra prestación: ", fTextoBold));
	        paragDos.add(new Chunk( fcDto.getOtro() +"\n", fTexto));
	    }
        
        return paragDos;
	}
	
	/**
	 * Genera segmento de Prestaciones
	 * @param fcDto
	 * @return
	 */
	private Paragraph genPrestaciones(CompensacionDto fcDto){
		Paragraph paragDos = new Paragraph("Prestaciones\n", fSubTitulo);
        paragDos.setSpacingBefore(comSpacBefore);
        paragDos.setSpacingAfter(comSpacAfter);
        
        if(fcDto.getDiasAguinaldo()!=null){
            paragDos.add(new Chunk("Aguinaldo: ", fTextoBold));
            paragDos.add(new Chunk( fcDto.getDiasAguinaldo() +" días\n", fTexto));
        }
        if(fcDto.getUltimoMontoUtilidades()!=null){
        	paragDos.add(new Chunk("Último monto Utilidades: ", fTextoBold));
            paragDos.add(new Chunk( getCurrencyFormated(fcDto.getUltimoMontoUtilidades()) +"\n", fTexto));
        }

        if(fcDto.getVacaciones()!=null){
        	if(fcDto.getVacaciones().getValorDias()!=null){
        		paragDos.add(new Chunk("Vacaciones: ", fTextoBold));
                paragDos.add(new Chunk( fcDto.getVacaciones().getValorDias() +" días\n", fTexto));
        	}
        	if(fcDto.getVacaciones().getValorPrima()!=null){
        		paragDos.add(new Chunk("Prima: ", fTextoBold));
                paragDos.add(new Chunk( getCurrencyFormated(fcDto.getVacaciones().getValorPrima()) +"\n", fTexto));        		
        	}
        }

        if(fcDto.getComedor()!=null){
        	paragDos.add(new Chunk("Servicio de Comedor: ", fTextoBold));
            paragDos.add(new Chunk( getCurrencyFormated(fcDto.getComedor()) +"\n", fTexto));
        }
        
        return paragDos;
	}
	
	private Paragraph genBeneficios(CompensacionDto fcDto){
		Paragraph paragDos = new Paragraph("Beneficios\n", fSubTitulo);
        paragDos.setSpacingBefore(comSpacBefore);
        paragDos.setSpacingAfter(comSpacAfter);
        
        paragDos.add(new Chunk("Nombre: ", fTextoBold));
        paragDos.add(new Chunk( fcDto.getNombreCompleto() +"\n", fTexto));
        
        paragDos.add(new Chunk("Celular: ", fTextoBold));
        paragDos.add(new Chunk( siNo(fcDto.getCelular()) +"\n", fTexto));
        
	    paragDos.add(new Chunk("Club-Gym: ", fTextoBold));
        paragDos.add(new Chunk( siNo(fcDto.getClubGym()) +"\n", fTexto));
        
	    paragDos.add(new Chunk("Check Up: ", fTextoBold));
        paragDos.add(new Chunk( siNo(fcDto.getCheckUp()) +"\n", fTexto));
        
        
	    if(fcDto.getAutomovil()!=null){
	    	paragDos.add(new Chunk("Auto de la compañia: \n", fTextoBold));
	    	
	    	if(fcDto.getAutomovil().getMarca()!=null){
	    		paragDos.add(new Chunk("Marca: ", fTextoBold));
		        paragDos.add(new Chunk( fcDto.getAutomovil().getMarca() +"\n", fTexto));
	    	}
	        
	    	if(fcDto.getAutomovil().getModelo()!=null){
	    		paragDos.add(new Chunk("Modelo: ", fTextoBold));
	    		paragDos.add(new Chunk( fcDto.getAutomovil().getModelo() +"\n", fTexto));
	    	}
	        
	        if(fcDto.getAutomovil().getGastosPagados()!=null){
		        paragDos.add(new Chunk("Gastos Pagados: ", fTextoBold));
		        paragDos.add(new Chunk( siNo(fcDto.getAutomovil().getGastosPagados()) +"\n", fTexto));	        	
	        }
	        
	        if(fcDto.getAutomovil().getOpcionCompra()!=null){
	        	paragDos.add(new Chunk("Opción de Compra: ", fTextoBold));
		        paragDos.add(new Chunk( siNo(fcDto.getAutomovil().getOpcionCompra()) +"\n", fTexto));
	        }
	        
	        if(fcDto.getAutomovil().getTiempoCambio()!=null){
	        	paragDos.add(new Chunk("Tiempo de Cambio: ", fTextoBold));
		        paragDos.add(new Chunk( fcDto.getAutomovil().getTiempoCambio() +"\n", fTexto));
	        }
	        
	    }
	    
	    if(fcDto.getBonos()!=null && !fcDto.getBonos().isEmpty()){
	    	paragDos.add(new Chunk("Bonos: \n", fTextoBold));
		    Iterator<BonoDto> itBono = fcDto.getBonos().iterator();
		    BonoDto dtoBono;
		    String valorBonoTMp;
		    while(itBono.hasNext()){
		    	dtoBono = itBono.next();
		    	valorBonoTMp = "";
		    	if(dtoBono.getPorcentajeCantidad()!=null || dtoBono.getValorCantidad()!=null){
		    		if(dtoBono.getPorcentajeCantidad()!=null){
		    			valorBonoTMp = " "+dtoBono.getPorcentajeCantidad()+ "% "
		    					+ (dtoBono.getLbPerioricidadBono()!=null?dtoBono.getLbPerioricidadBono():"");
			    	}else if(dtoBono.getValorCantidad()!=null){
			    		valorBonoTMp = getCurrencyFormated(dtoBono.getValorCantidad())+ " "
			    				+ (dtoBono.getLbPerioricidadBono()!=null?dtoBono.getLbPerioricidadBono():"");
			    	}
		    		
		    		 paragDos.add(new Chunk("\t- " + dtoBono.getTipoBono() 
					    		+ valorBonoTMp +"\n", fTexto));
		    	}
		    }
	    }
	    if(fcDto.getSeguros()!=null && !fcDto.getSeguros().isEmpty()){
	    	paragDos.add(new Chunk("Seguros: \n", fTextoBold));
		    Iterator<SeguroDto> itSeg = fcDto.getSeguros().iterator();
		    SeguroDto dtoSeg;
		    while(itSeg.hasNext()){
		    	dtoSeg = itSeg.next();
		    	paragDos.add(new Chunk("\t- " + dtoSeg.getLbTipoSeguro() 
			    		+ (dtoSeg.getFamiliar()!=null?dtoSeg.getFamiliar().equals("1")?" [Familiar]":" [Individual]":"") +"\n", fTexto));
		    }
	    }	 
        
        return paragDos;
	}

}
