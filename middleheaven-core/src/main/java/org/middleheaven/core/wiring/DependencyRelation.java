/**
 * 
 */
package org.middleheaven.core.wiring;

import java.util.HashMap;
import java.util.Map;

import org.middleheaven.collections.CollectionUtils;
import org.middleheaven.reflection.ReflectedClass;
import org.middleheaven.reflection.Reflector;



/**
 * 
 */
class DependencyRelation {

	
	private ReflectedClass<?> targetType;
	private boolean isRequired = false;
	
	private Map<String, Object> targetConstraints = new HashMap<String, Object>();
	private DependendableBean sourceBean;
	private boolean productionRelation = false;
	
	
	public DependencyRelation(Class<?> targetType) {
		super();
		this.targetType = Reflector.getReflector().reflect(targetType);
	}
	
	public DependencyRelation(ReflectedClass<?> targetType) {
		super();
		this.targetType = targetType;
	}
	
	public Map<String, Object> getTargetConstraints(){
		return this.targetConstraints;
	}
	
	public void addAllTargetConstraints(Map<String, Object>  all){
		 this.targetConstraints.putAll(all);
	}
	
	/**
	 * Obtains {@link Class<?>}.
	 * @return the targetType
	 */
	protected ReflectedClass<?> getTargetType() {
		return targetType;
	}
	/**
	 * Atributes {@link Class<?>}.
	 * @param targetType the targetType to set
	 */
	protected void setTargetType(ReflectedClass<?> targetType) {
		this.targetType = targetType;
	}
	/**
	 * Obtains {@link boolean}.
	 * @return the isRequired
	 */
	protected boolean isRequired() {
		return isRequired;
	}
	/**
	 * Atributes {@link boolean}.
	 * @param isRequired the isRequired to set
	 */
	protected void setRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}


	/**
	 * @param dependendableBean
	 */
	public void setSourceDependency(DependendableBean sourceBean) {
		this.sourceBean = sourceBean;
	}


	/**
	 * Obtains {@link DependendableBean}.
	 * @return the sourceBean
	 */
	protected DependendableBean getSourceBean() {
		return sourceBean;
	}
	
	public String toString(){
		return sourceBean.toString() + "->" + targetType;
	}
	
	public int hashCode(){
		return this.sourceBean.toString().hashCode();
	}
	
	public boolean equals(Object other){
		return (other instanceof DependencyRelation) && equalsOther((DependencyRelation) other);
	}


	/**
	 * @param other
	 * @return
	 */
	private boolean equalsOther(DependencyRelation other) {
		
		return other.targetType.equals(this.targetType) && 
		this.isRequired == other.isRequired 
		&& this.sourceBean.equals(other.sourceBean)
		&& CollectionUtils.equalContents(this.targetConstraints, other.targetConstraints);
	}


	/**
	 * @return
	 */
	public boolean isProductionRelation() {
		return productionRelation;
	}


	/**
	 * Atributes {@link boolean}.
	 * @param productionRelation the productionRelation to set
	 */
	protected void setProductionRelation(boolean productionRelation) {
		this.productionRelation = productionRelation;
	}
	
	
}
