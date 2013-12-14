/**
 * 
 */
package org.middleheaven.core.wiring;

import java.util.Collections;
import java.util.Map;

import org.middleheaven.reflection.ReflectedClass;

/**
 * 
 */
public class WiringQuery {

	public static WiringQuery search(Class<?> type){
		return search(type, Collections.<String,Object>emptyMap());
	}

	public static WiringQuery search(Class<?> type, Map<String, Object> params){
		return new WiringQuery(WiringSpecification.search(type, params));
	}
	
	protected static WiringQuery search(ReflectedClass<?> type){
		return new WiringQuery(new WiringSpecification(type,Collections.<String,Object>emptyMap()));
	}
	
	public static WiringQuery search(WiringSpecification spec){
		return new WiringQuery(spec);
	}

	private WiringSpecification specification;
	private WiringTarget target;
	
	private WiringQuery (WiringSpecification specification){
		this.specification = specification;
	}
	
	public WiringQuery on (WiringTarget target){
		this.target = target;
		return this;
	}
	
	public ReflectedClass<?> getContract(){
		return specification.getContract();
	}
	
	public Map<String, Object> getParams(){
		return specification.getParams();
	}
	
	public WiringTarget getTarget(){
		return this.target;
	}

	/**
	 * @return
	 */
	public boolean isRequired() {
		return this.specification.isRequired();
	}

	/**
	 * @param string
	 * @return
	 */
	public Object getParam(String key) {
		return this.specification.getParam(key);
	}
	
	public String toString(){
		return this.specification.toString();
	}
	
}
