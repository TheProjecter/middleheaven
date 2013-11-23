package org.middleheaven.global.text;

import java.util.Date;

import org.middleheaven.quantity.time.TimeUtils;
import org.middleheaven.util.coersion.AbstractTypeCoersor;
import org.middleheaven.util.coersion.CoersionException;

public class StringDateCoersor extends AbstractTypeCoersor<String, Date> {

	TimepointFormatter format;
	public StringDateCoersor(TimepointFormatter format){
		this.format = format;
	}
	
	@Override
	public <T extends Date> T coerceForward(String value, Class<T> type) {
		try {
			if (value==null || value.trim().isEmpty()){
				return null;
			}
			
			return type.cast(format.parse(value));
		} catch (RuntimeException e) {
			throw new CoersionException(e);
		}
		
	}

	@Override
	public <T extends String> T coerceReverse(Date value, Class<T> type) {
		if (value==null){
			return null;
		}
		return type.cast(format.format(TimeUtils.from(value)));
	}

	


}
