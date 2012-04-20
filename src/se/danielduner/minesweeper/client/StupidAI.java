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
		for (int y=0; y<mf.getHeight(); y++) {
			for (int x=0; x<mf.getWidth(); x++) {
				int hiddenSum = mf.getHiddenNeighbours(x, y);
				int flaggedSum = mf.getFlaggedNeighbours(x, y);
				if (hiddenSum>flaggedSum && hiddenSum==mf.getValue(x, y)) {
					for(int yd=y-1; yd<=y+1; yd++) {
						for(int xd=x-1; xd<=x+1; xd++) {
							if (!(yd==x && xd==y) && yd>=0 && yd<height && xd>=0 && xd<width
									&& mf.getValue(xd, yd)==MineField.HIDDEN) {
								mf.flag(xd, yd);
								return;
							}
						}
					}
				} else if (hiddenSum>0 && flaggedSum==mf.getValue(x, y)) {
					for(int yd=y-1; yd<=y+1; yd++) {
						for(int xd=x-1; xd<=x+1; xd++) {
							if (!(yd==x && xd==y) && yd>=0 && yd<height && xd>=0
									&& xd<width && mf.getValue(xd, yd)==MineField.HIDDEN) {
								mf.expose(xd, yd);
								return;
							}
						}
					}
				} 
			}
		}
		
		while (true) {
			int x = random.nextInt(mf.getWidth());
			int y = random.nextInt(mf.getHeight());
			if (mf.getValue(x, y)==MineField.HIDDEN) {
				mf.expose(x, y);
				return;
			}
		}
	}
}
