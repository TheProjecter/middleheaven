package org.middleheaven.util.coersion;

/**
 * Coersor for {@link Boolean} and {@link Number}.
 */
public class NumberBooleanCoersor extends AbstractTypeCoersor<Number, Boolean> {

	
	private JavaNumberCoersor numberCoersor = new JavaNumberCoersor();
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Boolean> T coerceForward(Number value, Class<T> type) {
	
		return (T)(value==null ? null : (value.intValue() == 0 ? Boolean.FALSE : Boolean.TRUE ));
	}

	@Override
	public <T extends Number> T coerceReverse(Boolean value, Class<T> type) {

		final Integer v =(value==null ? null : (value.booleanValue() ? Integer.valueOf(1) : Integer.valueOf(0)));
		
		return numberCoersor.convert(v, type);
	}





}
