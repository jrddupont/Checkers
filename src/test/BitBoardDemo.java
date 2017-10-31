package test;

public class BitBoardDemo {
	
	public static void main(String[] args) {
		int start0 	= 0b11111111111100000000000000000000;
		int start1 	= 0b00000000000000000000111111111111;
		int mask0 	= 0b00000111000001110000011100000111; //3, -5
		int mask1 	= 0b11100000111000001110000011100000; //5, -3
		int test1	= 0b00000000000001010000000000000000;
		
		shittyPrint(start0);
		System.out.println("----");
		shittyPrint(~start0 & start0 >>> 4);
		System.out.println("----");
		shittyPrint(~start0 & (start0 & mask0) >>> 3);
		System.out.println("----");
		shittyPrint(~start0 & (start0 & mask1) >>> 5);
		
		System.out.println("------\nOther Way:\n------");
		
		shittyPrint(start1);
		System.out.println("----");
		shittyPrint(~start1 & start1 << 4);
		System.out.println("----");
		shittyPrint(~start1 & (start1 & mask1) << 3);
		System.out.println("----");
		shittyPrint(~start1 & (start1 & mask0) << 5);
	}
	
	public static void shittyPrint(int board) {
		String boardStr = String.format("%32s", Integer.toBinaryString(board)).replace(' ', '0'); //string conversion cancer
		for (int row = 0; row < 8; row++) {
			System.out.println(boardStr.substring(row*4, row*4+4));
		}
	}
}