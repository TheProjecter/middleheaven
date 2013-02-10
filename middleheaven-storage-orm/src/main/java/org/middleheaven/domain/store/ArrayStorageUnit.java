package org.middleheaven.domain.store;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.middleheaven.domain.criteria.EntityCriteria;
import org.middleheaven.storage.inmemory.EntityInstanceEntityCriteriaInterpreter;
import org.middleheaven.util.identity.Identity;

public class ArrayStorageUnit implements StorageUnit {

	private final Collection<StoreAction> actions = new LinkedList<StoreAction>();

	@Override
	public void addAction(StoreAction action) {
		actions.add(action);
	}

	@Override
	public void simplify() {
	
		if (actions.isEmpty()){
			return;
		}
		
		LinkedList<StoreAction> test = new LinkedList<StoreAction>(actions);
		LinkedList<StoreAction> all = new LinkedList<StoreAction>();
		
		outter: while (!test.isEmpty()){
			StoreAction action = test.removeFirst();
			
			for (Iterator<StoreAction> it = all.iterator(); it.hasNext(); ){
				StoreAction a = it.next();
				if (a.isRepeated(action)){
					continue outter;
				} else if (a.isContrary(action)){
					it.remove();
					continue outter;
				}
			}
		
			all.add(action);
		}
		
		actions.clear();
		actions.addAll(all);
	}

	@Override
	public void commitTo(StoreActionCommiter executer) {
		for (StoreAction action : actions) {
			executer.commit(action);
		}
		actions.clear();
	}

	@Override
	public void roolback() {
		actions.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E> Collection<E> filter(Collection<E> all, EntityCriteria<E> c) {

		if (actions.isEmpty()){
			return all;
		}
		
		Class<?> type = c.getTargetClass();
		
		Map<Identity, E> result = new LinkedHashMap<Identity, E>();
		
		// put all
		for (E item : all){
			EntityInstance s = (EntityInstance) item; 
			result.put(s.getIdentity(), item);
		}
		
		for (StoreAction action : actions){
			final EntityInstance candidate = action.getStorable();
			if (type.isInstance(candidate)){
				
				EntityInstance persisted = (EntityInstance) result.get(candidate.getIdentity());
				
				if (persisted == null){
					//  if passes entity criteria, add it 
					if (EntityInstanceEntityCriteriaInterpreter.interpret(c).apply(candidate)){
						result.put(candidate.getIdentity(),  c.getTargetClass().cast(candidate));
					}
				} else {
					if( action.getStoreActionType().isDelete()){ 
						// if the instance will be deleted, remove it from the result
						result.remove(candidate.getIdentity());
					}
				}
			}
		}
		
		return result.values();
	}


}
