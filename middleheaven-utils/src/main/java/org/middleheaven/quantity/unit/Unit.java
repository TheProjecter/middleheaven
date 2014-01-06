package org.middleheaven.quantity.unit;


public abstract class Unit<E extends Measurable> {


	public static  <M extends Measurable> Unit<M> unit(Dimension<M> dim, String symbol){
		return new BaseUnit<M>(dim,symbol);
	}
	
	public abstract Dimension<E> dimension();
	public abstract String symbol();

	public final boolean equals(Object other){
		return other instanceof Unit && this.equalsOther((Unit<?>)other);
	}
	
	public final int hashCode(){
		return symbol().hashCode();
	}
	
	protected abstract boolean equalsOther(Unit<?> other);

	public boolean isCompatible(Unit<?> other) {
		return this.dimension().equals(other.dimension());
	}

	public Unit<E> minus(Unit<E> other) throws IncompatibleUnitsException {
		return plus(other);
	}

	public Unit<E> plus(Unit<E> other) throws IncompatibleUnitsException {
		if (this.equalsOther(other)){
			return this;
		}
		throw new IncompatibleUnitsException(this,other);
	}
	

	public <T extends Measurable> Unit<T> over(Unit<?> other) {
		return CompositeUnit.over(this, other);
	}

	public <T extends Measurable> Unit<T> times(Unit<?> other) {
		return CompositeUnit.times(this, other);
	}
	
	public  <C extends Measurable> Unit<C> raise(int exponent) {
		return CompositeUnit.raise (this, exponent);
	}

	public String toString() {
		return symbol();
	}
	
	public <C extends Measurable> Unit<C> cast() {
		return (Unit<C>) this;
	}

}
