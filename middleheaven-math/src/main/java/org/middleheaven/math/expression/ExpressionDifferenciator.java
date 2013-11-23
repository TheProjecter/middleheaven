package org.middleheaven.math.expression;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ExpressionDifferenciator {

	/**
	 * 
	 * u+v 	du/dx+dv/dx
	 * u-v 	du/dx-dv/dx
	 * u/v 	(v*du/dx-u*dv/dx)/v^2
	 * u*v 	u*dv/dx+v*du/dx
	 * c*u 	c*du/dx
	 * u^n 	n*u^(n-1)*du/dx
	 * c^u 	c^u*ln(c)*du/dx
	 * e^u 	e^u*du/dx
	 * 
	 * 
	 * 
	 * @param exp
	 * @return
	 */
	public Expression diff(Expression exp){
		List<Term> terms = new ArrayList<Term>(exp.terms); 
		Collections.reverse(terms);


		LinkedList<Term> exprTerms = new LinkedList<Term>();

		for (Term term : terms ){
			if (term instanceof Operator){
				Operator op = (Operator)term;
				new Expression(exprTerms);
				
			} else {
				exprTerms.add(term);
			}
		}

		return new Expression(terms);
	}
}
