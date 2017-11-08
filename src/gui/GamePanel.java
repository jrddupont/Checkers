package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JComponent;

import util.Board;
import util.Settings;
import client.Driver;



@SuppressWarnings("serial")
public class GamePanel extends AbstractMenuPanel{
	
	public GameBoardUI gameBoard;
	
	public GamePanel(){
		super();
		JButton mainMenuButton = new JButton("Main menu");
		add(new GameBoardUI());
		add(mainMenuButton);
		
		mainMenuButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Driver.switchMenu(new MenuPanel());
			}
		});
	}
	
	public class GameBoardUI extends JComponent{
		
		public static final byte MASK_P1 = 0;
		public static final byte MASK_P2 = 1;
		public static final byte MASK_KING = 2;
		
		public boolean hasPossibleMoves = false;
		public int possibleMoves = 0; 
		
		Board board;
		public GameBoardUI(){
			Random r = new Random();
			board = new Board();
			board.board[0] = r.nextInt() >>> 16;
			board.board[1] = r.nextInt() << 16;
			
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
						
						if(getBit(board.board[MASK_P1], curPos) == 1){
							g.setColor(Settings.currentTheme.player1Color);
							g.fillOval(i*cellSize, j*cellSize, cellSize, cellSize);
						}else if(getBit(board.board[MASK_P2], curPos) == 1){
							g.setColor(Settings.currentTheme.player2Color);
							g.fillOval(i*cellSize, j*cellSize, cellSize, cellSize);
						}
						if(getBit(board.board[MASK_KING], curPos) == 1){
							g.setColor(Settings.currentTheme.kingColor);
							g.fillOval(i*cellSize + cellSize/4, j*cellSize + cellSize/4, cellSize/2, cellSize/2);
						}
						if(hasPossibleMoves && getBit(possibleMoves, curPos) == 1){
							g.setColor(Settings.currentTheme.darkBoardColor.brighter());
							g.fillOval(i*cellSize, j*cellSize, cellSize, cellSize);
						}
						g.setColor(Color.WHITE);
						g.drawString(curPos+"", i*cellSize + cellSize / 2, j*cellSize + cellSize / 2);
					}
				}
			}
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
					boardGUI.possibleMoves = board.getMoves(Board.PLAYER_1, position) | board.getMoves(Board.PLAYER_2, position);
					boardGUI.repaint();
				}
				
			}
			
			private int getPosition(int x, int y, int size){
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
