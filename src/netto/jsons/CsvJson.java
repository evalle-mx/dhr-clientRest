package netto.jsons;

import org.json.JSONArray;
import org.json.JSONObject;

import net.utils.ClientRestUtily;

public class CsvJson extends CommonJsonDemo {

	static StringBuilder sb;
	
	public static void main(String[] args) {
		try {
			json2SQL();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	protected static void json2SQL() throws Exception {
		String absolutePathFile = "/home/netto/workspDhr/KredApp/JSONfile/kred/users.json";
		sb = new StringBuilder();
		
		JSONArray jsFile;
		JSONObject json;
		String stFileJson = ClientRestUtily.getJsonFile(absolutePathFile);
//		System.out.println("stFileJson: " + stFileJson);
		jsFile = new JSONArray(stFileJson);
		System.out.println("El response contiene "+jsFile.length()+ " json's: ");
		for(int x=0; x<jsFile.length();x++){
			json = jsFile.getJSONObject(x);
//			System.out.println(json);
			sb.append("update usuario ")
			.append("set username='").append(json.getString("userName")).append("', useralias='").append(json.getString("userAlias"))
			.append("', email='").append(json.getString("email")).append("', id_rol=").append(json.getInt("idRol")).append(",")
			.append("nombre='").append(json.getString("name")).append("', apellidos='").append(json.getString("lastName"))
			.append("', puesto='").append(json.getString("position"))
			.append("', id_sucursal=").append(json.getInt("idBranch"))
			.append(" where id_usuario = ").append(json.getInt("idUser")).append(" ;").append("\n");
		}
		
		System.out.println(sb);
		
	}
	
	
}
