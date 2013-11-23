/**
 * 
 */
package org.middleheaven.persistance.criteria.building;

import java.util.LinkedList;
import java.util.List;

import org.middleheaven.persistance.model.TypeDefinition;
import org.middleheaven.util.criteria.CriterionOperator;

/**
 * 
 */
public class GenericRestrictionValueCaptureBuilder<TERMBUILDER> implements RestrictionValueCaptureBuilder<TERMBUILDER> {

	
	private CriterionOperator operator;
	private List<ColumnValueCapture> captures = new LinkedList<ColumnValueCapture>();
	private AbstractComparisonOperatorBuilders<?, TERMBUILDER> parent;


	/**
	 * Constructor.
	 * @param abstractComparisonOperatorBuilders
	 * @param operator2
	 */
	public GenericRestrictionValueCaptureBuilder(
			AbstractComparisonOperatorBuilders<?, TERMBUILDER> abstractComparisonOperatorBuilders,
			CriterionOperator operator) {
		this.parent = abstractComparisonOperatorBuilders;
		this.operator = operator;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	public TERMBUILDER value(Object value) {
		return collect(new ExplicitValueLocator(value));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TERMBUILDER parameter(String parameterName) {
		return collect(new ParameterValueLocator(parameterName));
	}
	
	private TERMBUILDER collect (ColumnValueLocator locator){
		parent.addConstraint(new ColumnValueConstraint(parent.leftSideValueLocator, operator,  locator));
		
		return parent.termBuilder;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public TERMBUILDER column(TypeDefinition<?> typeDef) {
		if (parent.termBuilder instanceof DelegatingRelationsBuilder){
			DelegatingRelationsBuilder builder = (DelegatingRelationsBuilder) parent.termBuilder;
			builder.setTargetDataSetModelName(typeDef.getQualifiedName().getQualifier());
			return parent.termBuilder;
		} else {
			return collect( new ColumnNameValueLocator(typeDef.getQualifiedName()));
		}
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ValueConstraintTermBuilder<TERMBUILDER> yearIn() {

		captures.add(ColumnValueCapture.yearOfTimeStamp());
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ValueConstraintTermBuilder<TERMBUILDER> dayIn() {

		captures.add(ColumnValueCapture.dayOfTimeStamp());
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ValueConstraintTermBuilder<TERMBUILDER> monthIn() {

		captures.add(ColumnValueCapture.monthOfTimeStamp());
		return this;
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ValueConstraintTermBuilder<TERMBUILDER> timeIn() {

		captures.add(ColumnValueCapture.timeOfTimeStamp());
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ValueConstraintTermBuilder<TERMBUILDER> dateIn() {

		captures.add(ColumnValueCapture.dateOfTimeStamp());
		return this;
	}

}
