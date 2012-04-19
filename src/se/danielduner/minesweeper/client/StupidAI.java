package se.danielduner.minesweeper.client;

import java.util.Random;

import se.danielduner.minesweeper.client.MineField.GameStatus;

public class StupidAI {
	private MineField mf;
	private Random random = new Random();
	
	public StupidAI(MineField minefield) {
		this.mf = minefield;
	}
		
	public void act() {
		if (mf.getGameStatus() == GameStatus.PLAYING) {
			int x = random.nextInt(mf.getWidth());
			int y = random.nextInt(mf.getHeight());
			if (mf.getValue(x, y)==MineField.HIDDEN) {
				mf.expose(x, y);
				return;
			}
		}
	}
}
