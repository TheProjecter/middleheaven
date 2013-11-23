package org.middleheaven.math.expression;

import java.util.Queue;

public class DivOperator extends Operator {

	public DivOperator() {
		super("/");
	}


	@Override
	public Term evaluate(Queue<Term> operands, EvaluationContext context) {
		NumericTerm a = (NumericTerm)operands.poll();
		NumericTerm b = (NumericTerm)operands.poll();
		
		return a.divide(b);
	}

}
