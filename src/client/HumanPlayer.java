package client;

import gui.GamePanel.GameBoardUI;
import util.Board;
import util.GameState;

public class HumanPlayer extends LocalPlayer{
	GameState gameState;
	public GameBoardUI gbui = null;
	Board returnBoard;
	private final Object lock = new Object();
	
	public HumanPlayer(GameState gs){
		gameState = gs;
	}
	
	@Override
	public Board getMove(Board board) {		
		gbui.flagForMove(this, board);
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
