package org.middleheaven.reflection;

import java.lang.reflect.Modifier;

/**
 * Enumeration for member access
 */
public enum MemberAccess {

	PUBLIC,
	PROTECTED,
	PRIVATE,
	PACKAGE;
	
	/**
	 * 
	 * @param modifier the integer flag from {@link Modifier}.
	 * @return
	 */
	public boolean is(int modifier){
		switch (this){
		case PRIVATE:
			return Modifier.isPrivate(modifier);
		case PROTECTED:
			return Modifier.isProtected(modifier); 
		case PUBLIC:
			return Modifier.isPublic(modifier); 
		case PACKAGE:
			return !Modifier.isPrivate(modifier) && 
			!Modifier.isPublic(modifier) && 
			!Modifier.isProtected(modifier);
		default:
			throw new UnsupportedOperationException("Not recognized acess " + this);
		}
	}
}
