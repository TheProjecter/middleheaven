package org.middleheaven.core.wiring;
import java.lang.annotation.Annotation;
import java.util.Map;

import org.middleheaven.core.annotations.Default;
import org.middleheaven.core.annotations.ScopeSpecification;
import org.middleheaven.core.annotations.Shared;
import org.middleheaven.core.reflection.inspection.Introspector;

/**
 * Standard implementation of {@link BindingBuilder}.
 * 
 * @param <T> the type being binded.
 */
public class StandardBindingBuilder<T> implements BindingBuilder<T> {

	public static class StandardQualificationBuilder<T> implements QualificationBuilder<T>{
	
		private StandardBindingBuilder<T> standardBindingBuilder;

		/**
		 * Constructor.
		 * @param standardBindingBuilder
		 */
		public StandardQualificationBuilder(
				StandardBindingBuilder<T> standardBindingBuilder) {
			this.standardBindingBuilder = standardBindingBuilder;
		}

		public void toInstance(T object){
			standardBindingBuilder.binding.setResolver(new InstanceResolver(object));
			standardBindingBuilder.binding.setProvider(new ObjectProvider(object));
			
			if (standardBindingBuilder.binding.getScope() == null || standardBindingBuilder.binding.getScope().equals(Default.class.getSimpleName().toLowerCase())){
				standardBindingBuilder.binding.setScope("shared");
			}
			
			standardBindingBuilder.binder.addBinding(standardBindingBuilder.binding);

		} 
		
		public void toResolver(Class<? extends Resolver> type){
			Resolver r = (Resolver) standardBindingBuilder.binder.getInstance(WiringQuery.search(type));
			standardBindingBuilder.binding.setResolver(r);
			
			standardBindingBuilder.binder.addBinding(standardBindingBuilder.binding);
	
		}
		
		public void to(Class<? extends T> type){
			

			BeanDependencyModel model = standardBindingBuilder.binder.getBeanModel(type);
			
			standardBindingBuilder.binding.addParams(model.getParams());
			standardBindingBuilder.binding.setProfiles(model.getProfiles());
			standardBindingBuilder.binding.setResolver(FactoryResolver.instanceFor(model));
			standardBindingBuilder.binder.addBinding(standardBindingBuilder.binding);

		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public QualificationBuilder<T> named(String name){
			standardBindingBuilder.binding.addParam("name", name);
			return this;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public QualificationBuilder<T> profiled(String profile){
			standardBindingBuilder.binding.getProfiles().add(profile);
			return this;
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public QualificationBuilder<T> lazy(){
			standardBindingBuilder.binding.setLazy(true);
			return this;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public QualificationBuilder<T> in(Class<? extends Annotation> scope){
			if (!Introspector.of(scope).isAnnotadedWith(ScopeSpecification.class)){
				throw new IllegalArgumentException(scope.getName() + " is not a " + ScopeSpecification.class.getName());
			}
			standardBindingBuilder.binding.setScope(WiringUtils.readScope(scope));
			return this;
		}
		
		

		/**
		 * {@inheritDoc}
		 */
		@Override
		public QualificationBuilder<T> withParams(Map<String, Object> params) {
			standardBindingBuilder.binding.addParams(params);
			return this;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public QualificationBuilder<T> withParam(String key, Object value) {
			standardBindingBuilder.binding.addParam(key,value);
			return this;
		}


		/**
		 * {@inheritDoc}
		 */
		@Override
		public QualificationBuilder<T> inSharedScope() {
			return in(Shared.class);
		}


		/**
		 * {@inheritDoc}
		 */
		@Override
		public QualificationBuilder<T> inDefaultScope() {
			return in(Default.class);
		}
		
		
	}
	
	protected Binding binding;
	protected EditableBinder binder;
	protected StandardQualificationBuilder<T>  qualification = new StandardQualificationBuilder<T>(this);
	
	StandardBindingBuilder (EditableBinder binder , Class<T> type){
		this.binder = binder;
		this.binding = new Binding();


		binding.setSourceType(type);
	}


	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public QualificationBuilder<T> named(String name){
		binding.addParam("name", name);
		return this.qualification;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public QualificationBuilder<T> profiled(String profile){
		binding.getProfiles().add(profile);
		return this.qualification;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public QualificationBuilder<T> lazy(){
		binding.setLazy(true);
		return this.qualification;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public QualificationBuilder<T> in(Class<? extends Annotation> scope){
		if (!Introspector.of(scope).isAnnotadedWith(ScopeSpecification.class)){
			throw new IllegalArgumentException(scope.getName() + " is not a " + ScopeSpecification.class.getName());
		}
		binding.setScope(WiringUtils.readScope(scope));
		return this.qualification;
	}
	
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public QualificationBuilder<T> withParams(Map<String, Object> params) {
		binding.addParams(params);
		return this.qualification;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public QualificationBuilder<T> withParam(String key, Object value) {
		binding.addParam(key,value);
		return this.qualification;
	}




	/**
	 * {@inheritDoc}
	 */
	@Override
	public QualificationBuilder<T> inSharedScope() {
		return in(Shared.class);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public QualificationBuilder<T> inDefaultScope() {
		return in(Default.class);
	}
	

	

}
