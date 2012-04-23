package se.danielduner.minesweeper.client;

import java.util.Random;

import se.danielduner.minesweeper.client.PlayingField.ClickType;
import se.danielduner.minesweeper.client.resources.Images;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.RootPanel;

public class MineSweeperAI implements EntryPoint {
	public enum GameStatus {
		RESTART, PLAYING, LOST, WON
	}

	private static final EventBus eventBus = new SimpleEventBus();
	private PlayingField playingField;
	Random random = new Random();
	
	public void onModuleLoad() {
		GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void onUncaughtException(Throwable e) {
				System.err.println("Uncaught Exception: " + e.toString());
			}
		});
		
		Images images = GWT.create(Images.class);
		playingField = new PlayingField(eventBus, images);
		RootPanel.get("playingfield").add(playingField);
		
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
		
		//playingField.clickGridButton(10, 5, ClickType.LEFTCLICK);
	}
}
