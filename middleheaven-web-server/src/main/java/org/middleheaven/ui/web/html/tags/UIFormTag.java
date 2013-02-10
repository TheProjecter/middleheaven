/**
 * 
 */
package org.middleheaven.ui.web.html.tags;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;

import org.middleheaven.process.ContextScope;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.components.UIContainer;
import org.middleheaven.ui.components.UIForm;
import org.middleheaven.ui.web.tags.TagContext;

/**
 * 
 */
public class UIFormTag extends AbstractUIComponentBodyTagSupport {
	
	private static final long serialVersionUID = 5515115907731723591L;

	private final List<UIComponent> children = new LinkedList<UIComponent>();
	private String body;

	private String action;
	
	public void setAction(String action){
		this.action = action;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<? extends UIComponent> getComponentType() {
		return UIForm.class;
	}
	
	public UIComponent getUIComponent() {
		UIContainer uic = (UIContainer) super.getUIComponent();
		
		for(UIComponent c : children){
			uic.addComponent(c);
		}
		
		return uic;
	}

	public final int doAfterBody() throws JspException{

		BodyContent bc = getBodyContent();
		// get the bc as string
		body = bc.getString();
		// clean up
		bc.clearBody();


		return SKIP_BODY;

	}
	
	protected void prepareRender(TagContext attributeContext) {
		attributeContext.setAttribute(ContextScope.RENDERING, "body", body);
	}

	
	@Override
	public void releaseState() {
		this.children.clear();
	}
}
