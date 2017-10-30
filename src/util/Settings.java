package util;

import gui.Theme;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Settings {
	
	// Put information you want saved here
	public static Theme currentTheme;
	// Put information you want saved here
	
	
	public Settings(){
		currentTheme = new Theme();
	}
	
	public Settings(String input){
		
		JSONParser parser = new JSONParser();
		Object obj = null;
		try {
			obj = parser.parse(input);
		} catch (ParseException e) { }
        JSONObject jsonData = (JSONObject)obj;
        
        
        JSONObject themeJSON = (JSONObject) jsonData.get("theme");
        currentTheme = new Theme(themeJSON); 
	}
	
	@SuppressWarnings("unchecked")
	public String getJSONString(){
		JSONObject mainJSON = new JSONObject();
		
		mainJSON.put("theme", currentTheme.getJSONObject());
		
		return mainJSON.toJSONString();
	}
}
