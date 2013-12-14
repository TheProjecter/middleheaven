package org.middleheaven.core.wiring;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.middleheaven.reflection.ReflectedClass;
import org.middleheaven.reflection.Reflector;


/**
 * A model for wiring.
 * The wiring model consists of construct-point where the object is created/retrieved and multiple after-points where the object is injected.
 * 
 */
public class BeanDependencyModel {

	private final Map<String, Object > params = new HashMap<String, Object>();
	private final List<String> scopes = new LinkedList<String>();
	

	private final Collection<PublishPoint> publishPoints = new LinkedList<PublishPoint>();
	private final Collection<AfterWiringPoint> afterpoints = new HashSet<AfterWiringPoint>();
	
	private ProducingWiringPoint point;
	private PostCreatePoint postCreatePoint = EmptyCallPoint.getInstance();
	private PreDestroiPoint preDestroiPoint = EmptyCallPoint.getInstance();
	
	private ReflectedClass<?> type;
	private Collection<ReflectedClass<?>> contractTypes = new ArrayList<ReflectedClass<?>>();

	private ProfilesBag profiles = new ProfilesBag();

	public BeanDependencyModel(Class<?> type){
		this(Reflector.getReflector().reflect(type));
	}
	
	/**
	 * 
	 * Constructor.
	 */
	public BeanDependencyModel(ReflectedClass<?> type){
		this.type = type;
		this.contractTypes.add(type);
	}


	/**
	 * Obtains {@link Class<?>}.
	 * @return the contractType
	 */
	public Collection<ReflectedClass<?>> getContractTypes() {
		return contractTypes;
	}



	/**
	 * Atributes {@link Class<?>}.
	 * @param contractType the contractType to set
	 */
	public void addContractType(ReflectedClass<?> contractType) {
		this.contractTypes.add(contractType);
	}



	/**
	 * Obtains the {@link ProducingWiringPoint} that will obtain the object.
	 * @return the {@link ProducingWiringPoint}
	 */
	public ProducingWiringPoint getProducingWiringPoint(){
		return point;
	}

	/**
	 * Attributes the {@link ProducingWiringPoint} to use.
	 * @param point the {@link ProducingWiringPoint} to use.
	 */
	public void setProducingWiringPoint(ProducingWiringPoint point){
		if (this.point == null){
			this.point = point;
		} else {
			this.point = this.point.merge(point);
		}
	}

	/**
	 * 
	 * @return all the {@link AfterWiringPoint}s in this model.
	 */
	public Collection<AfterWiringPoint> getAfterPoints(){
		return Collections.unmodifiableCollection(afterpoints);
	}

	/**
	 * Add an {@link AfterWiringPoint}. A {@link AfterWiringPoint} is used to inject other objects after creation.
	 * @param point the {@link AfterWiringPoint} to add to this model.
	 */
	public void addAfterWiringPoint(AfterWiringPoint point){
		this.afterpoints.add(point);
	}

	/**
	 * Remove an {@link AfterWiringPoint}. A {@link AfterWiringPoint} is used to inject other objects after creation.
	 * @param point the {@link AfterWiringPoint} to remove to this model.
	 */
	public void removeAfterWiringPoint(AfterWiringPoint point){
		this.afterpoints.remove(point);
	}

	/**
	 * 
	 * @return <code>true</code> if any of the after-points is required, <code>false</code> if all after-points are optional.
	 */
	public boolean isRequired() {
		for (AfterWiringPoint ap : afterpoints){
			if(ap.isRequired()){
				return true;
			}
		}
		return false;
	}

	/**
	 * @return
	 */
	public Map<String , Object> getParams() {
		return this.params;
	}

	/**
	 * @param params
	 */
	public void addParams(Map<String, Object> all) {
		this.params.putAll(all);
	}

	/**
	 * @param annotationType
	 */
	public void addScope(String scopeName) {
		this.scopes.add(scopeName);
	}

	public List<String> getScopes(){
		return this.scopes;
	}

	/**
	 * @return
	 */
	public ReflectedClass<?> getBeanClass() {
		return type;
	}

	public int hashCode(){
		return type.getName().hashCode();
	}

	public boolean equals(Object other){
		return other instanceof BeanDependencyModel && 
				((BeanDependencyModel)other).type.getName().equals(this.type.getName());
	}

	public Collection<PublishPoint> getPublishPoints() {
		return Collections.unmodifiableCollection(publishPoints);
	}

	public void addPublishPoint(PublishPoint element) {
		this.publishPoints.add(element);
	}

	public void removePublishPoint(PublishPoint element) {
		this.publishPoints.remove(element);
	}

	public String toString(){
		return publishPoints.toString();
	}

	
	public void setPostCreatePoint(PostCreatePoint postCreatePoint) {
		this.postCreatePoint = postCreatePoint;
	}

	public PostCreatePoint getPostCreatePoint() {
		return postCreatePoint;
	}

	public PreDestroiPoint getpreDestroiPoint(){
		return preDestroiPoint;
	}

	/**
	 * @param methodPostCreatePoint
	 */
	public void setPreDestroiPoint(PreDestroiPoint preDestroiPoint) {
		this.preDestroiPoint = preDestroiPoint;
	}



	/**
	 * @param profiles
	 */
	public void setProfiles(ProfilesBag profiles) {
		this.profiles = profiles;
	}
	public ProfilesBag getProfiles() {
		return this.profiles;
	}


}
