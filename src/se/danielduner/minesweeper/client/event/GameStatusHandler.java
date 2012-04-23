package se.danielduner.minesweeper.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;

public interface GameStatusHandler extends EventHandler {
	void onGameStatusUpdate(GameStatusEvent event);
}

interface HasGameStatusEvents {
	public HandlerRegistration addGameStatusHandler(GameStatusHandler handler);
}