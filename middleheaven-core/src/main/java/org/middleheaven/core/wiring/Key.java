package org.middleheaven.core.wiring;

import java.util.Map;

import org.middleheaven.collections.CollectionUtils;


final class Key {

	private String targetClassName;
	private Map<String, Object> params;

	public String toString(){
		return targetClassName + "[" + params.toString() + "]";
	}
	
	public static  Key keyFor(Class<?> targetClass, Map<String, Object> params) {
		return new Key(targetClass.getName(),params);
	}
	
	public static  Key keyFor(String targetClassName, Map<String, Object> params) {
		return new Key(targetClassName,params);
	}
	
	private Key(String targetClassName, Map<String, Object> params) {
		this.targetClassName = targetClassName;
		this.params = params;	
	}
	
	public int hashCode(){
		return targetClassName==null ? 0 :  targetClassName.hashCode();
	}
	
	public boolean equals(Object other){
		return other instanceof Key && equalsOther((Key)other);
	}
	
	private boolean equalsOther(Key other){
		return  other.targetClassName.equals(this.targetClassName) &&
		 	CollectionUtils.equalContents(this.params, other.params);

	}

}
