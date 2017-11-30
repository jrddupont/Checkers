package gui;

import java.awt.Color;

import org.json.simple.JSONObject;

public class Theme {
	public static final Color defaultLightBoardColor =	new Color(255, 255, 255);
	public static final Color defaultDarkBoardColor = 	new Color( 85, 107,  47);
	public static final Color defaultPlayer1Color =		new Color(  0,   0,   0);
	public static final Color defaultPlayer2Color =		new Color(255,   0,   0);
	public static final Color defaultKingColor = 		new Color(212, 175,  55);
	
	public String name;
	public Color lightBoardColor;
	public Color darkBoardColor;
	public Color player1Color;
	public Color player2Color;
	public Color kingColor;
	
	/**
	 * Creates a new theme with all default values.
	 */
	public Theme(){
		name = "Default";
		lightBoardColor = defaultLightBoardColor;
		darkBoardColor = defaultDarkBoardColor;
		player1Color = defaultPlayer1Color;
		player2Color = defaultPlayer2Color;
		kingColor = defaultKingColor;
	}
	
	/**
	 * Creates a new theme object with data from a JSON file
	 * @param json The JSON to load from
	 */
	public Theme(JSONObject json){
		name = (String) json.get("name");
		lightBoardColor = new Color(((Long) json.get("light")).intValue());
		darkBoardColor = new Color(((Long) json.get("dark")).intValue());
		player1Color = new Color(((Long) json.get("p1")).intValue());
		player2Color = new Color(((Long) json.get("p2")).intValue());
		kingColor = new Color(((Long) json.get("king")).intValue());
	}

	/**
	 * Duplicates a theme
	 * @param oldTheme Theme to duplicate
	 */
	public Theme(Theme oldTheme) {
		name = oldTheme.name;
		lightBoardColor = oldTheme.lightBoardColor;
		darkBoardColor = oldTheme.darkBoardColor;
		player1Color = oldTheme.player1Color;
		player2Color = oldTheme.player2Color;
		kingColor = oldTheme.kingColor;
	}

	/**
	 * Creates a JSON object based off the data in the current theme
	 * @return	JSON object of theme data
	 */
	@SuppressWarnings("unchecked")
	public JSONObject getJSONObject() {
		JSONObject output = new JSONObject();
		
		output.put("name", name);
		output.put("light", lightBoardColor.getRGB());
		output.put("dark", darkBoardColor.getRGB());
		output.put("p1", player1Color.getRGB());
		output.put("p2", player2Color.getRGB());
		output.put("king", kingColor.getRGB());
		
		return output;
	}
}
