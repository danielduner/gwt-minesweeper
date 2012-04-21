package se.danielduner.minesweeper.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;

public interface SquareUpdateHandler extends EventHandler {
	void onSquareUpdate(SquareUpdateEvent event);
}

interface HasSquareUpdateEvents {
	public HandlerRegistration addSquareUpdateHandler(SquareUpdateHandler handler);
}