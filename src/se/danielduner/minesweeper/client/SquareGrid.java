package se.danielduner.minesweeper.client;

import se.danielduner.minesweeper.client.event.SquareClickEvent;
import se.danielduner.minesweeper.client.event.SquareClickHandler;
import se.danielduner.minesweeper.client.event.SquareUpdateEvent;
import se.danielduner.minesweeper.client.event.SquareUpdateHandler;
import se.danielduner.minesweeper.client.event.SquareClickEvent.SquareClickType;
import se.danielduner.minesweeper.client.resources.Images;

import com.google.gwt.animation.client.AnimationScheduler;
import com.google.gwt.animation.client.AnimationScheduler.AnimationCallback;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class SquareGrid extends Composite implements AnimationCallback {
	private AbsolutePanel absolutePanel;
	private Grid grid;
	private MineField mineField;
	
	public SquareGrid(EventBus eventBus, Images images, MineField mineField) {
		this.mineField = mineField;
		grid = new Grid(mineField.getWidth(), mineField.getHeight());
		for(int row=0; row<grid.getRowCount(); row++) {
			for(int column=0; column<grid.getColumnCount(); column++) {
				grid.setWidget(row, column, new Square(eventBus, images, column, row, mineField.getValue(column, row)));
			}
		}
		
		pointer = new Image(images.arrowImage());
		absolutePanel = new AbsolutePanel();
		absolutePanel.setSize((mineField.getWidth()*36)+"px", (mineField.getWidth()*36)+"px");
		absolutePanel.add(grid, 0, 0);
		absolutePanel.add(pointer, (int)pointerX, (int)pointerY);
		
		initWidget(absolutePanel);
		
		this.addDomHandler(new ContextMenuHandler() {
			@Override public void onContextMenu(ContextMenuEvent event) {
				event.preventDefault();
				event.stopPropagation();
			}
		}, ContextMenuEvent.getType());
		
		SquareClickEvent.register(eventBus, new SquareClickHandler() {
			@Override
			public void onSquareClick(SquareClickEvent event) {
				int x = event.getX();
				int y = event.getY();
				switch (event.getClickType()) {
				case LEFTCLICK:
					SquareGrid.this.mineField.leftclick(x, y);
					break;
				case RIGHTCLICK:
					SquareGrid.this.mineField.rightclick(x, y);
					break;
				}
			}
		});
		
		SquareUpdateEvent.register(eventBus, new SquareUpdateHandler() {
			@Override
			public void onSquareUpdate(SquareUpdateEvent event) {
				int x = event.getX();
				int y = event.getY();
				((Square)grid.getWidget(y, x)).setType(SquareGrid.this.mineField.getValue(x, y));
			}
		});
	}
	
	private Image pointer;
	private double pointerX = 50, pointerY = 50;
	private double lastTimestamp;
	private boolean firstMove;
	private int pointerTargetSquareX, pointerTargetSquareY;
	private int pointerTargetPixelX, pointerTargetPixelY;
	private boolean isAnimating;
	SquareClickType clickType;
	public void pointerMoveClick(int x, int y, SquareClickType clickType) {
		this.clickType = clickType;
		firstMove = true;
		pointerTargetSquareX = x;
		pointerTargetSquareY = y;
		Widget widget = grid.getWidget(y, x);
		pointerTargetPixelX = widget.getAbsoluteLeft() + (int)(0.5*widget.getOffsetWidth()) - grid.getAbsoluteLeft();
		pointerTargetPixelY = widget.getAbsoluteTop() + (int)(0.5*widget.getOffsetHeight()) - grid.getAbsoluteTop();
		doAnimation(true);
	}
	
	public void doAnimation(boolean animate) {
		if (isAnimating != animate) {
			isAnimating = !isAnimating;
			if (isAnimating) {
				AnimationScheduler.get().requestAnimationFrame(this, pointer.getElement());
			}
		}
	}
	
	@Override
	public void execute(double timestamp) {
		if (!isAnimating) {
			return;
		}
		if (firstMove) {
			firstMove = false;
		} else {
			double timediff = timestamp - lastTimestamp;
			if (timediff>0) {
				double speed = 0.5 * timediff;
				if (speed > 10) {
					speed = 10;
				}
				double dx = pointerTargetPixelX - pointerX;
				double dy = pointerTargetPixelY - pointerY;
				double length = Math.sqrt(dx*dx + dy*dy);
				if (speed > length) {
					speed = length;
				}
				dx = speed * dx/length;
				dy = speed * dy/length;
				pointerX += dx;
				pointerY += dy;
				absolutePanel.setWidgetPosition(pointer, (int)pointerX, (int)pointerY);
			}
		}
		lastTimestamp = timestamp;
		double distanceLeft = Math.sqrt(Math.pow(pointerTargetPixelX - pointerX, 2)
							+ Math.pow(pointerTargetPixelY - pointerY, 2));
		if (isAnimating) {
			if(distanceLeft > 2) {
				AnimationScheduler.get().requestAnimationFrame(this, pointer.getElement());
			} else {
				isAnimating = false;
				switch (clickType) {
				case LEFTCLICK: mineField.leftclick(pointerTargetSquareX, pointerTargetSquareY); break;
				case RIGHTCLICK: mineField.rightclick(pointerTargetSquareX, pointerTargetSquareY); break;
				}
			}
		}
	}
}
