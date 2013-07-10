package org.middleheaven.ui.web.tags;

import java.util.Date;

import javax.servlet.jsp.JspTagException;

import org.middleheaven.global.text.TimepointFormatter;
import org.middleheaven.quantity.time.CalendarDate;
import org.middleheaven.quantity.time.CalendarDateTime;
import org.middleheaven.quantity.time.TimeUtils;

public class DateInputTag extends TextInputTag{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2949347511597971603L;



	public DateInputTag() {
		this.setMaxLength(10);
		this.setMinLength(10);
		this.setSubtype("date");
	}

	protected void writeInputOption() throws JspTagException {
		
		// sinals the form that that his is an upload capacle form
		FormTag form = this.findAncestorTag(FormTag.class);
		form.addDatePickedFieldID(this.getWritableId());
		
		super.writeInputOption();
		
		writeAttribute("size", "10");
	}
	

	
	public void setValue(Object value) {
		
		if (value instanceof Date){
			value = localize(TimeUtils.from((Date)value), TimepointFormatter.Format.DATE_ONLY);
		} else if (value instanceof CalendarDate){
			value = localize((CalendarDate)value, TimepointFormatter.Format.DATE_ONLY);
		} else if (value instanceof CalendarDateTime){
			value = localize(((CalendarDateTime)value),TimepointFormatter.Format.DATE_AND_TIME);
		}
		
		super.setValue(value);
	}

	
}
