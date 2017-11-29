package test;

import util.*;
import server.*;

import org.json.simple.JSONObject;

import client.*;

public class NetworkingDemoSprintOne {
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws PlayerDisconnectException{
		ClientPlayer alice = new ClientPlayer();
		ClientPlayer bob = new ClientPlayer();
		ClientPlayer charles = new ClientPlayer();
		
		
		JSONObject out = new JSONObject();
		out.put(Netwrk.OPCODE, Netwrk.HELLO);
		out.put(Netwrk.GAME_ID, 3);
		
		out.put(Netwrk.USER_NAME, "alice");
		out.put(Netwrk.PASSWORD, "pizza");
		alice.sendPacket(out);
		
		out.put(Netwrk.USER_NAME, "bob");
		out.put(Netwrk.PASSWORD, "pizza");
		bob.sendPacket(out);
		
		out.put(Netwrk.USER_NAME, "charles");
		out.put(Netwrk.PASSWORD, "pizza");
		charles.sendPacket(out);
		
		alice.processPacket(alice.getMail());
		bob.processPacket(bob.getMail());
		
		charles.processPacket(charles.getMail());
		
		alice.processPacket(alice.getMail());
		bob.processPacket(bob.getMail());	
		
		out.put(Netwrk.GAME_ID, 7);
		charles = new ClientPlayer();
		charles.sendPacket(out);
		
		out.put(Netwrk.USER_NAME, "david");
		ClientPlayer david = new ClientPlayer();
		david.sendPacket(out);
		
		charles.processPacket(charles.getMail());
		david.processPacket(david.getMail());
		
		charles.processPacket(charles.getMail());
		david.processPacket(david.getMail());		
		
		out.put(Netwrk.GAME_ID, 7327);
		out.put(Netwrk.USER_NAME, "eli");
		ClientPlayer eli = new ClientPlayer();
		eli.sendPacket(out);
		
		eli.processPacket(eli.getMail());
		
	}
}