package client;

import gui.AbstractMenuPanel;
import gui.MenuPanel;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

@SuppressWarnings("serial")
public class Driver extends JFrame {
    public static void main(String[] args){
    	new Driver();
    }
    
    public Driver(){
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(AbstractMenuPanel.size);
		AbstractMenuPanel currentPanel = new MenuPanel(this);
		getContentPane().add(currentPanel);
		setVisible(true);
    }
    
    public void switchMenu(AbstractMenuPanel panel){
    	getContentPane().removeAll();
    	
    	getContentPane().add(panel);
    	
    	getContentPane().revalidate();
    	getContentPane().repaint();
    }
}