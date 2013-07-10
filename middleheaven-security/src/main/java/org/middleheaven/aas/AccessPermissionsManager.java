/**
 * 
 */
package org.middleheaven.aas;


/**
 * 
 */
public interface AccessPermissionsManager<R> {

	/**
	 * Returns the permissions nead to access the given resource.
	 * @param url
	 * @return
	 */
	public Permission[] getGuardPermission(R url);
}
