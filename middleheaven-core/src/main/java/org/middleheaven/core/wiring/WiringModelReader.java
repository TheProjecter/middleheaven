package org.middleheaven.core.wiring;



/**
 * Encapsulates an algorithm to determine wiring points for classes.
 * The algorithm can be based on the use of annotations and reflection to read them,
 * can be based on configurations preserved in external data soures like XML files or 
 * databases.
 * 
 */
public interface WiringModelReader {

	/**
	 * Determines wiring points for a given class.
	 * @param type
	 * @param model
	 */
	public <T> void readBeanModel(Class<T> type, BeanDependencyModel model);

}
