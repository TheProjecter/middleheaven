/**
 * 
 */
package org.middleheaven.io.repository.ftp;

/**
 * 
 */
public interface FtpCredentials {

	
	public String getFtpAddress();
	public String getUsername();
	public String getPassword();
	public boolean isAnonymous();
}
