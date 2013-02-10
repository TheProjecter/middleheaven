/**
 * 
 */
package org.middleheaven.domain.store;


class DeleteAction extends StoreAction{

	public DeleteAction (EntityInstance storable){
		super(storable, StoreActionType.DELETE);
	}


	

}