/**
 * 
 */
package org.middleheaven.ui.web.vaadin;

import org.middleheaven.events.EventListenersSet;
import org.middleheaven.global.text.LocalizableText;
import org.middleheaven.ui.CommandListener;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UISearch;
import org.middleheaven.ui.UISearch.UISearchFilter;
import org.middleheaven.ui.components.UICommand;
import org.middleheaven.ui.components.UIForm;
import org.middleheaven.ui.events.UIActionEvent;
import org.middleheaven.ui.property.Property;
import org.middleheaven.ui.property.ValueProperty;
import org.middleheaven.util.function.Block;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

/**
 * 
 */
public class VaadinButton extends VaadinUIComponent implements UICommand{

	
	
	/**
	 * 
	 */
	private static final class UIFormSearchFilter implements UISearchFilter {
		@Override
		public boolean canContinueSearch(UIComponent c) {
			return true;
		}

		@Override
		public boolean accept(UIComponent c) {
			return c instanceof UIForm;
		}
	}

	/**
	 * 
	 */
	private static final class OnChangeBlock implements Block<LocalizableText> {
		
		private VaadinButton button;

		/**
		 * Constructor.
		 * @param vaadinButton
		 */
		public OnChangeBlock(VaadinButton button) {
			this.button = button;
		}

		@Override
		public void apply(LocalizableText text) {
			button.getComponent().setCaption(button.localize(text));
		}
	}

	/**
	 * 
	 */
	private static final class ButtonClickListener implements ClickListener {
		private static final long serialVersionUID = 8681925013276524813L;
		
		private VaadinButton button;

		/**
		 * Constructor.
		 * @param commandListeners
		 */
		public ButtonClickListener(VaadinButton button) {
			this.button= button;
		}

		@Override
		public void buttonClick(ClickEvent event) {
			
			button.commandListeners.broadcastEvent().onCommand(new UIActionEvent( button.getNameProperty().get(), button));

		}
	}

	private final EventListenersSet<CommandListener> commandListeners = EventListenersSet.newSet(CommandListener.class);
	private transient UIForm form;
	
	private final Property<LocalizableText> text = ValueProperty.writable("text", LocalizableText.class);
	private final Property<String> name = ValueProperty.writable("name", String.class);
	
	/**
	 * Constructor.
	 * @param component
	 * @param type
	 */
	public VaadinButton(Button component) {
		super(component, UICommand.class);
		
		text.onChange(new OnChangeBlock(this));
		
		component.addListener(new ButtonClickListener(this));
	}
	
	protected UIForm getForm (){
		
		if (form == null) {
			
			form = (UIForm) UISearch.searchFirstUp(this, new UIFormSearchFilter());
		}
		
		return form;
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void copyModel() {
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Property<LocalizableText> getTextProperty() {
		return text;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Property<String> getNameProperty() {
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addCommandListener(CommandListener commandListener) {
		commandListeners.addListener(commandListener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeCommandListener(CommandListener commandListener) {
		commandListeners.removeListener(commandListener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<CommandListener> getCommandListeners() {
		return commandListeners;
	}

}
