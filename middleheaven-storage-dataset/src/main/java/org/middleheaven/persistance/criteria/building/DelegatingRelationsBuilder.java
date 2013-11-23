/**
 * 
 */
package org.middleheaven.persistance.criteria.building;

import java.util.Collection;

import org.middleheaven.persistance.RelatedDataSet;
import org.middleheaven.persistance.criteria.LogicConstraint;
import org.middleheaven.persistance.model.DataColumnsModel;
import org.middleheaven.persistance.model.DataSetModel;
import org.middleheaven.persistance.model.TypeDefinition;
import org.middleheaven.util.classification.LogicOperator;

/**
 * 
 */
public class DelegatingRelationsBuilder implements RelationsBuilder {

	private DataSetCriteriaBuilder dataSetCriteriaBuilder;
	private DelegatingRelationCrossBuilder delegatingRelationCrossBuilder;

	private RelatedDataSet relatedDataSet;
	

	/**
	 * Constructor.
	 * @param dataSetCriteriaBuilder 
	 * @param delegatingRelationCrossBuilder 
	 * @param relationOperator
	 */
	public DelegatingRelationsBuilder(DataSetCriteriaBuilder dataSetCriteriaBuilder, DelegatingRelationCrossBuilder delegatingRelationCrossBuilder, RelationOperator relationOperator) {
		this.dataSetCriteriaBuilder = dataSetCriteriaBuilder;
		this.delegatingRelationCrossBuilder= delegatingRelationCrossBuilder;
		this.relatedDataSet = new RelatedDataSet( new LogicConstraint(LogicOperator.and()), relationOperator);
		
	}
	
	/**
	 * @param qualifier
	 */
     void setTargetDataSetModelName(String name) {
    	 relatedDataSet.setTargetDataSetModel(new NameOnlyDataSetModel(name));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RelationColumnsBuilder relation() {
		return new RelationColumnsBuilder() {

			@Override
			public <Type> RelationComparisonOperatorBuilder on(TypeDefinition<Type> column) {
				
				relatedDataSet.setSourceDataSetModel(new NameOnlyDataSetModel(column.getQualifiedName().getQualifier()));
				dataSetCriteriaBuilder.criteria.addRelatedDataSet(relatedDataSet);
				
				return m(column);
			}

			private <Type> RelationComparisonOperatorBuilder m(
					TypeDefinition<Type> column) {
				
				return new DelegatingRelationComparisonOperatorBuilder( 
						new ColumnNameValueLocator(column.getQualifiedName()),
						dataSetCriteriaBuilder,
						new TargetRelationBuilder(){

							@Override
							public RelationCrossBuilder with() {
								return delegatingRelationCrossBuilder;
							}

							@Override
							public LogicRelationBuilder and() {
								return new LogicRelationBuilder(){

									@Override
									public <Type> RelationComparisonOperatorBuilder on(
											TypeDefinition<Type> column) {
										return m(column);
									}
									
								};
							}

							@Override
							public LogicRelationBuilder or() {
								return new LogicRelationBuilder(){

									@Override
									public <Type> RelationComparisonOperatorBuilder on(
											TypeDefinition<Type> column) {
										return m(column);
									}
									
								};
							}

							@Override
							public DataSetCriteriaBuilder endRelations() {
								return dataSetCriteriaBuilder;
							}
							
						},
						relatedDataSet.getRelationConstraint()
				);
			}

			@Override
			public ResultColumnSetBuilder endRelations() {
				return dataSetCriteriaBuilder;
			}

			@Override
			public RelationCrossBuilder with() {
				return delegatingRelationCrossBuilder;
			}
			
		};
	}
	
	private static class NameOnlyDataSetModel implements DataSetModel {

		private String name;

		public NameOnlyDataSetModel (String name){
			this.name = name;
		}
		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getName() {
			return name;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public DataColumnsModel getModelColumns() {
			throw new UnsupportedOperationException("Not implememented yet");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public DataColumnsModel getPrimaryKeyColumns() {
			throw new UnsupportedOperationException("Not implememented yet");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Collection<DataColumnsModel> getUniqueGroupsColumns() {
			throw new UnsupportedOperationException("Not implememented yet");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Collection<DataColumnsModel> getIndexGroupsColumns() {
			throw new UnsupportedOperationException("Not implememented yet");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Collection<DataColumnsModel> getForeignKeyColumns() {
			throw new UnsupportedOperationException("Not implememented yet");
		}
		
	}



}
