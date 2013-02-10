/**
 * 
 */
package org.middleheaven.domain.store;





class InsertAction extends StoreAction{

	public InsertAction (EntityInstance storable){
		super(storable, StoreActionType.INSERT);
	}


}