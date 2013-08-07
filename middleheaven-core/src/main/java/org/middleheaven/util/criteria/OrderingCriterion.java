package org.middleheaven.util.criteria;

import org.middleheaven.util.QualifiedName;


public final class OrderingCriterion {

	private boolean descendant = true;
	private QualifiedName name;
	
	public static OrderingCriterion asc(QualifiedName name){
		return new OrderingCriterion(false,name);
	}
	
	public static OrderingCriterion desc(QualifiedName name){
		return new OrderingCriterion(true,name);
	}
	
	private OrderingCriterion(boolean descendant, QualifiedName name) {
		super();
		this.descendant = descendant;
		this.name = name;
	}

	public boolean isDescendant() {
		return descendant;
	}

	public void desc(){
		descendant = true;
	}
	
	public void asc(){
		descendant= false;
	}
	
	public QualifiedName getFieldName(){
		return name;
	}
}
