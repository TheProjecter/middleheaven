package org.middleheaven.collections;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public final class ParamsMap extends EnhancedMapAdapter<String,String> implements Serializable{

	private static final long serialVersionUID = -5704086450957356147L;

	public ParamsMap() {
		super(new HashMap<String,String>());
	}
	
	public ParamsMap(Map<String,String> other) {
		super(new HashMap<String,String>(other));
	}

	public ParamsMap setParam(String name, String value){
		this.put(name, value);
		return this;
	}

	
	
}
