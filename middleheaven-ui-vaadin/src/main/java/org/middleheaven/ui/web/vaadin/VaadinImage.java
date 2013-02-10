/**
 * 
 */
package org.middleheaven.ui.web.vaadin;

import org.middleheaven.ui.UIReadState;
import org.middleheaven.ui.components.UIImage;
import org.middleheaven.util.property.BindedProperty;
import org.middleheaven.util.property.Property;
import org.middleheaven.util.property.ValueProperty;

import com.vaadin.ui.Label;

/**
 * 
 */
public class VaadinImage extends VaadinUIComponent implements UIImage {
	
	final Property<String> imageName = BindedProperty.bind("imageName" , this.getComponent(), "icon");
	
	/**
	 * Constructor.
	 */
	public VaadinImage() {
		super(new Label(), UIImage.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void copyModel() {
		//no-op
		
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public Property<UIReadState> getReadStateProperty() {
		return ValueProperty.readOnly("readState", UIReadState.OUTPUT_ONLY);
	}




	/**
	 * {@inheritDoc}
	 */
	@Override
	public Property<String> getImageNameProperty() {
		return imageName;
	}

}
