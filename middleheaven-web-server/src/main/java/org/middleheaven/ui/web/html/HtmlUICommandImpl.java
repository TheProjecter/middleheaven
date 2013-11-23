/**
 * 
 */
package org.middleheaven.ui.web.html;

import java.io.IOException;

import org.middleheaven.ui.CommandListener;
import org.middleheaven.ui.GenericUIComponent;
import org.middleheaven.ui.components.UICommand;
import org.middleheaven.ui.events.UIActionEvent;
import org.middleheaven.ui.rendering.RenderingContext;

/**
 * 
 */
public class HtmlUICommandImpl extends GenericUIComponent<UICommand> implements HtmlUICommand {

	private AbstractHtmlRender render;
	
	/**
	 * Constructor.
	 * @param component
	 * @param abstractHtmlCommandRender
	 */
	public HtmlUICommandImpl(UICommand component, AbstractHtmlRender render) {
		super(UICommand.class, component.getFamily());
		this.render = render;

		for (CommandListener listener : component.getCommandListeners()){
			this.addCommandListener(listener);
		}
		
		this.getVisibleProperty().set(component.getVisibleProperty().get());
		this.getEnableProperty().set(component.getEnableProperty().get());
		this.getNameProperty().set(component.getNameProperty().get());
		this.getTextProperty().set(component.getTextProperty().get());
	}

	@Override
	public boolean isRendered() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fireEvent(UIActionEvent event) {

		for (CommandListener listener : this.getCommandListeners()){
			listener.onCommand(event);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeTo(HtmlDocument doc, RenderingContext context) throws IOException {
		 this.render.write(doc, context, this);
	}

}
