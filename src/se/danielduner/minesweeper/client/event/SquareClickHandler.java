package se.danielduner.minesweeper.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;

public interface SquareClickHandler extends EventHandler {
	void onSquareClick(SquareClickEvent event);
}

interface HasSquareClickEvents {
	public HandlerRegistration addSquareClickHandler(SquareClickHandler handler);
}