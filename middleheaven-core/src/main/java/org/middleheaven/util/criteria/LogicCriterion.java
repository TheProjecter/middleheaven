package org.middleheaven.util.criteria;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.middleheaven.collections.CollectionUtils;
import org.middleheaven.util.Hash;
import org.middleheaven.util.classification.LogicOperator;

/**
 * A {@link BooleanCriterion} the enables logic relaction with other criteria.
 */
public class LogicCriterion implements BooleanCriterion , Iterable<Criterion>{


	private static final long serialVersionUID = 6060765610996634126L;

	private final LogicOperator operator;
	protected final LinkedList<Criterion> criteria;

	/**
	 * 
	 * Created a {@link LogicCriterion} using the AND operator.
	 * 
	 */
	public LogicCriterion(){
		this(LogicOperator.and());
	}

	/**
	 * 
	 * Copy Constructor.
	 * @param other the {@link LogicCriterion} to copy.
	 */
	public LogicCriterion(LogicCriterion other){
		this.operator = other.operator;
		this.criteria = new LinkedList<Criterion>(other.criteria);
	}

	/**
	 * 
	 * Created a {@link LogicCriterion} using the given operator.
	 * @param operator the oeprator to use
	 */
	public LogicCriterion(LogicOperator operator){
		this.operator = operator;
		this.criteria = new LinkedList<Criterion>();
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isJunction() {
		return false;
	}
	
	/**
	 * 
	 * @return the {@link LogicOperator} in use.
	 */
	public LogicOperator getOperator() {
		return operator;
	}

	



	private void addCriterion(Criterion c){

		if (c.isJunction()){
			// put JuntionRestriction on top of the list
			// but respect order of insertion of Criterion


			// Find the first item that is not a JuntionRestriction
			// and insert before it
			for (ListIterator<Criterion> it = criteria.listIterator();it.hasNext();){
				Criterion o = it.next();
				if (!o.isJunction()){
					it.previous();
					it.add(c);
					return;
				}
			}

			// if no item is different form JuntionRestriction
			// (or the list is empty)
			criteria.addLast(c);

		} else {
			criteria.addLast(c);
		}

	}

	public LogicCriterion reduce() {
		LogicCriterion lc = new LogicCriterion(this);

		if (lc.criteria.size() <= 1){
			return lc;
		} 

		// 2 or more
		Set<Criterion> set = new LinkedHashSet<Criterion>();
		for ( Criterion c  : this.criteria){
			if (!c.isEmpty()){
				set.add(c);
			} 
		}

		lc.criteria.clear();
		lc.criteria.addAll(set);
		return lc;

	}

	public Criterion simplify(){
		if (this.criteria.isEmpty()){
			return EmptyCriterion.empty();
		} else if (this.criteria.size()==1){
			return ((Criterion)criteria.get(0)).simplify();
		} 

		// 2 ou mais
		Set<Criterion> set = new LinkedHashSet<Criterion>();
		int count=0;
		for ( Criterion c  : this.criteria){
			if (!c.isEmpty()){
				set.add(c);
				count++;
			} 
		}

		if (set.isEmpty()){
			return  EmptyCriterion.empty();
		} else if (count==1){
			return (Criterion)set.iterator().next();
		}else {

			LogicCriterion lc = new LogicCriterion(this.operator);
			lc.criteria.clear();
			lc.criteria.addAll(set);
			return lc;
		}
	}

	public Object clone(){
		return new LogicCriterion(this);
	}

	public BooleanCriterion and(LogicCriterion a){
		return addComposedCriterion(a,LogicOperator.and());
	}

	public BooleanCriterion and(Criterion a){
		if (a instanceof LogicCriterion){
			return addComposedCriterion((LogicCriterion)a,LogicOperator.and());
		} 

		return addSimpleCriterion(a,LogicOperator.and());

	}

	public BooleanCriterion or(LogicCriterion a){
		return addComposedCriterion(a,LogicOperator.or());
	}

	public BooleanCriterion or(Criterion a){
		if (a instanceof LogicCriterion){
			return addComposedCriterion((LogicCriterion)a,LogicOperator.or());
		}

		return addSimpleCriterion(a,LogicOperator.or());

	}

	public LogicCriterion add(Criterion criterion){
		if (!criterion.isEmpty()){
			addCriterion(criterion);
		}
		return this;
	}

	protected BooleanCriterion addSimpleCriterion(Criterion a , LogicOperator op ){
		if (this.operator.equals(op)){
			add(a);
			return this;
		} else {
			// different operator
			return new LogicCriterion(op).add(this).add(a);
		}
	}

	protected BooleanCriterion addComposedCriterion(LogicCriterion a , LogicOperator op ){

		if (a.criteriaCount()==0){
			return this;
		}

		if (a.getOperator().equals(op) && a.getOperator().equals(this.operator)){
			// AND <AND> AND  
			// OR <OR> OR
			for (Criterion c : criteria){
				addCriterion(c);
			}
		}


		return  new LogicCriterion(op).add(this).add(a);

	}


	public boolean equals(Object other){
		return other instanceof LogicCriterion && equalsOther(((LogicCriterion)other));
	}

	private boolean equalsOther(LogicCriterion other){
		return this.operator.equals(other.operator) && 
		CollectionUtils.equalContents( this.criteria , other.criteria);

	}

	public int hashCode(){
		return Hash.hash(operator).hash(criteria).hashCode();
	}

	public int criteriaCount(){
		return criteria.size();
	}


	public List<Criterion> criterias(){
		return Collections.unmodifiableList(criteria);
	}

	public String toString(){
		StringBuffer buffer = new StringBuffer("(");
		for (int i=0;i<criteria.size();i++){
			buffer.append(criteria.get(i).toString());
			buffer.append(" ");
			if (i!=criteria.size()-1){
				buffer.append(operator.toString());
				buffer.append(" ");
			}
		}
		return buffer.append(")").toString();
	}

	public boolean isEmpty() {
		return this.criteria.isEmpty();
	}

	@Override
	public Iterator<Criterion> iterator() {
		return criteria.iterator();
	}





}
