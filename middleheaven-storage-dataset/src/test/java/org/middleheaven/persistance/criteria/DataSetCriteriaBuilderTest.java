package org.middleheaven.persistance.criteria;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.MalformedURLException;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.middleheaven.core.annotations.Wire;
import org.middleheaven.core.bootstrap.BootstrapChain;
import org.middleheaven.core.bootstrap.BootstrapContainerExtention;
import org.middleheaven.core.bootstrap.BootstrapContext;
import org.middleheaven.core.services.Service;
import org.middleheaven.core.services.ServiceBuilder;
import org.middleheaven.core.services.ServiceNotAvailableException;
import org.middleheaven.core.services.ServiceProvider;
import org.middleheaven.core.services.ServiceSpecification;
import org.middleheaven.core.wiring.BindConfiguration;
import org.middleheaven.core.wiring.Binder;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.machine.MachineFiles;
import org.middleheaven.persistance.DataQuery;
import org.middleheaven.persistance.DataRow;
import org.middleheaven.persistance.DataRowStream;
import org.middleheaven.persistance.DataService;
import org.middleheaven.persistance.DataServiceActivator;
import org.middleheaven.persistance.DataStore;
import org.middleheaven.persistance.DataStoreNotFoundException;
import org.middleheaven.persistance.DataStoreSchema;
import org.middleheaven.persistance.DataStoreSchemaName;
import org.middleheaven.persistance.DataStoreSchemaNotFoundException;
import org.middleheaven.persistance.DefaultDataService;
import org.middleheaven.persistance.ModelNotEditableException;
import org.middleheaven.persistance.criteria.building.DataSetCriteriaBuilder;
import org.middleheaven.persistance.db.datasource.DataSourceService;
import org.middleheaven.persistance.db.datasource.DataSourceServiceActivator;
import org.middleheaven.persistance.db.datasource.EmbeddedDSProvider;
import org.middleheaven.persistance.db.datasource.HashDataSourceService;
import org.middleheaven.persistance.db.mapping.ModelParsingException;
import org.middleheaven.persistance.model.DataSetDefinition;
import org.middleheaven.persistance.model.DataSetDefinitions;
import org.middleheaven.persistance.model.TypeDefinition;
import org.middleheaven.quantity.money.CentsMoney;
import org.middleheaven.storage.dataset.mapping.DatasetRepositoryModel;
import org.middleheaven.storage.dataset.mapping.DatasetRepositoryModelBuilder;
import org.middleheaven.storage.dataset.mapping.XmlDrivenDatasetRepositoryModelReader;
import org.middleheaven.tool.test.MiddleHeavenTestCase;
import org.middleheaven.util.QualifiedName;

public class DataSetCriteriaBuilderTest extends MiddleHeavenTestCase {

	
	private interface Person  extends DataSetDefinition<Person>{
		
		Person $ = DataSetDefinitions.define(Person.class);
		
		TypeDefinition<Integer> ID();
		TypeDefinition<String> Name();
		TypeDefinition<Date> Birthdate();
		
		
	}
	
	private interface Veicle  extends DataSetDefinition<Veicle> {
		
		Veicle $ = DataSetDefinitions.define(Veicle.class);
		
		TypeDefinition<Integer> OwnerID();
		TypeDefinition<String> Model();
		TypeDefinition<CentsMoney> Value();
		TypeDefinition<Integer> Year();
		
	}
	
	private interface Rent  extends DataSetDefinition<Rent>{
		
		Rent $ = DataSetDefinitions.define(Rent.class);
		
		TypeDefinition<Integer> PersonID();
		TypeDefinition<CentsMoney> Income();
		TypeDefinition<Date> Date();
	}

	protected void setupWiringBundles(WiringService service){
	
		service.addConfiguration(new BindConfiguration(){

			@Override
			public void configure(Binder binder) {
				binder.bind(DataSourceService.class).inSharedScope().toInstance(new HashDataSourceService());
				binder.bind(DataService.class).inSharedScope().toInstance(new DefaultDataService());
			}
			
		});
	}
	
	private DataService dataPersistanceService;

	@Wire 
	public void setDataPersistanceService (DataService dataPersistanceService){
		this.dataPersistanceService = dataPersistanceService;
		
	}

	@Test
	public void testDefinitions(){
	
		assertNotNull(Person.$);
		assertEquals(QualifiedName.qualify("person", "birthdate"),Person.$.Birthdate().getQualifiedName());
		
	}
	
	@Test @Ignore// TODO FIX
	public void testWriting() throws ManagedIOException, ModelParsingException, DataStoreNotFoundException, MalformedURLException, DataStoreSchemaNotFoundException, ModelNotEditableException{
		
		ManagedFile vfs =  MachineFiles.getDefaultFolder();
		
		ManagedFile config = vfs.retrive("src/test/resources/dsmapping.xml");

		final DataStoreSchemaName dataStoreSchemaName = DataStoreSchemaName.name("testdb", "public");

		DatasetRepositoryModel datasetModel = DatasetRepositoryModelBuilder.newInstance()
				.addReader(XmlDrivenDatasetRepositoryModelReader.newInstance(config))
				.build();
		
		EmbeddedDSProvider eds = EmbeddedDSProvider.provider("testdb", "sa", "");
		eds.start();

		dataPersistanceService.addProvider(eds.getDataStoreProvider());
		
		// config datastore 
		dataPersistanceService.registerDataStore(dataStoreSchemaName.getDataStoreName(), datasetModel);

		// Update DB Model
		DataStore store = dataPersistanceService.getDataStore(dataStoreSchemaName.getDataStoreName());

		DataStoreSchema schema = store.getDataStoreSchema(dataStoreSchemaName);

		schema.updateModel();
		
		DataSetCriteria criteriaA = DataSetCriteriaBuilder
		.retrive()
			.withColumns()
				.column(Person.$.ID()).as("id").value()
				.column(Person.$.Name()).value()
				.column(Person.$.Birthdate()).value()
				.column(Veicle.$.Year()).value()
				.column(Veicle.$.Model()).value()
				.column(Veicle.$.Value()).as("totalCost").sum()
				.column(Rent.$.Income()).as("maxIncome").max()
			.endColumns()
		.related()
			.with().inner().relation()
					.on(Person.$.ID()).eq().column(Veicle.$.OwnerID())
			.with().outter().left().relation()
					.on(Person.$.ID()).eq().column(Rent.$.PersonID())
					.and()
					.on(Person.$.ID()).eq().column(Rent.$.PersonID())
			.endRelations()
		.restricted()
				.withTerm()
					 .column(Veicle.$.Year()).gt().value(1940)
		 			 .and()
		 			 .column(Veicle.$.Year()).gt().yearIn().column(Rent.$.Date())
		 	   .endTerm()
		 	   		.or()
		 	   .withTerm()
		 	   		.column(Veicle.$.Year()).eq().value(1930)
		 	   		.and()
		 	   		.column(Veicle.$.Year()).gt().yearIn().column(Rent.$.Date())
		 	   .endTerms()
		.ordered()
			.withOrdering()
				.by(Person.$.Name()).asc()
				.by("totalCost").asc()
			.endOrdering()
		.grouped()
			.withGroup()
				.column(Veicle.$.Model())
				.column(Veicle.$.Year())
			.endGroup()
//		.aggregated()
//			.withExpectation()
//				.column(Veicle.$.Value()).as("totalCost").sum().gt().value(10000)
//			.endExpectation()
//				.and()
//			.withExpectation()
//				.column(Veicle.$.Value()).value().not().eq().column(null).sum()
//			.endExpectations()
		.all();
		
		assertNotNull(criteriaA);
		
//		
//		DataSetCriteria criteriaB = DataSetCriteriaBuilder.retrive()
//			.rowsCount().as("count")
//		.all();
//		
//		assertNotNull(criteriaB);
		
	
		
		 DataQuery dq = schema.query(criteriaA);
		 
		 DataRowStream st = dq.getRowStream();
		 
		 try {
			 while (st.next()){
				 
				 DataRow row = st.currentRow();
				 
				assertNotNull(row);
			 }
		 } finally {
			 st.close();
		 }
//		
//		
//		.addDataSet("Person").readColumns("ID", "Name", "Birthdate").orderBy("Name", "Birthdate").end();
//		
//		DataSetCriteriaBuilder.start().addDataSet("Person").calculate().count().column("Name").end();
//		DataSetCriteriaBuilder.start().addDataSet("Person").calculate().count().all().end();
//		
//		DataSetCriteriaBuilder.start()
//		.addDataSet("Veicle")
//			.readColumns("Model", "Value").groupBy("Model", "Year").calculate().sum().column("Value")
//			.relateBy().column("Veicle","OwnerID").to("Person","ID")
//				.and()
//				.column("Veicle","OwnerName").to("Person","Name")
//				.toDataSet("Person")
//					.readColumns("Name", "SocialNumber")
//					.conjunction().column("Birthday").lt(new Date())
//				.back()
//		.all();
		
		 
		 eds.stop();
	}


}
