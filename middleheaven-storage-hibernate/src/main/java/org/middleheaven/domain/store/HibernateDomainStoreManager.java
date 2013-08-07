package org.middleheaven.domain.store;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.EmptyInterceptor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Mappings;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.Property;
import org.hibernate.mapping.RootClass;
import org.hibernate.mapping.SimpleValue;
import org.hibernate.mapping.Table;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.BooleanType;
import org.hibernate.type.CustomType;
import org.hibernate.type.DateType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.hibernate.type.TextType;
import org.hibernate.type.TimeType;
import org.hibernate.type.TimestampType;
import org.hibernate.type.Type;
import org.middleheaven.domain.criteria.EntityCriteria;
import org.middleheaven.domain.criteria.FieldJuntionCriterion;
import org.middleheaven.domain.criteria.IdentityCriterion;
import org.middleheaven.domain.model.DomainModel;
import org.middleheaven.domain.model.EntityFieldModel;
import org.middleheaven.domain.model.EntityModel;
import org.middleheaven.domain.model.EnumModel;
import org.middleheaven.domain.query.QueryExecuter;
import org.middleheaven.domain.query.QueryParametersBag;
import org.middleheaven.events.EventListenersSet;
import org.middleheaven.persistance.db.mapping.IllegalModelStateException;
import org.middleheaven.storage.dataset.mapping.DatasetColumnModel;
import org.middleheaven.storage.dataset.mapping.DatasetModel;
import org.middleheaven.storage.dataset.mapping.DatasetRepositoryModel;
import org.middleheaven.util.criteria.Criterion;
import org.middleheaven.util.criteria.CriterionOperator;
import org.middleheaven.util.criteria.FieldInSetCriterion;
import org.middleheaven.util.criteria.FieldValueCriterion;
import org.middleheaven.util.criteria.LogicCriterion;
import org.middleheaven.util.criteria.ReadStrategy;
import org.middleheaven.util.function.Function;
import org.middleheaven.util.function.Maybe;

/**
 * Impleemntation of {@link DomainStoreManager} using the Hiberante framework.
 */
public final class HibernateDomainStoreManager extends AbstractDomainStoreManager {
	
	private SessionFactory sessionFactory;
	private InterceptorAdpater interceptor = new InterceptorAdpater();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> Query<T> retriveNameQuery(String name, Class<T> type) {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	
	public static class InterceptorAdpater extends EmptyInterceptor {
		
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
	
	public static HibernateDomainStoreManager manage(DomainModel domainModel, DatasetRepositoryModel dataSetModels){
		return new HibernateDomainStoreManager(domainModel, dataSetModels);
	}
	
	private HibernateDomainStoreManager (DomainModel domainModel, DatasetRepositoryModel dataSetModels){
		super(domainModel);

		Configuration configuration = new Configuration()
		.setInterceptor(interceptor)
	    .configure();
		
		Mappings mappings = configuration.createMappings();
		
		doMapping(domainModel, dataSetModels, mappings);
		
		
		ServiceRegistry serviceRegistry = new ServiceRegistryBuilder()
    	.applySettings(configuration.getProperties())
    	.buildServiceRegistry();
		
		sessionFactory =  configuration.buildSessionFactory(serviceRegistry);
	}

	
	/**
	 * @param domainModel
	 * @param dataSetModels 
	 * @param mappings
	 */
	private void doMapping(DomainModel domainModel, DatasetRepositoryModel dataSetModels, Mappings mappings) {
		
		for (DatasetModel model : dataSetModels.models()){
			// TODO handle schema
			Table table = mappings.addTable("schema", null, model.getHardName(), null, false);
			
			Map<String, Column> columns = new HashMap<String, Column>();
			
			for (DatasetColumnModel column : model.columns()){
				Column col = new Column();
				col.setName(column.getHardName());
				col.setLength(column.getSize());
				col.setPrecision(column.getPrecision());
				col.setScale(column.getScale());
				col.setNullable(column.isNullable());
				//col.setSqlTypeCode(jdbcType);
				//col.setSqlType(columnType);
				
				columns.put(column.getName(), col);
				
				table.addColumn(col);
				mappings.addColumnBinding(column.getHardName(), col, table);
			}
			
			EntityModel entityModel = domainModel.getModelFor(model.getName());
			
			RootClass clazz = new RootClass();
			clazz.setEntityName(entityModel.getEntityName());
			clazz.setJpaEntityName(entityModel.getEntityName());
			
//			Class javaClass = entityModel.getEntityClass().
//			
//			if (javaClass != null) {
//			    clazz.setClassName(javaClass);
//			    clazz.setProxyInterfaceName(javaClass);
//			}
			clazz.setLazy(false);
			clazz.setTable(table);
			
			for (EntityFieldModel field : entityModel.fields() ) {
				Property prop = new Property();
				prop.setName(field.getName().getDesignation());
				clazz.addProperty(prop);
				
				
				SimpleValue value = new SimpleValue(mappings, table);
				value.setTypeName(resolveHibernateTypeName(domainModel, field).getName());
//				if(typeParams != null) {
//				    value.setTypeParameters(typeParams);
//				}
				value.addColumn(columns.get(field.getName().getDesignation()));
				prop.setValue(value);
				
				
			}
			
		}
		
	}


	/**
	 * @param field
	 * @return
	 */
	private Type resolveHibernateTypeName(DomainModel domainModel,EntityFieldModel field) {
		
		switch (field.getDataType()){
		case DATE: 
			return DateType.INSTANCE;
		case DATETIME:
			return TimestampType.INSTANCE;
		case TIME:
			return TimeType.INSTANCE;
		case DECIMAL:
			return BigDecimalType.INSTANCE;
		case INTEGER:
			return LongType.INSTANCE;
		case ENUM:
			Maybe<EnumModel> enumModel = domainModel.getEmumModel(field.getValueType());
			return new CustomType(new EnumType(enumModel.get()));
		case LOGIC:
			return BooleanType.INSTANCE;
		case MEMO:
			return TextType.INSTANCE;
		case TEXT:
			return StringType.INSTANCE;
		case STATUS:
			return IntegerType.INSTANCE;
		case ONE_TO_MANY:
		case ONE_TO_ONE:
		case MANY_TO_MANY:
		case MANY_TO_ONE:
		case UNKONW:
		default:
			throw new IllegalModelStateException("No type found for " + field.getName());
		}
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

//			} else if (c instanceof FieldJuntionCriterion){
//				// TODO
			} else if (c instanceof FieldInSetCriterion){
				hcriteria.add(Restrictions.in( ((FieldInSetCriterion) c).getFieldName().getDesignation(), ((FieldInSetCriterion)c).valueHolder().getValue()));
				
			}
		}
		
	}


	@Override
	public void addStorageListener(DomainStoreListener listener) {
		interceptor.addStorageListener(listener);
	}

	@Override
	public void removeStorageListener(DomainStoreListener listener) {
		interceptor.removeStorageListener(listener);
	}


	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> Query<T> createQuery(final EntityCriteria<T> criteria,StorageUnit unit) {

		return new ParametrizedCriteriaQuery<T>(criteria, new QueryExecuter (){

			@Override
			public <E> Collection<E> retrive(EntityCriteria<E> query, ReadStrategy readStrategy,QueryParametersBag queryParametersBag) {
				return inSession(new Function<Collection<E> , Session >(){

					@Override
					public Collection<E> apply(Session session) {
						Criteria hcriteria = session.createCriteria(criteria.getTargetClass());
						
						interpreter(criteria, hcriteria);
						 
						return hcriteria.list();
					}
					
				});
			}

			@Override
			public <E> long count(EntityCriteria<E> query, QueryParametersBag queryParametersBag) {
				return inSession(new Function<Long , Session >(){

					@Override
					public Long apply(Session session) {
						Criteria hcriteria = session.createCriteria(criteria.getTargetClass());
						
						interpreter(criteria, hcriteria);
						 
						Long count = (Long) hcriteria.setProjection(Projections.rowCount()).uniqueResult();
						return count == null ? 0 : count.longValue();
					}
					
				});
			}

			@Override
			public <E> boolean existsAny(EntityCriteria<E> query, QueryParametersBag queryParametersBag) {
				return count(query,queryParametersBag) > 0L;
			}
			
		});

		
		
		
		

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void deleteInstance(final EntityInstance instance) {
		
		 inSession(new Function<Object, Session>(){

				@Override
				public Object apply(Session session) {
					
					session.delete( ((HibernateEntityInstance) instance).getObject());
					
					return null;
				}
				
				
			});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void insertInstance(final EntityInstance instance) {
		
		inSession(new Function<Object, Session>(){

			@Override
			public Object apply(Session session) {
				Object obj = ((HibernateEntityInstance) instance).getObject();
				
				session.save(obj);
				
				return obj;
			}
			
			
		});
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void updateInstance(final EntityInstance instance) {
		inSession(new Function<Object, Session>(){

			@Override
			public Object apply(Session session) {
				Object obj = ((HibernateEntityInstance) instance).getObject();
				
				session.save(obj);
				
				return obj;
			}
			
			
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected EntityInstance merge(Object object) {
		return new HibernateEntityInstance(object);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void assignIdentity(EntityInstance p) {
		// no-op
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void flatten(EntityInstance p, Set<EntityInstance> all) {
		all.add(p);
	}





}
