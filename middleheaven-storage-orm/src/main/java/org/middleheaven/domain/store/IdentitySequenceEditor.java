/**
 * 
 */
package org.middleheaven.domain.store;

import org.middleheaven.core.metaclass.MetaClass;
import org.middleheaven.util.identity.Identity;
import org.middleheaven.util.identity.IntegerIdentity;
import org.middleheaven.util.identity.LongIdentity;

/**
 * 
 */
class IdentitySequenceEditor implements SequenceEditor {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canEdit(MetaClass type) {
		return type.getName().equals(IntegerIdentity.class.getName()) || type.getName().equals(LongIdentity.class.getName());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Identity newInstance(Long seed, MetaClass type) {
		if (type.getName().equals(IntegerIdentity.class.getName())){
			return IntegerIdentity.valueOf(seed.intValue());
		} else if (type.getName().equals(LongIdentity.class.getName())){
			return LongIdentity.valueOf(seed);
		} else {
			throw new IllegalArgumentException(type + " is not supported");
		}
	}

}
