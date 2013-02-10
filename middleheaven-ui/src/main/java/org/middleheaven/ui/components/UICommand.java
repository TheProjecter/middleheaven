package org.middleheaven.ui.components;

import org.middleheaven.global.text.TextLocalizable;
import org.middleheaven.ui.CommandListener;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.util.property.Property;

public interface UICommand extends UIComponent {

	/**
	 * 
	 * @return
	 */
	public Property<TextLocalizable> getTextProperty();
	
	/**
	 * The name of the command trigged by this model.
	 * @return  The name of the command trigged by this model.
	 */
	public Property<String> getNameProperty();
	
	/**
	 * Add a {@link CommandListener} that will be notified when the command is ativated.
	 * @param listener the listener to add
	 */
	public void addCommandListener(CommandListener listener);
	
	/**
	 * Remove a {@link CommandListener} that would be notified when the command were ativated.
	 * @param listener the listener to remove
	 */
	public void removeCommandListener(CommandListener listener);

	
	public Iterable<CommandListener> getCommandListeners();
}
