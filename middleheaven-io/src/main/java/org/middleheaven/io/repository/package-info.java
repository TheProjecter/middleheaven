/**
 * Managed IO Repositories. 
 * Managed Repositories abstract the real location of a file. With this abstraction it possible, for example, to work 
 * with a zip file like it was a file system. Or access files on a web server like on a file system.
 * Different access is considered to repositories. Some are read only, some are read and write
 */
package org.middleheaven.io.repository;