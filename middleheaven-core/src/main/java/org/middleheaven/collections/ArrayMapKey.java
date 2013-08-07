package org.middleheaven.collections;

import java.util.Arrays;

import org.middleheaven.util.Hash;

/**
 * ComposedMap key composed by an array of objects.
 * The keys are equals if the objects are the same, and in the same order. 
 */
public final class ArrayMapKey extends ComposedMapKey{

	private Object[] objects;

	public ArrayMapKey(Object ... objects){
		this.objects = objects;
	}
	
	public Object[] getObjects(){
		return CollectionUtils.duplicateArray(this.objects);
	}
	
	public boolean equals(Object other){
		return other instanceof ArrayMapKey && Arrays.equals(((ArrayMapKey)other).objects , this.objects);
	}
	
	public int hashCode(){
		return Hash.hash(objects).hashCode();
	}
}
