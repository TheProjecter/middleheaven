/**
 * 
 */
package org.middleheaven.collections.range;

import org.middleheaven.util.CharacterIncrementor;
import org.middleheaven.util.Incrementor;

/**
 * A {@link RangeBuilder} for {@link Character}s.
 */
public class CharacterRangeBuilder<C extends Character> extends RangeBuilder<C, Integer> {

	/**
	 * Constructor.
	 * @param start
	 */
	CharacterRangeBuilder(C start) {
		super(start, (Incrementor<C, Integer>) new CharacterIncrementor(1));
	}


}
