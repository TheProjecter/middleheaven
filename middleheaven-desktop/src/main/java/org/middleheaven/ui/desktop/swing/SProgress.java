package org.middleheaven.ui.desktop.swing;

import javax.swing.JProgressBar;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.components.UIProgress;
import org.middleheaven.util.function.Block;
import org.middleheaven.util.property.Property;
import org.middleheaven.util.property.ValueProperty;

public class SProgress extends SBaseOutputPanel implements UIProgress{


	private static final long serialVersionUID = 7564245586904494636L;

	private final Property<Integer> maximum = ValueProperty.writable("maximum", Integer.class);
	
	private final JProgressBar progressBar = new JProgressBar();
	
	public SProgress(){
		
		this.add(progressBar);
		
		maximum.onChange(new Block<Integer>(){

			@Override
			public void apply(Integer value) {
				if (value == null){
					progressBar.setIndeterminate(true);
				} else {
					progressBar.setIndeterminate(false);
					progressBar.setMaximum(value);
					progressBar.setMinimum(0);
				}
			}
			
		});
		
		
	}
	
	@Override
	public <T extends UIComponent> Class<T> getComponentType() {
		return (Class<T>) UIProgress.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Property<Integer> getMaximumProperty() {
		return maximum;
	}


}
