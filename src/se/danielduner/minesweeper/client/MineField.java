package se.danielduner.minesweeper.client;

import java.util.Random;

import se.danielduner.minesweeper.client.MineSweeperAI.GameStatus;
import se.danielduner.minesweeper.client.PlayingField.ClickType;
import se.danielduner.minesweeper.client.event.GameStatusEvent;
import se.danielduner.minesweeper.client.event.SquareClickEvent;
import se.danielduner.minesweeper.client.event.SquareUpdateEvent;

import com.google.gwt.event.shared.EventBus;

public class MineField {
	public static final int HIDDEN=-1, EXPLODEDMINE=-2, HIDDENMINE=-3, FLAGGEDMINE=-4, FLAGGED=-5, FLAGGEDBAD=-6;
	
	private EventBus eventBus;
	
	private boolean levelGenerated;
	private boolean[][] mines;
	private boolean[][] flagged;
	private boolean[][] exposed;
	private int explodedX = -1, explodedY = -1;
	private int[][] mineNeighbourCount;
	private int[][] hiddenNeighbourCount;
	private int[][] flaggedNeighbourCount;
	
	private int width;
	private int height;
	private int mineCount;
	private int exposedCount = 0;
	
	private GameStatus gameStatus;
	
	public MineField(EventBus eventBus, int width, int height, int mineCount) {
		this.eventBus = eventBus;
		this.width = width;
		this.height = height;
		this.mineCount = mineCount;
		
		levelGenerated = false;
		mines = new boolean[width][height];
		flagged = new boolean[width][height];
		exposed = new boolean[width][height];
		mineNeighbourCount = new int[width][height];
		hiddenNeighbourCount = new int[width][height];
		flaggedNeighbourCount = new int[width][height];
		setGameStatus(GameStatus.PLAYING);
	}
	
	public void generateLevel(int safeX, int safeY) {
		int minesPlaced = 0;
		Random random = new Random();
		while (minesPlaced<mineCount) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			if (!mines[x][y] && !(x==safeX && y==safeY)) {
				mines[x][y] = true;
				minesPlaced++;
			}
		}
		for(int y=0; y<height; y++) {
			for(int x=0; x<width; x++) {
				for(int yd=y-1; yd<=y+1; yd++) {
					for(int xd=x-1; xd<=x+1; xd++) {
						if (!(yd==y && xd==x) && inBounds(xd, yd)) {
							if(mines[x][y]){
								mineNeighbourCount[xd][yd]++;
							}
							hiddenNeighbourCount[xd][yd]++;
						}
					}
				}
			}
		}
	}
	
	public int getValue(int x, int y) {
		if (explodedX == x && explodedY == y)
			return EXPLODEDMINE;
		if (gameStatus!=GameStatus.PLAYING) {
			if (mines[x][y] && flagged[x][y])
				return FLAGGEDMINE;
			if (!mines[x][y] && flagged[x][y])
				return FLAGGEDBAD;
			if (mines[x][y])
				return HIDDENMINE;
		}
		if(flagged[x][y])
			return FLAGGED;
		if(!exposed[x][y])
			return HIDDEN;
		return mineNeighbourCount[x][y];
	}
	
	public void click(int x, int y, ClickType clickType) {
		switch (clickType) {
		case LEFTCLICK:
			leftclick(x, y);
			break;
		case RIGHTCLICK:
			rightclick(x, y);
			break;
		}
	}
	
	private void leftclick(int x, int y) {
		if (!levelGenerated) {
			generateLevel(x, y);
			levelGenerated = true;
		}
		if (gameStatus != GameStatus.PLAYING)
			return;
		if (flagged[x][y]) {
			unflag(x, y);
			return;
		}
		
		if (exposed[x][y]) {
			if (getValue(x, y) <= flaggedNeighbourCount[x][y]) {
				for(int yd=y-1; yd<=y+1; yd++) {
					for(int xd=x-1; xd<=x+1; xd++) {
						if (!(yd==y && xd==x) && inBounds(xd, yd) && getValue(xd, yd)==HIDDEN) {
							eventBus.fireEvent(new SquareClickEvent(ClickType.LEFTCLICK, xd, yd));
						}
					}
				}
			}
		} else {
			exposed[x][y] = true;
			exposedCount++;
			
			for(int yd=y-1; yd<=y+1; yd++) {
				for(int xd=x-1; xd<=x+1; xd++) {
					if (!(yd==y && xd==x) && inBounds(xd, yd)) {
						hiddenNeighbourCount[xd][yd]--;
					}
				}
			}
			
			if (mines[x][y]) {
				explodedX = x;
				explodedY = y;
				setGameStatus(GameStatus.LOST);
				return;
			}
			
			eventBus.fireEvent(new SquareUpdateEvent(x, y));
			
			if (mineNeighbourCount[x][y]==0) {
				for(int yd=y-1; yd<=y+1; yd++) {
					for(int xd=x-1; xd<=x+1; xd++) {
						if (!(yd==y && xd==x) && inBounds(xd, yd) && getValue(xd, yd)==HIDDEN) {
							eventBus.fireEvent(new SquareClickEvent(ClickType.LEFTCLICK, xd, yd));
						}
					}
				}
			}
			
			if (width*height-exposedCount == mineCount) {
				setGameStatus(GameStatus.WON);
			}
		}
	}
	
	private void rightclick(int x, int y) {
		if (gameStatus!=GameStatus.PLAYING || exposed[x][y]) {
			return;
		}
		if (!flagged[x][y]) {
			flag(x, y);
		} else {
			unflag(x, y);
		}
	}
	
	private void flag(int x, int y) {
		if(!flagged[x][y]) {
			flagged[x][y] = true;
			for(int yd=y-1; yd<=y+1; yd++) {
				for(int xd=x-1; xd<=x+1; xd++) {
					if (!(yd==y && xd==x) && inBounds(xd, yd)) {
						flaggedNeighbourCount[xd][yd]++;
					}
				}
			}
			eventBus.fireEvent(new SquareUpdateEvent(x, y));
		}
	}
	
	private void unflag(int x, int y) {
		if(flagged[x][y]){
			flagged[x][y] = false;
			for(int yd=y-1; yd<=y+1; yd++) {
				for(int xd=x-1; xd<=x+1; xd++) {
					if (!(yd==y && xd==x) && inBounds(xd, yd)) {
						flaggedNeighbourCount[xd][yd]--;
					}
				}
			}
			eventBus.fireEvent(new SquareUpdateEvent(x, y));
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
	
	public int getHiddenNeighbours(int x, int y){
		return hiddenNeighbourCount[x][y];
	}
	
	public int getFlaggedNeighbours(int x, int y){
		return flaggedNeighbourCount[x][y];
	}
	
	private void setGameStatus(GameStatus updatedGameStatus) {
		if (gameStatus != updatedGameStatus) {
			gameStatus = updatedGameStatus;
			eventBus.fireEvent(new GameStatusEvent(gameStatus));
			for (int xd=0; xd<width; xd++){
				for (int yd=0; yd<width; yd++){
					if(mines[xd][yd] || flagged[xd][yd]) {
						eventBus.fireEvent(new SquareUpdateEvent(xd, yd));
					}
				}
			}
		}
	}
	
	public boolean inBounds(int x, int y) {
		return y>=0 && y<height && x>=0	&& x<width;
	}

	public void setRestart() {
		gameStatus = GameStatus.RESTART;
	}
}
