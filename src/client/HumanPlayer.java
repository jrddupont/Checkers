package client;

import gui.GamePanel.GameBoardUI;
import util.Board;

public class HumanPlayer extends LocalPlayer{
	GameBoardUI gameBoardUI;
	Board returnBoard;
	
	public HumanPlayer(GameBoardUI ui){
		gameBoardUI = ui;
	}
	
	@Override
	public Board getMove(Board board) {		
		gameBoardUI.flagForMove(this);
		while(true){
			try { wait(); } catch (InterruptedException e) { }
			if(returnBoard != null){
				Board retBrd = returnBoard;
				returnBoard = null;
				return retBrd;
			}
		}
	}
	
	public void callback(Board returnBoard){
		this.returnBoard = returnBoard;
		notifyAll();
	}
}
