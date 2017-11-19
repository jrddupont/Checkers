package test;

import gui.AbstractMenuPanel;
import gui.GamePanel;

import java.util.Random;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import util.Board;
import util.Player;
import util.Settings;
import client.HumanPlayer;

public class GetMoveTest {
	public static Settings settings = new Settings();
	public static void main(String[] args){
		Random r = new Random();
		Board b = new Board();
		b.getBoard()[0] = r.nextInt() >>> 16;
		b.getBoard()[1] = r.nextInt() << 16;
		
		JFrame mainFrame = new JFrame();
		mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    	mainFrame.setSize(AbstractMenuPanel.size);
		GamePanel currentPanel = new GamePanel();
		mainFrame.getContentPane().add(currentPanel);
		mainFrame.setVisible(true);
		
		HumanPlayer player1 = new HumanPlayer(currentPanel.gameBoard);
		player1.playerNumber = Board.PLAYER_1;
		HumanPlayer player2 = new HumanPlayer(currentPanel.gameBoard);
		player2.playerNumber = Board.PLAYER_2;
		Player[] players = {player1, player2};
		int curPlayer = 0;
		
		while(true){
			System.out.println(players[curPlayer].getMove(b));
			curPlayer = curPlayer == 0 ? 1 : 0;
		}
		
	}
}
