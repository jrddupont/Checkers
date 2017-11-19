package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.ProcessBuilder.Redirect;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JComponent;

import util.Board;
import util.Game;
import util.GameState;
import util.Player;
import util.Settings;
import client.Driver;
import client.DumbAIPlayer;
import client.HumanPlayer;



@SuppressWarnings("serial")
public class GamePanel extends AbstractMenuPanel{
	
	public GameBoardUI gameBoard;
	public Player player1;
	public Player player2;
	public GameState gameState = new GameState();
	
	public GamePanel(){
		super();
		gameBoard = new GameBoardUI(gameState);
		
		player1 = new HumanPlayer(gameBoard);
		player2 = new DumbAIPlayer(Board.PLAYER_2);
		gameState.PlayerOne = player1;
		gameState.PlayerTwo = player2;
		
		JButton mainMenuButton = new JButton("Main menu");
		add(gameBoard);
		add(mainMenuButton);
		
		mainMenuButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Driver.switchMenu(new MenuPanel());
			}
		});
	}
	
	public class GameBoardUI extends JComponent{
		
		private boolean hasDesiredMoves = false;	// If the player clicked on a piece that has moves
		private int desiredMoves = 0;	// Mask of possible moves of the currently clicked piece
		private boolean isJumping = false; // In a state of jumping, IE has jumped and needs to jump again
		
		private boolean waitingForMove = false; // If we should listen for moves
		private HumanPlayer playerToNotify = null;	// What player to notify of changes
		
		GameState gameState;
		
		public GameBoardUI(GameState gs){
			gameState = gs;
			Random r = new Random();
			gameState.board = new Board();
			gameState.board.getBoard()[0] = r.nextInt() >>> 16;
			gameState.board.getBoard()[1] = r.nextInt() << 16;
			
			BoardMouseListener bml = new BoardMouseListener();
			addMouseListener(bml);
			addMouseMotionListener(bml);
			requestFocus();
		}
		
		@Override
		public void paint(Graphics g){
			int boardSize = Math.min(getWidth(), getHeight());
			int cellSize = boardSize / 8;
			boardSize = cellSize * 8;
			g.setColor(Settings.currentTheme.lightBoardColor);
			g.fillRect(0, 0, boardSize, boardSize);
			
			for(int i = 0; i < 8; i++){
				for(int j = 0; j < 8; j++){
					g.setColor(Settings.currentTheme.darkBoardColor);
					boolean inFill = false;
					if(i % 2 == 0){
						if(j % 2 == 1){
							g.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
							inFill = true;
						}
					}else{
						if(j % 2 == 0){
							g.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
							inFill = true;
						}
					}
					if(inFill){
						int x = i % 2 == 0 ? i/2 : (i-1)/2;
						int curPos = toNum(x, j);
						
						int currentDrawX = i*cellSize;
						int currentDrawY = j*cellSize;
						
						if(gameState.board.hasPlayerAt(Board.PLAYER_1, curPos)){	// If the current position has a player 1 piece
							g.setColor(Settings.currentTheme.player1Color);
						}else if(gameState.board.hasPlayerAt(Board.PLAYER_2, curPos)){	// If the current position has a player 2 piece
							g.setColor(Settings.currentTheme.player2Color);
						}
						if(getBit( gameState.board.getMoveMask(player), curPos)){
							
						}
						g.fillOval(currentDrawX, currentDrawY, cellSize, cellSize);
						
						if(gameState.board.hasKingAt(curPos)){	// If the current position has a king piece
							g.setColor(Settings.currentTheme.kingColor);
							g.fillOval(currentDrawX + cellSize/4, currentDrawY + cellSize/4, cellSize/2, cellSize/2);
						}
						if(hasDesiredMoves && getBit(desiredMoves, curPos) == 1){	// If the current position has a desired move
							g.setColor(Color.GREEN);
							Graphics2D g2 = (Graphics2D) g;
							int strokeSize = 5;
			                g2.setStroke(new BasicStroke(strokeSize));
							g2.drawOval(currentDrawX + strokeSize/2, currentDrawY + strokeSize/2, cellSize - strokeSize, cellSize - strokeSize);
						}
						
						// Draw the position number
						g.setColor(Color.WHITE);
						g.drawString(curPos+"", currentDrawX + cellSize / 2, currentDrawY + cellSize / 2);
					}
				}
			}
		}
		int getBit(int mask, int position){
			return (mask >> position) & 1;
		}
		
		
		public void flagForMove(HumanPlayer player, Board b) {
			waitingForMove = true;
			playerToNotify = player;
			gameState.board = b;
			repaint();
		}
		
		private void makeMove(Board b){
			if(waitingForMove){
				playerToNotify.notifyPlayer(b);
			}
			waitingForMove = false;
			playerToNotify = null;
		}
		
		private int toNum(int x, int y){
			return (y*4) + x;
		}
		
		class BoardMouseListener extends MouseAdapter {
			@Override 
			public void mouseClicked(MouseEvent e) {
				GameBoardUI boardGUI = (GameBoardUI)e.getSource();
				if(waitingForMove && e.getButton() == MouseEvent.BUTTON1){
					int x = e.getX();
					int y = e.getY();
					int position = getPosition(x, y,  Math.min(boardGUI.getWidth(), boardGUI.getHeight()));
					
					if(hasDesiredMoves){
						if(((1 << position) & desiredMoves) > 0){
							
							//gameState.board.setBoard(movePiece(gameState.board.getBoard(), boardGUI.moveFrom, position));
							makeMove(gameState.board);
						}
					}
					if(position == -1){
						boardGUI.hasDesiredMoves = false;
					}else{
						boardGUI.hasDesiredMoves = true;
						boardGUI.desiredMoves = gameState.board.getMoves(playerToNotify.playerNumber, position);
					}
					boardGUI.repaint();
				}
			}
			
			private int getPosition(int x, int y, int size){
				if(x > size || y > size || x < 0 || y < 0){
					return -1;
				}
				int smallX = x / (size/8);
				int smallY = y / (size/8);
				if(smallY % 2 == 0){
					if((smallX - 1) % 2 == 0){
						return (smallY * 4) + ((smallX - 1) / 2);
					}else{
						return -1;
					}
				}else{
					if(smallX % 2 == 0){
						return (smallY * 4) + (smallX  / 2);
					}else{
						return -1;
					}
				}
			}
		}
		
	}
}
