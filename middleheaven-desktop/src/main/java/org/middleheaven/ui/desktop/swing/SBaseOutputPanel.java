/**
 * 
 */
package org.middleheaven.ui.desktop.swing;

import org.middleheaven.ui.UIReadState;
import org.middleheaven.ui.property.Property;
import org.middleheaven.ui.property.ValueProperty;
import org.middleheaven.util.function.Block;

/**
 * 
 */
public abstract class SBaseOutputPanel extends SBaseLeafPanel {


	private static final long serialVersionUID = 5123824028505700776L;
	
	private final Property<UIReadState> readState = ValueProperty.writable("readState", UIReadState.class);
	
	public SBaseOutputPanel(){
		this.readState.set(UIReadState.INPUT_ENABLED).onChange(new Block<UIReadState>(){

			@Override
			public void apply(UIReadState value) {
				switch (value) {
				case INPUT_DISABLED:
					getVisibleProperty().set(true);
					getEnableProperty().set(false);
					break;
				case INPUT_ENABLED:
					getVisibleProperty().set(true);
					getEnableProperty().set(true);
					break;
				case INVISIBLE:
					getVisibleProperty().set(false);
					break;
				case OUTPUT_ONLY:
					getVisibleProperty().set(true);
					getEnableProperty().set(true);
					
					// TODO create Label overlay
				default:
					break;
				}
			}
			
		});
	}
	
	public Property<UIReadState> getReadStateProperty(){
		return readState;
	}

}
