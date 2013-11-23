package org.middleheaven.global.text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.middleheaven.culture.Culture;
import org.middleheaven.quantity.time.CalendarDateTime;
import org.middleheaven.quantity.time.TimePoint;
import org.middleheaven.quantity.time.TimeUtils;


public class TimepointFormatter implements ParsableFormatter<TimePoint> {

	public enum Format {

		DATE_ONLY,
		TIME_ONLY,
		DATE_AND_TIME
	}

	private Format format = Format.DATE_AND_TIME;
	private Culture culture;


	public TimepointFormatter(Culture culture){
		this.culture = culture;
	}

	@Override
	public String format(TimePoint object) {
		return format(object,this.format);
	}

	public String format(TimePoint value,Format format) {
		if( value == null){
			return "";
		}
		
		Date date = TimeUtils.toDate(value);
		DateFormat formater=null;
		switch (format){
		case DATE_ONLY:
			formater = DateFormat.getDateInstance(DateFormat.SHORT, culture.toLocale());
			break;
		case TIME_ONLY:
			formater = DateFormat.getTimeInstance(DateFormat.SHORT, culture.toLocale());
			break;
		case DATE_AND_TIME:
		default:
			formater = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, culture.toLocale());
			break;
		}

		SimpleDateFormat sdf = (SimpleDateFormat)formater;
		sdf.applyPattern(sdf.toPattern().replaceAll("yy", "yyyy"));
		return sdf.format(date);
	}


	@Override
	public TimePoint parse(String stringValue) {
		try {
			return parse(stringValue,this.format);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public TimePoint parse(String stringValue,Format format) throws ParseException {
		DateFormat formater=null;
		switch (format){
		case DATE_ONLY:
			formater = DateFormat.getDateInstance(DateFormat.SHORT, culture.toLocale());
			break;
		case TIME_ONLY:
			formater = DateFormat.getTimeInstance(DateFormat.SHORT, culture.toLocale());
			break;
		case DATE_AND_TIME:
		default:
			formater = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, culture.toLocale());
			break;
		}

		SimpleDateFormat sdf = (SimpleDateFormat)formater;
		sdf.applyPattern(sdf.toPattern().replaceAll("yy", "yyyy"));
		CalendarDateTime timepoint = TimeUtils.from(sdf.parse(stringValue));
		
		switch (format){
		case DATE_ONLY:
			return timepoint.toDate();
		case TIME_ONLY:
			return timepoint.toTime();
		case DATE_AND_TIME:
		default:
			return timepoint;
		}
	}

	public void setPattern(Format format) {
		this.format = format;
	}

}
