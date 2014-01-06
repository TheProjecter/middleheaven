package org.middleheaven.util.coersion;

import org.middleheaven.reflection.ReflectedMethod;
import org.middleheaven.reflection.inspection.Introspector;
import org.middleheaven.util.identity.Identity;
import org.middleheaven.util.identity.IntegerIdentity;
import org.middleheaven.util.identity.LongIdentity;
import org.middleheaven.util.identity.StringIdentity;
import org.middleheaven.util.identity.UUIDIdentity;

/**
 * Coerces String to Identity objects and back.
 * Coercing Identity objects to String demands ad a String identified at the begining in order 
 * to differenced the identity types. 
 */
class StringIdentityCoersor extends AbstractIdentityCoersor<String> {

	@SuppressWarnings("unchecked")
	private static Class<? extends Identity>[] types = new Class[]{IntegerIdentity.class, LongIdentity.class, StringIdentity.class , UUIDIdentity.class};
		
	public StringIdentityCoersor(){}
	
	@Override
	public <T extends Identity> T coerceForward(String value, Class<T> type) {
		if (value ==null || value.trim().isEmpty()){
			return null;
		}
		
		for (Class<? extends Identity> t : types){
			if (t.isAssignableFrom(type)){
				ReflectedMethod valueOfMethod = Introspector.of(t).inspect().staticMethods()
					.named("valueOf")
					.withParametersType(new Class[]{String.class})
					.retrive();
				
				if (valueOfMethod == null){
					throw new CoersionException("Cannot find static method valueOf for type " + t);
				}
				return type.cast(valueOfMethod.invoke(null, value));
			}
		}
		
		if (Identity.class.isAssignableFrom(type)){
			// determine type from value
			return fromString(value,type);
		} else {
			throw new CoersionException("Cannot coerce from " + value + " to " + type);
		}

	}
	private <T extends Identity> T fromString(String value, Class<T>  type){
		String[] parts = value.split("_");
		for (Class<? extends Identity> t : types){
			if (parts[0].equals(t.getSimpleName().substring(0,1))){
				ReflectedMethod valueOfMethod = Introspector.of(t).inspect().staticMethods()
					.named("valueOf")
					.withParametersType(new Class[]{String.class})
					.retrive();
				
				if (valueOfMethod == null){
					throw new CoersionException("Cannot find static method valueOf for type " + t);
				}
				return type.cast(valueOfMethod.invoke(null, parts[1]));
			}
		}
		
		throw new CoersionException("Cannot coerce from " + value + " to " + type); 
		
	}
	
	private String toString(Identity value){
		return value.getClass().getSimpleName().substring(0,1) + "_" + value.toString();
		
	}
	
	@Override
	public <T extends String> T coerceReverse(Identity value, Class<T> type) {
		if (value ==null){
			return null;
		}
		
		return type.cast(toString(value));
	}




}
