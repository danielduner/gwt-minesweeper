package se.danielduner.minesweeper.client.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface Images extends ClientBundle {
	
	// Images
	@Source("flag.png")
	public ImageResource flagImage();

	@Source("mine.png")
	public ImageResource mineImage();
	
	@Source("bang.png")
	public ImageResource bangImage();

	@Source("face-smile.png")
	public ImageResource facePlaying();

	@Source("face-cool.png")
	public ImageResource faceAIPlaying();

	@Source("face-win.png")
	public ImageResource faceWin();
	
	@Source("face-sad.png")
	public ImageResource faceLost();
	
	@Source("arrow.png")
	public ImageResource arrowImage();
	
}
