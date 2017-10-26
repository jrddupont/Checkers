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
	
	public Theme(){
		name = "Default";
		lightBoardColor = defaultLightBoardColor;
		darkBoardColor = defaultDarkBoardColor;
		player1Color = defaultPlayer1Color;
		player2Color = defaultPlayer2Color;
		kingColor = defaultKingColor;
	}
	
	public Theme(JSONObject json){
		name = (String) json.get("name");
		lightBoardColor = new Color(((Long) json.get("light")).intValue());
		darkBoardColor = new Color(((Long) json.get("dark")).intValue());
		player1Color = new Color(((Long) json.get("p1")).intValue());
		player2Color = new Color(((Long) json.get("p2")).intValue());
		kingColor = new Color(((Long) json.get("king")).intValue());
	}

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
