/**
 * 
 */
package org.middleheaven.util.property;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;

/**
 * 
 */
public class PropertyBinder {

	/**
	 * Property A will change when B changes.
	 * @param a
	 * @param b
	 */
	public static <T extends Serializable> void bind(final Property<T> a, final Property<T> b){
		
		b.addListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				a.set((T)evt.getNewValue());
			}
		});
	}
}
