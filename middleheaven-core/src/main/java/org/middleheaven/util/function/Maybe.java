/**
 * 
 */
package org.middleheaven.util.function;

/**
 * Represents a placeholder for, a possibly <code>null</code> value.
 * The use of {@link Maybe} helps to avoid {@link NullPointerException}.
 */
public final class Maybe<T> {

	private T value;
	
	/**
	 * Creates a {@link Maybe} for the given value.
	 * @param value the given value.
	 * @return  a {@link Maybe} for the given value.
	 */
	public static <M> Maybe<M> of(M value){
		return new Maybe<M>(value);
	}
	
	/**
	 * 
	 * @return a {@link Maybe} instance thar represents an absent (<code>null</code>) value.
	 */
	public static <M> Maybe<M> absent(){
		return new Maybe<M>(null);
	}
	
	/**
	 * Constructor.
	 * @param value
	 */
	public Maybe(T value) {
		this.value = value;
	}
	
	/**
	 * 
	 * @return <code>true</code> if the value is absent, <code>false</code> otherwise.
	 */
	public boolean isAbsent(){
		return value == null;
	}
	
	/**
	 * 
	 * @return <code>true</code> if the value is present, <code>false</code> otherwise.
	 */
	public boolean isPresent(){
		return value != null;
	}
	
	/**
	 * returns the inner value. throws exception is the value is absent.
	 * This method never returns <code>null</code>.
	 * 
	 * @return the inner value. throws exception is the value is absent.
	 * @throws IllegalStateException it the value is absent.
	 */
	public T get(){
		if (this.isAbsent()){
			throw new IllegalStateException("Value is absent");
		}
		return value;
	}
	
	/**
	 * If this value is absent, return {@code other}
	 * @param other the other value to return
	 * @return this value is absent, return {@code other}, otherwise, return the value returned by this.get().
	 */
	public T or (T other){
		return this.isAbsent() ? other : value;
	}
	
	/**
	 * If this value is absent, return {@code other}
	 * @param other other the other value to return
	 * @return this value is absent, return {@code other}, otherwise, return this
	 */
	public Maybe<T> or (Maybe<T> other){
		return this.isAbsent() ? other : this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof Maybe) && equalsMaybe(Maybe.class.cast(obj)); 
	}
	
	
	private boolean equalsMaybe(Maybe<T> other) {
		return this.value == null ? other.value == null : this.value.equals(other.value);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode(){
		return this.value == null ? 0: value.hashCode();
	}
	
	public String toString(){
		return String.valueOf(value);
	}

}
