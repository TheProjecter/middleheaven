/**
 * 
 */
package org.middleheaven.core.wiring;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.middleheaven.reflection.ReflectedClass;

/**
 * 
 */
class DependendableBean {


	public static Collection<DependendableBean> fromModel(BeanDependencyModel model) {
		return fromModel(model, FactoryResolver.instanceFor(model));
	}

	/**
	 * @param model
	 * @return
	 */
	public static Collection<DependendableBean> fromModel(BeanDependencyModel model, Resolver resolver) {


		Collection<DependendableBean> result = new ArrayList<DependendableBean>(model.getContractTypes().size());

		for (ReflectedClass<?> contract : model.getContractTypes()){


			DependendableBean bean = new DependendableBean(contract);

			bean.scope = model.getScopes().get(0);
			bean.resolver = resolver;

			if (model.getProducingWiringPoint() != null ){


				for (WiringSpecification spec : model.getProducingWiringPoint().getParamsSpecifications()){

					if (!spec.getContract().equals(WiringTarget.class)){
						DependencyRelation rel = new DependencyRelation(spec.getContract());
						rel.setRequired(spec.isRequired());
						rel.addAllTargetConstraints(spec.getParams());

						bean.addRelation(rel);
					}

				}

			} else if (!model.getBeanClass().isInterface()){
				throw new IllegalStateException(model.getBeanClass() + " does not has a construction point");
			}

			for (AfterWiringPoint point : model.getAfterPoints()){
				for (WiringSpecification spec : point.getSpecifications()){
					if (!spec.getContract().equals(WiringTarget.class)){
						DependencyRelation rel = new DependencyRelation(spec.getContract());
						rel.setRequired(spec.isRequired());
						rel.addAllTargetConstraints(spec.getParams());

						bean.addRelation(rel);
					}
				}
			}


			for (PublishPoint point : model.getPublishPoints()){
				for (WiringSpecification spec : point.getSpecifications()){
					if (!spec.getContract().equals(WiringTarget.class)){
						DependencyRelation rel = new DependencyRelation(spec.getContract());
						rel.setRequired(spec.isRequired());
						rel.addAllTargetConstraints(spec.getParams());

						bean.addRelation(rel);
					}
				}
			}

			result.add(bean);
		}
		return result;
	}

	/**
	 * @param pp
	 * @param bean
	 * @return
	 */
	public static Collection<DependendableBean> fromPublishing(BeanDependencyModel ppModel , PublishPoint pp, ReflectedClass<?> publishingType) {

		Collection<DependendableBean> result = new ArrayList<DependendableBean>(ppModel.getContractTypes().size());

		for (ReflectedClass<?> contract : ppModel.getContractTypes()){

			DependendableBean bean = new DependendableBean(contract);

			bean.scope = pp.getScope();

			bean.resolver = new PublishPointResolver(pp, publishingType); 

			DependencyRelation rel = new DependencyRelation(publishingType);
			rel.setRequired(true);
			rel.setProductionRelation(true);

			bean.addRelation(rel);
			
			result.add(bean);

		}
		return result;
	}

	public static Collection<DependendableBean> fromImplementation(BeanDependencyModel interfaceBean, DependendableBean implementationBean) {

		Collection<DependendableBean> result = new ArrayList<DependendableBean>(interfaceBean.getContractTypes().size());

		for (ReflectedClass<?> contract : interfaceBean.getContractTypes()){
			DependendableBean bean = new DependendableBean(contract);

			bean.scope = implementationBean.getScope();

			bean.resolver = new ImplementationResolver(implementationBean); 

			DependencyRelation rel = new DependencyRelation(implementationBean.getType());
			rel.setRequired(true);

			bean.addRelation(rel);

			result.add(bean);
		}

		return result;
	}

	private List<DependencyRelation> relations = new LinkedList<DependencyRelation>();
	private ReflectedClass<?> type;
	private String scope;
	private Resolver resolver;

	private DependendableBean (ReflectedClass<?> type){
		this.type = type;
	}

	public String getScope(){
		return scope;
	}

	public ReflectedClass<?> getType(){
		return type;
	}


	protected void addRelation(DependencyRelation relation){
		relation.setSourceDependency(this);
		relations.add(relation);
	}


	public Collection<DependencyRelation> relations() {
		return relations;
	}


	public int relationsCount(){
		return relations.size();
	}

	/**
	 * @return
	 */
	public Resolver getResolver() {
		return this.resolver;
	}

	public String toString(){
		return type.toString() + "=>" + relationsToString();
	}

	/**
	 * @param relations2
	 * @return
	 */
	private String relationsToString() {
		StringBuilder builder = new StringBuilder("[");

		for (Iterator<DependencyRelation> it = relations.iterator(); it.hasNext();){
			builder.append(it.next().getTargetType().toString());
			if (it.hasNext()){
				builder.append(",");
			}
		}


		return builder.append("]").toString();
	}

	/**
	 * @param b
	 * @return
	 */
	public boolean dependsOn(DependendableBean other) {

		for (DependencyRelation relation  : relations){
			if (relation.getTargetType().equals(other.getType())){
				return true;
			}
		}
		return false;
	}





}
