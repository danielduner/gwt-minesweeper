package se.danielduner.minesweeper.client;

import java.util.Random;

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
				e.printStackTrace();
			}
		});
		
		Images images = GWT.create(Images.class);
		playingField = new PlayingField(eventBus, images);
		RootPanel.get("playingfield").add(playingField);
	}
}
