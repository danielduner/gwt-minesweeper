package se.danielduner.minesweeper.client;

import java.util.Random;

import se.danielduner.minesweeper.client.MineSweeperAI.GameStatus;
import se.danielduner.minesweeper.client.PlayingField.ClickType;

public class StupidAI {
	private MineField field;
	private Random random = new Random();
	private int nextX=-1, nextY=-1;
	private ClickType clickType = null;
	
	public StupidAI(MineField minefield) {
		field = minefield;
	}
		
	public void updateSuggestion() {
		if (field.getGameStatus()!=GameStatus.PLAYING) {
			return;
		}
		int width = field.getWidth();
		int height = field.getHeight();
		for (int y=0; y<field.getHeight(); y++) {
			for (int x=0; x<field.getWidth(); x++) {
				int hiddenSum = field.getHiddenNeighbours(x, y);
				int flaggedSum = field.getFlaggedNeighbours(x, y);
				if (flaggedSum!=field.getValue(x, y) && hiddenSum==field.getValue(x, y)) {
					for(int yd=y-1; yd<=y+1; yd++) {
						for(int xd=x-1; xd<=x+1; xd++) {
							if (!(yd==x && xd==y) && yd>=0 && yd<height && xd>=0 && xd<width
									&& field.getValue(xd, yd)==MineField.HIDDEN) {
								nextX = xd;
								nextY = yd;
								clickType = ClickType.LEFTCLICK;
								return;
							}
						}
					}
				} else if (hiddenSum>0 && flaggedSum==field.getValue(x, y)) {
					for(int yd=y-1; yd<=y+1; yd++) {
						for(int xd=x-1; xd<=x+1; xd++) {
							if (!(yd==x && xd==y) && yd>=0 && yd<height && xd>=0
									&& xd<width && field.getValue(xd, yd)==MineField.HIDDEN) {
								nextX = xd;
								nextY = yd;
								clickType = ClickType.RIGHTCLICK;
								return;
							}
						}
					}
				} 
			}
		}
		
		while (field.getGameStatus()==GameStatus.PLAYING) {
			int x = random.nextInt(field.getWidth());
			int y = random.nextInt(field.getHeight());
			if (field.getValue(x, y)==MineField.HIDDEN) {
				nextX = x;
				nextY = y;
				clickType = ClickType.LEFTCLICK;
				return;
			}
		}
	}
	
	public int getX() {
		return nextX;
	}
	
	public int getY() {
		return nextY;
	}
	
	public ClickType getClickType() {
		return clickType;
	}
}
