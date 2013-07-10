/**
 * 
 */
package org.middleheaven.ui.web.vaadin;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.rendering.RenderingContext;

/**
 * 
 */
public class VaadinImageRender extends AbstractVaadinRender {


	private static final long serialVersionUID = -5770821350108902191L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected VaadinUIComponent buildVaadin(RenderingContext context, UIComponent parent,
			UIComponent component) {
	// TODO	
//		// Create the stream resource with some initial filename.
//		StreamResource imageResource =
//		    new StreamResource(imageSource, "initial-filename.png",
//		                       getApplication());
//		 
//		// Instruct browser not to cache the image.
//		imageResource.setCacheTime(0);
//		 
//		// Display the image in an Embedded component.
//		Embedded embedded = new Embedded("", imageResource); 
//		
		return new VaadinImage();
	}

}
