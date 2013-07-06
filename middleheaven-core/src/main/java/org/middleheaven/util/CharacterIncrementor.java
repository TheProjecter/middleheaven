package org.middleheaven.util;


/**
 * Incrementor for {@link Character}.
 * 
 */
public class CharacterIncrementor implements Incrementor<Character, Integer> {

	int step;
	private boolean reversed;
	
	/**
	 * 
	 * Constructor.
	 * @param step
	 */
	public CharacterIncrementor(int step){
		this(step, false);
	}
	
	 CharacterIncrementor(int step, boolean reversed){
		this.step = step;
		this.reversed = reversed;
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public Character increment(Character c) {
		return Character.valueOf((char)(c.charValue()+step));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Incrementor<Character, Integer> reverse() {
		return new CharacterIncrementor(-this.step, !this.reversed);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Incrementor<Character, Integer> withStep(Integer step) {
		CharacterIncrementor inc = new CharacterIncrementor(step);
		if (this.reversed){
			return inc.reverse();
		}
		return inc;
	}

	


}
