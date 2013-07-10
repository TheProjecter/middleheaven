/**
 * 
 */
package org.middleheaven.domain.store;




class UpdateAction extends StoreAction{

	public UpdateAction (EntityInstance storable){
		super(storable, StoreActionType.UPDATE);
	}



}