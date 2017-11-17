package test;

import gui.AbstractMenuPanel;
import gui.GamePanel;

import java.util.Random;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import util.Board;
import client.HumanPlayer;

public class GetMoveTest {
	public static void main(String[] args){
		Random r = new Random();
		Board b = new Board();
		b.board[0] = r.nextInt() >>> 16;
		b.board[1] = r.nextInt() << 16;
		
		JFrame mainFrame = new JFrame();
		mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    	mainFrame.setSize(AbstractMenuPanel.size);
		GamePanel currentPanel = new GamePanel();
		mainFrame.getContentPane().add(currentPanel);
		mainFrame.setVisible(true);
		
		HumanPlayer player = new HumanPlayer(currentPanel.gameBoard);
		System.out.println(player.getMove(b));
	}
}
