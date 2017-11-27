package test;

import gui.AbstractMenuPanel;
import gui.GamePanel;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import util.Board;
import util.GameState;
import client.DumbAIPlayer;
import client.HumanPlayer;

public class MakeMoveDemo {
	public static void main(String[] args){
		JFrame mainFrame = new JFrame();  

		GameState gameState = new GameState();
		gameState.PlayerOne = new HumanPlayer(gameState);
		gameState.PlayerTwo = new DumbAIPlayer(Board.PLAYER_2);
		gameState.PlayerOne.playerNumber = Board.PLAYER_1;
		gameState.PlayerOne.playerNumber = Board.PLAYER_2;

		GamePanel gp = new GamePanel(gameState);

		mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		mainFrame.setSize(AbstractMenuPanel.size);
		mainFrame.getContentPane().add(gp);
		mainFrame.setVisible(true);

	}
}
