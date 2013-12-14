package org.middleheaven.core.wiring;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import org.middleheaven.collections.CollectionUtils;
import org.middleheaven.collections.Mergeable;
import org.middleheaven.collections.Pair;
import org.middleheaven.collections.enumerable.Enumerable;
import org.middleheaven.collections.enumerable.Enumerables;
import org.middleheaven.reflection.ReflectedConstructor;
import org.middleheaven.reflection.ReflectedParameter;
import org.middleheaven.reflection.ReflectionException;

/**
 * Implements a {@link ProducingWiringPoint} using a {@link Constructor}.
 */
public final class ConstructorWiringPoint extends AbstractProducingWiringPoint {

	private ReflectedConstructor<?> constructor;


	/**
	 * 
	 * Constructor.
	 * @param constructor the constructor to use to produce the object.
	 * @param specification the constructor wiring specification.
	 * @param paramsSpecifications the params wiring specifications.
	 */
	public ConstructorWiringPoint(ReflectedConstructor<?> constructor, WiringSpecification specification, Enumerable<WiringSpecification> paramsSpecifications) {
		super(specification , paramsSpecifications);

		if (Modifier.isPrivate(constructor.getModifiers())){
			throw new IllegalArgumentException("Constructor is private");
		}
		this.constructor = constructor;

	}

	/**
	 * Constructs the object.
	 * @param <T> the class of the constructed object.
	 * @param binder the current wiring binder.
	 * @return the constructed object.
	 */
	@SuppressWarnings("unchecked")
	public <T> T produceWith(InstanceFactory factory){
		try {
			
			final Enumerable<ReflectedParameter> parameters = constructor.getParameters();
			Object[] params = new Object[parameters.size()];

			for( Pair<ReflectedParameter,WiringSpecification>  pair : constructor.getParameters().join(this.getParamsSpecifications()))
			{
				ReflectedParameter parameter = pair.left();
				WiringSpecification specs = pair.right();
				
				WiringTarget target = new ParameterWiringTarget(parameter.getType(),  constructor.getDeclaringClass(), null);
				
				params[parameter.getIndex()] = factory.getInstance(WiringQuery.search(specs).on(target));
				if (params[parameter.getIndex()] == null){ // wired constructor params are always mandatory even if configured otherwise 
					throw new WiringSpecificationNotSatisfiedException(this.constructor.getDeclaringClass().getReflectedType(), specs);
				}
			}

			return (T) constructor.newInstance(params);
		} catch (BindingException e){
			throw e;
		} catch (Exception e) {
			throw ReflectionException.manage(e);
		}
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public ProducingWiringPoint merge(ProducingWiringPoint other) {
		if (other ==null){
			return this;
		}

		WiringSpecification thisSpecification = this.getSpecification();
		if (thisSpecification !=null){
			thisSpecification = thisSpecification.merge(other.getSpecification());
		}
		
		Mergeable[] a = 	this.getParamsSpecifications().asArray();
		Mergeable[] b = 	other.getParamsSpecifications().asArray();
		
		Mergeable[] c  = CollectionUtils.merge(a,b);
		
		return new ConstructorWiringPoint (this.constructor, thisSpecification, Enumerables.asEnumerable(c).cast(WiringSpecification.class));
		
	}

}
