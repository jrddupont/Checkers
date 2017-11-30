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
				boolean isXEven = x%2 == 0;
				boolean isYEven = y%2 == 0;
				if(x == 0 && y == 0){
					output += "╔";
				}else if(x == 16 && y == 16){
					output += "╝";
				}else if(x == 16 && y == 0){
					output += "╗";
				}else if(x == 0 && y == 16){
					output += "╚";
				}else if(y % 2 == 0){
					if(x % 2 == 0){
						if(y == 0){
							output += "╦";
						}else if(y == 16){
							output += "╩";
						}else if(x == 0){
							output += "╠";
						}else if(x == 16){
							output += "╣";
						}else{
							output += "╬";
						}
					}else{
						output += "═══";
					}
				}else{
					if(x % 2 == 0){
						output += "║";
					}else{
						if((x/2) % 2 == 0){
							if((y/2) % 2 == 1){
								output += " ░ ";
							}else{
								output += "   ";
							}
						}else{
							if((y/2) % 2 == 0){
								output += " ░ ";
							}else{
								output += "   ";
							}
						}
					}
				}
			}
			output += "\n";
		}
		return output;
	}
}
