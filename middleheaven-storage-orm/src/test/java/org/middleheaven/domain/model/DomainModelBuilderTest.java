/**
 * 
 */
package org.middleheaven.domain.model;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.middleheaven.core.reflection.ClassSet;
import org.middleheaven.storage.testdomain.TestSubject;

/**
 * 
 */
public class DomainModelBuilderTest {


	@Test
	public void testDomainModelReader (){
		
		ClassSet classes = new ClassSet().add(TestSubject.class.getPackage());
		
		DomainModel model = new DomainModelBuilder().build(classes);
		
		assertNotNull(model);
	}
}
