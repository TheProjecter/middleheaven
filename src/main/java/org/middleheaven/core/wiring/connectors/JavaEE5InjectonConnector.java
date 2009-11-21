package org.middleheaven.core.wiring.connectors;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.middleheaven.core.reflection.ClassIntrospector;
import org.middleheaven.core.reflection.Introspector;
import org.middleheaven.core.wiring.AbstractAnnotationBasedWiringModelParser;
import org.middleheaven.core.wiring.ConnectableBinder;
import org.middleheaven.core.wiring.ConstructorWiringPoint;
import org.middleheaven.core.wiring.FieldWiringPoint;
import org.middleheaven.core.wiring.MethodWiringPoint;
import org.middleheaven.core.wiring.ScoopingModel;
import org.middleheaven.core.wiring.WiringConnector;
import org.middleheaven.core.wiring.WiringModel;
import org.middleheaven.core.wiring.WiringSpecification;
import org.middleheaven.core.wiring.namedirectory.NameDirectoryScope;
import org.middleheaven.util.classification.BooleanClassifier;
import org.middleheaven.util.collections.EnhancedCollection;
import org.middleheaven.util.collections.Walker;

public class JavaEE5InjectonConnector implements WiringConnector {


	public JavaEE5InjectonConnector(){}

	@Override
	public void connect(ConnectableBinder binder) {

		binder.addWiringModelParser(new JavaEE5InjectonParser());
	}


	private static class JavaEE5InjectonParser extends AbstractAnnotationBasedWiringModelParser{

		@Override
		public <T> void readWiringModel(Class<T> type, final WiringModel model) {

			//if (model.getConstructorPoint()==null){
				// constructor
				ClassIntrospector<T> introspector = Introspector.of(type);
				
				EnhancedCollection<Constructor<T>> constructors = introspector.inspect()
				.constructors().retriveAll();
				
				if (constructors.size()==1){
					Constructor constructor = constructors.getFist();
					//ok, use this one
					WiringSpecification[] params = this.readParamsSpecification(constructor, new BooleanClassifier<Annotation>(){

						@Override
						public Boolean classify(Annotation a) {
							return a.annotationType().isAnnotationPresent(Resource.class);
						}
						
					});
					
					model.setConstructorPoint(new ConstructorWiringPoint(constructors.getFist(),null,params));
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
									model.setConstructorPoint(new ConstructorWiringPoint(constructor,null,null));
									break outer;
								}
							}
							
						}
					}
				}


			//}


			// injection points

			introspector.inspect().fields().annotatedWith(Resource.class)
			.each(new Walker<Field>(){

				@Override
				public void doWith(Field f) {
					WiringSpecification spec = readParamsSpecification(f, new BooleanClassifier<Annotation>(){

						@Override
						public Boolean classify(Annotation a) {
							return a.annotationType().isAnnotationPresent(Resource.class);
						}
					
					});
					
					model.addAfterWiringPoint(new FieldWiringPoint(f, spec));
				}
				
			});



			introspector.inspect().methods().annotatedWith(Resource.class)
			.each( new Walker<Method>(){

				@Override
				public void doWith(Method method) {
					WiringSpecification[] spec = readParamsSpecification(method, new BooleanClassifier<Annotation>(){

						@Override
						public Boolean classify(Annotation a) {
							return a.annotationType().equals(Resource.class);
						}
					
					});
					
					model.addAfterWiringPoint(new MethodWiringPoint(method,null,spec));
				}
				
			});
	
		}

		@Override
		public void readScoopingModel(Object obj, ScoopingModel model) {

			Set<Annotation> all = Introspector.of(obj.getClass()).inspect().annotations().retrive();
			
			for (Annotation a : all){
				if (a.annotationType().isAnnotationPresent(Resource.class)){
					Resource resource = a.annotationType().getAnnotation(Resource.class);
					model.addScope(NameDirectoryScope.class);
					model.addParam("name", resource.name());
					model.addParam("shareable",Boolean.valueOf(resource.shareable()).toString());
					
				}
			}
			
		}

	}
}