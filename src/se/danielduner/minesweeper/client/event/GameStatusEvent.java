package se.danielduner.minesweeper.client.event;

import se.danielduner.minesweeper.client.MineSweeperAI.GameStatus;

import com.google.web.bindery.event.shared.Event;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class GameStatusEvent extends Event<GameStatusHandler> {
	public static final Type<GameStatusHandler> TYPE = new Type<GameStatusHandler>();

	private GameStatus gameStatus;
	public GameStatusEvent(GameStatus gameStatus) {
		this.gameStatus = gameStatus;
	}

	@Override
	public Event.Type<GameStatusHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(GameStatusHandler handler) {
		handler.onGameStatusUpdate(this);
	}

	public static HandlerRegistration register(EventBus eventBus, GameStatusHandler gameStatusHandler) {
		return eventBus.addHandler(TYPE, gameStatusHandler);
	}

	public GameStatus getGameStatus() {
		return gameStatus;
	}
}
