package org.middleheaven.util.coersion;

import org.middleheaven.util.identity.Identity;
import org.middleheaven.util.identity.IntegerIdentity;
import org.middleheaven.util.identity.LongIdentity;


class NumberIdentityCoersor<N extends Number> extends AbstractIdentityCoersor<N> {

	private Class<? extends Number> numberClass;

	public static <T extends Number> NumberIdentityCoersor<T> newInstance(Class<T> numberClass){
		return new NumberIdentityCoersor<T>(numberClass);
	}
	
	private NumberIdentityCoersor(Class<? extends Number> numberClass) {
		this.numberClass = numberClass;
	}

	@Override
	public <T extends Identity> T coerceForward(N value, Class<T> type) {
		if (value ==null){
			return null;
		}
		if (type.isAssignableFrom(IntegerIdentity.class)){
			return type.cast(IntegerIdentity.valueOf(value.intValue()));
		} else if (type.isAssignableFrom(LongIdentity.class)){
			return type.cast(LongIdentity.valueOf(value.longValue()));
		}
		return null;
	}

	@Override
	public <T extends N> T coerceReverse(Identity value, Class<T> type) {
		if (value == null){
			return null;
		}
		if (value.getClass().isAssignableFrom(IntegerIdentity.class)){
			return type.cast(Integer.valueOf(value.toString()));
		} else if (value.getClass().isAssignableFrom(LongIdentity.class)){
			return type.cast(Long.valueOf(value.toString()));
		}
		return null;
	}



}
