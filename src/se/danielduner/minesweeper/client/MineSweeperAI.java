package se.danielduner.minesweeper.client;

import java.util.Random;

import se.danielduner.minesweeper.client.event.SquareClickEvent;
import se.danielduner.minesweeper.client.event.SquareClickHandler;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class MineSweeperAI implements EntryPoint {
	private static final EventBus eventBus = new SimpleEventBus();
	private MineField mineField;
	private SquareGrid boxGrid;
	private Label label;
	Random random = new Random();
	StupidAI ai;
	
	public void onModuleLoad() {
		mineField = new MineField(16, 16, 40);
		boxGrid = new SquareGrid(eventBus, mineField);
		label = new Label("PLAYING");
		RootPanel.get("minefield").add(boxGrid);
		RootPanel.get("title").add(label);
		ai = new StupidAI(mineField);

		SquareClickEvent.register(eventBus, new SquareClickHandler() {
			@Override
			public void onSquareClick(SquareClickEvent event) {
				int x = event.getX();
				int y = event.getY();
				switch (event.getClickType()) {
				case LEFTCLICK:
					mineField.leftclick(x, y);
					break;
				case RIGHTCLICK:
					mineField.rightclick(x, y);
					break;
				}
				boxGrid.update();
			}
		});
		
		/*Timer timer = new Timer() {
			@Override
			public void run() {
				if (mineField.getGameStatus()==GameStatus.PLAYING) {
					ai.act();
					boxGrid.update();
					switch (mineField.getGameStatus()) {
					case PLAYING: label.setText("PLAYING"); break;
					case LOST: label.setText("LOST"); break;
					case WON: label.setText("WON"); break;
					}
				}
			}
		};
		timer.scheduleRepeating(1000);*/
	}
}
