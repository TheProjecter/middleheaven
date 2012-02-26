/**
 * 
 */
package org.middleheaven.core.wiring.mock;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.middleheaven.core.wiring.annotations.Qualifier;

/**
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({
	ElementType.FIELD,
	ElementType.TYPE,
	ElementType.PARAMETER
	})
@Documented
@Qualifier
public @interface GermanDicionary {

}
