/**
 * 
 */
package org.middleheaven.ui.desktop.swing;

import javax.swing.AbstractButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;

import org.middleheaven.global.text.TextLocalizable;
import org.middleheaven.util.function.Block;
import org.middleheaven.util.property.Property;
import org.middleheaven.util.property.ValueProperty;

/**
 * 
 */
class STextProperty extends ValueProperty<TextLocalizable> {

	
	public static Property<TextLocalizable> bind(final AbstractButton component){
		return new STextProperty().onChange(new Block<TextLocalizable>(){

			@Override
			public void apply(TextLocalizable text) {
				component.setText(SDisplayUtils.localize(text));
			}
			
		});
	}
	
	public static Property<TextLocalizable> bind(final JInternalFrame component){
		return new STextProperty().onChange(new Block<TextLocalizable>(){

			@Override
			public void apply(TextLocalizable text) {
				component.setTitle(SDisplayUtils.localize(text));
			}
			
		});
	}	
	
	public static Property<TextLocalizable> bind(final JFrame component){
		return new STextProperty().onChange(new Block<TextLocalizable>(){

			@Override
			public void apply(TextLocalizable text) {
				component.setTitle(SDisplayUtils.localize(text));
			}
			
		});
	}
	
	/**
	 * @param sLabel
	 * @return
	 */
	public static Property<TextLocalizable> bind(final JLabel label) {
		return new STextProperty().onChange(new Block<TextLocalizable>(){

			@Override
			public void apply(TextLocalizable text) {
				label.setText(SDisplayUtils.localize(text));
			}
			
		});
	}
	
	
	/**
	 * Constructor.
	 * @param propertyName
	 * @param value
	 * @param readOnly
	 */
	public STextProperty() {
		super("text", null, TextLocalizable.class, false);

		
	}



}
