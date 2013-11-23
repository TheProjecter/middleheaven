package org.middleheaven.math.expression;


import java.util.Queue;

public abstract class Operator implements Term {

	private String symbol;
	
	public Operator(String symbol) {
		super();
		this.symbol = symbol;
	}

	public String getSymbol() {
		return symbol;
	}

	public String toString(){
		return symbol;
	}
	
	public abstract  Term evaluate(Queue<Term> operands, EvaluationContext context);

	
}
