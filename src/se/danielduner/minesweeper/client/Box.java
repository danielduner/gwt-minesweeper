package se.danielduner.minesweeper.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;

public class Box extends Composite {
	private Label button;
	
	public Box(int value) {
		button = new Label();
		initWidget(button);
		setStyleName("MineBox");
		setType(value);
	}
	
	public void setType(int value) {
		if(value == MineField.HIDDEN) {
			button.setText("");
			button.setStyleDependentName("hidden", true);
		} else if (value == MineField.FLAGGED) {
			button.setText("F");
			button.setStyleDependentName("hidden", true);
			button.setStyleDependentName("flagged", true);
		} else if (value == MineField.MINE) {
			button.setText("");
			button.setStyleDependentName("mine", true);
		} else if (value==0){
			button.setText("");
			button.setStyleDependentName("exposed", true);
			button.setStyleDependentName(Integer.toString(value), true);
		} else {
			button.setText(Integer.toString(value));
			button.setStyleDependentName("exposed", true);
			button.setStyleDependentName(Integer.toString(value), true);
		}
	}
}
