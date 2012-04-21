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
	
	@Source("arrow.png")
	public ImageResource arrowImage();
	
}
