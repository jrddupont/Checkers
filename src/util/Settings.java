package util;

import java.awt.Color;

public class Settings {
	
	public static final Color defaultLightBoardColor =	new Color(255, 255, 255);
	public static final Color defaultDarkBoardColor = 	new Color( 85, 107,  47);
	public static final Color defaultPlayer1Color =		new Color(  0,   0,   0);
	public static final Color defaultPlayer2Color =		new Color(255,   0,   0);
	public static final Color defaultKingColor = 		new Color(212, 175,  55);
	
	
	public Color lightBoardColor;
	public Color darkBoardColor;
	public Color player1Color;
	public Color player2Color;
	public Color kingColor;
	
	public Settings(){
		lightBoardColor = defaultLightBoardColor;
		darkBoardColor = defaultDarkBoardColor;
		player1Color = defaultPlayer1Color;
		player2Color = defaultPlayer2Color;
		kingColor = defaultKingColor;
	}
	
	public String getJSONString(){
		return "";
	}
	public void readJSONString(){

	}
}
