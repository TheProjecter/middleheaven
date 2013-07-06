package org.middleheaven.core.wiring;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.middleheaven.collections.CollectionUtils;
import org.middleheaven.collections.Mergable;

/**
 * Maintains data about the wiring contract to be fulfill by the wiring context.  
 *
 * @param <T> the type that must be retrieved
 */
public class WiringSpecification implements Mergable<WiringSpecification> {

	private Class<?> contract;
	private Map<String, Object> params;
	private boolean shareable = true;
	private boolean required = true;

	public static  WiringSpecification search(Class<?> contract) {
		return search(contract, Collections.<String, Object>emptyMap());
	}

	public static WiringSpecification search(Class<?> contract, Map<String,Object> params) {
		return new WiringSpecification(contract, params);
	}

	private WiringSpecification(Class<?> contract, Map<String, Object> params) {
		this.contract = contract;
		this.params = params;

	}

	public String toString(){
		return contract.toString() + params;
	}

	public Map<String, Object > getParams(){
		return Collections.unmodifiableMap(this.params);
	}

	public Object getParam(String key){
		return params.get(key);
	}

	public Class<?> getContract() {
		return contract;
	}

	public void setShareable(boolean shareable) {
		this.shareable = shareable;
	}
	
	public void setRequired(boolean required) {
		this.required = required;
	}

	/**
	 * Indicated if the resolved object can be shared. If not the context will create another object of the same type.
	 * If the type is a singleton, and exception wil be throwned. 
	 * @return {@code true} if the resolved object can be wired to other points, {@code false} otherwise.
	 */
	public boolean isShareable() {
		return shareable;
	}

	public boolean isRequired() {
		return required;
	}
	
	public boolean equals(Object other){
		return other instanceof WiringSpecification && equalsOther((WiringSpecification)other);
	}
	
	private boolean equalsOther(WiringSpecification other){
		return this.contract.getName().equals(other.contract.getName()) && 
		CollectionUtils.equalContents(this.params, other.params);
	}
	
	public int hashCode(){
		return this.contract.getName().hashCode();
	}


	@Override
	public boolean canMergeWith(WiringSpecification other) {
		return this.contract.getName().equals(other.contract.getName());
	}

	@Override
	public WiringSpecification merge(WiringSpecification other) {
		
		Map<String,Object> params = new HashMap<String, Object>(this.params);
		params.putAll(other.params);

		return new WiringSpecification(
			this.contract,
			params
		);
	}
}
