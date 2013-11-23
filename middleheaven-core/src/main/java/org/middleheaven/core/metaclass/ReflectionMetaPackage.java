package org.middleheaven.core.metaclass;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.middleheaven.core.reflection.inspection.Introspector;

/**
 * MetaPackage based on a reflection of {@link Package}.
 */
public class ReflectionMetaPackage implements MetaPackage {

	
	private Package packageObject;
	private Collection<MetaClass> classes = new HashSet<MetaClass>();
	
	/**
	 * 
	 * Constructor.
	 * @param packageObject the package to analyze.
	 */
	@SuppressWarnings("rawtypes")
	public ReflectionMetaPackage (Package packageObject){
		this.packageObject = packageObject;
		
		for ( Class c : Introspector.of(packageObject).getClasses()){
			classes.add(new ReflectionMetaClass(c));
		}
	}
	
	@Override
	public Iterator<MetaClass> iterator() {
		return classes.iterator();
	}

	@Override
	public String getName() {
		return packageObject.getName();
	}

}
