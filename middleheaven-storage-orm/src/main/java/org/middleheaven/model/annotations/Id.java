package org.middleheaven.model.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.middleheaven.util.identity.IntegerIdentity;

/**
 * Marks a property as the entity's identifier.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD , ElementType.METHOD})
@Documented
public @interface Id {
	
	/**
	 * Informs the identity object class. 
	 * Is mandatory if the identity property resolves to an interface or abstract class.
	 * @return the identity object class
	 */
	Class<?> type() default IntegerIdentity.class;
}
