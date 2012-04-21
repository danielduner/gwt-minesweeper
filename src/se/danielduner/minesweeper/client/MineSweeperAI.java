package se.danielduner.minesweeper.client;

import java.util.Random;

import se.danielduner.minesweeper.client.resources.Images;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
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
		Images images = GWT.create(Images.class);
		mineField = new MineField(eventBus, 16, 16, 40);
		boxGrid = new SquareGrid(eventBus, images, mineField);
		label = new Label("PLAYING");
		RootPanel.get("minefield").add(boxGrid);
		RootPanel.get("title").add(label);
		ai = new StupidAI(mineField);

		
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
