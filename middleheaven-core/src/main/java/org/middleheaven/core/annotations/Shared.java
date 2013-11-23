package org.middleheaven.core.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Indicates the instance of the object can be shared. This means only one instance of the object will be created.
 * This is not the default instance scope. 
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({
	ElementType.FIELD, 
	ElementType.PARAMETER,
	ElementType.TYPE
	})
@Documented
@Inherited
@ScopeSpecification(name = "shared")
public @interface Shared {}
