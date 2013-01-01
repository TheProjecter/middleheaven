package org.middleheaven.domain.store;
import java.io.Serializable;

import org.hibernate.Criteria;
import org.hibernate.EmptyInterceptor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.IdentifierEqExpression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.hibernate.type.Type;
import org.middleheaven.domain.criteria.EntityCriteria;
import org.middleheaven.domain.criteria.FieldJuntionCriterion;
import org.middleheaven.domain.criteria.IdentityCriterion;
import org.middleheaven.domain.model.DomainModel;
import org.middleheaven.domain.query.ListQuery;
import org.middleheaven.domain.query.Query;
import org.middleheaven.events.EventListenersSet;
import org.middleheaven.persistance.criteria.LogicConstraint;
import org.middleheaven.util.coersion.TypeCoercing;
import org.middleheaven.util.criteria.Criterion;
import org.middleheaven.util.criteria.CriterionOperator;
import org.middleheaven.util.criteria.FieldInSetCriterion;
import org.middleheaven.util.criteria.FieldValueCriterion;
import org.middleheaven.util.criteria.LogicCriterion;
import org.middleheaven.util.criteria.ReadStrategy;
import org.middleheaven.util.function.Function;
import org.middleheaven.util.identity.Identity;

public class HibernateDomainStore implements DomainStore {
	
	private SessionFactory sessionFactory;
	private InterceptorAdpater interceptor = new InterceptorAdpater();
	
	public class InterceptorAdpater extends EmptyInterceptor {
		
		private static final long serialVersionUID = -7018647411280423552L;

		EventListenersSet<DomainStoreListener> addListener = EventListenersSet.newSet(DomainStoreListener.class);
		EventListenersSet<DomainStoreListener> removeListener = EventListenersSet.newSet(DomainStoreListener.class);
		
		public boolean onSave(Object entity,
                   Serializable id,
                   Object[] state,
                   String[] propertyNames,
                   Type[] types) {
			   
				addListener.broadcastEvent().onEntityAdded(new DomainChangeEvent(entity));
			
			   return false;
		   }
		
		  public boolean onFlushDirty(Object entity,
                  Serializable id,
                  Object[] currentState,
                  Object[] previousState,
                  String[] propertyNames,
                  Type[] types) {

			  addListener.broadcastEvent().onEntityChanged(new DomainChangeEvent(entity));
			  
			  return false;
			
		   }
		  
		   public void onDelete(Object entity,
                   Serializable id,
                   Object[] state,
                   String[] propertyNames,
                   Type[] types) {
			   
			   removeListener.broadcastEvent().onEntityRemoved(new DomainChangeEvent(entity));
			   
		   }
		   
	
			public void addStorageListener(DomainStoreListener listener) {
				addListener.addListener(listener);
			}

		
			public void removeStorageListener(DomainStoreListener listener) {
				removeListener.addListener(listener);
			}

	}
	
	public HibernateDomainStore (DomainModel domainModel){


		Configuration configuration = new Configuration()

		
		.setInterceptor(interceptor)
	    .configure();
		
		
		ServiceRegistry serviceRegistry = new ServiceRegistryBuilder()
    	.applySettings(configuration.getProperties())
    	.buildServiceRegistry();
		
		sessionFactory =  configuration.buildSessionFactory(serviceRegistry);
	}

	
	protected final <T> T inSession(Function <T, Session> block) {
		Session session = null;

		try {
			session = sessionFactory.getCurrentSession();
			session.beginTransaction();

			return block.apply(session);
			
		} catch (Exception e){
			throw new InstanceStorageException(e);
		} finally {
			if (session != null){
				session.close();
			}
		}
	}
	
	@Override
	public Identity getIdentityFor(final Object object) {
		 return inSession(new Function<Identity, Session>(){

			@Override
			public Identity apply(Session session) {
				
				Serializable id = session.getIdentifier(object);
				
				return TypeCoercing.coerce(id, Identity.class); 
			}
			
			
		});
	}

	@Override
	public <T> T store(final T obj) {
		return inSession(new Function<T, Session>(){

			@Override
			public T apply(Session session) {
				
				session.save(obj);
				
				return obj;
			}
			
			
		});
	}

	@Override
	public <T> void remove(final T obj) {
	    inSession(new Function<T, Session>(){

			@Override
			public T apply(Session session) {
				
				session.delete(obj);
				
				return null;
			}
			
			
		});
	}

	@Override
	public <T> Query<T> createQuery(EntityCriteria<T> criteria) {
		return createQuery(criteria, ReadStrategy.fowardReadOnly());
	}

	@Override
	public <T> Query<T> createQuery(EntityCriteria<T> criteria, ReadStrategy strategy) {
	
		Session session = this.sessionFactory.getCurrentSession();
		
		Criteria hcriteria = session.createCriteria(criteria.getTargetClass());
		
				
		interpreter(criteria, hcriteria);
		 
		return new ListQuery<T>(hcriteria.list());
	
		
	}
	
	private <T> void interpreter(EntityCriteria<T> criteria , Criteria hcriteria) {
		
		 interpreter(criteria.constraints(), hcriteria);
	}

	private void interpreter(LogicCriterion criteria,Criteria hcriteria) {
		

		for (Criterion c : criteria) {
			if (c instanceof IdentityCriterion){
				
				hcriteria.add(Restrictions.idEq(((IdentityCriterion)c).getIdentity()));
				
			} else if (c instanceof FieldValueCriterion){
				
				FieldValueCriterion fc = (FieldValueCriterion) c;
				
				String propertyName = fc.getFieldName().getDesignation();
				Object value = fc.valueHolder().getValue();
				
				if (CriterionOperator.CONTAINS.equals(fc.getOperator()) || CriterionOperator.NEAR.equals(fc.getOperator())){
					hcriteria.add(Restrictions.ilike(propertyName, value.toString(), MatchMode.ANYWHERE));
				} else if (CriterionOperator.ENDS_WITH.equals(fc.getOperator())){
					hcriteria.add(Restrictions.ilike(propertyName, value.toString(), MatchMode.END));
				} else if (CriterionOperator.STARTS_WITH.equals(fc.getOperator())){
					hcriteria.add(Restrictions.ilike(propertyName, value.toString(), MatchMode.START));
				} else if (CriterionOperator.EQUAL.equals(fc.getOperator())){
					hcriteria.add(Restrictions.eq(propertyName, value));
				} else if (CriterionOperator.GREATER_THAN.equals(fc.getOperator())){
					hcriteria.add(Restrictions.gt(propertyName, value));
				} else if (CriterionOperator.GREATER_THAN_OR_EQUAL.equals(fc.getOperator())){
					hcriteria.add(Restrictions.ge(propertyName, value));
				} else if (CriterionOperator.IN.equals(fc.getOperator())){
					hcriteria.add(Restrictions.in( propertyName,  ((FieldInSetCriterion)c).valueHolder().getValue()));
				} else if (CriterionOperator.IS.equals(fc.getOperator())){
					throw new IllegalStateException("Cant interpreter IS operator");
				} else if (CriterionOperator.IS_NULL.equals(fc.getOperator())){
					hcriteria.add(Restrictions.isNull(propertyName));
				} else if (CriterionOperator.LESS_THAN.equals(fc.getOperator())){
					hcriteria.add(Restrictions.lt(propertyName, value));
				}  else if (CriterionOperator.LESS_THAN_OR_EQUAL.equals(fc.getOperator())){
					hcriteria.add(Restrictions.le(propertyName, value));
				}  else if (CriterionOperator.UNKOWN.equals(fc.getOperator())){
					throw new IllegalStateException("Cant interpreter criterion");
				} 

			} else if (c instanceof FieldJuntionCriterion){
				// TODO
			} else if (c instanceof FieldInSetCriterion){
				hcriteria.add(Restrictions.in( ((FieldInSetCriterion) c).getFieldName().getDesignation(), ((FieldInSetCriterion)c).valueHolder().getValue()));
				
			}
		}
		
	}

	@Override
	public <T> void remove(EntityCriteria<T> criteria) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addStorageListener(DomainStoreListener listener) {
		interceptor.addStorageListener(listener);
	}

	@Override
	public void removeStorageListener(DomainStoreListener listener) {
		interceptor.removeStorageListener(listener);
	}
	


}
