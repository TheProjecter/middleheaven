/**
 * 
 */
package org.middleheaven.util.property;

import java.beans.EventSetDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;

import org.middleheaven.core.reflection.ReflectionException;


/**
 * 
 */
public class BindedProperty<T> extends AbstractProperty<T> {



	public static <X> Property<X> bind(String myName, Object bean ){
		return bind( myName,  bean,  myName);
	}

	public static <X> Property<X> bind(String myName, Object bean, String objectProperty){
	
		try {
			PropertyDescriptor descriptor = null;
			for (PropertyDescriptor pd : Introspector.getBeanInfo(bean.getClass()).getPropertyDescriptors()){
				if (pd.getName().equals(objectProperty)){
					descriptor = pd;
					break;
				}
			}
			
			if (descriptor == null) {
				throw new ReflectionException("Property " + objectProperty + " does not existe for object of type " + bean.getClass());
			}
	
			BindedProperty<X> p = new BindedProperty<X>( myName,  bean,  descriptor);
			
			for (EventSetDescriptor evt : Introspector.getBeanInfo(bean.getClass()).getEventSetDescriptors()){
				if ("propertyChange".equals(evt.getName())){
					try {
						evt.getAddListenerMethod().invoke(bean, p.listener);
					} catch (Exception e) {
						throw ReflectionException.manage(e, bean.getClass());
					} 
				}
			}
			
			return p;
		} catch (IntrospectionException e) {
			throw new ReflectionException(e);
		}
		
		
	}

	private PropertyDescriptor descriptor;
	private Object target;

	private PropertyChangeListener listener = new PropertyChangeListener (){
		
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			fireChange((T) evt.getOldValue(), (T) evt.getNewValue());
		}
	};

	/**
	 * Constructor.
	 * @param myName
	 * @param bean
	 * @param objectProperty
	 */
	private BindedProperty(String myName, Object bean, PropertyDescriptor descriptor) {
		super(myName, descriptor.getPropertyType());
		this.descriptor = descriptor;
		this.target = bean;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public T get() {
		try {
			return (T) descriptor.getReadMethod().invoke(target, new Object[0]);
		} catch (Exception e) {
			throw ReflectionException.manage(e, target.getClass());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Property<T> set(T value) {

		T oldValue = this.get();

		if (valuesDiffer(oldValue, value)){
			
			try {
				descriptor.getWriteMethod().invoke(target, new Object[]{value});
			} catch (Exception e) {
				throw ReflectionException.manage(e, target.getClass());
			}
			
			fireChange( oldValue, value);

		}

		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isReadOnly() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasValue() {
		return get() != null;
	}



}
