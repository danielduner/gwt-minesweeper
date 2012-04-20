package se.danielduner.minesweeper.client.event;

import com.google.web.bindery.event.shared.Event;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class SquareClickEvent extends Event<SquareClickHandler> {
	public static final Type<SquareClickHandler> TYPE = new Type<SquareClickHandler>();
	public enum SquareClickType {
		LEFTCLICK, RIGHTCLICK
	}

	private SquareClickType clickType;
	private int x, y;
	public SquareClickEvent(SquareClickType clickType, int x, int y) {
		this.clickType = clickType;
		this.x = x;
		this.y = y;
	}

	@Override
	public Event.Type<SquareClickHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SquareClickHandler handler) {
		handler.onSquareClick(this);
	}

	public static HandlerRegistration register(EventBus eventBus, SquareClickHandler squareClickHandler) {
		return eventBus.addHandler(TYPE, squareClickHandler);
	}

	public SquareClickType getClickType() {
		return clickType;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
