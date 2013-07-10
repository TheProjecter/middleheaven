package org.middleheaven.core.wiring.connectors;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.middleheaven.collections.Enumerable;
import org.middleheaven.core.reflection.MethodHandler;
import org.middleheaven.core.reflection.inspection.ClassIntrospector;
import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.core.wiring.AbstractAnnotationBasedWiringModelParser;
import org.middleheaven.core.wiring.BeanDependencyModel;
import org.middleheaven.core.wiring.ConnectableBinder;
import org.middleheaven.core.wiring.ConstructorWiringPoint;
import org.middleheaven.core.wiring.FieldAfterWiringPoint;
import org.middleheaven.core.wiring.MethodAfterWiringPoint;
import org.middleheaven.core.wiring.WiringConnector;
import org.middleheaven.core.wiring.WiringSpecification;
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
		
		@Override
		public <T> void readBeanModel(Class<T> type, final BeanDependencyModel model) {


			// constructor
			ClassIntrospector<T> introspector = Introspector.of(type);

			Enumerable<Constructor<T>> constructors = introspector.inspect()
			.constructors().retriveAll();

			if (constructors.size()==1){
				Constructor constructor = constructors.getFirst();
				//ok, use this one
				WiringSpecification[] params = this.readParamsSpecification(constructor);

				model.setProducingWiringPoint(new ConstructorWiringPoint(constructors.getFirst(),null,params));
			} else {
				// search for one with parameters annotated with @Resource or @Resources

				outer:for (Constructor<T> constructor : constructors){
					Annotation[][] paramsAnnotation = constructor.getParameterAnnotations();

					for (int p =0; p< paramsAnnotation.length;p++){
						// inner classes have a added parameter on index 0 that 
						// get annotations does not cover.
						// read from end to start


						int annotIndex = paramsAnnotation.length - 1 -p;

						for (Annotation a : paramsAnnotation[annotIndex]){

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
			introspector.inspect().fields().annotatedWith(Resource.class)
			.each(new Block<Field>(){

				@Override
				public void apply(Field f) {
					WiringSpecification spec = readParamsSpecification(f);

					model.addAfterWiringPoint(new FieldAfterWiringPoint(f, spec));
				}

			});


			// search all methods annotated with Resource
			introspector.inspect().methods().annotatedWith(Resource.class)
			.each( new Block<MethodHandler>(){

				@Override
				public void apply(MethodHandler method) {
					WiringSpecification[] spec = readParamsSpecification(method);

					model.addAfterWiringPoint(new MethodAfterWiringPoint(method,null,spec));
				}

			});

		}

		/*
		 * {@inheritDoc}
		 */
		@Override
		protected WiringSpecification parseAnnotations(
				Annotation[] annnnotations, Class<?> type) {

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

			WiringSpecification spec = WiringSpecification.search(type,params);

			spec.setRequired(isParamRequired);
			spec.setShareable(isParamShareable);

			return spec;
			
		}


	}
}
