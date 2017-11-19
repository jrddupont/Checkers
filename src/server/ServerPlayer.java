package server;

import java.io.IOException;
import java.net.ServerSocket;

import org.json.simple.JSONObject;

import util.Board;
import util.NetworkedPlayer;
import util.Netwrk;

public class ServerPlayer extends NetworkedPlayer 
							implements Runnable{
	public Board board = new Board();
	ServerSocket ssocket;
	
	public ServerPlayer() {
		try {
			ssocket = new ServerSocket(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void processPacket(JSONObject json) {
		switch(((Long) json.get("Opcode")).byteValue())
		{
		case Netwrk.MOVE_REQUEST:
			board = (Board) json.get("Board");
			break;
		}	
	}

	@SuppressWarnings("unchecked")
	@Override
	public Board getMove(Board board) {
		JSONObject out = new JSONObject();
		out.put("Opcode", Netwrk.MOVE_REQUEST);
		out.put("Red", board.getBoard()[0]);
		out.put("Black", board.getBoard()[1]);
		out.put("King", board.getBoard()[2]);
		sendPacket(out);
		processPacket(getMail());
		return board;
	}
	
	public int getServerPort() {
		while(ssocket == null) System.out.printf("nullllll\n");
		
		int tempPort = ssocket.getLocalPort();
		return tempPort;
	}

	@Override
	public synchronized void run() {
		try {
			socket = ssocket.accept();
			
			notifyAll();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
