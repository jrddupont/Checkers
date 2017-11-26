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
		
		private int selectedPiece = -1;
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
						
						int playerAtCurPos = gameState.board.playerAt(curPos);
						if(playerAtCurPos >= 0){
							if(playerAtCurPos == Board.PLAYER_1){	// If the current position has a player 1 piece
								if(getBit( gameState.board.getMovablePieces(Board.PLAYER_1), curPos) == 1){
									g.setColor(Settings.currentTheme.player1Color.brighter());
								}else{
									g.setColor(Settings.currentTheme.player1Color);
								}
							}else{	// If the current position has a player 2 piece
								if(getBit( gameState.board.getMovablePieces(Board.PLAYER_2), curPos) == 1){
									g.setColor(Settings.currentTheme.player2Color.brighter());
								}else{
									g.setColor(Settings.currentTheme.player2Color);
								}
							}
							g.fillOval(currentDrawX, currentDrawY, cellSize, cellSize);
						}
						
						if(gameState.board.hasKingAt(curPos)){	// If the current position has a king piece
							g.setColor(Settings.currentTheme.kingColor);
							g.fillOval(currentDrawX + cellSize/4, currentDrawY + cellSize/4, cellSize/2, cellSize/2);
						}
						if(selectedPiece != -1){
							int playerAtSelectedPiece = gameState.board.playerAt(selectedPiece);
							int drawMask = gameState.board.getJumps(playerAtSelectedPiece, selectedPiece);
							if(drawMask == 0){
								drawMask = gameState.board.getNonJumps(playerAtSelectedPiece, selectedPiece);
							}
							if(getBit(drawMask, curPos) == 1){
								g.setColor(Color.GREEN);
								Graphics2D g2 = (Graphics2D) g;
								int strokeSize = 5;
				                g2.setStroke(new BasicStroke(strokeSize));
								g2.drawOval(currentDrawX + strokeSize/2, currentDrawY + strokeSize/2, cellSize - strokeSize, cellSize - strokeSize);
							}
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
					
					if(waitingForMove){
						if(selectedPiece == -1){
							if(getBit( gameState.board.getMovablePieces(player1.playerNumber), position) == 1){
								
							}
						}else{
							
						}
						
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
