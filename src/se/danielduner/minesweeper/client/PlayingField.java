package se.danielduner.minesweeper.client;

import se.danielduner.minesweeper.client.AIPointer.MoveCallback;
import se.danielduner.minesweeper.client.MineSweeperAI.GameStatus;
import se.danielduner.minesweeper.client.event.AIModeEvent;
import se.danielduner.minesweeper.client.event.AIModeEvent.AIMode;
import se.danielduner.minesweeper.client.event.AIModeHandler;
import se.danielduner.minesweeper.client.event.GameStatusEvent;
import se.danielduner.minesweeper.client.event.GameStatusHandler;
import se.danielduner.minesweeper.client.event.SquareClickEvent;
import se.danielduner.minesweeper.client.event.SquareClickHandler;
import se.danielduner.minesweeper.client.event.SquareUpdateEvent;
import se.danielduner.minesweeper.client.event.SquareUpdateHandler;
import se.danielduner.minesweeper.client.resources.Images;

import com.google.gwt.animation.client.AnimationScheduler;
import com.google.gwt.animation.client.AnimationScheduler.AnimationCallback;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Widget;

public class PlayingField extends Composite
implements AnimationCallback, SquareClickHandler, SquareUpdateHandler, GameStatusHandler, AIModeHandler {
	
	public enum ClickType {
		LEFTCLICK, RIGHTCLICK
	}
	
	private EventBus eventBus;
	private Images images;
	private AbsolutePanel absolutePanel;
	private RestartButton restartButton;
	
	private Grid grid;
	private Square[][] squares;
	
	private MineField mineField;
	private StupidAI ai;
	private AIPointer aiPointer;
	private int aiPointerParkingX=262, aiPointerParkingY=64;
	private AIButton aiButton;
	private boolean isAnimating;
	
	public PlayingField(EventBus eventBus, Images images) {
		this.eventBus = eventBus;
		this.images = images;
		absolutePanel = new AbsolutePanel();
		
		restartButton = new RestartButton(eventBus, images);
		absolutePanel.add(restartButton);
		aiButton = new AIButton(eventBus);
		absolutePanel.add(aiButton);
		makeNewGame(16, 16, 40);
		initWidget(absolutePanel);
		
		aiPointer = new AIPointer(images, aiPointerParkingX, aiPointerParkingY);
		absolutePanel.add(aiPointer, aiPointer.getX(), aiPointer.getY());
		absolutePanel.setStyleName("PlayingField");
		
		this.addDomHandler(new ContextMenuHandler() {
			@Override public void onContextMenu(ContextMenuEvent event) {
				event.preventDefault();
				event.stopPropagation();
			}
		}, ContextMenuEvent.getType());
		
		SquareClickEvent.register(eventBus, this);
		SquareUpdateEvent.register(eventBus, this);
		GameStatusEvent.register(eventBus, this);
		AIModeEvent.register(eventBus, this);
	}
	
	@Override
	public void onSquareUpdate(SquareUpdateEvent event) {
		int x = event.getX();
		int y = event.getY();
		squares[x][y].setType(mineField.getValue(x, y));
	}
	
	@Override
	public void onGameStatusUpdate(GameStatusEvent event) {
		if (event.getGameStatus() == GameStatus.RESTART) {
			makeNewGame(16, 16, 40);
		}
	}
	
	@Override
	public void onSquareClick(SquareClickEvent event) {
		mineField.click(event.getX(), event.getY(), event.getClickType());
	}
	
	private void makeNewGame(int width, int height, int mines) {
		MineField lastMineField = mineField;
		mineField = new MineField(eventBus, width, height, mines);
		if (grid==null || lastMineField==null || lastMineField.getWidth()!=width || lastMineField.getHeight()!=height) {
			squares = new Square[width][height];
			if (grid!=null) {
				grid.remove(grid);
			}
			grid = new Grid(width, height);
			for(int y=0; y<grid.getRowCount(); y++) {
				for(int x=0; x<grid.getColumnCount(); x++) {
					Square square = new Square(eventBus, images, x, y, mineField.getValue(x, y));
					grid.setWidget(y, x, square);
					squares[x][y] = square;
				}
			}
		} else {
			for(int y=0; y<grid.getRowCount(); y++) {
				for(int x=0; x<grid.getColumnCount(); x++) {
					if (lastMineField.getValue(x, y) != mineField.getValue(x, y)){
						squares[x][y].setType(MineField.HIDDEN);
					}
				}
			}
		}
		ai = new StupidAI(mineField);
		int pixelWidth = mineField.getWidth()*36;
		int pixelHeight = 105 + mineField.getHeight()*36;
		absolutePanel.setSize(pixelWidth+"px", pixelHeight+"px");
		absolutePanel.add(grid, 0, 95);
		absolutePanel.setWidgetPosition(restartButton, pixelWidth/2-27, 0);
		absolutePanel.setWidgetPosition(aiButton, pixelWidth/2-35, 60);
	}
	
	public void clickGridButton(final int x, final int y, final ClickType clickType) {
		if (aiButton.getAIMode()==AIMode.OFF){
			return;
		}
		Widget square = grid.getWidget(y, x);
		int xpixel = square.getAbsoluteLeft() + (int)(0.4*square.getOffsetWidth()) - absolutePanel.getAbsoluteLeft();
		int ypixel = square.getAbsoluteTop() + (int)(0.5*square.getOffsetHeight()) - absolutePanel.getAbsoluteTop();
		aiPointer.movePointer(xpixel, ypixel, new MoveCallback() {
			@Override
			public void onMoveComplete() {
				mineField.click(x, y, clickType);
				aiDoNext();
			}
		});
		isAnimating = true;
		requestAnimationFrame();
	}
	
	public void clickRestartButton() {
		if (aiButton.getAIMode()==AIMode.OFF){
			return;
		}
		int xpixel = restartButton.getAbsoluteLeft() + restartButton.getOffsetWidth()/2 - absolutePanel.getAbsoluteLeft();
		int ypixel = restartButton.getAbsoluteTop() + restartButton.getOffsetHeight()/2 - absolutePanel.getAbsoluteTop();
		aiPointer.movePointer(xpixel, ypixel, new MoveCallback() {
			@Override
			public void onMoveComplete() {
				restartButton.leftclick();
				aiDoNext();
			}
		});
		isAnimating = true;
		requestAnimationFrame();
	}
	
	public void parkAIPointer() {
		aiPointer.movePointer(aiPointerParkingX, aiPointerParkingY, new MoveCallback() {
			@Override
			public void onMoveComplete() {
				aiButton.setAIMode(AIMode.OFF);
			}
		});
		isAnimating = true;
		requestAnimationFrame();
	}
	
	private void requestAnimationFrame() {
		AnimationScheduler.get().requestAnimationFrame(this, aiPointer.getElement());
	}

	@Override
	public void execute(double timestamp) {
		if (isAnimating) {
			if (aiPointer.hasArrived()) {
				isAnimating = false;
			} else {
				aiPointer.updatePosition(timestamp);
				absolutePanel.setWidgetPosition(aiPointer, aiPointer.getX(), aiPointer.getY());
				if (!aiPointer.hasArrived()) {
					requestAnimationFrame();
				}
			}
		}
	}

	@Override
	public void onAIModeUpdate(AIModeEvent event) {
		switch (event.getAIStatus()) {
		case ON:
			if (mineField.getGameStatus()!=GameStatus.PLAYING) {
				mineField.setRestart();
			}
			aiDoNext();
			break;
		case OFF:
			parkAIPointer();
			break;
		}
	}
	
	public void aiDoNext() {
		if (aiButton.getAIMode() == AIMode.ON) {
			Timer t = new Timer() {
				@Override
				public void run() {
					switch (mineField.getGameStatus()) {
					case PLAYING:
						ai.updateSuggestion();
						clickGridButton(ai.getX(), ai.getY(), ai.getClickType());
						break;
					case WON:
						parkAIPointer();
						break;
					default:
						clickRestartButton();
					}
				}
			};
			t.schedule(300);
		}
	}
}
