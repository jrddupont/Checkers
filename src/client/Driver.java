package client;

import gui.AbstractMenuPanel;
import gui.MenuPanel;
import gui.Theme;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import util.Settings;

public class Driver  {
	// Makes a new driver and instantiates the static setting object
    public static void main(String[] args){
    	new Driver();
    	Settings.currentTheme = new Theme();
    }
    
    // Sets up the main frame
    public static JFrame mainFrame = new JFrame();
    public Driver(){
    	mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    	mainFrame.setSize(AbstractMenuPanel.size);
		AbstractMenuPanel currentPanel = new MenuPanel();
		mainFrame.getContentPane().add(currentPanel);
		mainFrame.setVisible(true);
    }
    
    /**
     * Given a panel, replace everything in frame with it.
     * @param panel What panel to switch to
     */
    public static void switchMenu(AbstractMenuPanel panel){
    	mainFrame.getContentPane().removeAll();
    	mainFrame.getContentPane().add(panel);
    	mainFrame.getContentPane().revalidate();
    	mainFrame.getContentPane().repaint();
    }
}