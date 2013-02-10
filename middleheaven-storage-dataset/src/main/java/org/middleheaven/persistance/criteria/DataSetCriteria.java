package org.middleheaven.persistance.criteria;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import org.middleheaven.persistance.RelatedDataSet;
import org.middleheaven.persistance.criteria.building.ColumnGroupConstraint;
import org.middleheaven.persistance.criteria.building.OrderConstraint;
import org.middleheaven.util.classification.LogicOperator;

/**
 * Criteria for DataSet search.
 */
public class DataSetCriteria {

	private boolean isDistinct = false;
	private Integer maxCount;
	private int offset = 0;
	
	
	private LogicConstraint logicConstraint = new LogicConstraint(LogicOperator.and());
	private Collection<ResultColumnDefinition> resultColumns = new LinkedList<ResultColumnDefinition>();
	private Collection<OrderConstraint> orderConstraints = new LinkedList<OrderConstraint>();
	private Collection<ColumnGroupConstraint> groupConstraints = new LinkedList<ColumnGroupConstraint>();
	private Collection<RelatedDataSet> relatedDataSets = new LinkedHashSet<RelatedDataSet>();
	
	
	
	
	/**
	 * The data columns that will be retrieved from persistence.
	 * @return The data columns that will be retrieved from persistence.
	 */
	public Collection<ResultColumnDefinition> getResultColumns(){
		return resultColumns;
	}

	
	public LogicConstraint getDataSetRestrictions(){
		return logicConstraint;
	}
	
	public void addResultColumn (ResultColumnDefinition column){
		resultColumns.add(column);
	}

	
	/**
	 * The maximum number of rows to be retrieved.
	 * 
	 * @return The maximum number of rows to be retrieved or <code>null</code> if no maximum is set.
	 */
	public Integer getMaxCount(){
		return maxCount;
	}
	
	/**
	 * The number of columns to be discarded from the resulting dataset read from persistance. 
	 * The default value is zero.
	 * 
	 * @return The number of columns to be discarded from the resulting dataset read from persistance.
	 */
	public int getOffset(){
		return offset;
	}

	/**
	 * Specifies if the results must be distinct from each other.
	 * @return <code>true</code> if the results must be distinct from each other, <code>false</code> otherwise.
	 */
	public boolean isDistinct(){
		return isDistinct;
	}


	public void setDistinct(boolean isDistinct) {
		this.isDistinct = isDistinct;
	}


	public void setMaxCount(Integer maxCount) {
		this.maxCount = maxCount;
	}


	public void setOffset(int offset) {
		this.offset = offset;
	}


	/**
	 * @param logic
	 */
	public void addLogicConstraint(LogicConstraint logic) {
		// TODO handle logic operators priority
		this.logicConstraint.addConstraint(logic);
	}


	/**
	 * @param orderConstraint
	 */
	public void addOrderingConstraint(OrderConstraint orderConstraint) {
		orderConstraints.add(orderConstraint);
	}

	

	public Collection<OrderConstraint> getOrderConstraints() {
		return orderConstraints;
	}


	/**
	 * @param columnGroupConstraint
	 */
	public void addGroupConstraint(ColumnGroupConstraint columnGroupConstraint) {
		groupConstraints.add(columnGroupConstraint);
	}
	
	public Collection<ColumnGroupConstraint> getGroupConstraint() {
		return groupConstraints;
	}

	public void addRelatedDataSet(RelatedDataSet ds){
		relatedDataSets.add(ds);
	}
	
	public Collection<RelatedDataSet> getRelatedDataSets(){
		return this.relatedDataSets;
	}


	/**
	 * Copies this criteria with a new start and max count
	 * @param startAt the index of the first result
	 * @param maxCount the maximum size of elements to return
	 * @return a {@link DataSetCriteria} that provides the same query than this, but limits the result rows index between startAt and startAt + maxCcunt
	 */
	public DataSetCriteria limit(int startAt, int maxCount) {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	
}
