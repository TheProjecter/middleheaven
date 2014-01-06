/**
 * 
 */
package org.middleheaven.reflection.impl;

import org.middleheaven.reflection.ReflectedPackage;

/**
 * 
 */
public class JavaReflectedPackage implements ReflectedPackage {

	/**
	 * @param package1
	 * @return
	 */
	public static ReflectedPackage valueOf(Package thePackage) {
		return new JavaReflectedPackage(thePackage);
	}

	private Package thePackage;
	
	private JavaReflectedPackage(Package thePackage){
		this.thePackage = thePackage;
	}

}
