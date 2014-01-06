package org.middleheaven.validation.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares the need for validation within an interval. 
 * Informs the limits and inclusions of the interval
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({
	ElementType.FIELD
	})
@Documented
public @interface ValidateInterval {

	double start();
	double end();
}
