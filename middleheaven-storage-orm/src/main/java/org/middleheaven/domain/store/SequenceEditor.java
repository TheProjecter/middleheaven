/**
 * 
 */
package org.middleheaven.domain.store;

import org.middleheaven.core.metaclass.MetaClass;
import org.middleheaven.util.identity.Identity;


interface SequenceEditor {
	
	
	public boolean canEdit(MetaClass type );
	
	public Identity newInstance (Long seed, MetaClass type);
}