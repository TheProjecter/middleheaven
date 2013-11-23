/**
 * 
 */
package org.middleheaven.persistance.db;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Iterator;

import org.middleheaven.collections.enumerable.IncrementIterator;
import org.middleheaven.persistance.model.ColumnValueType;
import org.middleheaven.persistance.model.DataColumnModel;
import org.middleheaven.persistance.model.DataColumnsModel;
import org.middleheaven.persistance.model.DataSetModel;
import org.middleheaven.util.QualifiedName;

/**
 * 
 */
public class ResultSetMetaDataDataColumnsModel implements DataColumnsModel {

	private ResultSetMetaData metaData;
	private RDBMSDialect dialect;

	/**
	 * Constructor.
	 * @param metaData
	 */
	public ResultSetMetaDataDataColumnsModel(ResultSetMetaData metaData, RDBMSDialect dialect) {
		this.metaData = metaData;
		this.dialect = dialect;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<DataColumnModel> iterator() {
		try {
			return new IncrementIterator<DataColumnModel> (metaData.getColumnCount()){
	
				@Override
				protected DataColumnModel nextFor(final int column) {
					return new DataColumnModel (){

						@Override
						public DataSetModel getDataSetModel() {
							return null;
						}

						@Override
						public QualifiedName getName() {
							try {
								return QualifiedName.qualify(
										metaData.getTableName(column),
										metaData.getColumnName(column)
										
								);
							} catch (SQLException e) {
								throw dialect.handleSQLException(e);
							}
						}

						@Override
						public int getSize() {
							try {
								// metaData.getScale(column) 
								return metaData.getColumnDisplaySize(column);
							} catch (SQLException e) {
								throw dialect.handleSQLException(e);
							}
						}

						@Override
						public int getPrecision() {
							try {
								return metaData.getPrecision(column);
							} catch (SQLException e) {
								throw dialect.handleSQLException(e);
							}
						}

						@Override
						public boolean isNullable() {
							try {
								return metaData.isNullable(column) == ResultSetMetaData.columnNullable ;
							} catch (SQLException e) {
								throw dialect.handleSQLException(e);
							}
						}
						
						@Override
						public ColumnValueType getType() {
							try {
								return dialect.typeFromNative(metaData.getColumnType(column)) ;
							} catch (SQLException e) {
								throw dialect.handleSQLException(e);
							}
						}

						@Override
						public String getIndexGroup() {
							return null;
						}

						@Override
						public String getUniqueGroup() {
							return null;
						}

						@Override
						public String getPrimaryKeyGroup() {
							return null;
						}

						@Override
						public boolean isInPrimaryKeyGroup() {
							return false;
						}

						@Override
						public boolean isInIndexGroup() {
							return false;
						}

						@Override
						public boolean isInUniqueGroup() {
							return false;
						}

				

						@Override
						public boolean isVersion() {
							return false;
						}
						
						
					};
				}
				
			};
		} catch (SQLException e){
			throw dialect.handleSQLException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DataColumnModel getDataColumnModel(QualifiedName columnName) {
		
		
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsColumn(QualifiedName name) {
		// TODO Auto-generated method stub
		return false;
	}

}
