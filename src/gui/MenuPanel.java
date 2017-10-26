package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;

import util.Settings;
import client.Driver;

@SuppressWarnings("serial")
public class MenuPanel extends AbstractMenuPanel{
	JButton localPlayButton = new JButton("Local Play");
	JButton onlinePlayButton = new JButton("Online Play");
	JButton optionsButton = new JButton("Options");
	JButton rulesButton = new JButton("Rules");
	Driver driver;
	
	Settings settings = new Settings();
	
	public MenuPanel(Driver d){
		super();
		driver = d;
		
		JButton[] allButtons = {localPlayButton, onlinePlayButton, optionsButton, rulesButton};
		
		add( Box.createVerticalGlue() );	// Add the labels to the panel and align them horizontally (The glue objects take up space)
		
		for(JButton button : allButtons){
			button.setAlignmentX(Component.CENTER_ALIGNMENT);	// Center align the labels
			button.setMaximumSize(new Dimension(size.width / 2, button.getPreferredSize().height));
			add(button);
			add(Box.createRigidArea(new Dimension(0, padding)));
		}
		add( Box.createVerticalGlue() );
		
		
		
		
		localPlayButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		onlinePlayButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				driver.switchMenu(new ServerBrowserPanel());
			}
		});
		optionsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				driver.switchMenu(new SettingsPanel(settings));
			}
		});
		rulesButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					java.awt.Desktop.getDesktop().browse(new URI("https://simple.wikipedia.org/wiki/Checkers"));
				} catch (IOException | URISyntaxException e1) {}
			}
		});
	}
	
}
