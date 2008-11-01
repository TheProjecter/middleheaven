package org.middleheaven.test.storage;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.middleheaven.core.Container;
import org.middleheaven.core.bootstrap.StandaloneBootstrap;
import org.middleheaven.core.bootstrap.client.DesktopUIContainer;
import org.middleheaven.core.services.ServiceContextConfigurator;
import org.middleheaven.core.services.engine.ActivatorBagServiceDiscoveryEngine;
import org.middleheaven.domain.AnnotatedDomainModel;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFileRepositories;
import org.middleheaven.measures.StorageManagerTeste.TestSubject;
import org.middleheaven.storage.DataStorage;
import org.middleheaven.storage.DomainDataStorage;
import org.middleheaven.storage.Query;
import org.middleheaven.storage.StorableDomainModel;
import org.middleheaven.storage.StorableEntityModel;
import org.middleheaven.storage.criteria.CriteriaBuilder;
import org.middleheaven.storage.datasource.DataSourceServiceActivator;
import org.middleheaven.storage.xml.XMLStoreKeeper;
import org.middleheaven.util.identity.Identity;
import org.middleheaven.util.identity.IntegerIdentity;
import org.middleheaven.util.sequence.service.FileSequenceStorageActivator;


public class XMLStorageTest {

	static DataStorage ds;
	
	@Before
	public void setUp(){
		Container container = new DesktopUIContainer(ManagedFileRepositories.resolveFile(new File(".")));
		StandaloneBootstrap bootstrap = new StandaloneBootstrap(container);
		bootstrap.start();

		// Activator
		ActivatorBagServiceDiscoveryEngine engine = new ActivatorBagServiceDiscoveryEngine();
		engine.addActivator(new DataSourceServiceActivator());
		engine.addActivator(new FileSequenceStorageActivator());
		new ServiceContextConfigurator().addEngine(engine);

		// Configured
		
		final AnnotatedDomainModel model = AnnotatedDomainModel.model();
		model.addEntity(TestSubject.class);
		
		ManagedFile source = ManagedFileRepositories.resolveFile(this.getClass().getResource("data.xml"));
		ds = new DomainDataStorage(XMLStoreKeeper.manage(source) , new StorableDomainModel(){

			@Override
			public StorableEntityModel getStorageModel(Class<?> type) {
				return (StorableEntityModel) model.getEntityModelFor(type);
			}

			@Override
			public Class<? extends Identity> indentityTypeFor(Class<?> entityType) {
				return IntegerIdentity.class;
			}

		});

	}
	
	@Test 
	public void test(){
		Query<Subject> q = ds.createQuery(CriteriaBuilder.search(Subject.class).all());
		
		assertTrue(q.count()>1);
		
		Subject a = q.find();
		
		assertTrue(a.getName().equals("Ana"));
		assertTrue(a.getBirthdate()!=null);
		
		q = ds.createQuery(CriteriaBuilder.search(Subject.class)
				.and("name").not().startsWith("Jo�o")
				.and("activo").not().eq(false)
				.all()
		);
		
		assertTrue(q.count()==1);
		
		Subject b = q.find();
		
		assertFalse(a==b);
		assertTrue(b.getName().equals("Ana"));
		
		q = ds.createQuery(CriteriaBuilder.search(Subject.class)
				.and("name").eq("Ana")
				.and("activo").eq(false)
				.all()
		);
		
		assertTrue(q.isEmpty());
	}
}
