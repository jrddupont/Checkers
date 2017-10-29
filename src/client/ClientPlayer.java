package client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import util.Board;
import util.NetworkedPlayer;

public class ClientPlayer extends NetworkedPlayer{
	
	public ClientPlayer()
	{
		try {
			socket = new Socket(serverIP, port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void processPacket(JSONObject json) {
		switch(((Long) json.get("Opcode")).byteValue())
		{
		case HELLO:
			if(((Long) json.get("Port")).intValue()>-1)
			{
				try {
					socket.close();
				
					socket = new Socket(serverIP, ((Long) json.get("Port")).intValue());
					port = ((Long) json.get("Port")).intValue();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			break;
		}
		
	}

	@Override
	public JSONObject getMail() {
		JSONParser parser = new JSONParser();
		JSONObject data=null;
		try {
			buffReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			data = (JSONObject) ((Object) parser.parse(buffReader.readLine()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return data;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void sendPacket(byte opCode, Object data) {
		JSONObject out = new JSONObject();
		
		out.put("Opcode", opCode);
		out.put("GameID", data);
		
		PrintWriter printer;
		try {
			printer = new PrintWriter(socket.getOutputStream(), true);
			printer.println(out.toJSONString());
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}

	@Override
	public Board getMove(Board board) {
		// TODO Auto-generated method stub
		return null;
	}
	public static void main(String args[])
	{
		ClientPlayer p = new ClientPlayer();
		System.out.println(p.socket.getPort());
		p.sendPacket(p.HELLO, -47);
		p.processPacket(p.getMail());
		System.out.println(p.socket.getPort());
		p.getMail();
		
	}

}
