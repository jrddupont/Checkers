package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;

import org.omg.CosNaming.IstringHelper;

import util.Board;
import util.GameState;
import util.Player;
import util.Settings;
import client.ClientPlayer;
import client.Driver;
import client.DumbAIPlayer;
import client.HumanPlayer;



@SuppressWarnings("serial")
public class GamePanel extends AbstractMenuPanel{
	
	public GameBoardUI gameBoard;
	public Player currentPlayer;
	public GameState gameState = new GameState();
	
	public JLabel playerOneJLabel = new JLabel();
	public JLabel playerTwoJLabel = new JLabel();
	
	public GamePanel(GameState gameState, Player player){
		super();
		this.gameState = gameState;
		gameBoard = new GameBoardUI(gameState);
		currentPlayer = player;
		
		JButton mainMenuButton = new JButton("Main menu");
		
		add(playerOneJLabel);
		add(playerTwoJLabel);
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
		private Player playerToNotify = null;	// What player to notify of changes
		
		GameState gameState;
		
		public GameBoardUI(GameState gs){
			gameState = gs;
			
			BoardMouseListener bml = new BoardMouseListener();
			addMouseListener(bml);
			requestFocus();
		}
		
		@Override
		public void paint(Graphics g){
			
			if(gameState.PlayerOne instanceof ClientPlayer || gameState.PlayerTwo instanceof ClientPlayer){
				playerOneJLabel.setText(gameState.playerOneUserName + ": " + gameState.playerOneWins + "/" + gameState.playerOneLosses + "/" + gameState.playerOneTies);
				playerTwoJLabel.setText(gameState.playerTwoUserName + ": " + gameState.playerTwoWins + "/" + gameState.playerTwoLosses + "/" + gameState.playerTwoTies);
			}else{
				playerOneJLabel.setText("");
				playerTwoJLabel.setText("");
			}
			
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
								g.setColor(Settings.currentTheme.player1Color);
							}else{	// If the current position has a player 2 piece
								g.setColor(Settings.currentTheme.player2Color);
							}
							g.fillOval(currentDrawX, currentDrawY, cellSize, cellSize);
						}
						if(waitingForMove && playerAtCurPos == currentPlayer.playerNumber){
							if(getBit( gameState.board.getMovablePieces(currentPlayer.playerNumber), curPos) == 1){
								if(isJumping){
									if(curPos == selectedPiece){
										g.setColor(Color.GRAY);
										Graphics2D g2 = (Graphics2D) g;
										int strokeSize = 6;
						                g2.setStroke(new BasicStroke(strokeSize));
										g2.drawOval(currentDrawX + strokeSize/2, currentDrawY + strokeSize/2, cellSize - strokeSize, cellSize - strokeSize);
									}
								}else{
									g.setColor(Color.GRAY);
									Graphics2D g2 = (Graphics2D) g;
									int strokeSize = 6;
					                g2.setStroke(new BasicStroke(strokeSize));
									g2.drawOval(currentDrawX + strokeSize/2, currentDrawY + strokeSize/2, cellSize - strokeSize, cellSize - strokeSize);
								}
							}
						}
						
						if(gameState.board.hasKingAt(curPos)){	// If the current position has a king piece
							g.setColor(Settings.currentTheme.kingColor);
							g.fillOval(currentDrawX + cellSize/4, currentDrawY + cellSize/4, cellSize/2, cellSize/2);
						}
						if(waitingForMove && selectedPiece != -1){
							
							int playerAtSelectedPiece = gameState.board.playerAt(selectedPiece);
							int drawMask = gameState.board.getAllMoves(playerAtSelectedPiece, selectedPiece);
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
		
		
		public void flagForMove(Player player, Board b) {
			System.out.println("Player " + player.playerNumber + " waiting for turn");
			waitingForMove = true;
			selectedPiece = -1;
			playerToNotify = player;
			gameState.board = b;
			repaint();
		}
		
		
		private void makeMove(Board b){
			if(waitingForMove){
				System.out.println("Player " + playerToNotify.playerNumber + " made turn");
				
				if(playerToNotify instanceof HumanPlayer){
					((HumanPlayer) playerToNotify).notifyPlayer(b);
				}else{
					((ClientPlayer) playerToNotify).notifyPlayer(b);
				}
			}
			waitingForMove = false;
			playerToNotify = null;
			selectedPiece = -1;
			isJumping = false;
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
					
					// If they clicked on a selectable piece
					if(!isJumping && getBit( gameState.board.getMovablePieces(currentPlayer.playerNumber), position) == 1){
						selectedPiece = position;
					}else{
						if(selectedPiece != -1 && getBit(gameState.board.getAllMoves(currentPlayer.playerNumber, selectedPiece), position) == 1){
							int jumps = gameState.board.getJumps(currentPlayer.playerNumber, selectedPiece);
							if(jumps > 0){
								int possibleJumps = gameState.board.jumpAndGetJumps(selectedPiece, position);
								isJumping = true;
								System.out.println("Jump");
								System.out.println(Integer.toBinaryString(possibleJumps));
								if(possibleJumps == 0){
									makeMove(gameState.board);
									System.out.println("Made move");
								}
								selectedPiece = position;
							}else{
								gameState.board.moveTo(selectedPiece, position);
								makeMove(gameState.board);
							}
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
