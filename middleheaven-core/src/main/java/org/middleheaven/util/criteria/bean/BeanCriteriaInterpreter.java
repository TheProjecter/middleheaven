/**
 * 
 */
package org.middleheaven.util.criteria.bean;

import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.util.classification.LogicComposedPredicate;
import org.middleheaven.util.criteria.Criteria;
import org.middleheaven.util.criteria.Criterion;
import org.middleheaven.util.criteria.CriterionOperator;
import org.middleheaven.util.criteria.FieldCriterion;
import org.middleheaven.util.criteria.FieldInSetCriterion;
import org.middleheaven.util.criteria.LogicCriterion;
import org.middleheaven.util.function.BinaryFunction;
import org.middleheaven.util.function.Function;
import org.middleheaven.util.function.Predicate;

/**
 * 
 */
public class BeanCriteriaInterpreter<T> implements Predicate<T> {

	public static <X> BeanCriteriaInterpreter<X> interpret(Criteria<X> criteria){
		return new BeanCriteriaInterpreter<X>(criteria , transform(criteria));
	}


	private Criteria<T> criteria;
	private Predicate<T> precidate;

	/**
	 * Constructor.
	 * @param criteria
	 */
	public BeanCriteriaInterpreter(Criteria<T> criteria , LogicComposedPredicate<T> precidate) {
		this.criteria = criteria;
		this.precidate = precidate;
	}

	private static <X> LogicComposedPredicate<X> transform (Criteria<X> criteria){
		return transform(criteria.constraints());
	}

	private static <X> LogicComposedPredicate<X> transform (LogicCriterion criterion){

		LogicComposedPredicate<X> res = new  LogicComposedPredicate<X>(criterion.getOperator());

		for (Criterion c : criterion){

			if (c instanceof LogicCriterion){
				Predicate<X> p = transform((LogicCriterion)c);
				res.add(p);
			} else if (c instanceof FieldInSetCriterion){
				throw new UnsupportedOperationException();
			} else if (c instanceof FieldCriterion){
				FieldCriterion f = (FieldCriterion) c;
				res.add(new FieldPredicate( f.getFieldName().getDesignation() , f.valueHolder().getValue() , translateOperator(f.getOperator()) ));
			}
		}
		return res;
	}

	private static BinaryFunction<Boolean, Object, Object> EQ = new  BinaryFunction<Boolean, Object, Object> (){

		@Override
		public Boolean apply(Object a, Object b) {
			return a == b || a.equals(b);
		}

	};

	private static BinaryFunction<Boolean, Object, Object> LESS_THAN = new  BinaryFunction<Boolean, Object, Object> (){

		@Override
		public Boolean apply(Object a, Object b) {

			Comparable ca  = (Comparable)a;
			Comparable cb  = (Comparable)b;

			return ca.compareTo(cb) < 0;
		}

	};

	private static BinaryFunction<Boolean, Object, Object> LESS_THAN_OR_EQUAL = new  BinaryFunction<Boolean, Object, Object> (){

		@Override
		public Boolean apply(Object a, Object b) {

			Comparable ca  = (Comparable)a;
			Comparable cb  = (Comparable)b;

			return ca.compareTo(cb) <= 0;
		}

	};

	private static BinaryFunction<Boolean, Object, Object> GREATER_THAN = new  BinaryFunction<Boolean, Object, Object> (){

		@Override
		public Boolean apply(Object a, Object b) {

			Comparable ca  = (Comparable)a;
			Comparable cb  = (Comparable)b;

			return ca.compareTo(cb) > 0;
		}

	};
	private static BinaryFunction<Boolean, Object, Object> GREATER_THAN_OR_EQUAL = new  BinaryFunction<Boolean, Object, Object> (){

		@Override
		public Boolean apply(Object a, Object b) {

			Comparable ca  = (Comparable)a;
			Comparable cb  = (Comparable)b;

			return ca.compareTo(cb) >= 0;
		}

	};

	private static BinaryFunction<Boolean, Object, Object> STARTS_WITH = new  BinaryFunction<Boolean, Object, Object> (){

		@Override
		public Boolean apply(Object a, Object b) {

			return a.toString().startsWith(b.toString());
		}

	};

	private static BinaryFunction<Boolean, Object, Object> CONTAINS = new  BinaryFunction<Boolean, Object, Object> (){

		@Override
		public Boolean apply(Object a, Object b) {

			return a.toString().contains(b.toString());
		}

	};

	private static BinaryFunction<Boolean, Object, Object> ENDS_WITH = new  BinaryFunction<Boolean, Object, Object> (){

		@Override
		public Boolean apply(Object a, Object b) {

			return a.toString().endsWith(b.toString());
		}

	};

	private static class Not< A, B> implements  BinaryFunction<Boolean, A, B>{


		private BinaryFunction<Boolean, A, B> original;

		public Not(BinaryFunction<Boolean, A, B> original){
			this.original = original;
		}
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Boolean apply(A a, B b) {
			return !original.apply(a, b).booleanValue();
		}

	}

	/**
	 * @param operator
	 * @return
	 */
	private static BinaryFunction<Boolean, Object, Object> translateOperator(CriterionOperator op) {

		BinaryFunction<Boolean, Object, Object> res;

		if ( op.equals(CriterionOperator.EQUAL)){
			res= EQ;
		} else if ( op.equals(CriterionOperator.LESS_THAN)){
			res= LESS_THAN;
		} else if ( op.equals(CriterionOperator.LESS_THAN_OR_EQUAL)){
			res= LESS_THAN_OR_EQUAL;
		}else if ( op.equals(CriterionOperator.GREATER_THAN)){
			res= GREATER_THAN;
		}else if ( op.equals(CriterionOperator.GREATER_THAN_OR_EQUAL)){
			res= GREATER_THAN_OR_EQUAL;
		}else if ( op.equals(CriterionOperator.STARTS_WITH)){
			res= STARTS_WITH;
		}else if ( op.equals(CriterionOperator.CONTAINS)){
			res= CONTAINS;	
		}else if ( op.equals(CriterionOperator.ENDS_WITH)){
			res= ENDS_WITH;
		} else {
			throw new IllegalArgumentException("Operator nor recognized");
		}

		if (op.isNegated()){
			res =  new Not<Object, Object>(res);
		}
		
		return res;
	}


	private static class FieldPredicate<T> implements Predicate<T> {

		private String propertyName;
		private Object test;
		private BinaryFunction<Boolean, Object,Object> operator;


		public FieldPredicate(String propertyName, Object test,BinaryFunction<Boolean, Object, Object> operator) {
			super();
			this.propertyName = propertyName;
			this.test = test;
			this.operator = operator;
		}


		/**
		 * {@inheritDoc}
		 */
		@Override
		public Boolean apply(T obj) {

			Object value = Introspector.of(obj.getClass()).inspect().properties().named(propertyName).retrive().getValue(obj);

			return operator.apply(value, test);
		}

	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean apply(T obj) {
		return precidate.apply(obj);
	}

}
