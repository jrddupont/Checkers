package client;

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
import javax.swing.JPanel;

public class MenuPanel extends JPanel{
	JButton localPlayButton = new JButton("Local Play");
	JButton onlinePlayButton = new JButton("Online Play");
	JButton optionsButton = new JButton("Options");
	JButton rulesButton = new JButton("Rules");
	
	public MenuPanel(){
		setPreferredSize(new Dimension(300, 600));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));	// Set the panel to a vertical layout
		
		localPlayButton.setAlignmentX(Component.CENTER_ALIGNMENT);	// Center align the labels
		onlinePlayButton.setAlignmentX(Component.CENTER_ALIGNMENT);	// Center align the labels
		optionsButton.setAlignmentX(Component.CENTER_ALIGNMENT);	// Center align the labels
		rulesButton.setAlignmentX(Component.CENTER_ALIGNMENT);	// Center align the labels
		
		add( Box.createVerticalGlue() );	// Add the labels to the panel and align them horizontally (The glue objects take up space)
		add(localPlayButton);
		add(onlinePlayButton);
		add(optionsButton);
		add(rulesButton);
		add( Box.createVerticalGlue() );
		
		
		localPlayButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});
		onlinePlayButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});
		optionsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

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
