package se.danielduner.minesweeper.client;

import java.util.Random;

import com.sun.org.apache.bcel.internal.generic.RETURN;

public class MineField {
	
	public enum GameStatus {
		PLAYING, LOST, WON
	}
	public static int HIDDEN=-1, MINE=-2, FLAGGED=-3;
	private boolean[][] mines;
	private boolean[][] flagged;
	private boolean[][] exposed;
	private int[][] neighbourCount;
	
	private int width;
	private int height;
	private int mineCount;
	private int exposedCount = 0;
	
	private GameStatus gameStatus = GameStatus.PLAYING;
	
	public MineField(int width, int height, int mineCount) {
		this.width = width;
		this.height = height;
		this.mineCount = mineCount;
		mines = new boolean[width][height];
		flagged = new boolean[width][height];
		exposed = new boolean[width][height];
		neighbourCount = new int[width][height];
		
		int minesPlaced = 0;
		Random random = new Random();
		while (minesPlaced<mineCount) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			if (!mines[x][y]) {
				mines[x][y] = true;
				minesPlaced++;
			}
		}
		
		for(int y=0; y<height; y++) {
			for(int x=0; x<width; x++) {
				if(mines[x][y]){
					for(int yd=-1; yd<=1; yd++) {
						for(int xd=-1; xd<=1; xd++) {
							if (!(yd==0 && xd==0) && y+yd>=0 && y+yd<height && x+xd>=0 && x+xd<width) {
								neighbourCount[x+xd][y+yd]++;
							}
						}
					}
				}
			}
		}
	}
	
	public int getValue(int x, int y) {
		if (gameStatus!=GameStatus.PLAYING && mines[x][y]) {
			return MINE;
		} if(flagged[x][y]) {
			return FLAGGED;
		}if(!exposed[x][y]){
			return HIDDEN;
		} else if (mines[x][y]) {
			return MINE;
		} else {
			return neighbourCount[x][y];
		}
	}
	
	public void expose(int x, int y){
		if(!exposed[x][y]){
			exposed[x][y] = true;
			if (flagged[x][y]) {
				unflag(x, y);
			}
			if (mines[x][y]) {
				gameStatus = GameStatus.LOST;
			}
			if (!mines[x][y] && neighbourCount[x][y]==0) {
				for(int yd=-1; yd<=1; yd++) {
					for(int xd=-1; xd<=1; xd++) {
						if (y+yd>=0 && y+yd<height && x+xd>=0 && x+xd<width && !exposed[x+xd][y+yd]) {
							expose(x+xd, y+yd);
						}
					}
				}
			}
			exposedCount ++;
			if (width*height-exposedCount == mineCount && gameStatus==GameStatus.PLAYING) {
				gameStatus = GameStatus.WON;
			}
		}
	}
	
	public void flag(int x, int y) {
		if(!exposed[x][y]){
			flagged[x][y] = true;
		}
	}
	
	public void unflag(int x, int y) {
		if(!flagged[x][y]){
			flagged[x][y] = false;
		}
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getMineCount() {
		return mineCount;
	}
	
	public GameStatus getGameStatus() {
		return gameStatus; 
	}
}
