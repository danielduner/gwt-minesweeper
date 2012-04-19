package se.danielduner.minesweeper.client;

import java.util.Random;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class MineSweeperAI implements EntryPoint {
	private MineField mineField;
	private BoxGrid boxGrid;
	private Label label;
	Random random = new Random();
	StupidAI ai;
	
	public void onModuleLoad() {
		mineField = new MineField(16, 16, 40);
		boxGrid = new BoxGrid(mineField);
		label = new Label("PLAYING");
		RootPanel.get("minefield").add(boxGrid);
		RootPanel.get("title").add(label);
		ai = new StupidAI(mineField);
		Timer timer = new Timer() {
			@Override
			public void run() {
				ai.act();
				boxGrid.update();
				switch (mineField.getGameStatus()) {
				case PLAYING: label.setText("PLAYING"); break;
				case LOST: label.setText("LOST"); break;
				case WON: label.setText("WON"); break;
				}
			}
		};
		timer.scheduleRepeating(1000);
	}
}