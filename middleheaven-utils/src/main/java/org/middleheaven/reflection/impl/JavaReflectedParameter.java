/**
 * 
 */
package org.middleheaven.reflection.impl;

import java.lang.annotation.Annotation;

import org.middleheaven.collections.enumerable.Enumerable;
import org.middleheaven.collections.enumerable.Enumerables;
import org.middleheaven.reflection.ReflectedClass;
import org.middleheaven.reflection.ReflectedParameter;
import org.middleheaven.util.Maybe;

/**
 * 
 */
public class JavaReflectedParameter implements ReflectedParameter {

	private int index;
	private JavaReflectedParametersEnumerable parent;

	/**
	 * Constructor.
	 * @param method
	 * @param index
	 */
	public JavaReflectedParameter(JavaReflectedParametersEnumerable parent, int index) {
		this.parent = parent;
		this.index = index;
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReflectedClass<?> getType() {
		return JavaReflectedClass.valueOf(parent.getParameterTypes()[index]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAnnotationPresent(Class<? extends Annotation> type) {
		return getAnnotation(type).isPresent();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <A extends Annotation> Maybe<A> getAnnotation(Class<A> type) {
		Annotation[] annotations = parent.getParameterAnnotations()[index];
		for (int i =0; i < annotations.length; i++){
			A annot = (A) annotations[i];
			if (type.isInstance(annot)){
				return Maybe.of(annot);
			}
		}
		return Maybe.absent();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enumerable<Annotation> getAnnotations() {
		return Enumerables.asEnumerable(parent.getParameterAnnotations()[index]);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getIndex() {
		return index;
	}

}
