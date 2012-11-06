package org.middleheaven.core.annotations;

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
@Documented
@ScopeSpecification(name ="default")
public @interface Default {

}