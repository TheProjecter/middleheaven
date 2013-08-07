/**
 * 
 */
package org.middleheaven.util.property;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;

import org.middleheaven.collections.CollectionUtils;
import org.middleheaven.util.function.Function;

/**
 * 
 */
public class ComposedProperty<T extends Serializable> extends AbstractProperty<T> {



	public static <X extends Serializable> ComposedProperty<X> bind (String name, Function<X, Property[]> composition, Property<?> ... others ){
		return new ComposedProperty<X> (name, others , composition);
	}

	private Property<?>[] others;
	private Function<T, Property[]> composition;
	private T value;
	
	/**
	 * Constructor.
	 * @param others
	 */
	public ComposedProperty(String name, Property<?>[] others , Function<T, Property[]> composition) {
		super(name, null);
		this.others = CollectionUtils.duplicateArray(others);
		this.composition = composition;
		this.value = composition.apply(others);
		
		for (Property<?> p  : others){
			p.addListener( new PropertyChangeListener() {
				
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					T oldValue = value;
					T newValue = ComposedProperty.this.composition.apply(ComposedProperty.this.others);
					
					if (valuesDiffer(oldValue, newValue)){
						value = newValue;
						fireChange(oldValue, newValue);
					}
				}
			});
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public T get() {
		return value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Property<T> set(T value) {
		throw new UnsupportedOperationException("This is a composed property. It cannot be changed directly");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isReadOnly() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasValue() {
		return value != null;
	}

}
