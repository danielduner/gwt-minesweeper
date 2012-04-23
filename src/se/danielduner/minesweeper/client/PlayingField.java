package se.danielduner.minesweeper.client;

import se.danielduner.minesweeper.client.AIPointer.MoveCallback;
import se.danielduner.minesweeper.client.MineSweeperAI.GameStatus;
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
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Widget;

public class PlayingField extends Composite implements AnimationCallback, SquareClickHandler, SquareUpdateHandler, GameStatusHandler {
	
	public enum ClickType {
		LEFTCLICK, RIGHTCLICK
	}
	
	private EventBus eventBus;
	private Images images;
	private AbsolutePanel absolutePanel;
	private RestartButton restartButton;
	private Grid grid;
	private MineField mineField;
	private StupidAI ai;
	private AIPointer aiPointer;
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
		
		aiPointer = new AIPointer(images, 262, 64);
		absolutePanel.add(aiPointer, aiPointer.getX(), aiPointer.getY());
		
		this.addDomHandler(new ContextMenuHandler() {
			@Override public void onContextMenu(ContextMenuEvent event) {
				event.preventDefault();
				event.stopPropagation();
			}
		}, ContextMenuEvent.getType());
		
		SquareClickEvent.register(eventBus, this);
		SquareUpdateEvent.register(eventBus, this);
		GameStatusEvent.register(eventBus, this);
	}
	
	@Override
	public void onSquareUpdate(SquareUpdateEvent event) {
		int x = event.getX();
		int y = event.getY();
		((Square)grid.getWidget(y, x)).setType(mineField.getValue(x, y));
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
			if (grid!=null) {
				grid.remove(grid);
			}
			grid = new Grid(width, height);
			for(int row=0; row<grid.getRowCount(); row++) {
				for(int column=0; column<grid.getColumnCount(); column++) {
					grid.setWidget(row, column, new Square(eventBus, images, column, row, mineField.getValue(column, row)));
				}
			}
		} else {
			for(int row=0; row<grid.getRowCount(); row++) {
				for(int column=0; column<grid.getColumnCount(); column++) {
					if (lastMineField.getValue(column, row) != mineField.getValue(column, row)){
						((Square)grid.getWidget(row, column)).setType(MineField.HIDDEN);
					}
				}
			}
		}
		ai = new StupidAI(mineField);
		int pixelWidth = mineField.getWidth()*36;
		int pixelHeight = 95 + mineField.getHeight()*36;
		absolutePanel.setSize(pixelWidth+"px", pixelHeight+"px");
		absolutePanel.add(grid, 0, 95);
		absolutePanel.setWidgetPosition(restartButton, pixelWidth/2-27, 0);
		absolutePanel.setWidgetPosition(aiButton, pixelWidth/2-35, 60);
	}
	
	public void doAIMove() {
	}
	
	public void clickGridButton(final int x, final int y, final ClickType clickType) {
		Widget square = grid.getWidget(y, x);
		int xpixel = square.getAbsoluteLeft();
		int ypixel = square.getAbsoluteTop();
		aiPointer.movePointer(xpixel, ypixel, new MoveCallback() {
			@Override
			public void onMoveComplete() {
				mineField.click(x, y, clickType);
			}
		});
		isAnimating = true;
		requestAnimationFrame();
	}
	
	public void clickRestartButton() {
		int xpixel = restartButton.getAbsoluteLeft() + restartButton.getOffsetWidth()/2;
		int ypixel = restartButton.getAbsoluteTop() + restartButton.getOffsetHeight()/2;
		aiPointer.movePointer(xpixel, ypixel, new MoveCallback() {
			@Override
			public void onMoveComplete() {
				restartButton.leftclick();
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
	
	/*
		if (!isAnimating) {
			return;
		}
		private int pointerTargetSquareX, pointerTargetSquareY;
		private SquareClickType clickType;
		Widget widget = grid.getWidget(y, x);
		pointerTargetPixelX = widget.getAbsoluteLeft() + (int)(0.5*widget.getOffsetWidth()) - grid.getAbsoluteLeft();
		pointerTargetPixelY = widget.getAbsoluteTop() + (int)(0.5*widget.getOffsetHeight()) - grid.getAbsoluteTop();	
		
		if (isAnimating) {
			if(distanceLeft > 2) {
				AnimationScheduler.get().requestAnimationFrame(this, pointer.getElement());
			} else {
				isAnimating = false;
				switch (clickType) {
				case LEFTCLICK:
					mineField.leftclick(pointerTargetSquareX, pointerTargetSquareY);
					break;
				case RIGHTCLICK:
					mineField.rightclick(pointerTargetSquareX, pointerTargetSquareY);
					break;
				}
			}
		}
	}
	*/
}