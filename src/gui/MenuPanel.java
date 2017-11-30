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
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

import util.Board;
import util.Game;
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
				String[] choices = { "Player vs Player", "Player vs AI" };
			    String input = (String) JOptionPane.showInputDialog(null, "How would you like to play?", "Local Play", JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
			    if(input.equals("Player vs Player")){
			    	startPVPGame();
			    }else{
			    	startPVEGame();
			    }
			}
		});
		onlinePlayButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ServerBrowserPanel sbp = new ServerBrowserPanel();
				Driver.switchMenu(sbp);
				sbp.init();
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
	
	public void startPVPGame(){
		JFrame mainFrame1 = new JFrame();  
		JFrame mainFrame2 = new JFrame();  
		
		GameState gameState = new GameState();
		gameState.PlayerOne = new HumanPlayer(gameState);
		gameState.PlayerTwo = new HumanPlayer(gameState);
		gameState.PlayerOne.playerNumber = Board.PLAYER_1;
		gameState.PlayerTwo.playerNumber = Board.PLAYER_2;

		GamePanel gp1 = new GamePanel(gameState, (HumanPlayer)gameState.PlayerOne);
		GamePanel gp2 = new GamePanel(gameState, (HumanPlayer)gameState.PlayerTwo);
		
		((HumanPlayer)(gameState.PlayerOne)).gameBoardUI = gp1.gameBoard;
		((HumanPlayer)(gameState.PlayerTwo)).gameBoardUI = gp2.gameBoard;
		
		mainFrame1.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		mainFrame1.setSize(AbstractMenuPanel.size);
		mainFrame1.getContentPane().add(gp1);
		mainFrame1.setVisible(true);
		
		mainFrame2.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		mainFrame2.setSize(AbstractMenuPanel.size);
		mainFrame2.getContentPane().add(gp2);
		mainFrame2.setVisible(true);
		
		Thread thread = new Thread(new Runnable() {
		    @Override
		    public void run() {
		    	Game game = new Game(gameState);
				game.start();        
		    }
		});
		thread.start();
	}
	
	public void startPVEGame(){
		
		GameState gameState = new GameState();
		gameState.PlayerOne = new HumanPlayer(gameState);
		gameState.PlayerTwo = new DumbAIPlayer(Board.PLAYER_2);
		gameState.PlayerOne.playerNumber = Board.PLAYER_1;
		gameState.PlayerTwo.playerNumber = Board.PLAYER_2;

		GamePanel gamePanel = new GamePanel(gameState, (HumanPlayer)gameState.PlayerOne);
		
		((HumanPlayer)(gameState.PlayerOne)).gameBoardUI = gamePanel.gameBoard;

		Driver.switchMenu(gamePanel);
		
		Thread thread = new Thread(new Runnable() {
		    @Override
		    public void run() {
		    	Game game = new Game(gameState);
				game.start();
		    }
		});
		thread.start();
	}
}
