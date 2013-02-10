/**
 * 
 */
package org.middleheaven.ui.web.vaadin;

import org.middleheaven.events.EventListenersSet;
import org.middleheaven.global.text.TextLocalizable;
import org.middleheaven.ui.CommandListener;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UISearch;
import org.middleheaven.ui.UISearch.UISearchFilter;
import org.middleheaven.ui.components.UICommand;
import org.middleheaven.ui.components.UIForm;
import org.middleheaven.ui.events.UIActionEvent;
import org.middleheaven.util.function.Block;
import org.middleheaven.util.property.Property;
import org.middleheaven.util.property.ValueProperty;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;

/**
 * 
 */
public class VaadinButton extends VaadinUIComponent implements UICommand{

	
	
	private final EventListenersSet<CommandListener> commandListeners = EventListenersSet.newSet(CommandListener.class);
	private transient UIForm form;
	
	private final Property<TextLocalizable> text = ValueProperty.writable("text", TextLocalizable.class);
	private final Property<String> name = ValueProperty.writable("name", String.class);
	
	/**
	 * Constructor.
	 * @param component
	 * @param type
	 */
	public VaadinButton(Component component) {
		super(component, UICommand.class);
		
		text.onChange(new Block<TextLocalizable>(){

			@Override
			public void apply(TextLocalizable text) {
				getComponent().setCaption(localize(text));
			}
			
		});
	}
	
	protected UIForm getForm (){
		
		if (form == null) {
			
			form = (UIForm) UISearch.searchFirstUp(this, new UISearchFilter() {
				
				@Override
				public boolean canContinueSearch(UIComponent c) {
					return true;
				}
				
				@Override
				public boolean accept(UIComponent c) {
					return c instanceof UIForm;
				}
			});
		}
		
		return form;
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void copyModel() {

		Button component = (Button) this.getComponent();

		component.addListener(new ClickListener(){

			private static final long serialVersionUID = 8681925013276524813L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				commandListeners.broadcastEvent().onCommand(new UIActionEvent( getNameProperty().get(), VaadinButton.this));

			}
			
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Property<TextLocalizable> getTextProperty() {
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
