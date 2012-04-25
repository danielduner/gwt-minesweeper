package se.danielduner.minesweeper.client;

import se.danielduner.minesweeper.client.event.AIModeEvent;
import se.danielduner.minesweeper.client.event.AIModeEvent.AIMode;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;

public class AIButton extends Composite implements ClickHandler {
	private EventBus eventBus;
	private AbsolutePanel absolutePanel = new AbsolutePanel();
	private Label button = new Label("AI");
	private AIMode aiMode = AIMode.OFF;
	
	public AIButton(final EventBus eventBus) {
		this.eventBus = eventBus;
		absolutePanel.add(button);
		initWidget(absolutePanel);
		button.addClickHandler(this);
		button.setStyleName("AIButton");
	}
	
	@Override
	public void onClick(ClickEvent event) {
		if (event.getNativeButton() == NativeEvent.BUTTON_LEFT) {
			leftclick();
		}
	}
	
	public void leftclick() {
		switch (aiMode) {
		case ON:
			aiMode = AIMode.OFF;
			eventBus.fireEvent(new AIModeEvent(AIMode.OFF));
			break;
		case OFF:
			aiMode = AIMode.ON;
			eventBus.fireEvent(new AIModeEvent(AIMode.ON));
			break;
		}
	}
	
	public AIMode getAIMode() {
		return aiMode;
	}
	
	public void setAIMode(AIMode aiMode) {
		this.aiMode = aiMode;
	}
}
