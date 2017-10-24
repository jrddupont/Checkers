package client;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class GUI extends JFrame {
    public static void main(String[] args){
    	new GUI();
    }
    
    public GUI(){
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(300, 500);
		MenuPanel menu = new MenuPanel();
		add(menu);
		setVisible(true);
    }
}