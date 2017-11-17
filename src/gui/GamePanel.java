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
		
		private boolean hasPossibleMoves = false;
		private boolean hasDesiredMoves = false;
		private int possibleMoves = 0;
		private int desiredMoves = 0;
		private int moveFrom;
		
		private boolean waitingForMove = false; 
		private HumanPlayer callbackPlayer = null;
		
		GameState gameState;
		
		public GameBoardUI(GameState gs){
			gameState = gs;
			Random r = new Random();
			gameState.board = new Board();
			gameState.board.board[0] = r.nextInt() >>> 16;
			gameState.board.board[1] = r.nextInt() << 16;
			
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
						int[] gameBoard = gameState.board.board; 
						
						if(getBit(gameBoard[Board.PLAYER_1], curPos) == 1){
							g.setColor(Settings.currentTheme.player1Color);
							g.fillOval(i*cellSize, j*cellSize, cellSize, cellSize);
						}else if(getBit(gameBoard[Board.PLAYER_2], curPos) == 1){
							g.setColor(Settings.currentTheme.player2Color);
							g.fillOval(i*cellSize, j*cellSize, cellSize, cellSize);
						}
						if(getBit(gameBoard[Board.KING], curPos) == 1){
							g.setColor(Settings.currentTheme.kingColor);
							g.fillOval(i*cellSize + cellSize/4, j*cellSize + cellSize/4, cellSize/2, cellSize/2);
						}
						if(hasPossibleMoves && getBit(possibleMoves, curPos) == 1){
							g.setColor(Settings.currentTheme.darkBoardColor.brighter());
							g.fillOval(i*cellSize, j*cellSize, cellSize, cellSize);
						}
						if(hasDesiredMoves && getBit(desiredMoves, curPos) == 1){
							g.setColor(Color.GREEN);
							Graphics2D g2 = (Graphics2D) g;
							int strokeSize = 5;
			                g2.setStroke(new BasicStroke(strokeSize));
							g2.drawOval(i*cellSize + strokeSize/2, j*cellSize + strokeSize/2, cellSize - strokeSize, cellSize - strokeSize);
						}
						g.setColor(Color.WHITE);
						g.drawString(curPos+"", i*cellSize + cellSize / 2, j*cellSize + cellSize / 2);
					}
				}
			}
		}
		
		public void flagForMove(HumanPlayer player, Board b) {
			waitingForMove = true;
			callbackPlayer = player;
			gameState.board = b;
			repaint();
		}
		
		private void makeMove(Board b){
			if(waitingForMove){
				callbackPlayer.callback(b);
			}
			waitingForMove = false;
			callbackPlayer = null;
		}
		
		private int toNum(int x, int y){
			return (y*4) + x;
		}
		int getBit(int mask, int position){
			return (mask >> position) & 1;
		}
		
		class BoardMouseListener extends MouseAdapter {
			@Override 
			public void mouseClicked(MouseEvent e) {
				GameBoardUI boardGUI = (GameBoardUI)e.getSource();
				if(e.getButton() == MouseEvent.BUTTON1){
					int x = e.getX();
					int y = e.getY();
					int position = getPosition(x, y,  Math.min(boardGUI.getWidth(), boardGUI.getHeight()));
					
					if(waitingForMove && hasDesiredMoves){
						if(((1 << position) & desiredMoves) > 0){
							gameState.board.board = movePiece(gameState.board.board, boardGUI.moveFrom, position);
							makeMove(gameState.board);
						}
					}
					if(position == -1){
						boardGUI.hasDesiredMoves = false;
					}else{
						boardGUI.hasDesiredMoves = true;
						boardGUI.desiredMoves = gameState.board.getMoves(Board.PLAYER_1, position) | gameState.board.getMoves(Board.PLAYER_2, position);
						boardGUI.moveFrom = position;
					}
					boardGUI.repaint();
				}
			}
			@Override 
			public void mouseMoved(MouseEvent e) {
				GameBoardUI boardGUI = (GameBoardUI)e.getSource();
				int x = e.getX();
				int y = e.getY();
				
				int position = getPosition(x, y,  Math.min(boardGUI.getWidth(), boardGUI.getHeight()));
				
				if(position == -1){
					boardGUI.hasPossibleMoves = false;
					boardGUI.repaint();
				}else{
					boardGUI.hasPossibleMoves = true;
					boardGUI.possibleMoves = gameState.board.getMoves(Board.PLAYER_1, position) | gameState.board.getMoves(Board.PLAYER_2, position);
					boardGUI.repaint();
				}
				
			}
			
			private int[] movePiece(int[] boardInts, int from, int to){	// Does NOT validate move
				for(int i = 0; i < boardInts.length; i++){
					if(getBit(boardInts[i], from) != getBit(boardInts[i], to)){
						boardInts[i] = toggleBit(boardInts[i], from);
						boardInts[i] = toggleBit(boardInts[i], to);
					}
				}
				return boardInts;
			}
			private int toggleBit(int mask, int position){
				return mask ^ (1 << position);
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
