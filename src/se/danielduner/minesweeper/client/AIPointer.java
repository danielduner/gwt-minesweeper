package se.danielduner.minesweeper.client;

import se.danielduner.minesweeper.client.resources.Images;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;

public class AIPointer extends Composite {
	private Image pointer;
	private double pointerX, pointerY;
	private double lastTimestamp;
	private boolean firstMove, hasArrived = true;
	private int pointerTargetPixelX, pointerTargetPixelY;
	MoveCallback callback;
	
	public interface MoveCallback {
		public void onMoveComplete();
	}
	
	public AIPointer(Images images, int x, int y) {
		pointerX = x;
		pointerY = y;
		pointer = new Image(images.arrowImage());
		pointer.addStyleName("mousepointer");
		initWidget(pointer);
	}

	public void movePointer(int x, int y, MoveCallback callback) {
		firstMove = true;
		hasArrived = false;
		pointerTargetPixelX = x;
		pointerTargetPixelY = y;
		this.callback = callback;
	}
	
	public void updatePosition(double timestamp) {
		if(hasArrived) {
			return;
		}
		if (firstMove) {
			firstMove = false;
		} else {
			double timediff = timestamp - lastTimestamp;
			if (timediff>0) {
				double speed = 0.3 * timediff;
				if (speed > 10) {
					speed = 10;
				}
				double dx = pointerTargetPixelX - pointerX;
				double dy = pointerTargetPixelY - pointerY;
				double length = Math.sqrt(dx*dx + dy*dy);
				if (speed > length) {
					speed = length;
				}
				dx = speed * dx/length;
				dy = speed * dy/length;
				pointerX += dx;
				pointerY += dy;
			}
		}
		lastTimestamp = timestamp;
		if (2 > Math.sqrt(Math.pow(pointerTargetPixelX - pointerX, 2)
				+ Math.pow(pointerTargetPixelY - pointerY, 2))) {
			hasArrived = true;
			callback.onMoveComplete();
		}
	}
	
	public int getX() {
		return (int)pointerX;
	}

	public int getY() {
		return (int)pointerY;
	}
	
	public boolean hasArrived() {
		return hasArrived;
	}
}
