package se.danielduner.minesweeper.client;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import se.danielduner.minesweeper.client.MineSweeperAI.GameStatus;
import se.danielduner.minesweeper.client.PlayingField.ClickType;

public class StupidAI {
	private MineField field;
	private int nextX=5, nextY=4;
	private ClickType clickType = null;
	private static Random random = new Random();
	
	public StupidAI(MineField minefield) {
		field = minefield;
	}
	
	public void updateSuggestion() {
		if (field.getGameStatus()!=GameStatus.PLAYING) {
			return;
		}
		Queue<Coordinate> coordinateQueue = new LinkedList<Coordinate>();
		Coordinate stupidGuess = null;
		int width = field.getWidth();
		int height = field.getHeight();
		boolean[][] explored = new boolean[width][height];
		coordinateQueue.add(new Coordinate(nextX, nextY));
		explored[nextX][nextY] = true;
		while (!coordinateQueue.isEmpty()) {
			Coordinate coordinate = coordinateQueue.poll();
			if (stupidGuess==null && field.getValue(coordinate.x, coordinate.y)==MineField.HIDDEN) {
				stupidGuess = coordinate;
			}
			
			int x = coordinate.x;
			int y = coordinate.y;
			int hiddenSum = field.getHiddenNeighbours(x, y);
			int flaggedSum = field.getFlaggedNeighbours(x, y);
			
			if (flaggedSum!=field.getValue(x, y) && hiddenSum==field.getValue(x, y)) {
				for(int yd=y-1; yd<=y+1; yd++) {
					for(int xd=x-1; xd<=x+1; xd++) {
						if (!(yd==y && xd==x) && field.inBounds(xd, yd) && field.getValue(xd, yd)==MineField.HIDDEN) {
							nextX = xd;
							nextY = yd;
							clickType = ClickType.RIGHTCLICK;
							return;
						}
					}
				}
			}
			
			if (hiddenSum>0 && flaggedSum==field.getValue(x, y)) {
				for(int yd=y-1; yd<=y+1; yd++) {
					for(int xd=x-1; xd<=x+1; xd++) {
						if (!(yd==y && xd==x) && field.inBounds(xd, yd) && field.getValue(xd, yd)==MineField.HIDDEN) {
							nextX = xd;
							nextY = yd;
							clickType = ClickType.LEFTCLICK;
							return;
						}
					}
				}
			} 
			
			List<Coordinate> neighbours = new LinkedList<Coordinate>();
			if(x+1<field.getWidth() && !explored[x+1][y]) {
				neighbours.add(new Coordinate(x+1, y));
				explored[x+1][y] = true;
			}
			if(y+1<field.getHeight() && !explored[x][y+1]) {
				neighbours.add(new Coordinate(x, y+1));
				explored[x][y+1] = true;
			}
			if(x-1>=0 && !explored[x-1][y]){
				neighbours.add(new Coordinate(x-1, y));
				explored[x-1][y] = true;
			}
			if(y-1>=0 && !explored[x][y-1]) {
				neighbours.add(new Coordinate(x, y-1));
				explored[x][y-1] = true;
			}
			shuffle(neighbours);
			coordinateQueue.addAll(neighbours);
		}
		
		nextX = stupidGuess.x;
		nextY = stupidGuess.y;
		clickType = ClickType.LEFTCLICK;
		return;
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
	
	private class Coordinate {
		int x, y;
		public Coordinate(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	
	private static <E> void shuffle(List<E> list) {
	    for (int i = list.size(); i > 1; i--) {
	    	int j = random.nextInt(i);
	    	E tmp = list.get(i-1);
		    list.set(i-1, list.get(j));
		    list.set(j, tmp);
	    }
	}
}
