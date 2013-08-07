package org.middleheaven.text.indexing.lucene;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.middleheaven.logging.Logger;
import org.middleheaven.text.indexing.DocumentField;
import org.middleheaven.text.indexing.DocumentFieldModel;
import org.middleheaven.text.indexing.DocumentModel;
import org.middleheaven.text.indexing.HitScore;
import org.middleheaven.text.indexing.IndexableDocument;
import org.middleheaven.text.indexing.ListSearchHits;
import org.middleheaven.text.indexing.SearchHits;
import org.middleheaven.text.indexing.TextIndex;
import org.middleheaven.text.indexing.TextIndexingException;
import org.middleheaven.util.criteria.text.TextSearchCriteria;

public class LucenceTextIndex implements TextIndex {

	private static final int PAGE_SIZE = 10;

	private Directory directory;
	private Analyzer analizer;
	private CriteriaInterpreter interpreter = new CriteriaInterpreter();
	private Map<Object, DocumentModel> models = new HashMap<Object,DocumentModel>();

	private boolean debug = false;
	
	public LucenceTextIndex(Directory directory, Analyzer analizer){
		this.analizer = analizer;
		this.directory = directory;
	}

	
	public boolean isDebug() {
		return debug;
	}


	public void setDebug(boolean debug) {
		this.debug = debug;
	}


	private Document asLuceneDocument(IndexableDocument document) throws IOException{
		Document doc = new Document();

		DocumentModel model = this.models.get(document.getDocumentModelIdentifier());

		if(model ==null){
			throw new IllegalStateException("No document model present for identifier " + document.getDocumentModelIdentifier());
		}



		doc.add(new Field("model_identifier", serilizeIdentifier(document.getDocumentModelIdentifier()), Store.YES));

		for (DocumentField f : document){
			if(f.getValue()!=null){
				DocumentFieldModel fm = model.getFieldModel(f.getName());

				doc.add(new Field(
						f.getName(), 
						f.getValue(), 
						fm.isStorable() ? Store.YES: Store.NO, 
								Index.toIndex(fm.isIndexable(), fm.isAnalizable(), fm.isNormUsable())		
				));
			}
		}

		return doc;
	}


	private byte[] serilizeIdentifier(Serializable object) throws IOException{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(out);
		os.writeObject(object);
		os.close();

		return out.toByteArray();
	}

	private Object deSerilizeIdentifier(byte[] data) throws IOException, ClassNotFoundException{
		ObjectInputStream os = new ObjectInputStream(new ByteArrayInputStream(data));
		try{
			return os.readObject();
		} finally{
			os.close();
		}

	}


	public void addDocumentModel(DocumentModel docModel) {
		this.models.put(docModel.getIdentifier(), docModel);
	}

	private IndexableDocument fromLuceneDocument(Document doc, Object modelIdentifier){
		DocumentModel model = models.get(modelIdentifier);
		return new LuceneDocumentAdapter(doc,model);
	}

	@Override
	public void addDocument(IndexableDocument document) {
		IndexWriter writer= null;
		try {
			writer = new IndexWriter(directory,analizer, IndexWriter.MaxFieldLength.UNLIMITED);
			
			if(debug){
				writer.setInfoStream(System.out);
			}
			
			writer.addDocument(asLuceneDocument(document));
			writer.close();
		} catch (Exception e) {
			throw handle(e);
		} finally {
			if (writer!=null){
				try {
					writer.close();
				} catch (IOException e) {
					throw handle(e);
				}
			}
		}
	}

	private TextIndexingException handle(Exception e) {
		// TODO CorruptIndexException
		// TODO Handle ParseException 

		return TextIndexingException.handle(e);
	}

	@Override
	public void deleteDocument(IndexableDocument document) {

	}

	public SearchHits<IndexableDocument> search(TextSearchCriteria<IndexableDocument> criteria){
		IndexSearcher searcher = null;
		try {
			searcher = new IndexSearcher(directory , true); 

			Query query = interpreter.interpret(criteria ,this.analizer);

			TopDocs hits = searcher.search(query, PAGE_SIZE); 

			ListSearchHits<IndexableDocument> result = new ListSearchHits<IndexableDocument>(hits.scoreDocs.length);

			for(int i=0;i<hits.scoreDocs.length;i++) {
				ScoreDoc scoreDoc = hits.scoreDocs[i];
				Document doc = searcher.doc(scoreDoc.doc);

				Object modelIdentifier = deSerilizeIdentifier(doc.getBinaryValue("model_identifier"));

				result.addHit(this.fromLuceneDocument(doc,modelIdentifier), HitScore.valueOf(scoreDoc.score));
			}

			searcher.close(); 
			return result;

		} catch (FileNotFoundException e){
			// could not find index files
			Logger.onBookFor(this.getClass()).warn(e, "Index file not found");
			return new ListSearchHits<IndexableDocument>(0);
		} catch (Exception e) {
			throw handle(e);
		} finally {
			if (searcher!=null){
				try {
					searcher.close();
				} catch (IOException e) {
					throw handle(e);
				}
			}
		}
	}




}
