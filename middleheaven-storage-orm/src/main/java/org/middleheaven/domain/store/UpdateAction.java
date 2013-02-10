/**
 * 
 */
package org.middleheaven.domain.store;

import java.util.Collections;



class UpdateAction extends StoreAction{

	public UpdateAction (EntityInstance storable){
		super(storable, StoreActionType.UPDATE);
	}



}