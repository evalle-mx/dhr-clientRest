package net.dothr.transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import net.dothr.MainAppTester;
import net.utils.ClientRestUtily;

public class ContenidoTester extends MainAppTester {
	
	static Logger log4j = Logger.getLogger( ContenidoTester.class );
	private static String newPathProf = "/home/dothr/app/webServer/imagenes/personas/";

	public static void main(String[] args) {
		try {
			pruebaFileGet();
//			String url = getUrlProfilePic("57");	System.out.println(url);
			
//			getMultiplePics();
//			contenidoRead();
		} catch (Exception e) {
			log4j.debug("error: ", e);
		}
	}
	
	public static void contenidoRead() throws Exception {
	
		JSONObject jsono = new JSONObject();
		
		jsono.put(P_JSON_IDCONF, IDSELEX );
		jsono.put(P_JSON_PERSONA, "2" );
		jsono.put("idContenido", "3"); //Imagen prueba Tracking
		
		
		String jSon = getJsonFromService(jsono.toString(), URI_FILE.concat(URI_READ) );
	}
	
	/**
	 * Verifica funcionamiento para lista de Contenido de Persona FILE.G
	 * @throws Exception
	 */
	public static void pruebaFileGet() throws Exception {
		int tipoContenido = 3;
		pruebaFileGet(tipoContenido, P_JSON_PERSONA, "2");
	}
	
	
	
	public static String getUrlProfilePic(String idPersona) throws Exception {
		String tipoContenido = "1", url;
		JSONObject jsono = new JSONObject();
		jsono.put(P_JSON_IDCONF, IDCONF_DOTHR );
		jsono.put(P_JSON_PERSONA, idPersona);
		jsono.put("idTipoContenido", tipoContenido);

		String stJson = getJsonFromService(jsono.toString(), URI_FILE.concat(URI_GET) );
		if(!stJson.equals("[]") && stJson.indexOf("code")==-1){
			JSONArray jsRsp = new JSONArray(stJson);
			JSONObject json = jsRsp.getJSONObject(0);
			
			url = json.getString("url");
		}else{
			log4j.debug("<getUrlProfilePic> idPersona "+idPersona+", no tiene imagen de perfil");
			url = null;
		}
		
		return url;
	}
	
	private static HashMap<Integer, String> mapIdMail = new HashMap<Integer, String>(){
		private static final long serialVersionUID = 1L;
		{
			put(165,"hwgutierrez12@gmail.com");
			put(185,"oaocampoc@gmail.com");
			put(205,"richyherga@gmail.com");
			put(225,"ilejgmtz@gmail.com");
			put(245,"pilar.castro@some.com");
			put(265,"luna_pao@hotmail.com");
			put(285,"zurisa_0812@hotmail.com");
			put(286,"robalvarez.or@gmail.com");
			put(287,"mvalladares2@hotmail.com");
			put(288,"orlandoibpe@hotmail.com");
			put(289,"tania.luz.gonzalez@gmail.com");
			put(290,"ramiro.barron.marquez@gmail.com");
			put(291,"bettymoralesh@gmail.com");
			put(292,"erickpsique@hotmail.com");
			put(293,"mayra.camacho@outlook.com");
			put(294,"ahsotelo@yahoo.com.mx");
			put(296,"montoyalorenz@gmail.con");
			put(297,"martin_moreno49@hotmail.com");
			put(298,"cesar.soto@hotmail.com");
			put(299,"cesar.soto21@hotmail.com");
			put(305,"mangelperezs@yahoo.com.mx");
			put(325,"ddluna@live.com.mx");
			put(326,"LEM.AARONESPINOZA@GMAIL.COM");
			put(327,"alex.castro.85@gmail.com");
			put(328,"edgar_gascpec@hotmail.com");
			put(329,"luis.agarza@hotmail.com");
			put(330,"vaikra.d@gmail.com");
			put(331,"erikazipol@hotmail.com");
			put(332,"ccaldera57@yahoo.com.mx");
			put(333,"juca_1_6@hotmail.com");
			put(334,"kmleosan@hotmail.com");
			put(335,"jbarraganc@hotmail.com");
			put(336,"adnkey@hotmail.com");
			put(337,"marisolmartinez0221@agentia.com.mx");
			put(338,"fabiolabella66@gmail.com");
			put(339,"israel-becerra@hotmail.com");
			put(340,"camila_Z69@hotmail.com");
			put(341,"lhmtz@live.com.mx");
			put(342,"psicsalaya@hotmail.com");
			put(343,"terejulian@hotmail.com");
			put(344,"ingenierornoriega@gmail.com");
			put(345,"mayela_rios@yahoo.com.mx");
			put(346,"daenny1@hotmail.com");
			put(347,"luisrodrigou@yahoo.com.mx");
			put(365,"yareli.montes@hotmail.com");
			put(385,"albapilarcruzcerero@ymail.com");
			put(386,"keyu_07@hotmail.com");
			put(387,"cintiuxrodriguez@gmail.com");
			put(388,"alex_vay2@hotmail.com");
			put(405,"viri_181@hotmail.com");
			put(425,"betty120583@outlook.com");
			put(445,"acollantesr@hotmail.com");
			put(465,"karlagoa@yahoo.com.mx");
			put(466,"marianita.mv78@gmail.com");
			put(467,"canonicomiguel@hotmail.com");
			put(485,"karinaelizondos@hotmail.com");
			put(486,"judithgaribay@hotmail.com");
			put(487,"recluta7707@yahoo.com.mx");
			put(488,"lucero.rodriguez.g@gmail.com");
			put(489,"rafamtzdiaz@hotmail.com");
			put(491,"azaralaz@hotmail.com");
			put(492,"angel.oran27@gmail.com");
			put(493,"alvaradojmfa@gmail.com");
			put(494,"rigorizza@hotmail.com");
			put(495,"liscano@live.com.mx");
			put(496,"danhya_74@hotmail.com");
			put(497,"verduzco36@hotmail.com");
			put(498,"julio@grupocaadmin.com");
			put(499,"munoz_daniel10@hotmail.com");
			put(500,"bre.carrizales@hotmail.com");
			put(501,"jervenzor@gmail.com");
			put(502,"karolexk@hotmail.com");
			put(503,"jonathanalanc@gmail.com");
			put(504,"javito0315@hotmail.com");
			put(505,"augustogcesar4@hotmail.com");
			put(506,"psic.carmin@gmail.com");
			put(507,"jpablo_bbeltran@yahoo.com.mx");
			put(525,"normapms@hotmail.com");
			put(545,"lizethlr4@hotmail.com");
			put(565,"menito15486@yahoo.com.mx");
			put(566,"lau_8585@hotmail.com");
			put(567,"ing_mgc1982@hotmail.com");
			put(568,"jorgerodrigo.castroreyes@hotmail.com");
			put(569,"victornufer@hotmail.com");
			put(570,"joseantoniocruzoteiza@gmail.com");
			put(571,"jfernandovel@hotmail.com");
			put(572,"ncorderor1@gmail.com");
			put(573,"adriana.diazgomez@yahoo.com.mx");
			put(574,"adreanola@gmail.com");
			put(575,"pablinrv@yahoo.com.mx");
			put(576,"regamez_g77@hotmail.com");
			put(577,"narvaezja8383@hotmail.com");
			put(578,"iwaly11@hotmail.com");
			put(579,"yari_neil@hotmail.com");
			put(580,"eli_cantisani@hotmail.com");
			put(581,"ingesol77@gmail.com");
			put(582,"lic.ricardo.alvarez@gmail.com");
			put(585,"edgargomeza.76@hotmail.com");
			put(586,"grysselc@hotmail.com");
			put(587,"ez23ch@yahoo.com.mx");
			put(588,"vanrro85@gmail.com");
			put(589,"jofri29@hotmail.es");
			put(590,"marug718@prodigy.net.mx");
			put(605,"hiramcarr81@gmail.com");
			put(606,"fco_leyvag@hotmail.com");
			put(625,"wendy14_2@hotmail.com");
			put(626,"pamela_apm11@hotmail.com");
			put(627,"vmo0323@yahoo.com");
			put(645,"flortorresalvarado@gmail.com");
			put(646,"noel.espinoza@hotmail.com");
			put(665,"emois@hotmail.com");
			put(685,"jorammtz@yahoo.com.mx");
			put(705,"lore_der@hotmail.com");
			put(706,"jmqp.1975@gmail.com");
			put(725,"e-manu-flores@hotmail.com");
			put(745,"jesus_qv@hotmail.com");
			put(765,"aida_ocnuj@hotmail.com");
			put(785,"rousyab@hotmail.com");
			put(805,"jonathanhector@live.com");
			put(825,"cesar_romero105@hotmail.com");
			put(826,"gerardo.tlacaelel@gmail.com");
			put(827,"ramon_2913@hotmail.com");
			put(828,"luisfe_rh@hotmail.com");
			put(829,"aleexac@gmail.com");
			put(830,"fredybrend@gmail.com");
			put(831,"tereflores@some.com");
			put(832,"jorgespra@gmail.com");
			put(833,"carlosgzzang@gmail.com");
			put(834,"j.gonzalez12@hotmail.com");
			put(835,"marbella@some.com");
			put(845,"edgar_gaspec@hotmail.com");
			put(865,"andres@stoa.com.mx");
			put(885,"ernestomarincvz@gmail.com");
			put(905,"cuevasvalery@gmail.com");
			put(925,"lrcruizmtz@hotmail.com");
			put(945,"joseluistoledo8@hotmail.com");
			put(946,"danny_starclub@hotmail.com");
			put(965,"tiggger85_26@hotmail.com");
			put(985,"ficoazcorra20@icloud.com");
			put(1005,"delia_gomez03@hotmail.com");
			put(1025,"kblancarte@gmail.com");
			put(1045,"landero.fuentes@gmail.com");
			put(1065,"lizropelca@live.com.mx");
			put(1085,"cindy_aril@hotmail.com");
			put(1105,"cindy_aril@hotmail.com");
			put(1125,"1960joal@gmail.com");
			put(1145,"bere_2824@hotmail.com");
			put(1165,"arielrxba@hotmail.com");
			put(1185,"mach_palma@yahoo.com.mx");
			put(1205,"monsecasas@gmail.com.mx");
			
		}
	};
	
	public static void getMultiplePics() throws Exception {
		StringBuilder sbMult = new StringBuilder();
		
		String DEF_WEBSERVER = "http://localhost:80";
		String path = "/home/dothr/app/webServer/imagenes/2.AWS(P.RH)/";
		String tipoContenido = "1", url;
		JSONObject jsono = null;
		JSONArray jsRsp;
		String idPersona, email, nUrl;
		Integer intPersona;
		Iterator<String> itIds = getIds().iterator();
		while(itIds.hasNext()){
			idPersona = itIds.next();
			intPersona = Integer.parseInt(idPersona);
			email = mapIdMail.get(intPersona);
			url = getUrlProfilePic(idPersona);
			//System.out.println(intPersona + ": " + getUserName(email) + " | url: " + url );
			if(url!=null){
				sbMult.append(intPersona).append(";").append(getUserName(email)).append(";").append(url);
				nUrl = copyProfImage(url, DEF_WEBSERVER, path, getUserName(email));
				if(nUrl!=null ){
					sbMult.append("|").append(nUrl).append("\n");
				}
			}
		}
		log4j.debug("\n\n <getMultiplePics> obtenidas: \n" + sbMult);
		
	}
	
	/**
	 * Realiza copia del archivo (imagen) del webServer a una carpeta única
	 * @param url
	 * @param urlReplace
	 * @param imgOutPath
	 * @param userName
	 * @return
	 */
	private static String copyProfImage(String url, String urlReplace, String imgOutPath, String userName){
//		log4j.debug("<copyProfImage>");
		String ext, imgName=null;		
		if(!url.equals("")){
			ext = ClientRestUtily.getFileExt(url);
			imgName = "p-"+userName+"."+ext;
			boolean moved = false;
			try{
				moved = ClientRestUtily.copyFile(url.replace(urlReplace, "/home/dothr/app/webServer"), imgOutPath+imgName);
			}catch (Exception e){
				log4j.fatal("<error>", e );
				imgName = null;
			}
			if(moved){
				log4j.debug("<copyProfImage> Se creo imagen de Perfil: "+ imgName  );
			}else{
				log4j.error("<copyProfImage> Error al crear imagen de Perfil: "+ imgName  );
				imgName = null;
			}
		}
		return imgName;
	}
	
	
	private static List<String> getIds(){
		List<String> lista = new ArrayList<String>();
		
		String sep = ",", ids = "286,326,332,335,487,497,496,566,501,502,578,573,581,588,605,646,825,826,828,725,834,905,945,965,1125";
				
		lista = Arrays.asList(ids.split("\\s*"+sep+"\\s*"));
		
		return lista;
	}
}
