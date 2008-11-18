package org.middleheaven.core.wiring;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({
	ElementType.FIELD, 
	ElementType.PARAMETER
	})
@BindingSpecification
@Documented
public @interface Property {

	String value();
}