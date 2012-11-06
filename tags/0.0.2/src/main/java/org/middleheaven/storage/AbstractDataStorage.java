package org.middleheaven.storage;

import java.util.Collection;

import org.middleheaven.core.reflection.Introspector;
import org.middleheaven.model.domain.EntityFieldModel;
import org.middleheaven.storage.db.StoreQuerySession;
import org.middleheaven.util.coersion.TypeCoercing;
import org.middleheaven.util.criteria.entity.EntityCriteria;
import org.middleheaven.util.criteria.entity.EntityCriteriaBuilder;
import org.middleheaven.util.identity.Identity;

public abstract class AbstractDataStorage implements DataStorage {

	private StorableModelReader reader;
	private StorableStateManager manager;

	public AbstractDataStorage(StorableModelReader reader){
		this.reader = new CachedStorableModelReader(reader);
	}
	
	public void setStorableStateManager(StorableStateManager manager){
		this.manager = manager;
	}
	
	protected StorableStateManager getStorableStateManager(){
		return this.manager;
	}

	protected StorableModelReader reader() {
		return reader;
	}

	/**
	 * Instantiate a new object.
	 * @param <E> the object type
	 * @param type the object class
	 * @return the object instance
	 */
	protected <E> E newInstance(Class<E> type){
		return Introspector.of(type).newInstance();
	}

	/* (non-Javadoc)
	 * @see org.middleheaven.storage.StorableModelResolver#resolveModel(java.util.Collection)
	 */
	public StorableEntityModel resolveModel(Collection<Storable> collection){
		if (collection.isEmpty()){
			return null;
		}

		Storable s = collection.iterator().next();
		return reader.read(s.getPersistableClass());
	}

	
	@Override
	public Identity getIdentityFor(Object object) {
		if (object instanceof Storable){
			return ((Storable)object).getIdentity();
		}
		return null;
	}

	protected final Storable copy(Storable from, Storable to,StorableEntityModel model, StoreQuerySession session) {

		to.setIdentity(from.getIdentity());
		to.setStorableState(from.getStorableState());

		for (StorableFieldModel fm : model.fields()){
			if(fm.getDataType().isToManyReference()){
				// lookfor the other ones
				StorableEntityModel otherModel = reader.read(fm.getValueType());

				StorableFieldModel frm = otherModel.fieldReferenceTo(to.getPersistableClass());

				if (frm !=null){
					EntityCriteria<?> criteria = EntityCriteriaBuilder.search(otherModel.getEntityClass())
					.and(frm.getLogicName().getName())
					.navigateTo(Introspector.of(to).getRealType())
					.and("identity").eq(to.getIdentity())
					.back()
					.all();

					Collection<?> all = this.createQuery(criteria , null).fetchAll();

					for (Object o : all){
						to.addFieldElement(fm,o);
					}

				}

			} else if(fm.getDataType().isToOneReference()) {
				Object obj = from.getFieldValue(fm);

				if(obj!=null){
					StorableEntityModel otherModel = reader.read(fm.getValueType());

					// convert to identity
					Identity id = (Identity)TypeCoercing.coerce(obj, otherModel.identityFieldModel().getValueType());


					Storable o = session.get(otherModel.getEntityClass(), id);
					if (o == null){
						EntityCriteria<?> criteria = EntityCriteriaBuilder.search(otherModel.getEntityClass())
						.and("identity").eq(id)
						.all();

						o = (Storable) this.createQuery(criteria , null).fetchFirst();
						session.put(o);
					}



					to.setFieldValue(fm, o);
				}

			} else {
				to.setFieldValue(fm,from.getFieldValue(fm));
				
				if (fm.isIdentity()){
					session.put(to);
				}
			}
		}
		
		return to;
	}





}