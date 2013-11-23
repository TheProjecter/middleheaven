package org.middleheaven.text.indexing.lucene;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.util.Version;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.middleheaven.io.repository.machine.MachineFiles;
import org.middleheaven.text.indexing.DocumentModel;
import org.middleheaven.text.indexing.IndexableDocument;
import org.middleheaven.text.indexing.SearchHits;
import org.middleheaven.text.indexing.SimpleDocumentFieldModel;
import org.middleheaven.text.indexing.SimpleIndexableDocument;
import org.middleheaven.text.indexing.TextIndex;
import org.middleheaven.util.criteria.text.TextCriteriaBuilder;


public class TestTextIndex {

	File file = new File("./indexes");

	@Before
	public void before(){
		file.mkdirs();
	}

	@After
	public void after(){
		for (File f : file.listFiles()){
			f.delete();
		}
		if (!file.delete()){
			throw new AssertionError("Could not delete file");
		}
	}

	@Test
	public void testIndexing(){


		DocumentModel docModel = new DocumentModel("ALFA");
		docModel.addFieldModel(SimpleDocumentFieldModel.field("text", true));
		docModel.addFieldModel(SimpleDocumentFieldModel.field("age", true));

		LuceneTextIndexingService service = new LuceneTextIndexingService();

		service.configurateIndex( "index", MachineFiles.getDefaultFolder().retrive("indexes") , new  StopAnalyzer(Version.LUCENE_30));
		service.configureDocument("index", docModel);

		TextIndex ti = service.getIndex("index");

		SimpleIndexableDocument doc = new SimpleIndexableDocument("ALFA");

		doc.addField("text", "The quick brown fox jump over the lazy dog");
		doc.addField("age", "13");

		ti.addDocument(doc);

		SimpleIndexableDocument doc2 = new SimpleIndexableDocument("ALFA");

		doc2.addField("text", "The lazy dog cross over the stree");
		doc2.addField("age", "14");

		ti.addDocument(doc2);
		

		SearchHits<IndexableDocument> hits = ti.search(TextCriteriaBuilder.search()
				.and("text").contains("quick").all());

		assertEquals(1, hits.getSize());

		final IndexableDocument indexableDocument = hits.iterator().next().getUserObject();
		assertEquals("13",indexableDocument.getField("age").getValue());
		assertEquals("ALFA",indexableDocument.getDocumentModelIdentifier());
		
		hits = ti.search(TextCriteriaBuilder.search()
				.and("text").contains("cross").all());
		
		assertEquals(1, hits.getSize());

		hits = ti.search(TextCriteriaBuilder.search()
				.and("text").contains("over").all());

		assertEquals(2, hits.getSize());
		
		hits = ti.search(TextCriteriaBuilder.search()
				.and("text").contains("over")
				.and("text").contains("dog")
				.all());

		assertEquals(2, hits.getSize());

		hits = null;


	}
}
