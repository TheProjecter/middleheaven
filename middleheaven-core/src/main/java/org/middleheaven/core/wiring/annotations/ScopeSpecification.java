package org.middleheaven.core.wiring.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Identifies that an annotation is a wiring scope defining annotation. 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({
	ElementType.ANNOTATION_TYPE
	})
@Documented
public @interface ScopeSpecification {

}