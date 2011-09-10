package org.middleheaven.io.repository;

public interface ManagedFilePath {

	
	public ManagedFileRepository getManagedFileRepository();
	
	
	/**
	 * A path is absolute if it does not need to be combined with other paths to point to a real file location within the repository.
	 * @return
	 */
	public boolean isAbsolute();

	/**
	 *     Returns the base name of this file. The base name is the last element of the file name. For example the base name of /somefolder/somefile is somefile.
	 *     The root of a file system has an empty base name.
	 *     Returns:
	 *      The base name. Never returns null.
	 * @return
	 */
	public String getBaseName();

	/**
	 *     Returns the extension of this file name.

    Returns:
        The extension. Returns an empty string if the name has no extension.
	 * @return
	 */
	public String getExtension();


	/**
	 * Returns the depth of this file name, within its file system. The depth of the root of a file system is 0. The depth of any other file is 1 + the depth of its parent. 

	 * @return
	 */
	int getNameCount();


	public String getName(int index);
	
	/**
	 *     Returns the absolute path of this file, within its file system. This path is normalised, so that . and .. elements have been removed. Also, the path only contains / as its separator character. The path always starts with /

    The root of a file system has / as its absolute path.

    Returns:
        The path. Never returns null.


	 * @return
	 */
	public String getPath();


	public ManagedFilePath getParent();



	/**
	 * @return the path's last name. If it is a file it will contain an extension. 
	 */
	public String getLastName();

	public String getFirstName();
	
	/**
	 * @return
	 */
	public ManagedFilePath getRoot();

	/**
	 * This method transforms the given name to a path using this path as basis.
	 * If this path represents /a/b then resolve will return /a/b/name
	 * 
	 * @param name
	 * @return
	 */
	public ManagedFilePath resolve(String name);
	
	/**
	 * This method transforms the given name to a path using this path parent as basis.
	 * If this path represents /a/b/c then resolve will return /a/b/name
	 * 
	 * @param name
	 * @return
	 */
	public ManagedFilePath resolveSibling(String name);

	/**
	 * @param path
	 * @return
	 */
	public ManagedFilePath resolve(ManagedFilePath path);

	/**
	 * This method transforms the given name to a path using this path parent as basis.
	 * If this path represents /a/b/c and <code>path</code> represent /a/d/f then relativize will return ../../d/f    
	 * If this path represents /a/b/c and <code>path</code> represent /a/b/c/f then relativize will return f    
	 *     
	 *     p.relativize(p.resolve(q)).equals(q) 
	 *     
	 * @param path
	 * @return
	 */
	public ManagedFilePath relativize(ManagedFilePath path);

}
