package org.middleheaven.persistance.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.middleheaven.collections.Enumerable;
import org.middleheaven.collections.Pair;
import org.middleheaven.logging.Logger;
import org.middleheaven.persistance.DataColumn;
import org.middleheaven.persistance.DataQuery;
import org.middleheaven.persistance.DataRow;
import org.middleheaven.persistance.DataRowStream;
import org.middleheaven.persistance.DataSet;
import org.middleheaven.persistance.DataSetNotFoundException;
import org.middleheaven.persistance.DataStore;
import org.middleheaven.persistance.DataStoreName;
import org.middleheaven.persistance.DataStoreNotFoundException;
import org.middleheaven.persistance.DataStoreProvider;
import org.middleheaven.persistance.DataStoreSchema;
import org.middleheaven.persistance.DataStoreSchemaName;
import org.middleheaven.persistance.HashDataRow;
import org.middleheaven.persistance.ModelNotEditableException;
import org.middleheaven.persistance.NamedQueryExecutor;
import org.middleheaven.persistance.ParameterizedDataQuery;
import org.middleheaven.persistance.RelatedDataSet;
import org.middleheaven.persistance.SequenceNotFoundException;
import org.middleheaven.persistance.criteria.DataSetConstraint;
import org.middleheaven.persistance.criteria.DataSetCriteria;
import org.middleheaven.persistance.criteria.LogicConstraint;
import org.middleheaven.persistance.criteria.building.ColumnNameValueLocator;
import org.middleheaven.persistance.criteria.building.ColumnValueConstraint;
import org.middleheaven.persistance.criteria.building.ExplicitValueLocator;
import org.middleheaven.persistance.db.datasource.DataSourceService;
import org.middleheaven.persistance.db.mapping.DataBaseMapper;
import org.middleheaven.persistance.db.mapping.DatasetRepositoryModelDataBaseMapper;
import org.middleheaven.persistance.db.mapping.IllegalModelStateException;
import org.middleheaven.persistance.db.metamodel.DBColumnModel;
import org.middleheaven.persistance.db.metamodel.DBTableModel;
import org.middleheaven.persistance.db.metamodel.DataBaseModel;
import org.middleheaven.persistance.db.metamodel.DataBaseObjectModel;
import org.middleheaven.persistance.db.metamodel.DataBaseObjectType;
import org.middleheaven.persistance.db.metamodel.EditableDBTableModel;
import org.middleheaven.persistance.db.metamodel.EditableDataBaseModel;
import org.middleheaven.persistance.db.metamodel.SequenceModel;
import org.middleheaven.persistance.model.DataColumnModel;
import org.middleheaven.sequence.Sequence;
import org.middleheaven.storage.dataset.mapping.DatasetRepositoryModel;
import org.middleheaven.util.QualifiedName;
import org.middleheaven.util.classification.LogicOperator;
import org.middleheaven.util.criteria.CriterionOperator;
import org.middleheaven.util.function.Block;
import org.middleheaven.util.function.Mapper;

/**
 * A {@link DataStore} that reads data from a Relational Data Base Management System (RDBMS)
 */
public class RDBMSDataStoreProvider implements DataStoreProvider  {


	private DataSourceService dsService;
	private DataSourceNameResolver dataSourceNameResolver;

	private final Map<DataStoreSchemaName , Map < String , NamedQueryExecutor>> executors = new HashMap<DataStoreSchemaName , Map < String , NamedQueryExecutor>>();


	private final Map<DataStoreName , DataStore> stores = new HashMap<DataStoreName , DataStore>();


	/**
	 * 
	 * Constructor.
	 * @param datasource the source of data.
	 * @param mapper the database mapper.
	 */
	protected RDBMSDataStoreProvider (DataSourceService dsService, DataSourceNameResolver dataSourceNameResolver){
		this.dsService = dsService;
		this.dataSourceNameResolver = dataSourceNameResolver;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerDataStore(DataStoreName name, DatasetRepositoryModel dataSetModel) {

		String dataSourceName = dataSourceNameResolver.resolveDataSourceName(name);

		DataSource datasource = dsService.getDataSource(dataSourceName);

		RDBMSDialect dialect = RDBMSDialectFactory.getDialect(datasource);

		DataBaseMapper mapper = DatasetRepositoryModelDataBaseMapper.newInstance(dataSetModel);

		DataStore ds = new RDBMSDataStore(name , mapper, dialect, datasource);

		this.stores.put(name, ds);

	}

	protected long countSearchPlan(SearchPlan plan) {
		// TODO 
		return 0;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public DataStore getDataStore(DataStoreName name) throws DataStoreNotFoundException {

		DataStore ds = this.stores.get(name);

		if (ds == null) {
			throw new DataStoreNotFoundException();
		}

		return ds;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isProviderDataStore(DataStoreName name) {
		return this.stores.containsKey(name);
	}


	private class RDBMSDataStore implements DataStore {


		private DataStoreName name;
		private final DataBaseMapper mapper;
		private final DataSource datasource;
		private final RDBMSDialect dialect;

		public RDBMSDataStore (DataStoreName name,  DataBaseMapper mapper, RDBMSDialect dialect , DataSource datasource){
			this.name = name;
			this.mapper = mapper;
			this.dialect = dialect;
			this.datasource = datasource;
		}

		private void executeCommand(DataBaseCommand command) {

			Connection con = null;
			try {
				con = datasource.getConnection();

				command.execute(mapper, con, null);

			} catch (SQLException e) {
				Logger.onBookFor(this.getClass()).trace("SQL : {0}", command.getSQL());
				throw dialect.handleSQLException(e);
			} finally {
				close(con);
			}
		}

		private void close(Connection con) {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				throw dialect.handleSQLException(e);
			}
		}


		/**
		 * {@inheritDoc}
		 */
		@Override
		public DataStoreName getName() {
			return name;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public DataStoreSchema getDataStoreSchema(final DataStoreSchemaName schemaName) {


			return new DataStoreSchema(){

				/**
				 * {@inheritDoc}
				 */
				@Override
				public DataQuery query(DataSetCriteria criteria) {
					return new RDBMSDataQuery(RDBMSDataStore.this, this, plan(criteria));
				}

				protected SearchPlan plan(DataSetCriteria criteria) {

					SearchPlan plan = new SearchPlan();
					plan.setCountOnly(false); // TODO determine
					plan.setDistinct(criteria.isDistinct());
					plan.setFowardOnly(true);
					plan.setReadOnly(true);
					plan.setMaxCount(criteria.getMaxCount());
					plan.setOffSet(criteria.getOffset());
					plan.setReadOnly(true);
					plan.setResultColumns(criteria.getResultColumns());
					plan.setConstraints(criteria.getDataSetRestrictions());
					plan.setOrdering(criteria.getOrderConstraints());
					plan.setGrouping(criteria.getGroupConstraint());

					for (RelatedDataSet rd : criteria.getRelatedDataSets()){

						TableRelation relation = new TableRelation(new LogicConstraint(rd.getRelationConstraint().getOperator()) , rd.getRelationOperator());
						relation.setSourceTableModel(mapper.getTableForDataSet(rd.getSourceDataSetModel().getName()));
						plan.add(relation);

						if (!rd.getRelationConstraint().getConstraints().isEmpty()){
							relation = new TableRelation(new LogicConstraint(rd.getRelationConstraint().getOperator()) , rd.getRelationOperator());


							for (DataSetConstraint c : rd.getRelationConstraint().getConstraints()){

								if (c instanceof ColumnValueConstraint) {
									QualifiedName q = ( (ColumnValueConstraint) c).getLeftValuelocator().getName();

									if (q != null){

										relation.setSourceTableModel(mapper.getTableColumnModel(q).getTableModel());
									}

									q = ( (ColumnValueConstraint) c).getRightValueLocator().getName();

									if (q != null){

										relation.setTargetTableModel(mapper.getTableColumnModel(q).getTableModel());
									}


									relation.getRelationConstraint().addConstraint(rd.getRelationConstraint());

								} else {
									throw new IllegalStateException("Should not happen");
								}

							}

							plan.add(relation);
						}

					}


					return plan;
				}

				/**
				 * {@inheritDoc}
				 */
				@Override
				public DataSet getDataSet(String name) throws DataSetNotFoundException {
					return new RDBMSDataSet(RDBMSDataStore.this, this, name);
				}

				/**
				 * {@inheritDoc}
				 */
				@Override
				public void updateModel() throws ModelNotEditableException {

					EditableDataBaseModel dbModel = new EditableDataBaseModel();

					for (DBTableModel tm  : mapper.getTableModels()){
						if (tm.isEmpty()){
							throw new IllegalModelStateException("Table " + tm.getName() + " has no columns");
						}
						dbModel.addDataBaseObjectModel(EditableDBTableModel.valueOf(tm));
					}


					dialect.extendsDatabaseModel(dbModel);

					DataBaseModel existingDBModel = dialect.readDataBaseModel(getName().getName(), datasource);


					for (DataBaseObjectModel dbObject : dbModel) {

						DataBaseObjectModel existingModel = existingDBModel.getDataBaseObjectModel(
								dbObject.getName(), 
								dbObject.getType()
								);

						if (existingModel != null) {
							// alter objets
							if (dbObject.getType().equals(DataBaseObjectType.TABLE)) {

								EditableDBTableModel newModel = (EditableDBTableModel) dbObject;
								EditableDBTableModel oldModel = (EditableDBTableModel) existingModel;

								EditableDBTableModel diff = newModel.differenceTo(oldModel);

								// alter only creates, does not remove
								if (!diff.isEmpty()) {
									DataBaseCommand command = dialect.createAlterTableCommand(diff);
									executeCommand(command);
								}

							}
						} else {
							// create object
							try {
								if (dbObject.getType().equals(DataBaseObjectType.TABLE)) {
									EditableDBTableModel tm = (EditableDBTableModel) dbObject;

									DataBaseCommand command = dialect.createCreateTableCommand(tm);

									executeCommand(command);

									// create Indexes	
									// Group columns into the indexes 

									final List<DataBaseCommand> indexComands = new LinkedList<DataBaseCommand>();

									tm.columns().groupBy(new Mapper<String, DBColumnModel>(){

										@Override
										public String apply(DBColumnModel obj) {
											return obj.getUniqueGroupName();
										}

									}).forEach( new Block<Pair<String,Enumerable<DBColumnModel>>>(){

										@Override
										public void apply(Pair<String,Enumerable<DBColumnModel>> entry) {

											indexComands.add(dialect.createCreateIndexCommand(entry.getValue() , true));

										}

									});

									for (DataBaseCommand idxCommand :  indexComands){
										executeCommand(idxCommand);
									}


								} else if (dbObject.getType().equals(
										DataBaseObjectType.SEQUENCE)) {
									DataBaseCommand command = dialect
											.createCreateSequenceCommand((SequenceModel) dbObject);
									executeCommand(command);
								}

							} catch (TableAlreadyExistsException e) {
								Logger.onBookFor(this.getClass()).info(
										"Table {0} already exists.", dbObject.getName());
							}
						}
					}

				}

				@Override
				public Sequence<Long> getSequence(
						String sequenceName) throws SequenceNotFoundException {

					DBTableModel tbModel = mapper.getTableForDataSet(sequenceName);

					if (tbModel  != null){
						sequenceName = tbModel.getName();
					}

					return dialect.getSequence(datasource, sequenceName);


				}

				@Override
				public void registerNamedCriteria(String name,
						NamedQueryExecutor queryExecutor) {

					Map<String, NamedQueryExecutor> map = executors.get(schemaName);

					if (map == null){
						map = new HashMap<String, NamedQueryExecutor>();
						map.put(name, queryExecutor);

						executors.put(schemaName, map);
					}

				}

				@Override
				public ParameterizedDataQuery namedQuery(String name) {

					Map<String, NamedQueryExecutor> map = executors.get(schemaName);

					if (map == null){
						throw new IllegalArgumentException("Not query named " + name + " exists");
					}

					NamedQueryExecutor queryExecutor = map.get(name);

					if (queryExecutor == null){
						throw new IllegalArgumentException("Not query named " + name + " exists");
					}

					return queryExecutor.execute(RDBMSDataStoreProvider.this);


				}

				@Override
				public boolean isReadable() {
					return true; // TODO check permitions
				}

				@Override
				public boolean isWritable() {
					return true; // TODO check permitions
				}

			};
		}


	}




	private class RDBMSDataQuery implements DataQuery {


		private DataStoreSchema dataStoreSchema;
		private SearchPlan searchPlan;
		private RDBMSDataStore rdbmsDataStore;

		public RDBMSDataQuery (RDBMSDataStore rdbmsDataStore, DataStoreSchema dataStoreSchema, SearchPlan searchPlan){
			this.dataStoreSchema = dataStoreSchema;
			this.searchPlan = searchPlan;
			this.rdbmsDataStore = rdbmsDataStore;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public DataRowStream getRowStream() {
			return executeSearchPlan(searchPlan);
		}

		protected DataRowStream executeSearchPlan(SearchPlan plan) {
			ResultSet rs = null;
			Connection con = null;
			try {

				con = rdbmsDataStore.datasource.getConnection();

				RetriveDataBaseCommand command = rdbmsDataStore.dialect.createSelectCommand(plan, rdbmsDataStore.mapper);
				Logger.onBook("SQL").trace(command.toString());

				command.execute( rdbmsDataStore.mapper, con, null);
				rs = command.getResult();

				// if dialect does not support offset
				if (!rdbmsDataStore.dialect.supportsOffSet()) {

					try {
						rs.absolute(plan.getOffSet() + 1);

					} catch (SQLFeatureNotSupportedException e){
						// iterate manually  until we arrive to the offset
						int off = 1;
						while (off < plan.getOffSet() && rs.next()) {
							//iterate
							off++;
						}
					}

				}

			} catch (SQLException e) {
				rdbmsDataStore.close(con);
				throw rdbmsDataStore.dialect.handleSQLException(e);
			}


			if (plan.isFowardOnly() && plan.isReadOnly()) {
				// fastlane
				try {
					return  ResultSetDataRowStream.newInstance(rs, rdbmsDataStore.mapper, plan, rdbmsDataStore.dialect);
				} catch (SQLException e) {
					throw rdbmsDataStore.dialect.handleSQLException(e);
				} 

			} else {

				try {
					// load all objects onto a list

					DataRow sourceRow = ResultSetDataRow.newInstance(rs, rdbmsDataStore.dialect);

					if (rdbmsDataStore.dialect.supportsCountLimit()) {
						ListDataRowStream stream = new ListDataRowStream();

						while (rs.next()) {
							stream.addRow(new HashDataRow(sourceRow));
						}
						return stream;
					} else {

						ListDataRowStream stream = new ListDataRowStream(plan.getMaxCount());

						int count = 0;
						while (rs.next() && count < plan.getMaxCount()) {

							stream.addRow(new HashDataRow(sourceRow));

							count++;

						}
						return stream;
					}
				} catch (SQLException e) {
					throw rdbmsDataStore.dialect.handleSQLException(e);
				} finally {
					try {
						rs.close();
						rdbmsDataStore.close(con);
					} catch (SQLException e) {
						throw rdbmsDataStore.dialect.handleSQLException(e);
					}
				}

			}

		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public long rowCount() {
			return countSearchPlan(searchPlan);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isEmpty() {
			return rowCount() == 0;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public DataQuery limit(int startAt, int maxCount) {
			return new RDBMSDataQuery (rdbmsDataStore,this.dataStoreSchema, searchPlan.limit(startAt , maxCount));
		}

	}

	private class RDBMSDataSet implements DataSet {


		private String datasetName;
		private DataStoreSchema schema;
		private RDBMSDataStore rdbmsDataStore;

		public RDBMSDataSet (RDBMSDataStore rdbmsDataStore, DataStoreSchema dataStoreSchema, String datasetName){
			this.schema = dataStoreSchema;
			this.datasetName = datasetName;
			this.rdbmsDataStore = rdbmsDataStore;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getName() {
			return datasetName;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void insert(Collection<DataRow> dataRows) {

			if (dataRows.isEmpty()){
				return;
			}

			DBTableModel model = rdbmsDataStore.mapper.getTableForDataSet(datasetName);

			if (model != null) {
				rdbmsDataStore.executeCommand(rdbmsDataStore.dialect.createInsertCommand(dataRows, model));
			}
		}


		/**
		 * {@inheritDoc}
		 */
		@Override
		public void delete(Collection<DataRow> dataRows) {

			if (dataRows.isEmpty()){
				return;
			}

			DBTableModel model = rdbmsDataStore.mapper.getTableForDataSet(datasetName);

			if (model != null) {

				// transform the rows to a criteria of keys
				DataSetCriteria deleteCriteria = new DataSetCriteria();

				if (dataRows.size() == 1){
					LogicConstraint and = new LogicConstraint(LogicOperator.and());

					DataRow row = dataRows.iterator().next();

					for (DataColumn col : row){
						final DataColumnModel dcm = col.getModel();
						if (dcm.isInPrimaryKeyGroup()){
							and.addConstraint(new ColumnValueConstraint(
									new ColumnNameValueLocator(dcm.getName()),
									CriterionOperator.EQUAL,
									new ExplicitValueLocator(row.getColumn(dcm.getName()))
									));
						}
					}

				} else {
					LogicConstraint or = new LogicConstraint(LogicOperator.or());
					for (DataRow row : dataRows) { 
						LogicConstraint and = new LogicConstraint(LogicOperator.and());

						for (DataColumn col : row){
							final DataColumnModel dcm = col.getModel();
							if (dcm.isInPrimaryKeyGroup()){
								and.addConstraint(new ColumnValueConstraint(
										new ColumnNameValueLocator(dcm.getName()),
										CriterionOperator.EQUAL,
										new ExplicitValueLocator(row.getColumn(dcm.getName()))
										));
							}
						}

						or.addConstraint(and);
					}
				}



				rdbmsDataStore.executeCommand(rdbmsDataStore.dialect.createDeleteCommand(deleteCriteria, model));
			}


		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void delete(DataSetCriteria criteria) {
			DBTableModel model = rdbmsDataStore.mapper.getTableForDataSet(datasetName);

			if (model != null){
				rdbmsDataStore.executeCommand(rdbmsDataStore.dialect.createDeleteCommand(criteria, model));
			}

		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void update(Collection<DataRow> dataRows) {
			if (dataRows.isEmpty()){
				return;
			}

			DBTableModel model = rdbmsDataStore.mapper.getTableForDataSet(datasetName);

			if (model != null) {
				rdbmsDataStore.executeCommand(rdbmsDataStore.dialect.createUpdateCommand(dataRows, model));
			}
		}



	}






}
