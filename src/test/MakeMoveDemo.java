package test;

import gui.AbstractMenuPanel;
import gui.GamePanel;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import util.Board;
import util.GameState;
import util.PlayerDisconnectException;
import util.Settings;
import client.HumanPlayer;

public class MakeMoveDemo {
	public static Settings settings = new Settings();

	public static void main(String[] args){
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
		
		while(true){
			try {
				gameState.PlayerOne.getMove(gameState.board);
				gameState.PlayerTwo.getMove(gameState.board);
			} catch (PlayerDisconnectException e) {
				e.printStackTrace();
			}
		}
	}
}
