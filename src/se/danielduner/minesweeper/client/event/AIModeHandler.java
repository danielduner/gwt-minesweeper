package se.danielduner.minesweeper.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;

public interface AIModeHandler extends EventHandler {
	void onAIModeUpdate(AIModeEvent event);
}

interface HasAIModeEvents {
	public HandlerRegistration addAIModeHandler(AIModeHandler handler);
}