package se.danielduner.minesweeper.client.event;

import com.google.web.bindery.event.shared.Event;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class AIModeEvent extends Event<AIModeHandler> {
	public static final Type<AIModeHandler> TYPE = new Type<AIModeHandler>();
	public enum AIMode {
		ON, OFF
	}
	
	private AIMode aiMode;
	public AIModeEvent(AIMode aiMode) {
		this.aiMode = aiMode;
	}

	@Override
	public Event.Type<AIModeHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(AIModeHandler handler) {
		handler.onAIModeUpdate(this);
	}

	public static HandlerRegistration register(EventBus eventBus, AIModeHandler aiModeHandler) {
		return eventBus.addHandler(TYPE, aiModeHandler);
	}

	public AIMode aiStatus() {
		return aiMode;
	}
}
