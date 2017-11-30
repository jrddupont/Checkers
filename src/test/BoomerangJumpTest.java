package test;

import util.Board;

public class BoomerangJumpTest {

	public static void main(String[] args) {
		int p1 = 0b0000000000000000000000000000001100000;
		int p2 = 0b0000000000000000000000000000100000000;


		Board boomerang = new Board(new int[] {p1, p2, 0});
		System.out.println(boomerang.getNextBoards(Board.PLAYER_2));
	}

}
