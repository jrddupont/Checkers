package gui;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;

import client.Driver;
import util.Settings;



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
		
		int[] gameState;
		public GameBoardUI(){
			gameState = new int[3];
		}
		
		public void setGameState(int[] gameState) {
			this.gameState = gameState;
			repaint();
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
						
						if(getBit(gameState[MASK_P1], curPos) == 1){
							g.setColor(Settings.currentTheme.player1Color);
							g.fillOval(i*cellSize, j*cellSize, cellSize, cellSize);
						}else if(getBit(gameState[MASK_P2], curPos) == 1){
							g.setColor(Settings.currentTheme.player2Color);
							g.fillOval(i*cellSize, j*cellSize, cellSize, cellSize);
						}
						if(getBit(gameState[MASK_KING], curPos) == 1){
							g.setColor(Settings.currentTheme.kingColor);
							g.fillOval(i*cellSize + cellSize/4, j*cellSize + cellSize/4, cellSize/2, cellSize/2);
						}
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
	}
}
