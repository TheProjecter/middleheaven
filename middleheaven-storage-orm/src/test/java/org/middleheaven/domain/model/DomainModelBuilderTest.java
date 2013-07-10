/**
 * 
 */
package org.middleheaven.domain.model;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.middleheaven.core.reflection.ClassSet;
import org.middleheaven.domain.store.mapping.AnnotationDomainModelDataSetTypeMapper;
import org.middleheaven.domain.store.mapping.DomainModelDataSetTypeMapper;
import org.middleheaven.domain.store.mapping.EntityModelDataSetMapping;
import org.middleheaven.persistance.DataStoreSchemaName;
import org.middleheaven.storage.testdomain.TestSubject;
import org.middleheaven.tool.test.MiddleHeavenTestCase;

/**
 * 
 */
public class DomainModelBuilderTest extends MiddleHeavenTestCase{


	@Test
	public void testDomainModelReader (){

		ClassSet classes = new ClassSet().add(TestSubject.class.getPackage());
		
		DomainModel domainModel = new DomainModelBuilder().build(classes);
		
		assertNotNull(domainModel);
		
		final DataStoreSchemaName dataStoreSchemaName = DataStoreSchemaName.name("javabuilding", "public");

		// mapping entity to dataset
		DomainModelDataSetTypeMapper dmMapper = AnnotationDomainModelDataSetTypeMapper.newInstance(dataStoreSchemaName, domainModel);
		
		EntityModelDataSetMapping mapping = dmMapper.getMappingFor(domainModel.getModelFor("TestSubject"));
		
		assertNotNull(mapping);
	}
}
