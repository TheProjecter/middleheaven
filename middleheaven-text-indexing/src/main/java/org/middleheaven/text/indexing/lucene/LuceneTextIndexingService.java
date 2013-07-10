package org.middleheaven.text.indexing.lucene;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.store.Directory;
import org.middleheaven.io.repository.ModificationTracableManagedFile;
import org.middleheaven.text.indexing.DocumentModel;
import org.middleheaven.text.indexing.TextIndex;
import org.middleheaven.text.indexing.TextIndexingException;
import org.middleheaven.text.indexing.TextIndexingService;

public class LuceneTextIndexingService implements TextIndexingService{

	private Map<Object, LucenceTextIndex> indexes = new HashMap<Object, LucenceTextIndex>();
	private boolean debug;
	
	public LuceneTextIndexingService(){}
	
	
	
	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void configurateIndex(Object indexIdentifier, ModificationTracableManagedFile managedFile, Analyzer analizer) {
		try {
			
//			managedFile = MachineFiles.ensureMachineFile(managedFile);
//			
//			File directory = new File(managedFile.getURI());
//			
			this.configurateIndex(indexIdentifier, ManagedFileDirectory.open(managedFile), analizer);

		} catch (IOException e) {
			throw TextIndexingException.handle(e);
		} 
	}
	
	public void configurateIndex(Object indexIdentifier,Directory directory, Analyzer analizer) {
		final LucenceTextIndex lucenceTextIndex = new LucenceTextIndex(directory, analizer);
		lucenceTextIndex.setDebug(this.debug);
		indexes.put(indexIdentifier, lucenceTextIndex);
	}

	@Override
	public TextIndex getIndex(Object indexIdentifier) {
		return indexes.get(indexIdentifier);
	}

	@Override
	public void configureDocument(Object indexIdentifier, DocumentModel docModel) {
		LucenceTextIndex textIndex = indexes.get(indexIdentifier);
		
		if (textIndex != null){
			textIndex.addDocumentModel(docModel);
		} else {
			throw new IllegalStateException("Cannot config documents without configuring index first.");
		}
		
	}

	
	
}
