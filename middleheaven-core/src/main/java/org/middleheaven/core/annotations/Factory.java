/**
 * 
 */
package org.middleheaven.core.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a methods to be a factory.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@BindingSpecification
@Documented
public @interface Factory {}
