package org.middleheaven.reflection;

import java.lang.annotation.Annotation;

public interface TypeMember {

	
	public boolean isAnnotationPresent(Class<? extends Annotation> annotation);
}
