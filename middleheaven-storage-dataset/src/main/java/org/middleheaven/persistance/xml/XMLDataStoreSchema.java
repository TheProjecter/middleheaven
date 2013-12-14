//package org.middleheaven.persistance.xml;
//
//import javax.xml.xpath.XPath;
//import javax.xml.xpath.XPathConstants;
//import javax.xml.xpath.XPathExpression;
//import javax.xml.xpath.XPathExpressionException;
//import javax.xml.xpath.XPathFactory;
//
//import org.middleheaven.io.ManagedIOException;
//import org.middleheaven.io.repository.ManagedFile;
//import org.middleheaven.io.xml.XMLException;
//import org.middleheaven.io.xml.XMLObjectContructor;
//import org.middleheaven.persistance.DataQuery;
//import org.middleheaven.persistance.DataRowStream;
//import org.middleheaven.persistance.DataSet;
//import org.middleheaven.persistance.DataSetNotFoundException;
//import org.middleheaven.persistance.DataStoreSchema;
//import org.middleheaven.persistance.DataStoreSchemaName;
//import org.middleheaven.persistance.ModelNotEditableException;
//import org.middleheaven.persistance.PersistanceException;
//import org.middleheaven.persistance.criteria.DataSetCriteria;
//import org.middleheaven.persistance.db.ListDataRowStream;
//import org.middleheaven.persistance.db.mapping.DataBaseMapper;
//import org.w3c.dom.Document;
//import org.w3c.dom.NodeList;
//
//public class XMLDataStoreSchema implements DataStoreSchema {
//
//	private static class XMLStoreContructor extends XMLObjectContructor<Document>{
//
//		@Override
//		protected void constructFrom(Document document) throws ManagedIOException, XMLException {
//			this.setConstructedObject(document);
//		}
//
//		public void constructFrom(ManagedFile file){
//			super.constructFrom(file);
//		}
//	}
//
//	private Document doc;
//	private XMLCriteriaInterpreter interpreter = new XMLCriteriaInterpreter();
//	private DataBaseMapper Function;
//
//	public XMLDataStoreSchema(DataStoreSchemaName name , ManagedFile source ,  DataBaseMapper Function){
//
//		this.Function = Function;
//
//		if (source.getType().isFile()){
//			XMLStoreContructor c = new XMLStoreContructor();
//			c.constructFrom(source);
//			doc = c.getConstructedObject();
//		}
//
//	}
//
//
//	//	@Override
//	//	public <T> Query<T> createQuery(EntityCriteria<T> criteria,ReadStrategy strategy) {
//	//		final StorableEntityModel model = reader().read(criteria.getTargetClass());
//	//		return new SimpleExecutableQuery<T>(criteria,model, this.xmlQueryExecuter);
//	//
//	//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public DataQuery query(DataSetCriteria criteria) {
//		return new XMLDataQuery(this, criteria);
//	}
//
//	/**
//	 * @param criteria2
//	 * @return
//	 */
//	private DataRowStream executeCriteria(DataSetCriteria criteria) {
//		
//		String xpathStr = interpreter.interpreter(model, criteria);
//		
//		try {
//
//			XPathFactory factory = XPathFactory.newInstance();
//			XPath xpath = factory.newXPath();
//			XPathExpression expr = xpath.compile(xpathStr);
//
//			NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
//
//			DataRowStream result = new ListDataRowStream(nodes.getLength());
//
//			for (int i = 0; i < nodes.getLength(); i++) {
//				T t = (T)getStorableStateManager().merge(newInstance(model.getEntityClass()));
//				NodeStorable s = new NodeStorable(nodes.item(i),model);
//				copy(s, (EntityInstance)t, model, session);
//
//				result.add(t);
//			}
//
//			return result;
//
//		} catch (XPathExpressionException e) {
//			throw new PersistanceException("Illegal expression " + xpathStr);
//		} 
//
//	}
//	
//	private long countCriteria(DataSetCriteria criteria) {
//		String xpathStr = interpreter.interpreter(model, criteria);
//		
//		try {
//
//			XPathFactory factory = XPathFactory.newInstance();
//			XPath xpath = factory.newXPath();
//			XPathExpression expr = xpath.compile(xpathStr);
//
//			NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
//
//			return nodes.getLength();
//
//		} catch (XPathExpressionException e) {
//			throw new PersistanceException("Illegal expression " + xpathStr);
//		} 
//	}
//
//
//
//	private class XMLDataQuery implements DataQuery {
//
//
//		private DataStoreSchema dataStoreSchema;
//		private DataSetCriteria criteria;
//
//		public XMLDataQuery (DataStoreSchema dataStoreSchema, DataSetCriteria criteria){
//			this.dataStoreSchema = dataStoreSchema;
//			this.criteria = criteria;
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		@Override
//		public DataRowStream getRowStream() {
//			return executeCriteria(criteria);
//		}
//
//
//		/**
//		 * {@inheritDoc}
//		 */
//		@Override
//		public long rowCount() {
//			return countCriteria(criteria);
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		@Override
//		public boolean isEmpty() {
//			return rowCount() == 0;
//		}
//
//		/**
//		 * {@inheritDoc}
//		 */
//		@Override
//		public DataQuery limit(int startAt, int maxCount) {
//			return new XMLDataQuery (this.dataStoreSchema, criteria.limit(startAt , maxCount));
//		}
//
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public DataSet getDataSet(String name) throws DataSetNotFoundException {
//		throw new UnsupportedOperationException("Not implememented yet");
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public void updateModel() throws ModelNotEditableException {
//		throw new UnsupportedOperationException("Not implememented yet");
//	}
//
//
//
//
//}
