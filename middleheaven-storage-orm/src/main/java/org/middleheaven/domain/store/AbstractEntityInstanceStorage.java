package org.middleheaven.domain.store;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.middleheaven.core.metaclass.MetaClass;
import org.middleheaven.domain.model.EntityModel;
import org.middleheaven.sequence.Sequence;


/**
 * 
 */
public abstract class AbstractEntityInstanceStorage implements EntityInstanceStorage {

	
	private DomainStoreManager storeManager;
	
	private final Collection<SequenceEditor> editors = new HashSet<SequenceEditor>();
	private final Map<String, SequenceEditor> editorsMapping = new HashMap<String, SequenceEditor>();

	public AbstractEntityInstanceStorage(){
		
		
		editors.add(new IdentitySequenceEditor());

		
	}
	
	@Override
	public void setStorableStateManager(DomainStoreManager storeManager) {
		this.storeManager = storeManager;
	}

	protected DomainStoreManager getStoreManager(){
		return storeManager;
	}
	

	/**
	 * @return and {@link IdentityManager} based on the data store
	 */
	public IdentityManager getIdentityManager() {
		return new IdentityManager(){

			@Override
			public EntityInstance assignIdentity(EntityInstance instance) {
				
				EntityModel model = instance.getEntityModel();
				
				if (model.isIdentityAssigned()){
					// identity already was assigned 
					if (instance.getIdentity() == null){
						throw new IllegalArgumentException("Instance indentity is expected to be assigned before storage");
					}
				} else {
					
					MetaClass type = model.getIdentityType();
					
					SequenceEditor editor = editorsMapping.get(type.getName());
					
					
					if (editor == null){
						
						
						for (SequenceEditor idEditor : editors){
							if (idEditor.canEdit(type)){
								editor = idEditor;
								editorsMapping.put(type.getName(), editor);
							}
						}
					}
					
					if (editor == null){
						throw new IllegalStateException("No IdentityEditor found for type " + model.getIdentityType().getName());
					}
					
					Sequence<Long> seedSequence = getSeedSequence(instance);
					
					Long seed = seedSequence.next().value();
					
					instance.setIdentity(editor.newInstance(seed, type));
					
				}
				
				return instance;
			}
			
		};
	}
	
	protected abstract Sequence<Long> getSeedSequence(EntityInstance instance);
	
}
