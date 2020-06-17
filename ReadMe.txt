############################################################
#		ClientRest. Proyecto Java con clientes Rest
#		para pruebas hacia servidor Tomcat/Glassfish
#
############################################################
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
+++++++++++++++++++++   Componentes Principales  +++++++++++++++++++++
- log4j.properties. archivo de propiedades para registro/bitacora (Log)


DB (net.db), paquetería con metodos y constantes para acceder a Base de datos en pruebas Unitarías

DOTHR (net.dothr), paquetería con clases Testing para los diversos servicios y funcionalidades del entorno productivo
- AppsPinger. Clase de prueba para conectividad con aplicaciones REST
- ClientJersey. Clase cliente encargada de conectarse con aplicaciones REST
- JsonDocGenerator. Clase (en prototipo) para generar documentación de los servicios 
- MainAppTester. Clase principal con los metodos y parámetros comunes a todas las clases de prueba en TCE
+ fromJson, compendio de clases para insertar datos por medio de la aplicación como lo haría el flujo natural
+ recreate, compendio de clases para insertar datos masivamente via aplicación, con el flujo natural
+ report, clases prototipo para generar reportes, que se implementan en Productivo
+ temp, clases temporales para diversos propositos
+ test, clases temporales enfocados en pruebas aisladas
+ toJson, compendio de clases para exportar datos de la aplicación en archivos json planos
+ transactional, grupo de clases enfocadas en probar los servicios desarrollados en Transactional y Operational

MOCK, clases para generar y procesar datos para el ambiente de pruebas Emulado

TCE (net.tce):
* Paquetería réplica del entorno productivo del proyecto TCE
+ net.tce.util:
 - net.tce.util.Constante.java. Clase abstracta con las constantes requeridas en el entorno TCE
+ net.tce.dto: (Clases Data Transfer Object empleadas en productivo)
 - ***
 
UTILS Utilerias generales para funcionalidad de estre proyecto
- net.utils.ConnectionBD. Clase de acceso a Base de Datos principal
- net.utils.ClientRestUtily. Clase con metodos auxiliares para proceso de archivos
- net.utils.ConstantesREST. CLase que contiene las constantes requeridas por la funcionalidad Rest de este cliente
- net.utils.DateUtily. Diversos metodos auxiliares para el manejo de fechas
- net.utils.JsonUtily. Funcionalidades comunes para procesar Json

+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
++      Proceso para cargar (recreate) el ambiente en productivo:                ++
+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
0. Verificar que MainAppTester este apuntando a WEB_TRANSACT_LOCAL

1. net.dothr.recreate.PersonaFullFromJson, para leer json de /home/dothr/JsonUI/PersonaRecreate/curriculumManagement, 
	Descomentando la carpeta a cargar
  1.b. Verificar que no haya existido error: "verificar la especificación correspondiente"
2. FileJAXWSTest. [ClientFileTCE] para cargar las imagenes relacionadas (/home/dothr/JsonUI/PersonaRecreate/curriculumManagement)
	Descomentando la carpeta a cargar
(Hacer esto por cada carpeta a cargar)
===> XE-Base.0.backup  (9 Usuarios internos de DOTHR)

3. Ejecutar hasta Selex(P.Disen) (175p) e Insertar area_persona con net.dothr.recreate.AreaPersona_full>insertaAreaPersonaFull  

4. ejecutar en PosgreSql: update area_persona set confirmada = true where id_area != 100000;
	 para confirmar areas persona
	4.1. Ejecutar XeEmailUpdater.updaterEmail para actualizar los correos a pruebas @dhrtest.net.
===> XE-Base.1.backup (9 + 175 pp)
===> XE-Base.1b.backup (9+175+1081 pp)  

5. Inicializar los servicios de BIg data ( Removiendo wars Oper/Transact y reiniciando servidor TOmcat) 
	- su - hadoop
	- $HADOOP_HOME/bin/start-all.sh
	- Reiniciar tomcat
	- Si es requerido, depurar solr: http://localhost:8080/solr/core/update?stream.body=%3Cdelete%3E%3Cquery%3E*:*%3C/query%3E%3C/delete%3E&commit=true

6. net.dothr.recreate.PersonaPublisher, para publicar los idPersona cargados
===> XE-Base.2.backup

7. PosicionFullFromJson, para cargar posiciones 
===> XE-Base.3.backup

8. PlantillaRSC_full, para cargar ModelosRsc (plantillas de proceso)
===> XE-Base.4.backup

9. ModeloPosicionFromJson, para cargar los modelosPosicion asignados a posiciones
===> XE-Base.5.backup


++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
++++++++++++++++++++ Modificar Permiso.csv para carga en Script  +++++++++++++++++++
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
* Insertar los nuevos permisos en el archivo Roles_Permisos.xls junto con sus valores por rol
* Copiar desde la columna de contexto (sin idPermiso, idPermisoRelacionada) en un archivo inRolesPErm.csv
* Verificar la estructura y el separador ";"
* Ejecutar  CreateDBPermiso.reordenaMatriz para generar archivo RolesPermiso.csv con los id's Correspondientes
* Verificar orden, estructura y Separador ';'
* Ejecutar CreateDBPermiso.generaCsvsDeMatriz();
* Generar archivos CSV para TMock (y paso3)

/*
 *  *Para reinsertar los datos en la base sin reiniciar: *
 * 	> DELETE from rol_permiso; DELETE from permiso;
 * 	cd /home/dothr/workspace/ClientRest/files/csvOut/
 * 	psql xe -U dothr -p 5432 -h localhost -c "\copy permiso from 'permiso.csv' with DELIMITER ';' NULL AS ''"
 * 	psql xe -U dothr -p 5432 -h localhost -c "\copy rol_permiso from 'rol_permiso.csv' with DELIMITER ';' NULL AS ''"
 * [AWS: localhost >  dothrinstance.cclfhplprrct.us-east-1.rds.amazonaws.com ]
*/





 

