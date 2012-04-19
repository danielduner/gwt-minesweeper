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
		if (mf.getGameStatus()!=GameStatus.PLAYING) {
			return;
		}
		int width = mf.getWidth();
		int height = mf.getHeight();
		int[][] hiddenNeighbours = new int[mf.getWidth()][mf.getHeight()];
		int[][] flaggedNeighbours = new int[mf.getWidth()][mf.getHeight()];
		for (int y=0; y<mf.getHeight(); y++) {
			for (int x=0; x<mf.getWidth(); x++) {
				for(int yd=-1; yd<=1; yd++) {
					for(int xd=-1; xd<=1; xd++) {
						if (!(yd==0 && xd==0) && y+yd>=0 && y+yd<height && x+xd>=0 && x+xd<width) {
							if(mf.getValue(x+xd, y+yd)==MineField.HIDDEN){
								hiddenNeighbours[x][y]++;
							} else if (mf.getValue(x+xd, y+yd)==MineField.FLAGGED) {
								flaggedNeighbours[x][y]++;
							}
						}
					}
				}
			}
		}
		
		for (int y=0; y<mf.getHeight(); y++) {
			for (int x=0; x<mf.getWidth(); x++) {
				int hiddenFlagSum = hiddenNeighbours[x][y]+flaggedNeighbours[x][y];
				if(flaggedNeighbours[x][y]>0 && flaggedNeighbours[x][y]==mf.getValue(x, y)) {
					for(int yd=-1; yd<=1; yd++) {
						for(int xd=-1; xd<=1; xd++) {
							if (!(yd==0 && xd==0) && y+yd>=0 && y+yd<height && x+xd>=0 && x+xd<width) {
								if(mf.getValue(x+xd, y+yd)==MineField.HIDDEN){
									mf.expose(x+xd, y+yd);
									return;
								}
							}
						}
					}
				} else if(hiddenFlagSum>0 && hiddenFlagSum==mf.getValue(x, y)) {
					for(int yd=-1; yd<=1; yd++) {
						for(int xd=-1; xd<=1; xd++) {
							if (!(yd==0 && xd==0) && y+yd>=0 && y+yd<height && x+xd>=0 && x+xd<width) {
								if(mf.getValue(x+xd, y+yd)==MineField.HIDDEN){
									mf.flag(x+xd, y+yd);
									return;
								}
							}
						}
					}
				}
			}
		}
		
		for (int y=0; y<mf.getHeight(); y++) {
			for (int x=0; x<mf.getWidth(); x++) {
				if(hiddenNeighbours[x][y]>0 && hiddenNeighbours[x][y]==mf.getValue(x, y)) {
					for(int yd=-1; yd<=1; yd++) {
						for(int xd=-1; xd<=1; xd++) {
							if (!(yd==0 && xd==0) && y+yd>=0 && y+yd<height && x+xd>=0 && x+xd<width) {
								if(mf.getValue(x+xd, y+yd)==MineField.HIDDEN){
									mf.flag(x+xd, y+yd);
									return;
								}
							}
						}
					}
				}
			}
		}
	
		int x = random.nextInt(mf.getWidth());
		int y = random.nextInt(mf.getHeight());
		if (mf.getValue(x, y)==MineField.HIDDEN) {
			mf.expose(x, y);
			return;
		}
	}
}
