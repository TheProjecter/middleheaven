package org.middleheaven.measures;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.middleheaven.storage.Storable;
import org.middleheaven.storage.Query;
import org.middleheaven.storage.StorageManager;
import org.middleheaven.storage.criteria.CriteriaBuilder;
import org.middleheaven.storage.naive.NaiveStoreManager;


public class StorageManagerTeste {

	StorageManager manager;
	TestSubject subj = new TestSubject();
	NaiveStoreManager store = new NaiveStoreManager();
	
	@Before
	public void setUp(){
		manager = new StorageManager(store);
		subj.setNacimento(new Date());
		subj.setName("Alberto");
	}
	
	@Test
	public void testInsert(){
		
		Query<TestSubject> q = manager.createQuery(CriteriaBuilder.createCriteria(TestSubject.class));
		
		subj = manager.store(subj);
		// verify correct types
		assertTrue (subj instanceof TestSubject);
		assertTrue (subj instanceof Storable);
		
		// very key is set
		Storable p = (Storable)subj;
		
		assertTrue(p.getKey()!=null);
		assertEquals( new Long(0), p.getKey());
		assertEquals(new Long(1) , new Long(q.count()));
		
		// verify re-insert does nothing
		subj = manager.store(subj);
		assertEquals( new Long(0), p.getKey());

		assertEquals(new Long(1) , new Long(q.count()));
		
	}
	

	public class TestSubject {

		
		private String name;
		private Date nacimento;
		
		protected String getName() {
			return name;
		}
		protected void setName(String name) {
			this.name = name;
		}
		protected Date getNacimento() {
			return nacimento;
		}
		protected void setNacimento(Date nacimento) {
			this.nacimento = nacimento;
		}
		
	}

}