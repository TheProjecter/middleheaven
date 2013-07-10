/**
 * 
 */
package org.middleheaven.ui.layout;

import org.middleheaven.ui.UILayoutConstraint;

/**
 * 
 */
public enum UIClientLayoutConstraint implements UILayoutConstraint {

	/*The first window th user sees (after login)*/
	MAIN,
	/*A window that is displayed while the system loads (normally for desktop apps)*/
	SPLASH,
	/*A window that is displayed to adquire user credentials*/
	LOGIN,
	/*A window that is displayed when the user has not permission to use the system*/
	ACCESS_DENIED,
	/*A window that is displayed when an unexpected error occurs */
	ERROR,
	/*Other windows acesses by navigating the system*/
	NAVIGATIONAL
}
