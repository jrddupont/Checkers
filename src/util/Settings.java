package util;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.colorchooser.ColorSelectionModel;

import gui.Theme;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Settings {
	
	// Put information you want saved here
	public Theme currentTheme;
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
