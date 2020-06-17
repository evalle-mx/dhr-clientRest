package net.db;

/**
 * Clase abstracta donde se concentran principales QUery's para reportes e Inserts
 * @author dothr
 *
 */
public abstract class QueryXe {
	
	/* *********  QUERY'S para Datos internos de Persona no accesibles por Servicio Rest (PersonaFullToJson/DBdataToJson)  *********** */
	/* DATOS DE PERSONA Internos, PARA  getMapAllPersonaDB */
	
	private static final String idPersonaMin = "161";
	
	/**
	 * Query para entre persona, historico_PWD y Relacion_persona y obtener:<br>
	 * <ul><li>idPersona</li><li>email</li><li>nombreCompleto</li><li>password</li>
	 * <li>tipoRelacion</li>
	 * <li>estatusInscripcion</li><li>idEmpresa</li>
	 * </ul>
	 */
	public static final StringBuilder SQL_PERSONAS_PWD = new StringBuilder()
	.append("SELECT per.id_persona AS idPersona, per.email AS email,")
	.append(" concat(per.nombre, ' ', per.apellido_paterno, ' ', per.apellido_materno) AS nombreCompleto,")
	.append(" per.token_inicio AS tokenInicio, per.fecha_creacion AS fCreacion, per.fecha_modificacion as fMod,")
	.append(" hp.password AS password, per.id_estatus_inscripcion AS estatus,")
	//.append(" hp.password AS password, remp.id_rol AS idRol, per.id_estatus_inscripcion AS estatus,")
	.append(" remp.id_empresa AS idEmpresa")
	.append(" FROM persona AS per")
	.append(" FULL OUTER JOIN historico_password AS hp ON per.id_persona = hp.id_persona")
	.append(" INNER JOIN relacion_empresa_persona AS remp on remp.id_persona = per.id_persona")
	.append(" WHERE remp.id_empresa = <idEmpresa>  ORDER BY per.id_persona ;");
	
	/**
	 *  Seleccionar TODOS los uricodes sin importar Rol 
	 */
	public static final StringBuilder SQL_URICODES = new StringBuilder()
	.append(" SELECT id_permiso, contexto, valor, descripcion, id_tipo_permiso ")
	.append(" FROM permiso ")
	.append(" WHERE id_tipo_permiso = 1 ")
	.append(" ORDER BY contexto; ");
	
	/**
	 * Query para Obtener registros de Area_persona
	 */
	public static final StringBuilder SQL_AREAPERSONA = new StringBuilder()
	.append(" SELECT arp.id_area_persona, arp.id_area, ar.descripcion ")
	.append(" FROM area_persona AS arp")
	.append(" INNER JOIN area AS ar")
	.append(" ON arp.id_area = ar.id_area")
	.append(" WHERE arp.id_persona = ").append("<idPersona>")
	.append(" ORDER BY arp.id_area; ");
	

	/* *********  INSERTS para CARGA INICIAL (recreate.PersonaFullFromJson)  *********** */	
	/* Insert a Tabla Persona */
	public static final StringBuilder sqlInsPersona = new StringBuilder()
		.append("INSERT INTO persona (id_persona, email, id_estatus_inscripcion, permiso_trabajo, token_inicio, ")
		.append(" clasificado, fecha_creacion, fecha_confirmar_inscripcion) VALUES ");
	/* Insert a Tabla Historico_Password */
	public static final StringBuilder sqlInsHistPwd = new StringBuilder()
		.append("INSERT INTO HISTORICO_PASSWORD (id_historico_password,id_persona,password,fecha) VALUES ");
	/* Insert a Tabla Relacion_empresa_persona */
	public static final StringBuilder sqlInsRelEmpPers = new StringBuilder()
		.append("INSERT INTO RELACION_EMPRESA_PERSONA ")
		.append("(ID_RELACION_EMPRESA_PERSONA, ID_ROL, ID_EMPRESA, ID_PERSONA, FECHA_CREACION,")
		.append(" REPRESENTANTE, ESTATUS_REGISTRO,CLAVE_INTERNA) VALUES ");
	/* Insert a Tabla area_persona */
	public static final StringBuilder sqlInsAreaPers = new StringBuilder()
	.append("INSERT INTO AREA_PERSONA (id_area_persona, id_area, id_persona) VALUES ");
	
	
/* *********************************************************************************************** */	
	/* *********  QUERY'S PARA REPORTES DE ACTIVIDAD EN LA BASE XE  *********** */
	/**
	 * Query para cambiar estatus a los que ingresaron Datos sin validar por correo 
	 */
	public static final StringBuilder SQL_0_SET_ACTIVOS_DE_INSCRITOS = new StringBuilder()
	.append(" UPDATE persona")
	.append(" SET id_estatus_inscripcion = 2")
	.append(" WHERE id_estatus_inscripcion = 1")
	.append(" AND (nombre is not null or fecha_modificacion is not null)")
	.append(" AND id_persona > ").append(idPersonaMin)
	.append(" and id_persona not in (163,164,165,166,167,168,169,170,171,172,173,180)");
	/**
	 * Query para listar personas inscritas [id_estatus_inscripcion = 1]
	 */
	public static final StringBuilder SQL_1_INSCRITOS = new StringBuilder()
	.append(" SELECT id_persona, email, concat(nombre, ' ', apellido_paterno,' ',apellido_materno) as nombrecompleto,")
	.append(" to_char(fecha_creacion, 'Mon DD/YYYY HH24:mm'), ")
//	.append(" --to_char(fecha_modificacion, 'Mon dd/YYYY')")
	.append("  anio_nacimiento, mes_nacimiento, id_tipo_genero, id_estado_civil")
	.append(" , cambio_domicilio, salario_min, salario_max, id_tipo_disp_viajar, disponibilidad_horario")
	.append(" FROM persona")
	.append(" WHERE id_estatus_inscripcion = 1")
	.append(" AND id_persona > ").append(idPersonaMin)
//	.append(" --and fecha_creacion < '2016-07-21 00:00:00'::timestamp")
	.append(" and id_persona not in (163,164,165,166,167,168,169,170,171,172,173,180)")
	.append(" ORDER BY fecha_creacion asc;");
	
	/**
	 * Query para listar personas que validaron, pero no accedieron [id_estatus_inscripcion = 2]
	 */
	public static final StringBuilder SQL_2_ACTIVOS_NULOS = new StringBuilder()
	.append(" SELECT id_persona, email, concat(nombre, ' ', apellido_paterno,' ',apellido_materno) as nombrecompleto,")
	.append(" to_char(fecha_creacion, 'Mon dd/YYYY HH24:mm'), to_char(fecha_modificacion, 'Mon dd/YYYY HH24:mm')")
	.append(" , anio_nacimiento, mes_nacimiento, id_tipo_genero, id_estado_civil, cambio_domicilio, salario_min, salario_max")
	.append(" , id_tipo_disp_viajar, disponibilidad_horario")
	.append(" , fecha_creacion, fecha_modificacion")
	.append(" FROM persona")
	.append(" WHERE id_estatus_inscripcion = 2")
	.append(" AND id_persona > ").append(idPersonaMin)
	.append(" and id_persona not in (163,164,165,166,167,168,169,170,171,172,173,180)")
	.append(" AND nombre IS null")
	.append(" AND anio_nacimiento IS NULL")
	.append(" AND mes_nacimiento IS NULL")
	.append(" AND id_tipo_genero IS NULL")
	.append(" AND id_estado_civil IS NULL")
//	.append(" AND cambio_domicilio IS NULL")
	.append(" AND salario_min IS NULL")
	.append(" AND salario_max IS NULL")
	.append(" AND id_tipo_disp_viajar IS NULL")
//	.append(" AND disponibilidad_horario IS NULL")
//	.append(" -- AND fecha_modificacion < '2016-07-22 00:00:00'::timestamp  --Dos dias")
	.append(" ORDER BY fecha_creacion ASC;");
	
	/**
	 * Query personas que validaron, tienen datos, pero aún no se ha publicado [id_estatus_inscripcion = 2]
	 */
	public static final StringBuilder SQL_3_ACTIVOS_PORPUBLICAR = new StringBuilder()
	.append(" SELECT id_persona, email, concat(nombre, ' ', apellido_paterno,' ',apellido_materno) as nombrecompleto,")
	.append(" to_char(fecha_creacion, 'Mon dd/YYYY HH24:mm'), to_char(fecha_modificacion, 'Mon dd/YYYY HH24:mm')")
	.append(" , anio_nacimiento, mes_nacimiento, id_tipo_genero, id_estado_civil, cambio_domicilio, salario_min, salario_max")
	.append(" , id_tipo_disp_viajar, disponibilidad_horario")
	.append(" , fecha_creacion, fecha_modificacion")
//	.append(" --, to_char(fecha_creacion, 'Mon dd/YYYY'), to_char(fecha_modificacion, 'Mon dd/YYYY')")
	.append(" FROM persona")
	.append(" WHERE id_estatus_inscripcion = 2")
	.append(" AND id_persona > ").append(idPersonaMin)	
	.append(" AND id_persona NOT IN (63,164,165,166,167,168,169,170,171,172,173,180<idPersonas>) ")  /* Agregar los idPersona del query anterior*/
	.append(" ORDER BY fecha_modificacion asc;");
	
	/**
	 * Query lista personas que publicaron correctamente [id_estatus_inscripcion = 3]
	 */
	public static final StringBuilder SQL_4_PUBLICADOS = new StringBuilder()
	.append(" SELECT id_persona, email, concat(nombre, ' ', apellido_paterno,' ',apellido_materno) as nombrecompleto,")
	.append(" to_char(fecha_creacion, 'Mon dd/YYYY HH24:mm'), to_char(fecha_modificacion, 'Mon dd/YYYY HH24:mm')")
	.append(" , anio_nacimiento, mes_nacimiento, id_tipo_genero, id_estado_civil, cambio_domicilio, salario_min, salario_max")
	.append(" , id_tipo_disp_viajar, disponibilidad_horario")
	.append(" , fecha_creacion, fecha_modificacion")
//	.append(" --, to_char(fecha_creacion, 'Mon dd/YYYY'), to_char(fecha_modificacion, 'Mon dd/YYYY')")
	.append(" FROM persona")
	.append(" WHERE id_estatus_inscripcion = 3")
	.append(" AND id_persona > ").append(idPersonaMin)
	.append(" and id_persona not in (163,164,165,166,167,168,169,170,171,172,173,180)")
	.append(" ORDER BY fecha_modificacion asc;");
}
