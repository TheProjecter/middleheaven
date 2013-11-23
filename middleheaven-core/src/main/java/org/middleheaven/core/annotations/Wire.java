package org.middleheaven.core.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares the need for the injection toolbox to operate 
 * on the declared member
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({
	ElementType.CONSTRUCTOR,
	ElementType.METHOD,
	ElementType.FIELD, 
	ElementType.PARAMETER,
	ElementType.TYPE
	})
@BindingSpecification
@Inherited
@Documented
public @interface Wire {

	/**
	 * Indicates the dependency must exist and be wirable. true by default.
	 * @return true if the dependency must exist and be wirable
	 */
	boolean required() default true;
	
	/**
	 * Indicates the dependency can be shared with other objects, oterwise forces a new object to be wired
	 * 
	 * @return true if the dependency can be shared with other objects.
	 */
	boolean shareable() default true;
}
