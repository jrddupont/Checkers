package test;

import util.Board;

public class TBoard extends Board{
	public static void main(String[] args){
		new TBoard();
	}
	public TBoard(){
		super();
		System.out.println(this);
	}
	@Override
	public String toString() {
		String output = "";
		for(int y = 0; y < 17; y++){
			for(int x = 0; x < 17; x++){
				if(x == 0 && y == 0){
					output += "╔";
				}else if(x == 16 && y == 16){
					output += "╔";
				}else if(x == 16 && y == 0){
					output += "╔";
				}else if(x == 0 && y == 16){
					output += "╚";
				}else if(y % 2 == 0){
					output += "-";
				}else{
					output += "O";
				}
			}
			output += "\n";
		}
		return output;
	}
}
