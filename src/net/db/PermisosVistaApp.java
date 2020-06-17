package net.db;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.tce.dto.EmpresaParametroDto;
import net.utils.ClientRestUtily;
import net.utils.ConnectionBD;
import net.utils.ConstantesREST;

/**
 * Procesa los datos para activar los permisos en la tabla rol_permiso 
 * para determinada funcionalidad o vista
 * @author dothr
 *
 */
public class PermisosVistaApp {

	private ConnectionBD conn;
//	private final String rutaSql = "/home/dothr/workspace/ClientRest/files/out/UPD.RolPermisoVista.sql";
	private static final String WEB_RESOURCE = ConstantesREST.WEB_TRANSACT_LOCAL;
	private StringBuilder sbRep, sbSql;
	
	private final String REPORTE_PATH = "/home/dothr/workspace/ClientRest/files/out/procesaPermisosVista.txt";
	
	/* Queries */
	private final String QUERY_SEL_PERMISOS =
			"SELECT id_permiso, contexto, valor, id_tipo_permiso FROM permiso WHERE contexto in ("
			+ "<CONTEXTO_VISTA> ) ORDER BY id_tipo_permiso desc, id_permiso ASC;";
	private final String QUERY_INSERT_EMP_PARAM = "INSERT INTO empresa_parametro VALUES (<ID>,<IDTIPOPARAM>,1,'<CONTEXTO>','<VALOR>',null,'Lista de Contextos de Permiso para la vista de <CONTEXTO>',to_date('01-JAN-19','DD-MON-RR'),null,null,0);";
	
	/* Permisos */
	/* ***** #### MI CUENTA #### ***** */
	private final String PERM_PWD_USR = "'Mi Cuenta', 'Curriculum Vitae', 'PERSON.PW'";
	private final String CV = "('Mi Cuenta','Curriculum Vitae','PERSON.R','FILE.G','PERSON.U','SETTLEMENT.C','CONTACT.C','CONTACT.U','CONTACT.D','ACADBACK.C','ACADBACK.U','ACADBACK.D','WORKEXP.C','WORKEXP.U','WORKEXP.D','PERSCERT.C','PERSCERT.U','PERSCERT.D','LANGUAGE.C','LANGUAGE.U','LANGUAGE.D','PERSKILL.C','PERSKILL.U','PERSKILL.D','PERSON.P')";
	private final String FORMATO_COMP = "'Mi Cuenta','Formato de Compensación','COMPENSATION.C','COMPENSATION.R','COMPENSATION.U','VOUCHER.U','AUTOMOBILE.C','BOND.C','BOND.U','BOND.D','INSURANCE.C','INSURANCE.U','INSURANCE.D','PLAN.C','PLAN.U','PLAN.D','REFERENCE.C','REFERENCE.U','REFERENCE.D'";
	private final String PROCESOS_PERSONA = "'Mi Cuenta','Procesos','TRACKPOSTULANT.G','TRACKPOSTULANT.U','TRACKPOSTULANT.R'";

	/* ***** #### TALENTO #### ***** */
	private final String NUEVO_USUARIO = "'Talento','Crear nuevo','ROL.G','PERSON.G','PERSON.R','COMPENSATION.R','REPORT.C','PERSON.U'";
	private final String BUSCAR_CV = "'Talento','Buscar CV','ROL.G','PERSON.CM'";

	/* ***** #### MODELO RSC #### ***** */
	private final String NUEVO_MRSC = "'Modelos RSC','Nuevo','MODELRSC.C','MODELRSC.R','MODELRSC.U'";
	private final String LISTA_MRSC = "'Modelos RSC','Nuevo','MODELRSC.G','MODELRSC.R','MODELRSC.D'";
	
	/* ***** ####  POSICIÓN  #### ***** */
	private final String NUEVA_POS = "'Posición','Nueva posición','CATALOGE.G','VACANCY.C',";
	private final String LISTA_POS_CTE = "'Posición','Posiciones creadas','VACANCY.G','COMPANY.G','VACANCY.U','Editar Posición','VACANCY.R','ROL.G','COMPANY.G','MONITOR.G','VACANCY.U','SETTLEMENT.C','PROFILETEX.C','POSITIONCERT.C','POSITIONCERT.U','POSITIONCERT.D','LANGUAGE.C','LANGUAGE.U','LANGUAGE.D','PERSON.G','MONITOR.G','MONITOR.C','MONITOR.D','VACANCY.P','APPLICANT.S','Modelo Rsc Posición','MODELRSCPOS.G','ROL.G','MODELRSC.G','MODELRSCPOS.C','VACANCY.R','MODELRSCPOSF.U','Precandidatos','APPLICANT.G','MODELRSCPOS.G','PERSON.R','TRACKPOSTULANT.C','Postulantes','POSTULANT.G','ROL.G','MODELRSCPOS.G','PERSON.R','TRACKPOSTULANT.R','TRACKPOSTULANT.U','TRACKMONITOR.R','TRACKMONITOR.U','CALENDAR.GD','TRACKPOSTULANT.CF','PERSON.G','TRACKPOSTULANT.C','TRACKPOSTULANT.D'";
	private final String LISTA_POS_MON = "'Posición','Posiciones asignadas','VACANCY.G','COMPANY.G','VACANCY.R','MODELRSCPOS.G','Candidatos Monitor','POSTULANT.G','MODELRSCPOS.G','ROL.G','PERSON.R','TRACKMONITOR.R','TRACKMONITOR.U','CALENDAR.GD'";
	
	/* ***** ####  ADMINISTRACIÓN  #### ***** */
	private final String EMP_PARAM = "'Administración','Empresa Parametro','EPARAMS.G','EPARAMS.U','EPARAMS.RL','EPARAMS.C','EPARAMS.D'";
	private final String ASIG_PERMISOS = "'Administración','Roles y Permisos','Roles','Asignar Permisos','ROL.G','TIPOPERMISO.G','PERMISSION.G','ROL.AP'";
	private final String ROL_EDIT = "'Administración','Roles y Permisos','Roles','Rol','ROL.G','ROL.C','ROL.U','ROL.D')";
	private final String PERM_EDIT = "'Administración','Roles y Permisos','Permisos','ROL.G','TIPOPERMISO.G','PERMISSION.G','PERMISSION.C','PERMISSION.U','PERMISSION.D'";
	private final String SYNC_DOCS = "'Administración','Documentos','Sincronización','SYNCDOCS.LD','SYNCHRONIZE.T'";
	private final String DOC_CLASS = "'Administración','Documentos','Clasificados','DOCSCLASS.G','CATAREA.G','CATALOGUE.G ','DOCSCLASS.U'";

	private final String REMOD = "'Administración','Modelo','Remodelar','REMODELING.LD','REMODELING.T'";
	private final String RECLASS = "'Administración','Modelo','Reclasificar','RECLASSDOCS.LD','RECLASIFY.T'";
	private final String RELOADCORE = "'Administración','Modelo','Reload Core Solr','RELOADCORESOLR.LD','RELOADCORESOLR.T'";
	
	
	public static void main(String[] args) {
		PermisosVistaApp app = new PermisosVistaApp();
		try {
			app.insertsEmpParam();
//			app.verStPermisos();
//			app.procesaPermisosVista("3", 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void procesaPermisosVista(String idRol, int iFuncion) throws Exception {
		conn = new ConnectionBD(WEB_RESOURCE);
		sbRep = new StringBuilder("###  Conectando con XE \n");
		conn.getDbConn();
		sbSql = null;
		Long idPermiso;
		
		String stPermisos;
		
		//TODO determinar listado de cadenas permiso
		stPermisos = QUERY_SEL_PERMISOS.replace("<CONTEXTO_VISTA>", getStPermisosVista(iFuncion));

		ResultSet rs = conn.getQuerySet(stPermisos);
		//1.Listar personas en Sistema
		if(rs!= null ){
			sbSql = new StringBuilder();	
			while (rs.next()){
				idPermiso = rs.getLong(1);				
				sbSql.append(idPermiso).append(rs.isLast()?"":",");
			}
		}
		System.out.println("SI existieron resultados, generar query de update");
		if(sbSql!=null){
			System.out.println("sbSql: "+sbSql.toString());
			String updQuery = "UPDATE rol_permiso SET activo = true \n WHERE id_permiso IN ("+
					sbSql.toString()
					+") \n AND id_rol = "+idRol+";\n";
			System.out.println("Query Update: \n"+updQuery);
		}
		
		
		sbRep.append("\n #Termina proceso, cerrando conexión \n");
		conn.closeConnection();
		sbRep.append("#######  FIN DE PROCESO   ##### ");
		ClientRestUtily.writeStringInFile(REPORTE_PATH, sbRep.toString(), false);
		System.out.println("Se escribio archivo reporte: \n" + REPORTE_PATH );
		
	}
	
	private void verStPermisos(){
		String stPermiso;
		for(int iFuncion=0; iFuncion<=20;iFuncion++){
			stPermiso = getStPermisosVista(iFuncion);
			System.out.println(stPermiso);
		}
	}
	
	
	public String getStPermisosVista(int iFuncion){
		/* ***** #### MI CUENTA #### ***** */
		if(iFuncion==1){//"Cambio contraseña"
			System.out.println("Cambio contraseña: ");
			return PERM_PWD_USR;
		}
		if(iFuncion==2){
			System.out.println("Formulario Captura Curriculum Vitae: ");
			return CV;
		}
		if(iFuncion==3){
			System.out.println("Formato de Compensación: ");
			return FORMATO_COMP;
		}
		if(iFuncion==4){
			System.out.println("Procesos del candidato: ");
			return PROCESOS_PERSONA;
		}
		/* ***** #### TALENTO #### ***** */
		if(iFuncion==5){
			System.out.println("Crear nuevo Usuario: ");
			return NUEVO_USUARIO;
		}
		if(iFuncion==6){
			System.out.println("Buscar persona: ");
			return BUSCAR_CV;
		}
		
		/* ***** #### MODELO RSC #### ***** */
		if(iFuncion==7){
			System.out.println("Generar nuevo Modelo RSC: ");
			return NUEVO_MRSC;
		}
		if(iFuncion==8){
			System.out.println("Lista de Modelos RSC: ");
			return LISTA_MRSC;
		}
		
		/* ***** ####  POSICIÓN  #### ***** */
		if(iFuncion==9){
			System.out.println("Crear una nueva Posición: ");
			return NUEVA_POS;
		}
		if(iFuncion==10){
			System.out.println("Lista de Posiciones Contratante: ");
			return LISTA_POS_CTE;
		}
		if(iFuncion==11){
			System.out.println("Lista de Posiciones Monitor: ");
			return LISTA_POS_MON;
		}
		
		/* ***** ####  ADMINISTRACIÓN  #### ***** */
		if(iFuncion == 12){
			System.out.println("Empresa Parámetro:");
			return EMP_PARAM;
		}
		if(iFuncion == 13){
			System.out.println("Asignar permisos:");
			return ASIG_PERMISOS;
		}
		if(iFuncion == 14){
			System.out.println("Rol:");
			return ROL_EDIT;
		}
		if(iFuncion == 15){
			System.out.println("Permisos (Edición):");
			return PERM_EDIT;
		}
		if(iFuncion == 16){
			System.out.println("Sincronizacion de Docs:");
			return SYNC_DOCS;
		}
		if(iFuncion == 17){
			System.out.println("Documentos Clasificados:");
			return DOC_CLASS;
		}

		if(iFuncion == 18){
			System.out.println("Remodelar:");
			return REMOD;
		}
		if(iFuncion == 19){
			System.out.println("Reclasificar:");
			return RECLASS;
		}
		if(iFuncion == 20){
			System.out.println("Reload Core");
			return RELOADCORE;
		}
		
		
		return "";
	}
	
	
	private void insertsEmpParam(){
		int indice = 1146;
		List<EmpresaParametroDto> lsDto = lsEmpParamVistas(indice);
		Iterator<EmpresaParametroDto> itDto = lsDto.iterator();
		EmpresaParametroDto dto; 
		String insert;
		int idTipoParamNvo = 9;
		sbSql = new StringBuilder("/* INSERT DE TIPO PARAMETRO */ \n INSERT INTO tipo_parametro \n VALUES (")
			.append(idTipoParamNvo).append(",'Contextos por Vista',null,true);\n")
				.append("/*  *** INSERTS DE EMPRESA PARAMETRO PROPUESTOS PERMISO-VISTA *********** */\n");
		while(itDto.hasNext()){
			dto = itDto.next();
			System.out.println(dto.getContexto()+ ": "+ dto.getValor());
//			insert = QUERY_INSERT_EMP_PARAM
//					.replace("<ID>", dto.getIdEmpresaParametro())
//					.replace("<IDTIPOPARAM>", String.valueOf(idTipoParamNvo))
//					.replace("<CONTEXTO>", dto.getContexto())
//					.replace("<VALOR>", dto.getValor());
//			sbSql.append(insert).append("\n");
		}
		
		System.out.println(sbSql);
	}
	
	public List<EmpresaParametroDto> lsEmpParamVistas(int indice){
		List<EmpresaParametroDto> lsDto = new ArrayList<EmpresaParametroDto>();
		EmpresaParametroDto dto;
//		int indice = 1146;
		
		dto = new EmpresaParametroDto();dto.setIdEmpresaParametro(String.valueOf(indice++));dto.setContexto("Cambio contraseña");dto.setValor("Mi Cuenta, Curriculum Vitae, PERSON.PW");
		lsDto.add(dto); dto = new EmpresaParametroDto();dto.setIdEmpresaParametro(String.valueOf(indice++));dto.setContexto("Formulario Captura Curriculum Vitae");dto.setValor("Mi Cuenta,Curriculum Vitae,PERSON.R,FILE.G,PERSON.U,SETTLEMENT.C,CONTACT.C,CONTACT.U,CONTACT.D,ACADBACK.C,ACADBACK.U,ACADBACK.D,WORKEXP.C,WORKEXP.U,WORKEXP.D,PERSCERT.C,PERSCERT.U,PERSCERT.D,LANGUAGE.C,LANGUAGE.U,LANGUAGE.D,PERSKILL.C,PERSKILL.U,PERSKILL.D,PERSON.P");
		lsDto.add(dto); dto = new EmpresaParametroDto();dto.setIdEmpresaParametro(String.valueOf(indice++));dto.setContexto("Formato de Compensación");dto.setValor("Mi Cuenta,Formato de Compensación,COMPENSATION.C,COMPENSATION.R,COMPENSATION.U,VOUCHER.U,AUTOMOBILE.C,BOND.C,BOND.U,BOND.D,INSURANCE.C,INSURANCE.U,INSURANCE.D,PLAN.C,PLAN.U,PLAN.D,REFERENCE.C,REFERENCE.U,REFERENCE.D");
		lsDto.add(dto); dto = new EmpresaParametroDto();dto.setIdEmpresaParametro(String.valueOf(indice++));dto.setContexto("Procesos del candidato");dto.setValor("Mi Cuenta,Procesos,TRACKPOSTULANT.G,TRACKPOSTULANT.U,TRACKPOSTULANT.R");
		lsDto.add(dto); dto = new EmpresaParametroDto();dto.setIdEmpresaParametro(String.valueOf(indice++));dto.setContexto("Crear nuevo Usuario");dto.setValor("Talento,Crear nuevo,ROL.G,PERSON.G,PERSON.R,COMPENSATION.R,REPORT.C,PERSON.U");
		lsDto.add(dto); dto = new EmpresaParametroDto();dto.setIdEmpresaParametro(String.valueOf(indice++));dto.setContexto("Buscar persona");dto.setValor("Talento,Buscar CV,ROL.G,PERSON.CM");
		lsDto.add(dto); dto = new EmpresaParametroDto();dto.setIdEmpresaParametro(String.valueOf(indice++));dto.setContexto("Generar nuevo Modelo RSC");dto.setValor("Modelos RSC,Nuevo,MODELRSC.C,MODELRSC.R,MODELRSC.U");
		lsDto.add(dto); dto = new EmpresaParametroDto();dto.setIdEmpresaParametro(String.valueOf(indice++));dto.setContexto("Lista de Modelos RSC");dto.setValor("Modelos RSC,Nuevo,MODELRSC.G,MODELRSC.R,MODELRSC.D");
		lsDto.add(dto); dto = new EmpresaParametroDto();dto.setIdEmpresaParametro(String.valueOf(indice++));dto.setContexto("Crear una nueva Posición");dto.setValor("Posición,Nueva posición,CATALOGE.G,VACANCY.C,");
		lsDto.add(dto); dto = new EmpresaParametroDto();dto.setIdEmpresaParametro(String.valueOf(indice++));dto.setContexto("Lista de Posiciones Contratante");dto.setValor("Posición,Posiciones creadas,VACANCY.G,COMPANY.G,VACANCY.U,Editar Posición,VACANCY.R,ROL.G,COMPANY.G,MONITOR.G,VACANCY.U,SETTLEMENT.C,PROFILETEX.C,POSITIONCERT.C,POSITIONCERT.U,POSITIONCERT.D,LANGUAGE.C,LANGUAGE.U,LANGUAGE.D,PERSON.G,MONITOR.G,MONITOR.C,MONITOR.D,VACANCY.P,APPLICANT.S,Modelo Rsc Posición,MODELRSCPOS.G,ROL.G,MODELRSC.G,MODELRSCPOS.C,VACANCY.R,MODELRSCPOSF.U,Precandidatos,APPLICANT.G,MODELRSCPOS.G,PERSON.R,TRACKPOSTULANT.C,Postulantes,POSTULANT.G,ROL.G,MODELRSCPOS.G,PERSON.R,TRACKPOSTULANT.R,TRACKPOSTULANT.U,TRACKMONITOR.R,TRACKMONITOR.U,CALENDAR.GD,TRACKPOSTULANT.CF,PERSON.G,TRACKPOSTULANT.C,TRACKPOSTULANT.D");
		lsDto.add(dto); dto = new EmpresaParametroDto();dto.setIdEmpresaParametro(String.valueOf(indice++));dto.setContexto("Lista de Posiciones Monitor");dto.setValor("Posición,Posiciones asignadas,VACANCY.G,COMPANY.G,VACANCY.R,MODELRSCPOS.G,Candidatos Monitor,POSTULANT.G,MODELRSCPOS.G,ROL.G,PERSON.R,TRACKMONITOR.R,TRACKMONITOR.U,CALENDAR.GD");
		lsDto.add(dto); dto = new EmpresaParametroDto();dto.setIdEmpresaParametro(String.valueOf(indice++));dto.setContexto("Empresa Parámetro");dto.setValor("Administración,Empresa Parametro,EPARAMS.G,EPARAMS.U,EPARAMS.RL,EPARAMS.C,EPARAMS.D");
		lsDto.add(dto); dto = new EmpresaParametroDto();dto.setIdEmpresaParametro(String.valueOf(indice++));dto.setContexto("Asignar permisos");dto.setValor("Administración,Roles y Permisos,Roles,Asignar Permisos,ROL.G,TIPOPERMISO.G,PERMISSION.G,ROL.AP");
		lsDto.add(dto); dto = new EmpresaParametroDto();dto.setIdEmpresaParametro(String.valueOf(indice++));dto.setContexto("Rol");dto.setValor("Administración,Roles y Permisos,Roles,Rol,ROL.G,ROL.C,ROL.U,ROL.D");
		lsDto.add(dto); dto = new EmpresaParametroDto();dto.setIdEmpresaParametro(String.valueOf(indice++));dto.setContexto("Permisos (Edición)");dto.setValor("Administración,Roles y Permisos,Permisos,ROL.G,TIPOPERMISO.G,PERMISSION.G,PERMISSION.C,PERMISSION.U,PERMISSION.D");
		lsDto.add(dto); dto = new EmpresaParametroDto();dto.setIdEmpresaParametro(String.valueOf(indice++));dto.setContexto("Sincronizacion de Docs");dto.setValor("Administración,Documentos,Sincronización,SYNCDOCS.LD,SYNCHRONIZE.T");
		lsDto.add(dto); dto = new EmpresaParametroDto();dto.setIdEmpresaParametro(String.valueOf(indice++));dto.setContexto("Documentos Clasificados");dto.setValor("Administración,Documentos,Clasificados,DOCSCLASS.G,CATAREA.G,CATALOGUE.G ,DOCSCLASS.U");
		lsDto.add(dto); dto = new EmpresaParametroDto();dto.setIdEmpresaParametro(String.valueOf(indice++));dto.setContexto("Remodelar");dto.setValor("Administración,Modelo,Remodelar,REMODELING.LD,REMODELING.T");
		lsDto.add(dto); dto = new EmpresaParametroDto();dto.setIdEmpresaParametro(String.valueOf(indice++));dto.setContexto("Reclasificar");dto.setValor("Administración,Modelo,Reclasificar,RECLASSDOCS.LD,RECLASIFY.T");
		lsDto.add(dto); dto = new EmpresaParametroDto();dto.setIdEmpresaParametro(String.valueOf(indice++));dto.setContexto("Reload Core");dto.setValor("Administración,Modelo,Reload Core Solr,RELOADCORESOLR.LD,RELOADCORESOLR.T");
		lsDto.add(dto); dto = new EmpresaParametroDto();dto.setIdEmpresaParametro(String.valueOf(indice++));dto.setContexto("Administración,Modelo,Reload Core Solr,RELOADCORESOLR.LD,RELOADCORESOLR.T");
	
		return lsDto;
	
	}
	
}
