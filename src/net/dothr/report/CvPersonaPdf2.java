package net.dothr.report;

import java.awt.Color;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Iterator;

import net.tce.dto.AcademicBackgroundDto;
import net.tce.dto.CertificacionDto;
import net.tce.dto.ContactInfoDto;
import net.tce.dto.CurriculumDto;
import net.tce.dto.FileDto;
import net.tce.dto.IdiomaDto;
import net.tce.dto.PersonSkillDto;
import net.tce.dto.WorkExperienceDto;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class CvPersonaPdf2 extends AbstractPDFCreator {

	private static final String OutPath = "/home/netto/app/webServer/repository/docs/X/";
	private static final String httpServerLoc = "http://localhost/repository/docs/X/";
	private static final String defaultImgPath = "/home/netto/app/webServer/s3/common/img/silh.jpg";
	
	private StringBuilder sbtmp;
	
	private static float[] columnWidths = {4, (float)10.25 };
	private static float imgX = 80f;
	private static float imgY = 50f;

	private static Color fondoCUadroImagen = new Color(0X2A3E49);	//Imagen, 0x2E2E2E
	private static Color fondoLateralIzquierdo = new Color(0X2A3E49); //Demograficos, 0xBFBFBF
//	private static Color fondoContPrincipal = new Color(0xB3B3B3);	// Profesionales, 0xF4F5F4 
	
	protected static final Rectangle tamPagina = PageSize.LETTER;	//PageSize.A4.rotate()); /* Horizontal */
	
	private static int fontSizeBase = 10; //tamanio fuente mas pequenia (8)
	private static int maxLengthLateral = 35;
	
	
	private static final Font fontTituloSDemo = FontFactory.getFont(
			FontFactory.HELVETICA_BOLD, 
			fontSizeBase+4, 
			Font.NORMAL, 
			new Color(0xF4F5F4)
			);
	private static final Font fontTxtWhiteMd = FontFactory.getFont(
			FontFactory.HELVETICA, 
			fontSizeBase+1, 
			Font.NORMAL, 
			new Color(0xF4F5F4)	//0, 0, 255)
			);

	private static final Font fontTxtWhiteBold = FontFactory.getFont(
			FontFactory.HELVETICA, 
			fontSizeBase, 
			Font.BOLD, 
			new Color(0xF4F5F4)	//0, 0, 255)
			);
	private static final Font fontTxtWhiteSm = FontFactory.getFont(
			FontFactory.HELVETICA, 
			fontSizeBase, 
			Font.NORMAL, 
			new Color(0xF4F5F4)	//0, 0, 255)
			);
	/* Columna Datos Profesionales */
	private static final Font fontNombre = FontFactory.getFont(
			FontFactory.HELVETICA_BOLD, //HELVETICA_BOLDOBLIQUE
			fontSizeBase+9, 
			Font.NORMAL, 
			new Color(0x000000)	// 0x424242  | 
			);
	private static final Font fontTituloProf = FontFactory.getFont(
			FontFactory.HELVETICA, //HELVETICA_BOLDOBLIQUE
			fontSizeBase+6, 
			Font.NORMAL, 
			new Color(0x000000)	// 0x424242  | 
			);
	private static final Font fontUbicacion = FontFactory.getFont(
			FontFactory.HELVETICA, //HELVETICA_BOLDOBLIQUE
			fontSizeBase+4, 
			Font.NORMAL, 
			new Color(0xB3B3B3)	// 0x424242  | 
			);
	
	private static final Font fontTituloSPr = FontFactory.getFont(
			FontFactory.HELVETICA_BOLD, 
			fontSizeBase+6, 
			Font.NORMAL, 
			new Color(0x428BCA)	//0, 0, 255)
			);
	private static final Font fontTituloExpAcad = FontFactory.getFont(
			FontFactory.HELVETICA, 
			fontSizeBase+3, 
			Font.NORMAL, 
			new Color(0x151515)
			);
	private static final Font fontFecha = FontFactory.getFont(
			FontFactory.HELVETICA, 
			fontSizeBase+2, 
			Font.NORMAL, 
			new Color(0x848484)
			);
	private static final Font fontPuesto = FontFactory.getFont(
			FontFactory.HELVETICA, 
			fontSizeBase+2, 
			Font.BOLD, 
			new Color(0x424242)
			);
	private static final Font fontEmpresa = FontFactory.getFont(
			FontFactory.HELVETICA, 
			fontSizeBase+1, 
			Font.NORMAL, 
			new Color(0xB3B3B3)
			);
	private static final Font fontMotSeparacionBold = FontFactory.getFont(
			FontFactory.HELVETICA, 
			fontSizeBase, 
			Font.BOLDITALIC, 
			new Color(0x424242)
			);
	private static final Font fontMotSeparacion = FontFactory.getFont(
			FontFactory.HELVETICA, 
			fontSizeBase, 
			Font.ITALIC, 
			new Color(0x424242)
			);
	

	private static final Font fontInstitucion = FontFactory.getFont(
			FontFactory.HELVETICA, 
			fontSizeBase+2, 
			Font.BOLD, 
			new Color(0x424242)
			);
	private static final Font fontTituloInst = FontFactory.getFont(
			FontFactory.HELVETICA, 
			fontSizeBase+2, 
			Font.NORMAL, 
			new Color(0xB3B3B3)
			);	
	
	private static final Font fontTxtNormalBold = FontFactory.getFont(
			FontFactory.HELVETICA, 
			fontSizeBase+1, 
			Font.BOLD, 
			new Color(0x424242)
			);
	private static final Font fontTxtNormal = FontFactory.getFont(
			FontFactory.HELVETICA, 
			fontSizeBase, 
			Font.NORMAL, 
			new Color(0x424242)
			);
	
	private static final String sepDemog = "  \n\n"; //"____________________\n";
			//"________________________________________________________________________";
	
	private Paragraph pEncabezado, pPuesto, pMotSeparacion, pEscolaridad;

	
//  /* **** SE comenta Main para ocupar unicamente la clase CvPersonaPdfTester ******* */
//	public static void main(String[] args) {
//				
//		CvPersonaPdf2 pdfRep = new CvPersonaPdf2();
////		String OutPath = "/home/dothr/app/webServer/docs/";
//		
//		CurriculumDto dtoCV = new CurriculumDto();
//		dtoCV.setImgPerfil(new FileDto());		
//		dtoCV.setNombre("Violeta");
//		dtoCV.setApellidoPaterno("Romero");
//		dtoCV.setApellidoMaterno("Magallanes");
//		dtoCV.getImgPerfil().setUrl("http://localhost/demo/selex/imagenes/p-violetta.roma.jpg");
//		
//		FileDto dtoF = pdfRep.createPdfDotHR("dotHR-pdf2.pdf", dtoCV);
//		System.out.println("Documento creado: " + (dtoF.getUrl()!=null?dtoF.getUrl():dtoF.getMessage()) );
//	}
	
	
	/**
	 * Metodo principal de Creación de PDF (TIpo CV) a partir de un 
	 * Objeto CurriculumDto, que regresa un Objeto FileDto con url del archivo Creado o Error al generar
	 * @param dest
	 * @param cvPersona
	 * @return
	 */
	public FileDto createPdfDotHR(String fileName, CurriculumDto cvPersona) {
		FileDto fileDto = new FileDto();
		
		try{
			System.out.println("CV de "+cvPersona.getNombre() + " " + cvPersona.getApellidoPaterno());
			System.out.println("Titulo? " + cvPersona.getTituloMax());
	        Document document =  new Document(tamPagina);
	        PdfWriter.getInstance(document, new FileOutputStream(OutPath+fileName));
	        document.open();
	        
	        /* >>>>>>>>>  ARMADO DEL DOCUMENTO (Comun con Produccion)  <<<<<<<<<<<<< */
	        PdfPTable mainTable;  
	        
	        /* ****   TABLA PRINCIPAL   **** */
	        mainTable = new PdfPTable(columnWidths);
	        mainTable.setWidthPercentage(100);
	        mainTable.setTotalWidth(document.getPageSize().getWidth());//pagWidth) | getWidth Usa el tamanio Maximo
	        mainTable.setLockedWidth(true);

	        /* ********* INSERCIÓN DE SECCIONES (COLUMNAS) ******* */
	        PdfPCell cell, subCell;
	        
	        /* ====== (LATERAL) ====== */
	    	PdfPTable innerTable1 = new PdfPTable(1);
	    	innerTable1.setSpacingAfter(5);		//setPadding(5);
	        innerTable1.setWidthPercentage(100);
	        
	        /* subCelda de Imagen y Datos Personales */
	        if(cvPersona.getImgPerfil()!=null) {
		        subCell = getImgCell(cvPersona);        
		        innerTable1.addCell(subCell);
	        }
	        
	        /* subCelda de Datos Demográficos  */
	        subCell = getLateral(cvPersona);
	        innerTable1.addCell(subCell);
	        /* Inserta subTabla1 en nueva celda */
	        cell = new PdfPCell(innerTable1);
	        cell.setBackgroundColor(fondoLateralIzquierdo);
	        cell.setBorderWidth(Rectangle.NO_BORDER);
	        /* Inserta celda en Columna Uno (IZQUIERDA) de la tabla mayor */
	        mainTable.addCell(cell);
	        
	        /* ====== (PRINCIPAL) ====== */
	        PdfPTable innerTable2 = new PdfPTable(1);
	        innerTable2.setSpacingAfter(0);
	        innerTable2.setWidthPercentage(100);
	        innerTable2.setSpacingAfter(10);
	        innerTable2.setSpacingBefore(10);
	        
	        /* subCelda de Datos Profesionales */
	        subCell = getPrincipal(cvPersona);
	        innerTable2.addCell(subCell);
	        cell = new PdfPCell(innerTable2);
//	        cell.setBackgroundColor(fondoContPrincipal);
	        cell.setBorderWidth(Rectangle.NO_BORDER);
	        /* Inserta celda en Columna Dos (DERECHA) de la tabla mayor */
	        mainTable.addCell(cell);	        
	        
	        document.add(mainTable);
	        
	        document.close();
	        /* >>>>>>>>>>> FIN DE CUERPO DEL ARMADO DE DOCUMENTO  <<<<<<<<<<<<< */
	        
	        //fileDto.setUrl(dest.replace("/home/dothr/app/webServer", "http://localhost"));	//convertir a url	        
	        fileDto.setUrl(httpServerLoc+fileName);	//convertir a url
		}catch (Exception ex){
			ex.printStackTrace();
			System.out.println("<createCVPdf> Error al crear Archivo: "+ex.getMessage());	
			//log4j.fatal("<createCVPdf> Error al crear Archivo: "+ex.getMessage(), ex);s
			fileDto.setUrl(null);
			fileDto.setCode("000");
			fileDto.setType("F");
			fileDto.setMessage("Error: "+ex.getMessage());
		}
		
		if(fileDto.getUrl()==null){
			fileDto.setCode("000");
			fileDto.setType("E");
			fileDto.setMessage("Error: al crear el Documento");
		}
		
		return fileDto;
    }
	
	
	/**
	 * Segmento de Imagen de Perfil y datos Personales
	 * @param cvPersona
	 * @return
	 * @throws Exception
	 */
	protected PdfPCell getImgCell(CurriculumDto cvPersona) throws Exception{
		PdfPCell subCell = new PdfPCell();
		sbtmp = new StringBuilder();
		String imgPath = cvPersona.getImgPerfil()!=null&&cvPersona.getImgPerfil().getUrl()!=null? cvPersona.getImgPerfil().getUrl(): defaultImgPath;
        subCell.setBorderWidth(Rectangle.NO_BORDER);
        subCell.setBackgroundColor(fondoCUadroImagen);
        System.out.println("imgPath: " + imgPath);
        Image img = Image.getInstance(imgPath);
        img.scaleAbsolute(imgX, imgY);
        	//scaleAbsolute(80f, 50f);
        img.setSpacingBefore(5f);
        img.setSpacingAfter(0f);
        img.setAlignment(Element.ALIGN_MIDDLE);
        subCell.addElement(img);
       
        return subCell;
	}
	
	/**
	 * Segmento datos Demograficos
	 * @param cvPersona
	 * @return
	 */
	protected PdfPCell getLateral(CurriculumDto cvPersona) {
		PdfPCell subCell = new PdfPCell();
        subCell.setBorderWidth(Rectangle.NO_BORDER);
        Paragraph dataParag = new Paragraph();
        dataParag.setSpacingBefore(50);
		dataParag.setSpacingAfter(50);
		
		 if(cvPersona.getImgPerfil()==null) { //Adiciona espacio en blanco si no existe imagen profesional
			 dataParag.add(new Chunk("\n\n", fontTxtWhiteSm) );
		 }
			
		/* Contacto(s) */
		dataParag.add(new Chunk("Datos de contacto:\n", fontTxtWhiteBold) );
		dataParag.add(new Chunk(xLongLine(""+cvPersona.getEmail())+"\n", fontTxtWhiteSm));
		if(cvPersona.getContacto()!=null){
			
			ContactInfoDto dto;
			Iterator<ContactInfoDto> itCont = cvPersona.getContacto().iterator();
			while(itCont.hasNext()){
				dto = itCont.next();				
				//TODO agregar Icono con dto.getIdTipoContacto()
				System.out.println("COntacto: " + dto);
				sbtmp = new StringBuilder("");//" - ");
				sbtmp
				.append(dto.getCodigoArea()!=null?"["+dto.getCodigoArea()+"] ":"" )
				.append(dto.getNumero()!=null?dto.getNumero():"" )
				.append(dto.getContacto()!=null?dto.getContacto():"" )
				.append(dto.getAdicional()!=null?" Ext: "+dto.getAdicional():"")
				.append("\n");
				dataParag.add(new Chunk(sbtmp.toString(), fontTxtWhiteSm));
			}
		}
        dataParag.add(new Chunk(sepDemog, fontTxtWhiteSm) );
//        System.out.println(" cvPersona.getSalarioMin(): " + cvPersona.getSalarioMin());
        
        /* Información General:  */
		if(cvPersona.getEdad()!=null || cvPersona.getLbEstadoCivil()!=null || cvPersona.getLbGenero()!=null){
			dataParag.add(new Chunk("Información General:\n", fontTxtWhiteBold) );
			
			if(cvPersona.getEdad()!=null){
				dataParag.add(new Chunk("Edad: ", fontTxtWhiteMd) );
	            dataParag.add(new Chunk(cvPersona.getEdad()+"\n", fontTxtWhiteSm) );
			}
			
			if(cvPersona.getLbGenero()!=null){
				dataParag.add(new Chunk("Estado civil: ", fontTxtWhiteMd) );
	            dataParag.add(new Chunk(cvPersona.getLbEstadoCivil()+"\n", fontTxtWhiteSm) );
			}
		    
		    if(cvPersona.getLbGenero()!=null){
				dataParag.add(new Chunk("Genero: ", fontTxtWhiteMd) );
		        dataParag.add(new Chunk(cvPersona.getLbGenero()+"\n", fontTxtWhiteSm) );
		    }
		}
        
        /* Salario & Ubicacion */
        if(cvPersona.getSalarioMin()!=null || cvPersona.getSalarioMax()!=null){
        	dataParag.add(new Chunk("Rango Salarial:\n", fontTxtWhiteMd) );
            dataParag.add(new Chunk( formtSalary(cvPersona.getSalarioMin()) + " - " + 
            		formtSalary(cvPersona.getSalarioMax()) +"\n", fontTxtWhiteSm) );//TODO mover a clase "Filtros"
        }
        
//        dataParag.add(new Chunk(sepDemog, fontTxtWhiteSm) ); //Separador de secciones
        
        if(cvPersona.getCambioDomicilio()!=null){
        	dataParag.add(new Chunk("Cambio de Domicilio: ", fontTxtWhiteMd) );
            dataParag.add(new Chunk(siNo(cvPersona.getCambioDomicilio())+"\n", fontTxtWhiteSm) );
        }
        if(cvPersona.getDisponibilidadHorario()!=null){            
            dataParag.add(new Chunk("Disp. Horario: ", fontTxtWhiteMd) );
            dataParag.add(new Chunk(siNo(cvPersona.getDisponibilidadHorario())+"\n", fontTxtWhiteSm) );
        }
        if(cvPersona.getLbDispViajar()!=null){            
        	dataParag.add(new Chunk("Disp. Viajar: ", fontTxtWhiteMd) );
            dataParag.add(new Chunk(cvPersona.getLbDispViajar()+"\n", fontTxtWhiteSm) );
        }
        
        dataParag.add(new Chunk(sepDemog, fontTxtWhiteSm) );
        
        subCell.addElement(dataParag);
        return subCell;
	}
	
	/**
	 * Segmento de Datos Profesionales (Experiencias, Escolaridades, idiomas y Habilidades)
	 * @param cvPersona
	 * @return
	 */
	protected PdfPCell getPrincipal(CurriculumDto cvPersona) {
		PdfPCell subCell = new PdfPCell(), tmpCell;
		subCell.setPaddingLeft(5);
        subCell.setBorderWidth(0);	//Rectangle.NO_BORDER
        
        pEncabezado = new Paragraph();
        
        /* Datos Personales */
        if(cvPersona.getNombre()!=null || cvPersona.getApellidoPaterno()!=null){
            sbtmp = new StringBuilder(cvPersona.getNombre()!=null?cvPersona.getNombre()+" ":"")
            .append(cvPersona.getApellidoPaterno()!=null?cvPersona.getApellidoPaterno()+" ":"")
            .append(cvPersona.getApellidoMaterno()!=null?cvPersona.getApellidoMaterno()+"":"");
            
            pEncabezado.add(new Chunk(sbtmp.toString(), fontNombre));
        }
        /* Titulo */
        if(cvPersona.getTituloMax()!=null || cvPersona.getLbGradoMax()!=null){
        	sbtmp = new StringBuilder("\n");
        	sbtmp
        	.append(cvPersona.getTituloMax()!=null?cvPersona.getTituloMax():cvPersona.getLbGradoMax())
        	.append("");
        	pEncabezado.add(new Chunk(sbtmp.toString(), fontTituloProf));
        }
        /* Ubicacion */
        if(cvPersona.getLocalizacion()!=null && !cvPersona.getLocalizacion().isEmpty()){
        	sbtmp = new StringBuilder("\n");
        	sbtmp
        	.append(cvPersona.getLocalizacion().get(0).getStColonia()!=null?cvPersona.getLocalizacion().get(0).getStColonia():"").append(", ")
        	.append(cvPersona.getLocalizacion().get(0).getStMunicipio()!=null?cvPersona.getLocalizacion().get(0).getStMunicipio():"").append(", ")
        	.append(cvPersona.getLocalizacion().get(0).getStEstado()!=null?cvPersona.getLocalizacion().get(0).getStEstado():"").append(" ")
        	.append("\n\n");
        	pEncabezado.add(new Chunk(sbtmp.toString(), fontUbicacion));
        }

        subCell.addElement(pEncabezado);
        	
		/* section WorkExperience */
        if(cvPersona.getExperienciaLaboral()!=null){
        	subCell.addElement(gTxtSecc("Experiencia Laboral", 1));
        	Iterator<WorkExperienceDto> itExpLab = cvPersona.getExperienciaLaboral().iterator();
        	WorkExperienceDto expLabDto;
        	while(itExpLab.hasNext()){
        		//Puesto
        		expLabDto = itExpLab.next();
        		PdfPTable expTable = new PdfPTable(4); //2 => columnas
    			expTable.setWidthPercentage(100);
    			tmpCell = new PdfPCell();
    			tmpCell.setPaddingLeft(5f);
    			tmpCell.setBorderWidth(0);		//   2 | Rectangle.NO_BORDER
    			tmpCell.setColspan(3);

    			pPuesto = new Paragraph();			
    			Chunk chunkPuesto = new Chunk( expLabDto.getPuesto()!=null? expLabDto.getPuesto()+"":"", fontPuesto);
    			Chunk chunkEmpresa = new Chunk( "\n"+expLabDto.getNombreEmpresa(), fontEmpresa);
    			pPuesto.add(chunkPuesto); pPuesto.add(chunkEmpresa);	
    			tmpCell.addElement(pPuesto);
    			expTable.addCell(tmpCell);
    		
    			// Periodo de trabajo    			
    			tmpCell = new PdfPCell();
    			tmpCell.setPaddingRight(15f);
    			tmpCell.setBorderWidth(0);	//   2 | Rectangle.NO_BORDER
    			tmpCell.addElement(gTxtSecc(getFechaRang(expLabDto.getAnioInicio(), expLabDto.getAnioFin(), expLabDto.getTrabajoActual()), 3));
    			expTable.addCell(tmpCell);
    			subCell.addElement(expTable);
    			
    			//Texto
//    			expTable = new PdfPTable(1);
//    			expTable.setWidthPercentage(100);
//    			tmpCell = new PdfPCell();
//    			tmpCell.setBorderWidth(Rectangle.NO_BORDER);
//    			tmpCell.addElement(gTxtSecc(notNull(expLabDto.getTexto()), 5));
//    			expTable.addCell(tmpCell);
//    			subCell.addElement(expTable);
    			
    			// Motivo de separacion
    			expTable = new PdfPTable(1);
    			expTable.setWidthPercentage(100);
    			pMotSeparacion = new Paragraph();
    			if(expLabDto.getMotivoSeparacion()!=null){        			
        			Chunk chunk1 = new Chunk("Motivo Separación: ", fontMotSeparacionBold);
        			Chunk chunk2 = new Chunk(expLabDto.getMotivoSeparacion()+"\n\n", fontMotSeparacion);
        			pMotSeparacion.add(chunk1);pMotSeparacion.add(chunk2);
    			}
    			pMotSeparacion.add(" \n");
    			tmpCell = new PdfPCell(pMotSeparacion);
    			tmpCell.setBorderWidth(Rectangle.NO_BORDER);
    			tmpCell.setPaddingLeft(5f);
    			expTable.addCell(tmpCell);
    			subCell.addElement(expTable);
        	}
        }		
        
        /* seccion Escolaridad */
        if(cvPersona.getEscolaridad()!=null){
        	AcademicBackgroundDto acadDto;
        	subCell.addElement(gTxtSecc("Formación Académica", 1));
        	Iterator<AcademicBackgroundDto> itAcad = cvPersona.getEscolaridad().iterator();
        	while(itAcad.hasNext()){
        		acadDto = itAcad.next();
        		PdfPTable expTable = new PdfPTable(4);	//2 => # columnas
    			expTable.setWidthPercentage(100);
    			tmpCell = new PdfPCell();
    			tmpCell.setPaddingLeft(5f);
    			tmpCell.setBorderWidth(0);	// Rectangle.NO_BORDER | 
    			tmpCell.setColspan(3);
    			
    			pEscolaridad = new Paragraph();			
    			Chunk chunkInstitucion = new Chunk( acadDto.getNombreInstitucion()!=null? acadDto.getNombreInstitucion()+"":"", fontInstitucion);
    			Chunk chunkTituloInst = new Chunk( "\n"+acadDto.getTitulo() , fontTituloInst);
    			pEscolaridad.add(chunkInstitucion); pEscolaridad.add(chunkTituloInst);	
    			if(acadDto.getTexto()!=null) {
        			Chunk chunkAprendizaje = new Chunk( "\n"+acadDto.getTexto() , fontTxtNormal);
        			pEscolaridad.add(chunkAprendizaje);
    			}	
    			tmpCell.addElement(pEscolaridad);
    			expTable.addCell(tmpCell);
    			
    			tmpCell = new PdfPCell();
    			tmpCell.setBorderWidth(Rectangle.NO_BORDER);
    			tmpCell.addElement(gTxtSecc(getFechaRang(acadDto.getAnioInicio(), acadDto.getAnioFin(), null), 3));
    			expTable.addCell(tmpCell);
    			subCell.addElement(expTable);
        	}
        }
        
        /* section Certificaciones */
        if(cvPersona.getCertificacion()!=null){
        	subCell.addElement(gTxtSecc("", 4));
        	subCell.addElement(gTxtSecc("Certificaciones", 1));
        	Iterator<CertificacionDto> itCert = cvPersona.getCertificacion().iterator();
        	CertificacionDto certDto;
        	while(itCert.hasNext()){
        		certDto = itCert.next();
        		PdfPTable expTable = new PdfPTable(1);
    			expTable.setWidthPercentage(100);
    			tmpCell = new PdfPCell();
    			tmpCell.setBorderWidth(0);	//Rectangle.NO_BORDER | 2
    			tmpCell.addElement(gTxtSecc(sinDef(certDto.getTituloCert()), 4));
    			expTable.addCell(tmpCell);
    			subCell.addElement(expTable);
        	}
        }
		
		/* section IDIomas/Habilidades */
        if(cvPersona.getIdioma()!=null || cvPersona.getHabilidad()!=null){
        	subCell.addElement(gTxtSecc("", 5));
        	subCell.addElement(gTxtSecc("Idiomas y Habilidades", 1));
        	//Idiomas
        	
        	
            if(cvPersona.getIdioma()!=null){
            	Paragraph pIdioma = new Paragraph();
            	Iterator<IdiomaDto> itIdioma = cvPersona.getIdioma().iterator();
            	IdiomaDto idiomaDto;
            	while(itIdioma.hasNext()){
            		idiomaDto = itIdioma.next();
            		PdfPTable expTable = new PdfPTable(3); //Tres para simular tabulacion
        			expTable.setWidthPercentage(100);
        			expTable.setSpacingAfter(0);
        			expTable.setSpacingBefore(0);
        			        			
        			tmpCell = new PdfPCell();
        			tmpCell.setBorderWidth(0);	//Rectangle.NO_BORDER | 2
        			tmpCell.addElement(gTxtSecc(sinDef(idiomaDto.getLbIdioma()), 5));
        			
        			expTable.addCell(tmpCell);
        			tmpCell = new PdfPCell();
        			tmpCell.setColspan(2);
        			tmpCell.setBorderWidth(0);//Rectangle.NO_BORDER
        			tmpCell.addElement(gTxtSecc(notNull(idiomaDto.getLbDominio()), 4));
        			expTable.addCell(tmpCell);
        			subCell.addElement(expTable);
            	}
            }
            if(cvPersona.getHabilidad()!=null){
            	Iterator<PersonSkillDto> itSkill = cvPersona.getHabilidad().iterator();
            	PersonSkillDto skillDto;
            	while(itSkill.hasNext()){
            		skillDto = itSkill.next();
            		PdfPTable expTable = new PdfPTable(3);
        			expTable.setWidthPercentage(100);
        			tmpCell = new PdfPCell();
        			tmpCell.setBorderWidth(Rectangle.NO_BORDER);
        			tmpCell.addElement(gTxtSecc(notNull(skillDto.getDescripcion()), 5));
        			
        			expTable.addCell(tmpCell);
        			tmpCell = new PdfPCell();
        			tmpCell.setColspan(2);
        			tmpCell.setBorderWidth(Rectangle.NO_BORDER);
        			tmpCell.addElement(gTxtSecc(notNull(skillDto.getLbDominio()), 4));
        			expTable.addCell(tmpCell);
        			subCell.addElement(expTable);
            	}
            }
        }
		return subCell;
	}
	
	/**
	 * Genera parrafo a partir de un texto y un tipo <br>
	 * 1=>TítuloSección<br> 2=>Título exp/Acad<br> 3=>fecha <br>4 Empresa<br> x=>Texto
	 * @param texto
	 * @param type
	 * @return
	 */
	private Paragraph gTxtSecc(String texto, int type){
		Paragraph expParag = null;
		if(type==-1){ //TítuloSecciónDemografico
			expParag = gTxtSecc(texto, fontTituloSDemo, Element.ALIGN_LEFT);
		}else if(type==0){ //TítuloSecciónDemografico
			expParag = gTxtSecc(texto, fontNombre, Element.ALIGN_CENTER);
		}else if(type==1){ //TítuloSección
			expParag = gTxtSecc(texto, fontTituloSPr, Element.ALIGN_LEFT);
		}else if(type==2){ //Título exp/Acad
			expParag = gTxtSecc(texto, fontTituloExpAcad, Element.ALIGN_LEFT);
		}
		else if(type==3 || type==4){ //fecha
			expParag = gTxtSecc(texto, fontFecha, type==3?Element.ALIGN_RIGHT:Element.ALIGN_LEFT);
		}

		else { // Texto/Default
			expParag = gTxtSecc(texto, fontTxtNormal, Element.ALIGN_LEFT);
		}
		return expParag;
	}
	
	/**
	 * Genera parrafo con las configuraciones definidas
	 * @param texto
	 * @param fuente
	 * @param alineacion
	 * @return
	 */
	private Paragraph gTxtSecc(String texto, Font fuente, int alineacion) {
			//int type){
		Paragraph expParag = new Paragraph();
		expParag.setFont(fuente);
		expParag.setAlignment(alineacion);
		expParag.setSpacingBefore(5f);
		expParag.setSpacingAfter(5f);
		expParag.add(texto);
		return expParag;
	}
	

	
	
	
	/*  *********************** FILTERS  ***************************** */
	private String siNo(String value){
		if(value==null || value.trim().equals("")){
			return "No";
		}else{
			if(value.equals("1")){
				return "Si";
			}else{
				return "No";
			}
		}
	}
	
	private String formtSalary(String value){
		if(value==null || value.trim().equals("")){
			return "$ 0.00";
		}else{
			try{
				NumberFormat nf = new DecimalFormat("$ #,###.00");
				
				Double dbl = new Double(value); 
						//nf.parse(value).doubleValue();
				return nf.format(dbl);
			}catch(Exception e){
				return "$ ";
			}
		}
	}
	private String xLongLine(String email){
		if(email!=null){
			if(email.length()>maxLengthLateral){
				if(email.indexOf("@")>maxLengthLateral){
					email = email.substring(0,maxLengthLateral)+(email.substring(maxLengthLateral,email.length()).replace("@", "\n@"));
				}else{
					email=email.replace("@", "\n@");
				}
			}
		}
		return email;
	}
	
}
