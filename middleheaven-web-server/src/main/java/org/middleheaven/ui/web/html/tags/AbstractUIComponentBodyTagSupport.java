package org.middleheaven.ui.web.html.tags;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.jsp.JspException;

import org.middleheaven.process.ContextScope;
import org.middleheaven.ui.GenericUIComponent;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.components.UIContainer;
import org.middleheaven.ui.rendering.RenderKit;
import org.middleheaven.ui.rendering.RenderingContext;
import org.middleheaven.ui.web.Browser;
import org.middleheaven.ui.web.html.HtmlDocument;
import org.middleheaven.ui.web.html.HtmlUIComponent;
import org.middleheaven.ui.web.tags.AbstractBodyTagSupport;
import org.middleheaven.ui.web.tags.TagContext;

public abstract class AbstractUIComponentBodyTagSupport extends AbstractBodyTagSupport implements UIComponentTag{


	private static final long serialVersionUID = 1865799329800955061L;
	
	private String familly;
	private boolean enabled = true;
	private String name;
	private boolean visible = true;
	
	
	public void setVisible (boolean visible){
		this.visible = visible;
	}
	
	protected boolean getVisible(){
		return this.visible;
	}
	
	public void setFamilly (String familly){
		this.familly = familly;
	}

	protected String getFamilly(){
		return this.familly;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	protected String getName(){
		return name;
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	protected boolean getEnabled(){
		return this.enabled;
	}

	@Override
	public final UIComponent getUIComponent() {
		UIComponent uic = GenericUIComponent.getInstance(getComponentType(), familly);

		uic.getVisibleProperty().set(this.visible);
		uic.getEnableProperty().set(this.enabled);
		
		if(this.getId()!=null){
			uic.setGID(this.getId());
		}
		
		uic.setFamily(this.familly);
		
		final TagContext tagContext = new TagContext(pageContext);
		
		populateProperties(tagContext, uic);
		
		if (uic.isType(UIContainer.class)){
			
			UIContainer container = (UIContainer) uic;
			for(UIComponent c : getUIComponentChildren()){
				container.addComponent(c);
			}
		}
	
		return uic;
	}

	/**
	 * @return
	 */
	protected Iterable<UIComponent> getUIComponentChildren() {
		return Collections.emptySet();
	}

	protected void doRender() throws IOException {
		
		final TagContext tagContext = new TagContext(pageContext);
		
		UIComponent templateComponent = getUIComponent();
		
		RenderKit renderKit = tagContext.getAttribute(ContextScope.REQUEST, RenderKit.class.getName(),RenderKit.class);

		RenderingContext context = new RenderingContext(tagContext.getAttributes() , renderKit );
		
		UIComponent client = new Browser(renderKit.getSceneNavigator()); // TODO Fragment
		try{
			UIComponentTag parentTag = (UIComponentTag)this.getParent();
			if(parentTag != null){
				client = parentTag.getUIComponent();
			}
		} catch (ClassCastException e){
			
		}
		
		HtmlUIComponent html = (HtmlUIComponent) renderKit.renderComponent(context, client,  templateComponent);

		HtmlDocument doc = HtmlDocument.newInstance(tagContext.getContextPath(), tagContext.getCulture());
		
		html.writeTo(doc,context);
	
		doc.writeToResponse(pageContext.getOut());
	}
	
	protected void populateProperties(TagContext attributeContext, UIComponent templateComponent) {

	}

	public int doEndTag() throws JspException{
		
		try {
			doRender();
			return EVAL_PAGE;
		} catch (IOException e) {
			throw new JspException(e);
		} finally {
			releaseState();
		}

	}


}
