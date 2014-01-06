package org.middleheaven.validation.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.middleheaven.validation.Validator;

/**
 * Declares the need for validation. 
 * Informs the validator to use
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({
	ElementType.FIELD
	})
@Documented
public @interface Validate {
	
	/**
	 * @return <code>Validator</code> to use
	 */
	Class<Validator<?>> validator(); 
	
	/**
	 * @return <code>true</code> if fields marked with transient are to be ignored; <code>false</code> otherwise.
	 * Defaults to <code>true</code>
	 */
	boolean ignoreTransient() default true;
	
}
