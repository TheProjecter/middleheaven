/**
 * 
 */
package org.middleheaven.process.web.server;

import java.util.Iterator;
import java.util.Map;

import org.middleheaven.collections.TransformedIterator;
import org.middleheaven.process.Attribute;
import org.middleheaven.process.ContextScope;
import org.middleheaven.process.ObjectAttribute;
import org.middleheaven.process.ScopedAttributesResolutionStrategy;
import org.middleheaven.util.coersion.TypeCoercing;
import org.middleheaven.util.function.Function;

class ParametersMapScopedAttributesResolutionStrategy implements ScopedAttributesResolutionStrategy {

	/**
	 * 
	 */
	private static final class ObjectAttributeFunction implements
			Function<Attribute, Map.Entry<String, String[]>> {
		@Override
		public Attribute apply(Map.Entry<String, String[]> next) {
			return new ObjectAttribute(next.getKey(), next.getValue());
		}
	}

	private static final ObjectAttributeFunction objectAttributeFunction = new ObjectAttributeFunction();
	private Map<String, String[]> parameters;
	
	public ParametersMapScopedAttributesResolutionStrategy (Map<String, String[]> parameters){
		this.parameters= parameters;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isReaddable() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isWritable() {
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<Attribute> iterator() {
		return TransformedIterator.transform(parameters.entrySet().iterator(), objectAttributeFunction);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return parameters.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return parameters.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContextScope getScope() {
		return ContextScope.REQUEST_PARAMETERS;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T getAttribute(String name, Class<T> type) {
		String[] valueArray = parameters.get(name);
		
		if (valueArray != null){
			// the result is an array and the expected type is array
			if (type.isArray()){
				try{
					return type.cast(valueArray);
				} catch (ClassCastException e ){
					throw new ClassCastException("Cannot cast " + valueArray.getClass() + " to " + type);
				}
			} else {
				return TypeCoercing.coerce(valueArray[0], type);
			}
		} else if (type.isArray()){ // value is null and is expected array
			// parameters can only be Strings, so an array can only of string
			return TypeCoercing.coerce(new String[0], type);
		}
		
		return null;
	
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAttribute(String name, Object value) {
		throw new UnsupportedOperationException("Parameters scope is read only");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAttribute(String name) {
		throw new UnsupportedOperationException("Parameters scope is read only");
	}

	
	
}