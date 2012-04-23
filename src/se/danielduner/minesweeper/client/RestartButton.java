package se.danielduner.minesweeper.client;

import se.danielduner.minesweeper.client.MineSweeperAI.GameStatus;
import se.danielduner.minesweeper.client.event.GameStatusEvent;
import se.danielduner.minesweeper.client.event.GameStatusHandler;
import se.danielduner.minesweeper.client.resources.Images;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

public class RestartButton extends Composite implements GameStatusHandler, ClickHandler {
	private EventBus eventBus;
	private AbsolutePanel absolutePanel = new AbsolutePanel();
	private Label button = new Label("");
	private Image currentImage;
	private Image playingImage, winImage, lostImage; 
	
	public RestartButton(final EventBus eventBus, Images images) {
		this.eventBus = eventBus;
		playingImage = new Image(images.facePlaying());
		winImage = new Image(images.faceWin());
		lostImage = new Image(images.faceLost());
		absolutePanel.add(button);
		setImage(playingImage);
		initWidget(absolutePanel);
		button.setStyleName("RestartButton");
		
		GameStatusEvent.register(eventBus, this);
		
		button.addClickHandler(this);
	}
	
	private void setImage(Image image) {
		if (currentImage!=null) {
			absolutePanel.remove(currentImage);
		}
		currentImage = image;
		absolutePanel.add(image, 8, 8);
		image.addStyleName("RestartButton-image");
	}

	@Override
	public void onGameStatusUpdate(GameStatusEvent event) {
		switch (event.getGameStatus()) {
		case PLAYING:
			setImage(playingImage);
			break;
		case LOST:
			setImage(lostImage);
			break;
		case WON:
			setImage(winImage);
			break;
		}
	}
	
	@Override
	public void onClick(ClickEvent event) {
		eventBus.fireEvent(new GameStatusEvent(GameStatus.RESTART));
	}
	
	public void leftclick() {
		eventBus.fireEvent(new GameStatusEvent(GameStatus.RESTART));
	}
}
