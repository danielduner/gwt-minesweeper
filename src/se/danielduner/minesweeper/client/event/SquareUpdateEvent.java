package se.danielduner.minesweeper.client.event;

import com.google.web.bindery.event.shared.Event;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class SquareUpdateEvent extends Event<SquareUpdateHandler> {
	public static final Type<SquareUpdateHandler> TYPE = new Type<SquareUpdateHandler>();

	private int x, y;
	public SquareUpdateEvent(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public Event.Type<SquareUpdateHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SquareUpdateHandler handler) {
		handler.onSquareUpdate(this);
	}

	public static HandlerRegistration register(EventBus eventBus, SquareUpdateHandler squareClickHandler) {
		return eventBus.addHandler(TYPE, squareClickHandler);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
