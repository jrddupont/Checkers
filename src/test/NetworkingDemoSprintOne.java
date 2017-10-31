package test;

import util.*;
import server.*;

import org.json.simple.JSONObject;

import client.*;

public class NetworkingDemoSprintOne {
	final protected static byte HELLO = 0;
	
	public static void main(String[] args) {
		ClientPlayer alice = new ClientPlayer();
		ClientPlayer bob = new ClientPlayer();
		ClientPlayer charles = new ClientPlayer();
		
		
		JSONObject out = new JSONObject();
		out.put("Opcode", HELLO);
		out.put("GameID", 3);
		
		out.put("Username", "alice");
		alice.sendPacket(out);
		
		out.put("Username", "bob");
		bob.sendPacket(out);
		
		out.put("Username", "charles");
		charles.sendPacket(out);
		
		alice.processPacket(alice.getMail());
		bob.processPacket(bob.getMail());
		
		charles.processPacket(charles.getMail());
		
		alice.processPacket(alice.getMail());
		bob.processPacket(bob.getMail());	
		
		out.put("GameID", 7);
		charles = new ClientPlayer();
		charles.sendPacket(out);
		
		out.put("Username", "david");
		ClientPlayer david = new ClientPlayer();
		david.sendPacket(out);
		
		charles.processPacket(charles.getMail());
		david.processPacket(david.getMail());
		
		charles.processPacket(charles.getMail());
		david.processPacket(david.getMail());		
		
	}
}