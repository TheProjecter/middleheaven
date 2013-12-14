/**
 * 
 */
package org.middleheaven.core.wiring;

import org.middleheaven.reflection.ReflectedClass;
import org.middleheaven.reflection.ReflectedField;

/**
 * 
 */
class FieldWiringTarget implements WiringTarget {

	private ReflectedField field;
	private Object instance;

	/**
	 * Constructor.
	 * @param field
	 * @param object
	 */
	public FieldWiringTarget(ReflectedField field, Object instance) {
		this.field = field;
		this.instance = instance;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReflectedClass<?> getType() {
		return field.getValueType();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReflectedClass<?> getDeclaringType() {
		return field.getDeclaringClass();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getInstance() {
		return instance;
	}

}
