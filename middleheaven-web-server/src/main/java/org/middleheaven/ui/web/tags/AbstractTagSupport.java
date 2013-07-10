package org.middleheaven.ui.web.tags;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.middleheaven.core.bootstrap.ServiceRegistry;
import org.middleheaven.global.Culture;
import org.middleheaven.global.LocalizationService;
import org.middleheaven.global.text.LocalizableText;
import org.middleheaven.global.text.TimepointFormatter;
import org.middleheaven.process.ContextScope;
import org.middleheaven.quantity.math.Real;
import org.middleheaven.quantity.time.CalendarDateTime;

public class AbstractTagSupport extends TagSupport {


	/**
	 * 
	 */
	private static final long serialVersionUID = 6508668442968194800L;

	public String localize(CalendarDateTime date ,TimepointFormatter.Format format){
		Culture culture =  new TagContext(pageContext).getCulture();
		
		final LocalizationService i18nService = ServiceRegistry.getService(LocalizationService.class);

		return i18nService.getCultureModel(culture).getTimestampFormatter().format(date,format);
	}
	
	public String localize(LocalizableText message,ContextScope scope){
		
		LocalizationService service = ServiceRegistry.getService(LocalizationService.class);
		return service.getMessage(message, new TagContext(pageContext).getCulture());
		
	}
	
	public String localize(Number number){
		Culture culture =  new TagContext(pageContext).getCulture();
		
		final LocalizationService i18nService = ServiceRegistry.getService(LocalizationService.class);

		return i18nService.getCultureModel(culture).getQuantityFormatter().format(Real.valueOf(number));

	}
	
	public <T extends Tag> T findAncestorTag(Class<T> type){
		return type.cast(this.findAncestorWithClass(this, type));
	}
	
	public void writeAttribute(CharSequence name, Object value) throws JspTagException{
		try {
			pageContext.getOut().append(" ").append(name)
			.append("=\"").append(value==null ? "" :  value.toString()).append("\" ");
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
}
