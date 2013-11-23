package org.middleheaven.math.expression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 
 * Parses expressions like 3 - 4*5 + sin( 23*PI )
 * 
 */
public class InfixParser implements ExpressionParser{

	private  Map<String, Operator> operators = new HashMap<String, Operator>();


	public InfixParser(){
		operators.put("(", new SentielOperator());
		operators.put("+", new PlusOperator());
		operators.put("-", new MinusOperator());
		operators.put("*", new TimesOperator());
		operators.put("/", new DivOperator());
		operators.put("^", new RaiseOperator());
	}

	@Override
	public Expression parse(CharSequence text) {

		List<Term> terms = new ArrayList<Term>();
		toReversedPolish(terms , text.toString());

		return new Expression(terms);

	}

	private void toReversedPolish(List<Term> terms, String expression){
		
		expression  =expression.trim();
		// * and / and ^ have precedence
		// so split by + and - not inside

		if (expression.isEmpty()){
			return;
		}


		int pos = findOperator(expression,"+","-");

		if (pos==-1){
			// parentesis

			int end = expression.lastIndexOf(")");
			int start = expression.lastIndexOf("(", end);
			if (end >=0 && start>=0){


				toReversedPolish( terms, expression.substring(start+1, end));
				toReversedPolish( terms, expression.substring(end+1));
				toReversedPolish( terms, expression.substring(0, start));
				return;
			} else {

				//evaluate * and /
				pos = findOperator(expression,"*","/","^");

				if (pos==-1){
					// Value
					try{
						terms.add(new NumericTerm(expression));
					} catch (NumberFormatException e){
						terms.add(new LiteralTerm(expression));
					}
					return;
				}
			}
		} 

		Operator op = operators.get(expression.substring(pos, pos+1).trim());
		if (op==null){
			throw new ExpressionParsingException("Operator " + op + " not recognized");
		}

		toReversedPolish( terms, expression.substring(pos+1));
		toReversedPolish( terms, expression.substring(0, pos));

		terms.add(op);


	}

	private int findOperator(String expression, String ... operators){
		// find the next operator not inclosed in ()
		int pos = -1;
		int place = expression.length();
		for (String s : operators){

			pos = Math.max(expression.lastIndexOf(s,place), pos);
			if (pos>=0){
				int spos =  expression.lastIndexOf('(' , pos);
				int epos =  expression.indexOf(')' , spos);
				if (spos >=0 && pos > spos && pos < epos){
					pos= -1;
					place = spos;
				}
			}
			
			if (pos>=0){
				return pos;
			}
		}
		return pos;
	}
}
