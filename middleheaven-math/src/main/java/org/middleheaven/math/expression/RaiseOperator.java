package org.middleheaven.math.expression;

import java.util.Queue;

public class RaiseOperator extends Operator {

	public RaiseOperator() {
		super("^");
	}

	@Override
	public Term evaluate(Queue<Term> operands, EvaluationContext context) {
		NumericTerm a = (NumericTerm)operands.poll();
		NumericTerm b = (NumericTerm)operands.poll();
		
		return a.raise(b);
	}

}
