/**
 * 
 */
package org.middleheaven.core.wiring;

import java.util.Iterator;

import org.middleheaven.collections.TransformedIterator;
import org.middleheaven.core.reflection.ClassSet;
import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.core.reflection.inspection.PackageIntrospector;
import org.middleheaven.util.function.Mapper;

/**
 * 
 */
public class ClassSetWiringBundle implements WiringItemBundle {

	
	/**
	 * 
	 */
	private static final class TypeMapper implements
			Mapper<WiringItem, Class<?>> {
		@Override
		public WiringItem apply(Class<?> obj) {
			return new TypeWiringItem(obj);
		}
	}

	private static TypeMapper typeMapper = new TypeMapper();
	private ClassSet contextClasses;

	/**
	 * 
	 * Constructor.
	 * @param set
	 */
	public ClassSetWiringBundle (ClassSet set){
		this.contextClasses = set;
	}
	
	public ClassSetWiringBundle (){
		this.contextClasses = new ClassSet();
	}

	/**
	 * 
	 * @param type
	 * @return
	 */
	public ClassSetWiringBundle add(Class<?> type){
		this.contextClasses.add(type);
		return this;
	}
	
	/**
	 * 
	 * @param pack
	 * @return
	 */
	public ClassSetWiringBundle add(Package pack){
		this.contextClasses.add(pack);
		return this;
	}
	
	/**
	 * 
	 * @param parentPackage
	 * @param includeSubPackages
	 * @return
	 */
	public ClassSetWiringBundle add(Package parentPackage, boolean includeSubPackages){
		
		contextClasses.add(parentPackage);

		if (includeSubPackages){

			for (PackageIntrospector p : Introspector.of(parentPackage).getSubpackages()){

				contextClasses.add(p.getIntrospected());

				add(p.getIntrospected(), true);
			}
		}

		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<WiringItem> iterator() {
		return TransformedIterator.transform(contextClasses.iterator(), typeMapper);
	}
	
	

}
