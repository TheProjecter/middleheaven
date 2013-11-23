package org.middleheaven.math.expression;

import java.math.BigDecimal;

public class NumericTerm implements Term {

	private BigDecimal number;
	
	public NumericTerm(String value) {
		number = new BigDecimal(value.trim());
	}
	
	private NumericTerm(BigDecimal value) {
		number = value;
	}
	
	public Number getNumber(){
		return number;
	}
	
	public String toString(){
		return number.toString();
	}

	public NumericTerm plus(NumericTerm b) {
		return new NumericTerm(this.number.add(b.number));
	}

	public NumericTerm minus(NumericTerm b) {
		return new NumericTerm(this.number.subtract(b.number));
	}

	public NumericTerm times(NumericTerm b) {
		return new NumericTerm(this.number.multiply(b.number));
	}
	public NumericTerm divide(NumericTerm b) {
		return new NumericTerm(this.number.divide(b.number));
	}

	public NumericTerm raise(NumericTerm b) {
		return new NumericTerm(this.number.pow(b.number.intValue()));
	}
}
