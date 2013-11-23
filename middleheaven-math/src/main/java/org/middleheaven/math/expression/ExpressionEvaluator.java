package org.middleheaven.math.expression;

import java.util.LinkedList;

public class ExpressionEvaluator {

	public Number evaluate(Expression exp){

		LinkedList<Term> stack = new LinkedList<Term>();
		EvaluationContext context = new EvaluationContext();
		for (Term term : exp.terms ){
			if (term instanceof Operator){
				Operator op = (Operator)term;
				stack.addFirst(op.evaluate(stack, context));
			} else {
				stack.addFirst(term);
			}
		}

		NumericTerm previous = (NumericTerm)stack.poll();
		
		return previous.getNumber();
	}
}
