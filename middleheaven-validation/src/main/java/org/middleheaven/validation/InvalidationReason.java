package org.middleheaven.validation;

import java.io.Serializable;

public interface InvalidationReason extends Serializable{

	
	public InvalidationSeverity getSeverity();
	public String getMessage();
	public Object[] getParams();

}
