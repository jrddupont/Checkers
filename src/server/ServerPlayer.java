package server;

import java.net.Socket;

import util.Board;
import util.NetworkedPlayer;

public class ServerPlayer extends NetworkedPlayer {
	
	public ServerPlayer(Socket socket) {
		
	}

	@Override
	public void processPacket(String json) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getMail() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendPacket(byte opCode, String data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Board getMove(Board board) {
		// TODO Auto-generated method stub
		return null;
	}

}
