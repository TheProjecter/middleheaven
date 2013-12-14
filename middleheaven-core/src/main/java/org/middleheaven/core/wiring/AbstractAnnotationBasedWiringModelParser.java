package org.middleheaven.core.wiring;

import java.lang.annotation.Annotation;

import org.middleheaven.collections.enumerable.Enumerable;
import org.middleheaven.reflection.ReflectedClass;
import org.middleheaven.reflection.ReflectedConstructor;
import org.middleheaven.reflection.ReflectedField;
import org.middleheaven.reflection.ReflectedMethod;
import org.middleheaven.reflection.ReflectedParameter;
import org.middleheaven.util.function.Function;


/**
 * 
 */
public abstract class AbstractAnnotationBasedWiringModelParser implements WiringModelReader {


	protected Enumerable<WiringSpecification> readParamsSpecification(ReflectedConstructor<?> constructor){
		return parseAnnotations(constructor.getParameters());
	}

	protected Enumerable<WiringSpecification> readParamsSpecification(ReflectedMethod method){
		return parseAnnotations(method.getParameters());
	}

	protected WiringSpecification readParamsSpecification(ReflectedField field){

		return parseAnnotations(field.getAnnotations(),field.getValueType());
	}

	protected final Enumerable<WiringSpecification> parseAnnotations(Enumerable<ReflectedParameter> parameters){

		return parameters.map(new Function<WiringSpecification, ReflectedParameter>(){

			@Override
			public WiringSpecification apply(ReflectedParameter parameter) {
				// TODO inner classes have a added parameter on index 0 that 
				// get annotations does not cover.
				// read from end to start
				
				return parseAnnotations(parameter.getAnnotations(), parameter.getType());
			}
			
		});
		

	}

	protected abstract WiringSpecification parseAnnotations(Enumerable<Annotation> annnnotations ,ReflectedClass<?> type);





}
