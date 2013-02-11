package org.middleheaven.ui;

import java.io.Serializable;

/**
 * Represents the pair of vertical and horizontal dimensions of a UI object.
 */
public final class UISize implements Serializable {
  
	private static final long serialVersionUID = 6059819788937306305L;
	
	private UIDimension height;
	private UIDimension width;

	public static UISize pixels(int width, int height){
		return new UISize(
				UIDimension.pixels(width),
				UIDimension.pixels(height)
		);
	}
	
	public static UISize valueOf(UIDimension width, UIDimension height){
		return new UISize(
				width,
				height
		);
	}
	
	private UISize(UIDimension width, UIDimension height) {
		this.height = height;
		this.width = width;
	}

	public UIDimension getHeight(){
		return this.height;
	}
	
    public UIDimension getWidth(){
    	return this.width;
    }
}
