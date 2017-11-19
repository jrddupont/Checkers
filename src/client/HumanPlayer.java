package client;

import gui.GamePanel.GameBoardUI;
import util.Board;

public class HumanPlayer extends LocalPlayer{
	GameBoardUI gameBoardUI;
	Board returnBoard;
	private final Object lock = new Object();
	
	public HumanPlayer(GameBoardUI ui){
		gameBoardUI = ui;
	}
	
	@Override
	public Board getMove(Board board) {		
		gameBoardUI.flagForMove(this, board);
		while(true){
			try {
				synchronized(lock) {
					lock.wait();	
				}
			} catch (InterruptedException e) { 
				e.printStackTrace();
			}
			
			if(returnBoard != null){
				Board retBrd = returnBoard;
				returnBoard = null;
				return retBrd;
			}
		}
	}
	
	public void notifyPlayer(Board returnBoard){
		this.returnBoard = returnBoard;
		synchronized(lock) {
			lock.notifyAll();	
		}
	}
}
