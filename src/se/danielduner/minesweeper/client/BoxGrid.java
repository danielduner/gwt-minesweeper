package se.danielduner.minesweeper.client;

import com.google.gwt.animation.client.AnimationScheduler;
import com.google.gwt.animation.client.AnimationScheduler.AnimationCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;

public class BoxGrid extends Composite {
	private AbsolutePanel absolutePanel;
	private Grid grid;
	private MineField mineField;
	
	private Label pointer;
	private int pointerX = 50, pointerY = 50;

	public BoxGrid(MineField mineField) {
		this.mineField = mineField;
		
		grid = new Grid(mineField.getWidth(), mineField.getHeight());
		for(int row=0; row<grid.getRowCount(); row++) {
			for(int column=0; column<grid.getColumnCount(); column++) {
				grid.setWidget(row, column, new Box(mineField.getValue(column, row)));
			}
		}
		
		pointer = new Label("<-");
		
		absolutePanel = new AbsolutePanel();
		absolutePanel.setSize((mineField.getWidth()*50)+"px", (mineField.getWidth()*50)+"px");
		absolutePanel.add(grid, 0, 0);
		absolutePanel.add(pointer, pointerX, pointerY);
		
		initWidget(absolutePanel);
	}
	
	public void movePointer(final int x, final int y) {
		//TODO
		AnimationScheduler scheduler = AnimationScheduler.get();
		AnimationCallback callback = new AnimationCallback() {
			@Override
			public void execute(double timestamp) {
				int dx = pointerX - x;
				int dy = pointerY - y;
				pointerX += dx * timestamp / 1000.0;
				pointerY += dy * timestamp / 1000.0;
			}
		};
		scheduler.requestAnimationFrame(callback, pointer.getElement());
	}
	
	public void update() {
		for(int row=0; row<grid.getRowCount(); row++) {
			for(int column=0; column<grid.getColumnCount(); column++) {
				((Box)grid.getWidget(row, column)).setType(mineField.getValue(column, row));
			}
		}
	}
}
