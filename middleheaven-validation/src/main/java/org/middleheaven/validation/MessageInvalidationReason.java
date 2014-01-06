package org.middleheaven.validation;

import org.middleheaven.collections.CollectionUtils;

public final class MessageInvalidationReason implements InvalidationReason{

	private static final long serialVersionUID = 7853697504391291517L;
	
	private InvalidationSeverity severity;
	private String message;
	private Object[] params;
	
	public static MessageInvalidationReason invalid(Object validatedObject){
		return new MessageInvalidationReason(InvalidationSeverity.ERROR, "invalid",validatedObject);
	}
	
	public static MessageInvalidationReason error(Object validatedObject,String message){
		return new MessageInvalidationReason(InvalidationSeverity.ERROR, message,validatedObject);
	}
	
	public static MessageInvalidationReason warn(Object validatedObject,String message){
		return new MessageInvalidationReason(InvalidationSeverity.ERROR, message,validatedObject);
	}
	
	public static MessageInvalidationReason error(Object validatedObject,String message, Object ... params){
		return new MessageInvalidationReason(InvalidationSeverity.ERROR, message, validatedObject,params);
	}
	
	public static MessageInvalidationReason warn(Object validatedObject,String message, Object ... params){
		return new MessageInvalidationReason(InvalidationSeverity.ERROR, message,validatedObject, params);
	}
	

	private MessageInvalidationReason(InvalidationSeverity severity, String message,Object validatedObject, Object ... params) {
		super();
		this.severity = severity;
		this.message = message;
		this.params =CollectionUtils.addToArray(new Object[]{validatedObject}, params);
	}

	@Override
	public InvalidationSeverity getSeverity() {
		return severity;
	}

	public String getMessage() {
		return message;
	}

	public Object[] getParams() {
		return CollectionUtils.duplicateArray(params);
	}
	
	@Override
	public boolean equals(Object other) {
		return other instanceof MessageInvalidationReason
				&& equalsOther((MessageInvalidationReason) other);
	}

	private boolean equalsOther(MessageInvalidationReason other) {
		return this.severity.equals(other.severity) && this.message.equals(other.message);
	}

	@Override
	public int hashCode() {
		return this.severity.hashCode() ^ this.message.hashCode();
	}
	
	@Override
	public String toString(){
		return this.message;
	}
}
