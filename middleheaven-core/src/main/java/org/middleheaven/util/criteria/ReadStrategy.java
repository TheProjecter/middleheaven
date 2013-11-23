package org.middleheaven.util.criteria;

import org.middleheaven.util.Hash;


public class ReadStrategy {

	private boolean isReadOnly = false;
	private boolean isFowardOnly = false;
	private int maxFetchDeep = 2;

	public static ReadStrategy defaultStrategy(){
		return fowardReadOnly();
	}

	
	public static ReadStrategy editableRandomAccess(){
		return new ReadStrategy(false,false);
	}

	public static ReadStrategy fowardReadOnly(){
		return new ReadStrategy(true,true);
	}

	private ReadStrategy(boolean isReadOnly, boolean isFowardOnly) {
		this.isReadOnly = isReadOnly;
		this.isFowardOnly = isFowardOnly;
	}

	/**
	 * Sets the max fecth deep for the read. 
	 * A deep of zero means that only the query target type objects will be loaded.
	 * A deep of 1 means that the query target type objects and it's imediate dependency types will be loaded.
	 * A deep of <bold>n</bold> means that the query target type objects and it's <bold>n</bold> imediate dependency types will be loaded.
	 * 
	 * Objects will be instanciated until the n+1 level, however those objects in the last level will only have their's identity loaded.
	 * @param deep the fecth deep.
	 * @return this object.
	 */
	public ReadStrategy setMaxFetchDeep(int deep){
		this.maxFetchDeep = deep;
		return this;
	}
	
	public int getMaxFetchDeep(){
		return maxFetchDeep;
	}
	
	
	public boolean isReadOnly() {
		return isReadOnly;
	}
	public boolean isFowardOnly() {
		return isFowardOnly;
	}

	public ReadStrategy readOnly(){
		return new ReadStrategy(true,this.isFowardOnly);
	}

	public ReadStrategy fowardOnly(){
		return new ReadStrategy(this.isReadOnly,true);
	}

	public final boolean equals(Object other){
		return other instanceof ReadStrategy && equalsOther((ReadStrategy) other);
	}

	public int hashCode(){
		return Hash.hash(isFowardOnly).hash(isReadOnly).hashCode();
	}

	private boolean equalsOther(ReadStrategy other){
		return this.isFowardOnly == other.isFowardOnly &&
				this.isReadOnly == other.isReadOnly &&
				this.maxFetchDeep == other.maxFetchDeep;
	}

	@Override
	public String toString() {
		return "ReadStrategy [isFowardOnly=" + isFowardOnly + ", isReadOnly="
				+ isReadOnly + "]";
	}


}
