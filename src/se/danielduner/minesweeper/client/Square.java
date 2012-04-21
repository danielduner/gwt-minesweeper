package se.danielduner.minesweeper.client;

import se.danielduner.minesweeper.client.event.SquareClickEvent;
import se.danielduner.minesweeper.client.event.SquareClickEvent.SquareClickType;
import se.danielduner.minesweeper.client.resources.Images;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

public class Square extends Composite {
	private Images images;
	private int value = Integer.MIN_VALUE;
	private AbsolutePanel absolutePanel = new AbsolutePanel();
	private Label button;
	private Image emptyImage = new Image();
	private Image image = emptyImage;
	
	
	public Square(final EventBus eventBus, Images images, final int x, final int y, int value) {
		this.images = images;
		absolutePanel.setSize("32px", "32px");
		initWidget(absolutePanel);
		button = new Label();
		image = new Image();
		absolutePanel.add(button, 0, 0);
		absolutePanel.add(image, 1, 2);
		button.setStyleName("MineBox");
		setType(value);
		
		button.addDomHandler(new ContextMenuHandler() {
			@Override public void onContextMenu(ContextMenuEvent event) {
				event.preventDefault();
				event.stopPropagation();
			}
		}, ContextMenuEvent.getType());
		
		button.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (event.getNativeButton() == NativeEvent.BUTTON_LEFT) {
					eventBus.fireEvent(new SquareClickEvent(SquareClickType.LEFTCLICK, x, y));
				}
			}
		});
		
		button.addMouseDownHandler(new MouseDownHandler() {
			@Override
			public void onMouseDown(MouseDownEvent event) {
				if (event.getNativeButton() == NativeEvent.BUTTON_RIGHT) {
					eventBus.fireEvent(new SquareClickEvent(SquareClickType.RIGHTCLICK, x, y));
				}
			}
		});
	}
	
	public void setType(int value) {
		if (this.value == value) {
			return;
		}
		button.setText("");
		switch (value) {
		case MineField.HIDDEN:
			setImage(emptyImage);
			button.setStyleDependentName("hidden", true);
			break;
		case MineField.FLAGGED:
			setImage(new Image(images.flagImage()));
			button.setStyleDependentName("flagged", true);
			break;
		case MineField.FLAGGEDBAD:
			setImage(new Image(images.flagImage()));
			button.setStyleDependentName("error", true);
			break;
		case MineField.EXPLODEDMINE:
			setImage(new Image(images.bangImage()));
			button.setStyleDependentName("error", true);
			break;
		case MineField.HIDDENMINE:
			setImage(new Image(images.mineImage()));
			button.setStyleDependentName("hidden", true);
			break;
		case MineField.FLAGGEDMINE:
			setImage(new Image(images.mineImage()));
			button.setStyleDependentName("found", true);
			break;
		case 0:
			setImage(emptyImage);
			button.setStyleDependentName("exposed", true);
			button.setStyleDependentName(Integer.toString(value), true);
			break;
		default:
			button.setText(Integer.toString(value));
			setImage(emptyImage);
			button.setStyleDependentName("exposed", true);
			button.setStyleDependentName(Integer.toString(value), true);
		}
	}
	
	private void setImage(Image image) {
		absolutePanel.remove(this.image);
		this.image = image;
		absolutePanel.add(image, 4, 4);
		image.addStyleName("MineBox-image");
	}
	
}
