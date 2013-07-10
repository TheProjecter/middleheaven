package org.middleheaven.ui.web.tags;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import org.middleheaven.global.text.TimepointFormatter.Format;
import org.middleheaven.quantity.time.CalendarDateTime;
import org.middleheaven.quantity.time.TimeUtils;
import org.middleheaven.util.StringUtils;
import org.middleheaven.util.coersion.TypeCoercing;
import org.middleheaven.util.identity.Identity;

public class OutTag extends AbstractTagSupport {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3978710146550722490L;
	private Object value;
	private int start = 0;
	private int end = -1;
	private int cut = -1;
	private String cutTermination = "...";
	
	private String exportName = "";
	
	public void setValue(Object value) {
		this.value = value;
	}
	
	
	/**
	 * Atributes {@link int}.
	 * @param cut the cut to set
	 */
	public void setCut(int cut) {
		this.cut = cut;
	}


	/**
	 * Atributes {@link String}.
	 * @param cutTermination the cutTermination to set
	 */
	public void setCutTermination(String cutTermination) {
		this.cutTermination = cutTermination;
	}


	public void setExportTo(String exportName){
		this.exportName = exportName;
	}
	
	public String getExportTo(){
		return this.exportName;
	}
	
	public void setStart(int value){
		this.start = value;
	}
	
	public void setEnd(int value){
		this.end  = value;
	}
	
	private void print(String str) throws IOException{
		if (cut > 0) {
			if (str.length() > cut){
				str = StringUtils.subString(str, cut) + this.cutTermination;
			}
		} else if (start > 0 && end > 0){
			str = str.substring(start, end);
		} else if (start > 0){
			str = str.substring(start);
		}
		
		if (exportName.isEmpty()){
			pageContext.getOut().print(str);
		} else {
			pageContext.setAttribute(exportName, str);
		}
		
	}
	
	public int doStartTag() throws JspException {
		try {		
			
			if (value instanceof CharSequence) {
				print(value.toString());
			} else if (value instanceof Date){
				print(localize(TimeUtils.from((Date)value), Format.DATE_ONLY));
			} else if (value instanceof Calendar) {
				print(localize(TimeUtils.from(((Calendar)value).getTime()), Format.DATE_ONLY));
			} else if (value instanceof CalendarDateTime) {
				print(localize(((CalendarDateTime)value),Format.DATE_AND_TIME));
			} else if (double.class.isInstance(value)||Double.class.isInstance(value)) {
				print(localize(new Double(value.toString())));
			} else if (value instanceof Identity){
				print(TypeCoercing.coerce(value, String.class));
			} else if (value != null) {
				print(value.toString());
			}
			return SKIP_BODY;
		} catch (Exception e) {
			throw new JspTagException(e.getMessage());
		}
	}
	
	public int doEndTag() {
		return EVAL_PAGE;
	}
}