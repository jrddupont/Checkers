package client;
import java.io.IOException;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.json.simple.JSONObject;

import gui.GamePanel.GameBoardUI;
import util.Board;
import util.GameState;
import util.NetworkedPlayer;
import util.Netwrk;
import util.Player;
import util.PlayerDisconnectException;

public class ClientPlayer extends NetworkedPlayer implements Runnable{
	
	public GameBoardUI gameBoardUI = null;
	
	public GameState gameState = new GameState();
	private final Object lock = new Object();
	Board returnBoard;
	
	public String name = null;
	
	JSONObject out = new JSONObject();
	
	public ClientPlayer()
	{
		try {
			socket = new Socket(Netwrk.IP_ADDRESS, port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ClientPlayer(String name, String password, int id)
	{
		String newPass="";
		try {
			socket = new Socket(Netwrk.IP_ADDRESS, port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			MessageDigest hashObj = MessageDigest.getInstance("SHA-256");
			hashObj.update((name+password).getBytes());
			newPass = new String(hashObj.digest());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.put(Netwrk.OPCODE, Netwrk.HELLO);
		out.put(Netwrk.GAME_ID, id);
		out.put(Netwrk.USER_NAME, name);
		out.put(Netwrk.PASSWORD, newPass.substring(0, 32));
		
		this.sendPacket(out);
	}
	
	public void go() {
		for(;;) {
			try {
				processPacket(getMail());
			} catch (PlayerDisconnectException e) {
				e.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void processPacket(JSONObject json) {
		switch(((Long) json.get(Netwrk.OPCODE)).byteValue())
		{
		case Netwrk.HELLO:			
			if(((Long) json.get(Netwrk.PORT)).intValue() > 0)
			{
				try {
					socket.close();
					port = ((Long) json.get(Netwrk.PORT)).intValue();
					
					socket = new Socket(Netwrk.IP_ADDRESS, port);
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.printf("Accepted into a game\n");
			}
			else System.out.printf("Tried to join a full game\n");
			break;
		
		case Netwrk.EXIT:
			gameState.endStatus = gameState.EXIT_REQUESTED;	
			break;
			
		case Netwrk.GAME_START:		
			rematch=false;
			gameState.playerOneUserName = json.get(Netwrk.PLAYER_ONE_UNAME).toString();
			gameState.playerTwoUserName = json.get(Netwrk.PLAYER_TWO_UNAME).toString();
			gameState.playerOneWins = ((Long) json.get(Netwrk.PLAYER_ONE_WINS)).intValue();
			gameState.playerTwoWins = ((Long) json.get(Netwrk.PLAYER_TWO_WINS)).intValue();
			gameState.playerOneLosses = ((Long) json.get(Netwrk.PLAYER_ONE_LOSSES)).intValue();
			gameState.playerTwoLosses = ((Long) json.get(Netwrk.PLAYER_TWO_LOSSES)).intValue();
			gameState.playerOneTies = ((Long) json.get(Netwrk.PLAYER_ONE_TIES)).intValue();
			gameState.playerTwoTies = ((Long) json.get(Netwrk.PLAYER_TWO_TIES)).intValue();
			gameState.gameID = ((Long) json.get(Netwrk.GAME_ID)).intValue();			
			gameState.board.getBoard()[0] = ((Long) json.get(Netwrk.PLAYER_ONE_BOARD)).intValue();
			gameState.board.getBoard()[1] = ((Long) json.get(Netwrk.PLAYER_TWO_BOARD)).intValue();
			gameState.board.getBoard()[2] = ((Long) json.get(Netwrk.KINGS_BOARD)).intValue();
			System.out.printf("Joined game %d\n", gameState.gameID);
			System.out.println("Game started!");
			break;
			
		case Netwrk.GAME_END:
			gameState.endStatus = gameState.GAME_ENDED;			
			
			gameState.playerOneUserName = json.get(Netwrk.PLAYER_ONE_UNAME).toString();
			gameState.playerTwoUserName = json.get(Netwrk.PLAYER_TWO_UNAME).toString();
			gameState.playerOneWins = ((Long) json.get(Netwrk.PLAYER_ONE_WINS)).intValue();
			gameState.playerTwoWins = ((Long) json.get(Netwrk.PLAYER_TWO_WINS)).intValue();
			gameState.playerOneLosses = ((Long) json.get(Netwrk.PLAYER_ONE_LOSSES)).intValue();
			gameState.playerTwoLosses = ((Long) json.get(Netwrk.PLAYER_TWO_LOSSES)).intValue();
			gameState.playerOneTies = ((Long) json.get(Netwrk.PLAYER_ONE_TIES)).intValue();
			gameState.playerTwoTies = ((Long) json.get(Netwrk.PLAYER_TWO_TIES)).intValue();
			gameState.gameID = ((Long) json.get(Netwrk.GAME_ID)).intValue();			
			gameState.board.getBoard()[0] = ((Long) json.get(Netwrk.PLAYER_ONE_BOARD)).intValue();
			gameState.board.getBoard()[1] = ((Long) json.get(Netwrk.PLAYER_TWO_BOARD)).intValue();
			gameState.board.getBoard()[2] = ((Long) json.get(Netwrk.KINGS_BOARD)).intValue();
			
			gameEnd(gameState);
			
			break;
			
		case Netwrk.REMATCH_REQUEST:
			gameState.endStatus++;
			break;
			
		case Netwrk.MOVE_REQUEST:
			gameState.board.getBoard()[0] = ((Long) json.get(Netwrk.PLAYER_ONE_BOARD)).intValue();
			gameState.board.getBoard()[1] = ((Long) json.get(Netwrk.PLAYER_TWO_BOARD)).intValue();
			gameState.board.getBoard()[2] = ((Long) json.get(Netwrk.KINGS_BOARD)).intValue();
			
			Board moveBoard = gameState.board;
			
			try
			{
				moveBoard = getMove(gameState.board);
			} catch (PlayerDisconnectException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			JSONObject out = new JSONObject();			
			out.put(Netwrk.OPCODE, Netwrk.MOVE_REQUEST);
			out.put(Netwrk.PLAYER_ONE_BOARD, moveBoard.getBoard()[0]);
			out.put(Netwrk.PLAYER_TWO_BOARD, moveBoard.getBoard()[1]);
			out.put(Netwrk.KINGS_BOARD, moveBoard.getBoard()[2]);
			sendPacket(out);
			break;
		}
	}

	@Override
	public Board getMove(Board board) throws PlayerDisconnectException{
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

	@Override
	public void run()
	{
		try
		{
			processPacket(getMail());
			System.out.printf("got my packet 2\n");
		} catch (PlayerDisconnectException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // get a game state
		
		System.out.printf("username: %s\n", gameState.playerOneUserName);
		System.out.printf("name %s\n", name);
		
		if(gameState.playerOneUserName.equals(name)) {
	    	gameState.PlayerOne = this;
	    } else {
	    	gameState.PlayerTwo = this;
	    	playerNumber = 1;
	    }
		
		go();
	}

	@Override
	public void gameEnd(GameState g) {		
		if(g.PlayerOne == null) {
			if(g.turn == 0)
				;// you lost
			else
				;//you win
		} else {
			if(g.turn == 0)
				;// you lost
			else
				;//you win
		}
	}
}









