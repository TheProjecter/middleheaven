package org.middleheaven.ui.web.html.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import org.middleheaven.process.ContextScope;
import org.middleheaven.ui.GenericUIComponent;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.rendering.RenderKit;
import org.middleheaven.ui.rendering.RenderingContext;
import org.middleheaven.ui.web.Browser;
import org.middleheaven.ui.web.html.HtmlDocument;
import org.middleheaven.ui.web.html.HtmlUIComponent;
import org.middleheaven.ui.web.tags.AbstractIterationTagSupport;
import org.middleheaven.ui.web.tags.TagContext;

public abstract class AbstractUIComponentIterationTagSupport extends AbstractIterationTagSupport implements UIComponentTag{

	private String familly;
	private boolean enabled;
	private String name;
	
	public void setFamilly (String familly){
		this.familly = familly;
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
	
	public UIComponent getUIComponent(){
		UIComponent uic = GenericUIComponent.getInstance(getComponentType(), familly);

		uic.getVisibleProperty().set(true);
		uic.getEnableProperty().set(this.enabled);
		
		if(this.getId()!=null){
			uic.setGID(this.getId());
		}
		
		uic.setFamily(this.familly);
		return uic;
	}
	
	protected void doRender() throws IOException {
		final TagContext tagContext = new TagContext(pageContext);
		
		UIComponent templateComponent = getUIComponent();
		
		prepareRender(tagContext, templateComponent);
		
		RenderKit renderKit = tagContext.getAttribute(ContextScope.REQUEST, RenderKit.class.getName(),RenderKit.class);
		
	
		RenderingContext context = new RenderingContext(tagContext.getAttributes() , renderKit );
		
		UIComponent parent = new Browser(renderKit.getSceneNavigator()); // TODO Fragment
		try{
			UIComponentTag parentTag = (UIComponentTag)this.getParent();
			if(parentTag != null){
				parent = parentTag.getUIComponent();
			}
		} catch (ClassCastException e) {
			
		}
		
		if (parent == null){
			HtmlUIComponent html = (HtmlUIComponent) renderKit.renderComponent(context, parent, templateComponent);
			
			HtmlDocument doc = HtmlDocument.newInstance(tagContext.getContextPath(), tagContext.getCulture());
			
			html.writeTo(doc,context);
		
			doc.writeToResponse(pageContext.getOut());
		}
	
	}
	
	protected void prepareRender(TagContext attributeContext, UIComponent templateComponent) {
		//this.getModel().setEnabled(enabled);		
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
