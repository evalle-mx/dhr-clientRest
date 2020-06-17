package net.dothr.report;

import java.io.FileOutputStream;
import java.util.Iterator;

import net.dothr.transactional.assembler.CompensationAssembler;
import net.tce.dto.BonoDto;
import net.tce.dto.CompensacionDto;
import net.tce.dto.ContactInfoDto;
import net.tce.dto.ReferenceDto;
import net.tce.dto.SeguroDto;
import net.tce.dto.ValeDto;
import net.utils.ConstantesREST;

import org.apache.log4j.Logger;
import org.apache.poi.xwpf.usermodel.Borders;
//import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.TextAlignment;
//import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.json.JSONObject;

public class FormatoCompDoc {
	private static String DOC_PATH = "/home/dothr/app/webServer/test/"+
			"formatoTest.docx";
	private static String tab = "    ";
	
	private final static String mainTitleFont = "Arial";
	private final static int mainTitleSize = 20;
	private final static String mainTitleColor = "1F497D";
	
	private final static String subTitleFont = "Arial";
	private final static int subTitleSize = 13;
	private final static String subTitleColor = "808080";
	
	private final static String textoFont = "Arial";	
	private final static int textoSize = 9;
	private final static String textoColor = "a6a6a6";
	
	private static final String TITULO_FORMCOMPENSACION = "INFORMACIÓN PERSONAL Y DE COMPENSACIONES";
	
	
	static Logger log4j = Logger.getLogger( FormatoCompDoc.class );
	
	public static void main(String[] args) {
		try {
			JSONObject jsonComp = CompensationAssembler.getJsonObject("read.json", 
					ConstantesREST.JSON_HOME+"module/compensation/");
			CompensacionDto fcDto = CompensationAssembler.getDto(jsonComp);
			log4j.debug("fcDto: " + fcDto );			
			
			FormatoCompDoc formDoc = new FormatoCompDoc();
			formDoc.generaFormComp(fcDto, DOC_PATH);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Metodo principal generador de documento
	 * @param fcDto
	 * @param outPath
	 * @throws Exception
	 */
	protected void generaFormComp(CompensacionDto fcDto, String outPath) throws Exception {
		 XWPFDocument doc = new XWPFDocument();
		 
		 
		 XWPFParagraph paragraph1 = doc.createParagraph();
		 paragraph1.setAlignment(ParagraphAlignment.CENTER);
//		 paragraph1.setBorderBottom(Borders.DOUBLE);
//		 paragraph1.setBorderTop(Borders.DOUBLE);
//		 paragraph1.setBorderRight(Borders.DOUBLE);
//		 paragraph1.setBorderLeft(Borders.DOUBLE);
		 paragraph1.setBorderBetween(Borders.SINGLE);

		 paragraph1.setVerticalAlignment(TextAlignment.TOP);
		 mainTitle(paragraph1.createRun(), TITULO_FORMCOMPENSACION);   
	        
	    /* Datos Personales */
	    XWPFParagraph pDatos = getParag(doc);
	    sectionTitle(pDatos.createRun(), "Datos Personales");
	    setDatosPersonales(pDatos.createRun(), fcDto);
	        
	    /* Compensación económica */
	    XWPFParagraph pCompEcon = getParag(doc);
	    sectionTitle(pCompEcon.createRun(), "Compensación económica");
	    setEconomica(pCompEcon.createRun(), fcDto);
	    
	    /* Prestaciones */
	    XWPFParagraph pPrestac = getParag(doc);
	    sectionTitle(pPrestac.createRun(), "Prestaciones");
	    setPrestaciones(pPrestac.createRun(), fcDto);
	    
	    /*  Beneficios*/
	    XWPFParagraph pBenefs = getParag(doc);
	    sectionTitle(pBenefs.createRun(), "Beneficios");
	    setBeneficios(pBenefs.createRun(), fcDto);
	    
	    /*  Referencias Laborales*/
	    XWPFParagraph pRefers = getParag(doc);
	    sectionTitle(pRefers.createRun(), "Referencias Laborales");
	    setReferencias(pRefers.createRun(), fcDto);
	    
	    /*>> OUTPUT <<  */
	    FileOutputStream out = new FileOutputStream(outPath);
	    doc.write(out);
	    out.close();
	    doc.close();
	    System.out.println("Se genero archivo: " + outPath );
	}
	
	/**
	 * OBtiene un objeto de Parrafo genérico
	 * @param doc
	 * @return
	 */
	private XWPFParagraph getParag(XWPFDocument doc){
		 XWPFParagraph paragraph = doc.createParagraph();
		    paragraph.setAlignment(ParagraphAlignment.LEFT);
//		    paragraph.setBorderBottom(Borders.DOUBLE);
//		    paragraph.setBorderRight(Borders.DOUBLE);
//		    paragraph.setBorderLeft(Borders.DOUBLE);
//		    paragraph.setBorderBetween(Borders.SINGLE);
//		    paragraph.setBorderTop(Borders.DOUBLE);

		    paragraph.setVerticalAlignment(TextAlignment.TOP);
		    
		    return paragraph;
	}

	
	protected static void sectionTitle(XWPFRun rDTitle, String titulo){
		rDTitle.setFontFamily(subTitleFont);
		rDTitle.setFontSize(subTitleSize);
		rDTitle.setColor(subTitleColor);
		rDTitle.setBold(true);
		rDTitle.setText(titulo);
//		rDTitle.setUnderline(UnderlinePatterns.DASH_LONG);
		rDTitle.addBreak();
	}
	
	protected static void mainTitle(XWPFRun mainTitleRun, String titulo){
        mainTitleRun.setBold(true);
        mainTitleRun.setText(titulo.toUpperCase());
        mainTitleRun.setFontFamily(mainTitleFont);
        mainTitleRun.setColor(mainTitleColor);
        mainTitleRun.setFontSize(mainTitleSize);
        mainTitleRun.addBreak();
	}

	
	protected static void setDatosPersonales(XWPFRun rDatos, CompensacionDto dto){
		 rDatos.setFontFamily(textoFont);
		 rDatos.setFontSize(textoSize);
		 rDatos.setColor(textoColor);
		 
		 rDatos.setText("Nombre: ");
		 rDatos.setText(dto.getNombreCompleto());
		 rDatos.addBreak();
		 rDatos.setText("Fecha de Nacimiento: ");
		 rDatos.setText(dto.getFechaNacimiento());
		 rDatos.addBreak();

		 rDatos.setText("Género: ");
		 rDatos.setText(dto.getLbGenero());
		 rDatos.addBreak();
		 rDatos.setText("Estado civil: ");
		 rDatos.setText(dto.getLbEstadoCivil());
		 rDatos.addBreak();
		 if(dto.getNumeroHijos()!=null){
			 rDatos.setText("Número de Hijos: ");
			 rDatos.setText(dto.getNumeroHijos());
			 rDatos.addBreak();
		 }
		 rDatos.setText("País de origen: ");
		 rDatos.setText(dto.getStPais());
		 rDatos.addBreak();

		 if(dto.getContactos()!=null && !dto.getContactos().isEmpty()){
			 ContactInfoDto contactDto;
			 Iterator<ContactInfoDto> itConts = dto.getContactos().iterator();
			 while(itConts.hasNext()){
				 contactDto = itConts.next();
				 rDatos.setText(contactDto.getLbTipoContacto() + ": ");
				 rDatos.setText((contactDto.getNumero()!=null?contactDto.getNumero():contactDto.getContacto()!=null?contactDto.getContacto():"")); //" [55] 32956775"+tab);
				 rDatos.addBreak();
			 }
		 }

		 rDatos.setText("CURP: ");
		 rDatos.setText(dto.getCurp());
		 
		 rDatos.addBreak();
	}
	
	protected static void setEconomica(XWPFRun rComp, CompensacionDto dto){
		rComp.setFontFamily(textoFont);
		rComp.setFontSize(textoSize);
		rComp.setColor(textoColor);
		
	    if(dto.getSueldo()!=null){
	    	rComp.setText("Sueldo Base: $ ");
		    rComp.setText( notNull(dto.getSueldo().getCantidad()) );
		    rComp.addBreak();
		    rComp.setText("Periodicidad: ");
		    rComp.setText( notNull(dto.getSueldo().getLbPeriodicidadSueldo()) );
		    rComp.addBreak();
	    }   
	    
	    rComp.setText(" Fondo de Ahorro: $ ");
	    rComp.setText( notNull(dto.getCantidadFondoAhorro(),"0") );
	    rComp.addBreak();   
	    
	    if(dto.getVales()!=null && !dto.getVales().isEmpty()){
	    	ValeDto valedto;
	    	Iterator<ValeDto> itVales = dto.getVales().iterator();
	    	while(itVales.hasNext()){
	    		valedto = itVales.next();
	    		if(valedto.getActivo()!=null && valedto.getActivo().equals("1")){
	    			rComp.setText(valedto.getLbTipoVale()+": ");
				    rComp.setText(notNull(valedto.getCantidad()));
				    rComp.addBreak();
	    		}
	    	}
	    }
	    if(dto.getOtro()!=null){
	    	rComp.setText("Otra prestación: ");
		    rComp.setText(dto.getOtro());
		    rComp.addBreak();
	    }
	    
	}
	
	protected static void setPrestaciones(XWPFRun rPrest, CompensacionDto dto){
		rPrest.setFontFamily(textoFont);
		rPrest.setFontSize(textoSize);
		rPrest.setColor(textoColor);
	    
	    rPrest.setText("Aguinaldo: ");
	    rPrest.setText( notNull(dto.getDiasAguinaldo(),"0"));
	    rPrest.addBreak();
	    rPrest.setText("Último monto Utilidades: $");
	    rPrest.setText( notNull(dto.getUltimoMontoUtilidades(),"0"));
	    rPrest.addBreak();
	    
	    if(dto.getVacaciones()!=null){
	    	rPrest.setText("Días de Vacaciones: ");
		    rPrest.setText( notNull(dto.getVacaciones().getValorDias(),"0")+ " días  ");
		    rPrest.setText("Prima:");
		    rPrest.setText( notNull(dto.getVacaciones().getValorPrima(),"0")+ "");
		    rPrest.addBreak();
	    }    
	    
	    rPrest.setText("Servicio de Comedor: $");
	    rPrest.setText( notNull(dto.getComedor(),"0"));	// "$ 25 diarios"+tab);
	    rPrest.addBreak();
	}
	
	protected static void setBeneficios(XWPFRun rBenef, CompensacionDto dto){
		rBenef.setFontFamily(textoFont);
		rBenef.setFontSize(textoSize);
		rBenef.setColor(textoColor);
	    
	    rBenef.setText("Celular: ");
	    rBenef.setText(bolYesNo(dto.getCelular())+tab);
	    rBenef.setText("Club-Gym: ");
	    rBenef.setText(bolYesNo(dto.getClubGym())+tab);
	    rBenef.setText("Check Up: ");
	    rBenef.setText(bolYesNo(dto.getCheckUp())+tab);
	    rBenef.addBreak();
	    
	    if(dto.getAutomovil()!=null){
	    	rBenef.setText("Auto de la compañia ");
		    rBenef.addBreak();	    
		    rBenef.setText("Marca: ");
		    rBenef.setText( notNull(dto.getAutomovil().getMarca())+tab);  // "NISSAN"+tab);
		    rBenef.setText("Modelo: ");
		    rBenef.setText( notNull(dto.getAutomovil().getModelo())+tab);  // "Tsuru"+tab);
		    rBenef.setText("Gastos Pagados: ");
		    rBenef.setText( bolYesNo(dto.getAutomovil().getGastosPagados())+tab);
		    rBenef.addBreak();
		    rBenef.setText("Opción de Compra: ");
		    rBenef.setText( bolYesNo(dto.getAutomovil().getOpcionCompra())+tab);
		    rBenef.setText("Tiempo de Cambio: ");
		    rBenef.setText( notNull(dto.getAutomovil().getTiempoCambio())+" años"+tab);
		    rBenef.addBreak();
	    }
	    
	    if(dto.getBonos()!=null && !dto.getBonos().isEmpty()){
	    	rBenef.setText("BONOS ");
		    rBenef.addBreak();
		    Iterator<BonoDto> itBono = dto.getBonos().iterator();
		    BonoDto dtoBono;
		    while(itBono.hasNext()){
		    	dtoBono = itBono.next();
		    	if(dtoBono.getPorcentajeCantidad()!=null || dtoBono.getValorCantidad()!=null){
		    		rBenef.setText(dtoBono.getTipoBono());
			    	if(dtoBono.getPorcentajeCantidad()!=null){
			    		rBenef.setText( " "+dtoBono.getPorcentajeCantidad()+ "% "+ notNull(dtoBono.getLbPerioricidadBono()));
			    	}else if(dtoBono.getValorCantidad()!=null){
			    		rBenef.setText( " $ "+dtoBono.getValorCantidad()+ " " +notNull(dtoBono.getLbPerioricidadBono()));
			    	}
				    rBenef.addBreak();
		    	}
		    }
//		    rBenef.setText("Bono Puntualidad ");
//		    rBenef.setText("20 % Quincenal"+tab);
//		    rBenef.addBreak();
//		    rBenef.setText("Bono Productividad ");
//		    rBenef.setText("$ 2,850 Quincenal"+tab);
//		    rBenef.addBreak();
	    }
	    if(dto.getSeguros()!=null && !dto.getSeguros().isEmpty()){
	    	rBenef.setText("SEGUROS ");
		    rBenef.addBreak();
		    Iterator<SeguroDto> itSeg = dto.getSeguros().iterator();
		    SeguroDto dtoSeg;
		    while(itSeg.hasNext()){
		    	dtoSeg = itSeg.next();
		    	rBenef.setText( dtoSeg.getLbTipoSeguro() +(dtoSeg.getFamiliar()!=null?dtoSeg.getFamiliar().equals("1")?" [Familiar]":" [Individual]":"") ); //"Seguro Dental (Individual)");
			    rBenef.addBreak();
		    }
	    }	    
	}
	
	protected static void setReferencias(XWPFRun rRef, CompensacionDto dto){
		rRef.setFontFamily(textoFont);
		rRef.setFontSize(textoSize);
		rRef.setColor(textoColor);
	    
	    if(dto.getReferencias()!=null && !dto.getReferencias().isEmpty()){
	    	Iterator<ReferenceDto> itRef = dto.getReferencias().iterator();
	    	ReferenceDto dtoRef;
	    	while(itRef.hasNext()){
	    		dtoRef = itRef.next();
	    		rRef.setText("Nombre: ");
			    rRef.setText( notNull(dtoRef.getNombre()) +tab);	//"Roberto Carlos"+tab);
			    rRef.addBreak();
			    rRef.setText("Empresa: ");
			    rRef.setText( notNull(dtoRef.getEmpresa()) +tab);	//"Real Madrid FC"+tab);
			    rRef.addBreak();
			    rRef.setText("Puesto: ");
			    rRef.setText( notNull(dtoRef.getPuesto()) +tab);	//"Defensa"+tab);
			    rRef.addBreak();
			    if(dtoRef.getEmail()!=null){
				    rRef.setText("Email: ");
				    rRef.setText( notNull(dtoRef.getEmail()) +tab);	//"rcarlos4@rm.com"+tab);	
				    rRef.addBreak();		    	
			    }
			    if(dtoRef.getTelParticular()!=null){
				    rRef.setText("Partícular: ");
				    rRef.setText( notNull(dtoRef.getTelParticular()) +tab );	//"25151515"+tab);	
				    rRef.addBreak();
			    }
			    if(dtoRef.getTelCelular()!=null){
				    rRef.setText(" Móvil: ");
				    rRef.setText( notNull(dtoRef.getTelCelular()) );	//"55325151");
				    rRef.addBreak();
			    }
			    rRef.addBreak();rRef.addBreak();
	    	}
	    }else{
	    	rRef.setText("NO HAY NINGUNA REFERENCIA CAPTURADA");
		    rRef.addBreak();
	    }
	}
	/* ******************** AUXILIARES  ******************** */

	
	private static String notNull(Object stValue){
		if(stValue==null){
			return "";
		}
		return (String)stValue;
	}
	private static String notNull(Object stValue, String defValue){
		if(stValue==null){
			return defValue;
		}
		return (String)stValue;
	}
	private static String bolYesNo(Object stValue){
		if(stValue==null || !stValue.equals("1")){
			return "No";
		}else{
			return "Sí";
		}
	}
}
