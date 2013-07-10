package org.middleheaven.ui.web.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;

import org.middleheaven.core.bootstrap.ServiceRegistry;
import org.middleheaven.global.Culture;
import org.middleheaven.global.LocalizationService;
import org.middleheaven.global.text.LocalizableText;
import org.middleheaven.global.text.TimepointFormatter;
import org.middleheaven.process.ContextScope;
import org.middleheaven.quantity.math.Real;
import org.middleheaven.quantity.time.CalendarDateTime;

/**
 * 
 */
public abstract class AbstractBodyTagSupport extends BodyTagSupport {

	public String localize(CalendarDateTime date ,TimepointFormatter.Format format){
		Culture culture =  new TagContext(pageContext).getCulture();
		
		final LocalizationService i18nService = ServiceRegistry.getService(LocalizationService.class);

		return i18nService.getCultureModel(culture).getTimestampFormatter().format(date,format);
	}

	public void release(){
		releaseState();
	}
	
	public String localize(Number number){
		Culture culture =  new TagContext(pageContext).getCulture();
		
		final LocalizationService i18nService = ServiceRegistry.getService(LocalizationService.class);

		return i18nService.getCultureModel(culture).getQuantityFormatter().format(Real.valueOf(number));
	}
	
	public <T extends Tag> T findAncestorTag(Class<T> type){
		return type.cast(this.findAncestorWithClass(this, type));
	}
	
	public String localize(LocalizableText message){
		return localize(message,ContextScope.APPLICATION);
	}
	
	public String localize(LocalizableText message,ContextScope scope){
		
		Culture culture = new TagContext(pageContext).getCulture();
		
		LocalizationService service = ServiceRegistry.getService(LocalizationService.class);
		return service.getMessage(message, culture);
		
	}
	
	public void writeAttribute(CharSequence name, Object value) throws JspTagException{
		if (value==null){
			return;
		}
		try {
			pageContext.getOut().append(" ").append(name)
			.append("=\"").append(value.toString()).append("\"");
		} catch (Exception e) {
			throw new JspTagException(e);
		}
	}
	
	public void write(CharSequence text) throws JspTagException{
		try {
			pageContext.getOut().print(text);
		} catch (Exception e) {
			throw new JspTagException(e);
		}
	}
	
	public void writeLine(CharSequence text) throws JspTagException{
		try {
			pageContext.getOut().println(text);
		} catch (Exception e) {
			throw new JspTagException(e.getMessage());
		}
	}

	public int doEndTag() throws JspException {
		releaseState();
		return EVAL_PAGE;
	}
	
	public abstract void releaseState();
	
}
