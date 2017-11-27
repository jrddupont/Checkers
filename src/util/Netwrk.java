package util;

public class Netwrk
{
	// opcodes
	final static public byte HELLO = 0;
	final static public byte EXIT = 1;
	final static public byte AUNTENTICATE = 2;
	final static public byte GAME_START = 3;
	final static public byte GAME_END = 4;
	final static public byte REMATCH_REQUEST = 5;
	final static public byte MOVE_REQUEST = 6;
	final static public byte SERVER_LIST_REQUEST = 7;
	
	// JSON field names
	final static public String OPCODE = "opcode";
	final static public String USER_NAME = "user_name";
	final static public String PASSWORD = "password";
	final static public String GAME_ID = "game_id";;
	final static public String PORT = "port";
	final static public String PLAYER_ONE_UNAME = "player_one_user_name";
	final static public String PLAYER_ONE_WINS = "player_one_wins";
	final static public String PLAYER_ONE_LOSSES = "player_one_losses";
	final static public String PLAYER_ONE_TIES = "player_one_ties";
	final static public String PLAYER_TWO_UNAME = "player_two_user_name";
	final static public String PLAYER_TWO_WINS = "player_two_wins";
	final static public String PLAYER_TWO_LOSSES = "player_two_losses";
	final static public String PLAYER_TWO_TIES = "player_two_ties";
	final static public String PLAYER_ONE_BOARD = "red_board";
	final static public String PLAYER_TWO_BOARD = "black_board";
	final static public String KINGS_BOARD = "kings_board";
	final static public String GAME_BOARD = "game_board";
}
