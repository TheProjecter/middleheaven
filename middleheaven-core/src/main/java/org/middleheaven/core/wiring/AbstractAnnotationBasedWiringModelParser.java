package org.middleheaven.core.wiring;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.middleheaven.core.reflection.MethodHandler;

/**
 * 
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractAnnotationBasedWiringModelParser implements WiringModelReader {

	
	protected WiringSpecification[] readParamsSpecification(Constructor constructor){
		return parseAnnotations(
				constructor.getParameterAnnotations(),
				constructor.getParameterTypes()
				);
	}
	
	protected WiringSpecification[] readParamsSpecification(MethodHandler method){

		return parseAnnotations(
				method.getParameterAnnotations(),
				method.getParameterTypes()
				);
	}

	protected WiringSpecification readParamsSpecification(Field field){
		
		return parseAnnotations(
				field.getAnnotations(),
				field.getType()
				
				);
	}

	protected final WiringSpecification[] parseAnnotations(Annotation[][] annnnotations ,Class<?>[] types){

		WiringSpecification[] specs = new WiringSpecification[types.length];

		for (int p =0; p< annnnotations.length;p++){
			// inner classes have a added parameter on index 0 that 
			// get annotations does not cover.
			// read from end to start

			int typeIndex = types.length - 1 - p;
			int annotIndex = annnnotations.length - 1 -p;

			boolean isParamRequired = true;
			boolean isParamShareable = true;
			
			specs[typeIndex] = parseAnnotations(annnnotations[annotIndex],types[typeIndex]);

			specs[typeIndex].setRequired(isParamRequired);
			specs[typeIndex].setShareable(isParamShareable);
		}

		return specs;
	}
	
	protected abstract WiringSpecification parseAnnotations(Annotation[] annnnotations ,Class<?> type);

	
	
	

}
