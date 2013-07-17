package org.middleheaven.persistance.db;

import java.util.Collection;
import java.util.LinkedList;

import org.middleheaven.persistance.criteria.LogicConstraint;
import org.middleheaven.persistance.criteria.ResultColumnDefinition;
import org.middleheaven.persistance.criteria.building.ColumnGroupConstraint;
import org.middleheaven.persistance.criteria.building.OrderConstraint;

/**
 * A search plan for the execution of a query in a DataSetProvider.
 */
public final class SearchPlan {

	private boolean fowardOnly;
	private boolean readOnly;
	private Integer maxCount;
	private boolean distinct;
	private int offSet;
	private boolean countOnly;
	private Collection<ResultColumnDefinition> resultColumns;
	private LogicConstraint constraints;
	private Collection<TableRelation> tableRelations = new LinkedList<TableRelation>();
	private Collection<OrderConstraint> ordering = new LinkedList<OrderConstraint>();
	private Collection<ColumnGroupConstraint> groupConstraint  = new LinkedList<ColumnGroupConstraint>();
	
	public SearchPlan(){
		
	}

	public Collection<TableRelation> getSourceDataSets() {
		return tableRelations;
	}



	public void add(TableRelation relation){
		tableRelations.add(relation);
	}

	public void addAll(Collection<TableRelation> sourceDataSet){
		tableRelations.addAll(sourceDataSet);
	}



	public LogicConstraint getConstraints() {
		return constraints;
	}

	public void setConstraints(LogicConstraint constraints) {
		this.constraints = constraints;
	}

	public boolean isFowardOnly() {
		return fowardOnly;
	}
	public void setFowardOnly(boolean fowardOnly) {
		this.fowardOnly = fowardOnly;
	}


	public boolean isReadOnly() {
		return readOnly;
	}
	
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	
	public Integer getMaxCount() {
		return maxCount;
	}
	
	public void setMaxCount(Integer maxCount) {
		this.maxCount = maxCount;
	}
	
	public boolean isDistinct() {
		return distinct;
	}
	
	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}
	
	public int getOffset() {
		return offSet;
	}
	
	public void setOffset(int offset) {
		this.offSet = offset;
	}
	
	public Collection<ResultColumnDefinition> getResultColumns() {
		return resultColumns;
	}

	
	public void setResultColumns(Collection<ResultColumnDefinition> resultColumns) {
		this.resultColumns = resultColumns;
	}

	public boolean isCountOnly() {
		return countOnly;
	}

	public void setCountOnly(boolean countOnly) {
		this.countOnly = countOnly;
	}

	public Collection<OrderConstraint> getOrdering() {
		return ordering;
	}
	
	

	public void setOrdering(Collection<OrderConstraint> ordering) {
		this.ordering = ordering;
	}

	public Collection<ColumnGroupConstraint> getGroups() {
		return this.groupConstraint;
	}

	/**
	 * @param startAt
	 * @param maxCount2
	 * @return
	 */
	public SearchPlan limit(int startAt, int maxCount2) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param groupConstraint
	 */
	public void setGrouping(Collection<ColumnGroupConstraint> groupConstraint) {
		this.groupConstraint = groupConstraint;
	}

	/**
	 * @return
	 */
	public boolean hasMaxCount() {
		return this.maxCount != null && this.maxCount.intValue() > 0;
	}

	
	
	

	

}
