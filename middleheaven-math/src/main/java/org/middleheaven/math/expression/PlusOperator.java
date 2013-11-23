package org.middleheaven.math.expression;

import java.util.Queue;

public class PlusOperator extends Operator {

	public PlusOperator() {
		super("+");
	}

	@Override
	public Term evaluate(Queue<Term> operands, EvaluationContext context) {
		NumericTerm a = (NumericTerm)operands.poll();
		NumericTerm b = (NumericTerm)operands.poll();
		
		return a.plus(b);
	}





}
