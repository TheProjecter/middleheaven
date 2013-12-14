///**
// * 
// */
//package org.middleheaven.persistance.xml;
//
//import org.middleheaven.io.repository.ManagedFile;
//import org.middleheaven.persistance.DataStore;
//import org.middleheaven.persistance.DataStoreName;
//import org.middleheaven.persistance.DataStoreNotFoundException;
//import org.middleheaven.persistance.DataStoreProvider;
//import org.middleheaven.persistance.DataStoreSchema;
//import org.middleheaven.persistance.DataStoreSchemaName;
//import org.middleheaven.persistance.db.mapping.DataBaseMapper;
//
///**
// * 
// */
//public class XMLDataStoreProvider implements DataStoreProvider {
//
//	
//	private DataBaseMapper Function;
//	private ManagedFile folder;
//
//	public XMLDataStoreProvider (ManagedFile folder, DataBaseMapper Function) {
//		this.Function = Function;
//		this.folder = folder;
//	}
//	
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public DataStore getDataStore(final DataStoreName name)
//			throws DataStoreNotFoundException {
//		return new DataStore() {
//
//			@Override
//			public DataStoreName getName() {
//				return name;
//			}
//
//			@Override
//			public DataStoreSchema getDataStoreSchema(DataStoreSchemaName name) {
//				return new XMLDataStoreSchema(name, resolveDataStoreSchemaFile(name), Function);
//			}
//			
//			
//		};
//	}
//	
//	protected ManagedFile resolveDataStoreSchemaFile(DataStoreSchemaName name){
//		return folder.retrive(name.getSchema().concat(".xml"));
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public boolean isProviderDataStore(DataStoreName name) {
//		throw new UnsupportedOperationException("Not implememented yet");
//	}
//
//	
//}
