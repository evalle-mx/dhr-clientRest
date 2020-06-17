package net.utils;

import java.util.HashMap;

import net.tce.dto.TrackingDto;

public abstract class ConstantesREST {

	/* COMUNES */
	public final static String CONTENT_TYPE=";charset=UTF-8";	
	public final static String GSON_DATEFORMAT="yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	public static final String PORT_GLASSFISH = "8080";
	public static final String PORT_SOLAR = "8090";
	public static final String DNS_LOCALHOST = "http://localhost:8080/"; 
	
	public static final String LOCAL_HOME =  System.getProperty("user.home"); //Tested on UNIX (/home/dothr)
	
	public static final String PWD_DEF_123456789 = "15e2b0d3c33891ebb0f1ef609ec419420c20e320ce94c65fbc8c3312448eb225"; //123456789	//DEF_PASSWORD_MOCK
	public static final String IDEMPRESA_CONF = "1"; //idEmpresaConf
	public static final String JSON_HOME = "/home/netto/workspDhr/JSONUI/";
			//LOCAL_HOME+"/JsonUI/";
	public static final String SQL_OUTPUT_DIR = LOCAL_HOME+"/JSONUI/SQL/";	//OUTPUT_DIR_SQL
	
	/*
	 * TRANSACTIONAL AWS-ec2
	 */
	public static final String DNS_TRANSACT_EC2 = "http://ec2-54-144-71-131.compute-1.amazonaws.com:8090/";
	public static final String IP_OCTAVIO = "192.168.1.67";
	public static final String IP_HPSERVER = "192.168.0.5";
	public static final String WEB_TRANSACT_LOCAL = DNS_LOCALHOST+"AppTransactionalStructured";
			//"TransactionalStructured/rest";
	public static final String WEB_TRANSACT_AWS = DNS_TRANSACT_EC2+"AppTransactionalStructured";
			//"TransactionalStructured/rest";
	public static final String WEB_TRANSACT_MOCK = DNS_LOCALHOST+"TransactionalStructured/rest";
	
	public static final String OCTAVIO_APP = "http://"+IP_OCTAVIO+":8090/AppTransactionalStructured";
	public static final String HPSERVER_APP = "http://"+IP_HPSERVER+":8080/AppTransactionalStructured";
	
	/*
	 * OperationalStructured AWS-ec2
	 */	
	public static final String DNS_OPERAT_EC2 = "http://ec2-52-2-52-80.compute-1.amazonaws.com:8090/";
	public static final String WEB_OPERAT_LOCAL = DNS_LOCALHOST+"AppOperationalStructured";
			//"OperationalStructured/rest";
	public static final String WEB_OPERAT_AWS = DNS_OPERAT_EC2+"AppOperationalStructured";
				//"OperationalStructured/rest";
	
	/*
	 * TEST Tomcat enviroment en RedMine Ec2
	 */
	public static final String DNS_REDMINE_EC2 = "http://ec2-54-165-119-109.compute-1.amazonaws.com:8080/";
	public static final String WEB_TEST_TRANSACT_AWS = DNS_REDMINE_EC2+"AppTransactionalStructured";
	public static final String WEB_TEST_OPERAT_AWS = DNS_REDMINE_EC2+"AppOperationalStructured";
	
	
	/**
	 * Tipo INFORMATIVO 
	 * Mapa estatico conteniendo los UriCode-UriServices disponibles a la aplicación
	 * (Debe ser copia identica, se realiza con el metodo CCCC  )
	 * query: SELECT contexto, valor FROM permiso WHERE id_tipo_permiso = 1 ORDER BY contexto;
	 */
	public static HashMap<String, String> hmUriCodes = new HashMap<String, String>(){
		private static final long serialVersionUID = 1L;
		{
			put("ACADBACK.C","/module/academicBackground/create");
			put("ACADBACK.D","/module/academicBackground/delete");
			put("ACADBACK.G","/module/academicBackground/get");
			put("ACADBACK.U","/module/academicBackground/update");
			put("ADMIN.M","/admin/management/menu");
			put("APPLICANT.G","/module/applicant/getApplicants");
			put("APPLICANT.S","/module/applicant/searchApplicants");
			put("APPLICANTVACANCY.R","/module/applicant/readVacancy");
			put("ASSOCIATE.C","/module/curriculumCompany/requestAssociation");
			put("ASSOCIATE.G","/module/curriculumCompany/getAssociates");
			put("ASSOCIATE.U","/module/curriculumCompany/updateAssociation");
			put("CATALOGUE.C","/admin/catalogue/createCatalogueRecord");
			put("CATALOGUE.G","/admin/catalogue/getCatalogueValues");
			put("CATALOGUE.U","/admin/catalogue/updateCatalogueRecord");
			put("CATAREA.G","/admin/catalogue/getAreas");
			put("COMPANY.C","/module/curriculumCompany/create");
			put("COMPANY.DIS","/module/curriculumCompany/disassociate");
			put("COMPANY.FS","/module/curriculumCompany/findSimilar");
			put("COMPANY.G","/module/curriculumCompany/get");
			put("COMPANY.P","/module/curriculumCompany/setEnterpriseResumePublication");
			put("COMPANY.R","/module/curriculumCompany/read");
			put("COMPANY.U","/module/curriculumCompany/update");
			put("CONTACT.C","/module/contact/create");
			put("CONTACT.D","/module/contact/delete");
			put("CONTACT.U","/module/contact/update");
			put("DOCSCLASS.G","/module/classify/get");
			put("DOCSCLASS.U","/module/classify/update");
			put("FILE.D","/module/file/delete");
			put("FILE.G","/module/file/get");
			put("HANDSHAKE.C","/module/handshake/create");
			put("LOCATION.C","/module/location/create");
			put("LOCATION.D","/module/location/delete");
			put("LOCATION.G","/module/location/get");
			put("LOCATION.U","/module/location/update");
			put("NOTIFICATION.G","/module/notification/get");
			put("PERSKILL.C","/module/personSkill/create");
			put("PERSKILL.D","/module/personSkill/delete");
			put("PERSKILL.U","/module/personSkill/update");
			put("PERSON.D","/module/curriculumManagement/delete");
			put("PERSON.P","/module/applicant/setResumePublication");
			put("PERSON.PW","/admin/management/updpwd ");
			put("PERSON.R","/module/curriculumManagement/read");
			put("PERSON.U","/module/curriculumManagement/update");
			put("POSITIONSKILL.C","/module/positionSkill/create");
			put("POSITIONSKILL.D","/module/positionSkill/delete");
			put("POSITIONSKILL.G","/module/positionSkill/get");
			put("POSITIONSKILL.U","/module/positionSkill/update");
			put("PROFILE.A","/module/profile/updateAssociation");
			put("PROFILE.CPB","/module/profile/createpublic");
			put("PROFILE.CPV","/module/profile/createprivate");
			put("PROFILE.D","/module/profile/delete");
			put("PROFILE.G","/module/profile/get");
			put("PROFILE.R","/module/profile/read");
			put("PROFILETEX.C","/module/profile/textcreate");
			put("PROFILETEX.D","/module/profile/textdelete");
			put("PROFILETEX.G","/module/profile/textget");
			put("PROFILETEX.U","/module/profile/textupdate");
			put("PROFILE.U","/module/profile/update");
			put("RECLASIFY.T","/module/task/reClassification");
			put("REMODELING.T","/module/task/reModel");
			put("SETTLEMENT.C","/module/settlement/create");
			put("SYNCHRONIZE.T","/module/task/syncClassDocs");
			put("TOKENUPLOAD.C","/module/classify/loadTokens");
			put("VACANCYACADEMICWEIGHING.C","/module/vacancyAcademicWeighing/create");
			put("VACANCYACADEMICWEIGHING.D","/module/vacancyAcademicWeighing/delete");
			put("VACANCYACADEMICWEIGHING.U","/module/vacancyAcademicWeighing/update");
			put("VACANCY.C","/module/vacancy/create");
			put("VACANCY.CL","/module/vacancy/clone");
			put("VACANCY.D","/module/vacancy/delete");
			put("VACANCY.DC","/module/vacancy/dataconf");
			put("VACANCY.G","/module/vacancy/get");
			put("VACANCY.P","/module/vacancy/setVacancyPublication");
			put("VACANCY.R","/module/vacancy/read");
			put("VACANCYTEXT.C","/module/vacancyText/create");
			put("VACANCYTEXT.D","/module/vacancyText/delete");
			put("VACANCYTEXT.U","/module/vacancyText/update");
			put("VACANCY.U","/module/vacancy/update");
			put("WORKEXP.C","/module/workExperience/create");
			put("WORKEXP.D","/module/workExperience/delete");
			put("WORKEXP.G","/module/workExperience/get");
			put("WORKEXP.U","/module/workExperience/update");
	 		
    		//Siguientes directamente por UI
    		put("ENTERPRISE.G","/module/enterpriseParameter/get");
    		put("FILE.DC","/module/file/dataconf");
			put("LOGOUT.D","/logout/delete");
			put("PERSON.I","/module/curriculumManagement/initial");
			put("PERSON.E","/module/curriculumManagement/exists");
			put("PERSON.RM","/module/curriculumManagement/recovery");
			put("PERSON.RP","/module/curriculumManagement/restore");
    	}
	};

}
