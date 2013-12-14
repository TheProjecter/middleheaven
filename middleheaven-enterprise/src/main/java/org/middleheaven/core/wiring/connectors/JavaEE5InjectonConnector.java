package org.middleheaven.core.wiring.connectors;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.middleheaven.collections.enumerable.Enumerable;
import org.middleheaven.core.wiring.AbstractAnnotationBasedWiringModelParser;
import org.middleheaven.core.wiring.BeanDependencyModel;
import org.middleheaven.core.wiring.ConnectableBinder;
import org.middleheaven.core.wiring.ConstructorWiringPoint;
import org.middleheaven.core.wiring.FieldAfterWiringPoint;
import org.middleheaven.core.wiring.MethodAfterWiringPoint;
import org.middleheaven.core.wiring.WiringConnector;
import org.middleheaven.core.wiring.WiringSpecification;
import org.middleheaven.reflection.ReflectedClass;
import org.middleheaven.reflection.ReflectedConstructor;
import org.middleheaven.reflection.ReflectedField;
import org.middleheaven.reflection.ReflectedMethod;
import org.middleheaven.reflection.ReflectedParameter;
import org.middleheaven.reflection.Reflector;
import org.middleheaven.util.function.Block;

/**
 * {@link WiringConnector} that adds Java EE 5 annotations reading.
 * 
 * The @{@link Resource} is used as a marked for were to wire objects.
 */
public class JavaEE5InjectonConnector implements WiringConnector {

	/**
	 * 
	 * Constructor.
	 */
	public JavaEE5InjectonConnector(){}

	@Override
	public void connect(ConnectableBinder binder) {

		binder.addWiringModelParser(new JavaEE5InjectonParser());
	}


	private static final class JavaEE5InjectonParser extends AbstractAnnotationBasedWiringModelParser{

		private JavaEE5InjectonParser (){}
		
		@SuppressWarnings("unchecked")
		@Override
		public <T> void readBeanModel(Class<T> atype, final BeanDependencyModel model) {

			ReflectedClass<T> type = Reflector.getReflector().reflect(atype);
					
			// constructor
			Enumerable<ReflectedConstructor<T>> constructors = type.getConstructors();

			if (constructors.size()==1){
				ReflectedConstructor<T> constructor = constructors.getFirst();
				//ok, use this one
				Enumerable<WiringSpecification> params = this.readParamsSpecification(constructor);

				model.setProducingWiringPoint(new ConstructorWiringPoint(constructors.getFirst(),null,params));
			} else {
				// search for one with parameters annotated with @Resource or @Resources

				outer:for (ReflectedConstructor<T> constructor : constructors){
					
					for (ReflectedParameter parameter : constructor.getParameters()){
						// inner classes have a added parameter on index 0 that 
						// get annotations does not cover.
						// read from end to start

						for (Annotation a : parameter.getAnnotations()){
							if (a.annotationType().isAnnotationPresent(Resource.class)){
								model.setProducingWiringPoint(new ConstructorWiringPoint(constructor,null,null));
								break outer;
							}
						}

					}
				}
			}


			// injection points

			// search all fields annotated with Resource
			type.inspect().fields().annotatedWith(Resource.class)
			.each(new Block<ReflectedField>(){

				@Override
				public void apply(ReflectedField f) {
					model.addAfterWiringPoint(new FieldAfterWiringPoint(f, readParamsSpecification(f)));
				}

			});


			// search all methods annotated with Resource
			type.inspect().methods().annotatedWith(Resource.class)
			.each( new Block<ReflectedMethod>(){

				@Override
				public void apply(ReflectedMethod method) {
					model.addAfterWiringPoint(new MethodAfterWiringPoint(method,null,readParamsSpecification(method)));
				}

			});

		}

		/*
		 * {@inheritDoc}
		 */
		@Override
		protected WiringSpecification parseAnnotations(Enumerable<Annotation> annnnotations, ReflectedClass<?> type) {

			boolean isParamRequired = true;
			boolean isParamShareable = true;

			Map<String, Object> params = new HashMap<String, Object>();

			for (Annotation a : annnnotations){

				if (Resource.class.isAssignableFrom(a.annotationType())){
					Resource resource = (Resource)a;
					
					params.put("name", resource.name());
					
					isParamShareable = resource.shareable();
				}
				
			}

			WiringSpecification spec = WiringSpecification.search(type.getReflectedType(),params);

			spec.setRequired(isParamRequired);
			spec.setShareable(isParamShareable);

			return spec;
			
		}


	}
}
