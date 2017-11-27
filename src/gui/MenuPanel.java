package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.Box;
import javax.swing.JButton;

import util.Board;
import util.GameState;
import client.Driver;
import client.DumbAIPlayer;
import client.HumanPlayer;

@SuppressWarnings("serial")
public class MenuPanel extends AbstractMenuPanel{
	JButton localPlayButton = new JButton("Local Play");
	JButton onlinePlayButton = new JButton("Online Play");
	JButton optionsButton = new JButton("Options");
	JButton rulesButton = new JButton("Rules");
	
	
	public MenuPanel(){
		super();
		
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
				GameState gameState = new GameState();
				gameState.PlayerOne = new HumanPlayer(gameState);
				gameState.PlayerTwo = new DumbAIPlayer(Board.PLAYER_2);
				gameState.PlayerOne.playerNumber = Board.PLAYER_1;
				gameState.PlayerOne.playerNumber = Board.PLAYER_2;
				Driver.switchMenu(new GamePanel(gameState, (HumanPlayer)gameState.PlayerOne));
			}
		});
		onlinePlayButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Driver.switchMenu(new ServerBrowserPanel());
			}
		});
		optionsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Driver.switchMenu(new SettingsPanel());
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
