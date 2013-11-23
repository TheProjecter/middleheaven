package org.middleheaven.core.metaclass;

import org.middleheaven.core.metaclass.MetaClass;
import org.middleheaven.core.metaclass.MetaPackage;

/**
 * An interface for a package object.
 * A {@link MetaPackage} provides an iterator for its MetaClasses.
 * @see MetaClass
 */
public interface MetaPackage extends Iterable<MetaClass>{

	/**
	 * The name of the package.
	 * @return name of the package.
	 */
	public String getName();
	

}
