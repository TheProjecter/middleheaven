/**
 * 
 */
package org.middleheaven.storage.dataset.mapping;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.persistance.db.mapping.IllegalModelStateException;
import org.middleheaven.persistance.db.mapping.ModelParsingException;
import org.middleheaven.persistance.model.ColumnValueType;
import org.middleheaven.reflection.ReflectedProperty;
import org.middleheaven.reflection.inspection.ClassIntrospector;
import org.middleheaven.reflection.inspection.Introspector;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 
 */
public final class XmlDrivenDatasetRepositoryModelReader implements
		DatasetRepositoryModelReader {

	/**
	 * Creates {@link XmlDrivenDatasetRepositoryModelReader} for the given file.
	 * @param file the file to read
	 * @return an XmlDrivenDatasetRepositoryModelReader object
	 */
	public static XmlDrivenDatasetRepositoryModelReader newInstance(ManagedFile file){
		return new XmlDrivenDatasetRepositoryModelReader(file);
	}

	private ManagedFile file;
	
	/**
	 * Constructor.
	 * @param file
	 */
	private XmlDrivenDatasetRepositoryModelReader(ManagedFile file) {
		this.file = file;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void read(EditableDatasetRepositoryModel model) {
		try {
			SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
			
			InputSource input = new InputSource(file.getContent().getInputStream());

			parser.parse(input, new XmlHandler(model));

			
		} catch (ParserConfigurationException e) {
			throw new ModelParsingException(e);
		} catch (SAXException e) {
			throw new ModelParsingException(e);
		} catch (IOException e) {
			throw ManagedIOException.manage(e);
		}  
	}

	private static class UserType {
		
		String name;
		String type;
		int size;
		int scale;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public int getSize() {
			return size;
		}
		public void setSize(int size) {
			this.size = size;
		}
		public int getScale() {
			return scale;
		}
		public void setScale(int scale) {
			this.scale = scale;
		}
		
		
	}
	
	private static class XmlHandler extends DefaultHandler {
		
		private  Map<String , UserType> userTypes = new HashMap<String , UserType>();
		private  Map<String , ColumnValueType> typesMapping = new HashMap<String , ColumnValueType>();

		private EditableDatasetRepositoryModel model;
	
		private EditableDatasetModel atual;
		private Set<String> actualLogicNames;
		private Set<String> actualHardNames;
		
		private ClassIntrospector<EditableDatasetModel> inspectorDataSet;
		private ClassIntrospector<EditableDatasetColumnModel> inspectorColumn;
		private ClassIntrospector<UserType> inspectorUserType;
		
		public XmlHandler (EditableDatasetRepositoryModel model){
			this.model = model;
			
			inspectorDataSet = Introspector.of(EditableDatasetModel.class);
			inspectorColumn = Introspector.of(EditableDatasetColumnModel.class);
			inspectorUserType = Introspector.of(UserType.class);
			
			typesMapping.put("blob", ColumnValueType.BLOB);
			typesMapping.put("clob", ColumnValueType.CLOB);
			typesMapping.put("date", ColumnValueType.DATE);
			typesMapping.put("datetime", ColumnValueType.DATETIME);
			typesMapping.put("timestamp", ColumnValueType.DATETIME);
			typesMapping.put("decimal", ColumnValueType.DECIMAL);
			typesMapping.put("int", ColumnValueType.INTEGER);
			typesMapping.put("integer", ColumnValueType.INTEGER);
			typesMapping.put("logic", ColumnValueType.LOGIC);
			typesMapping.put("short", ColumnValueType.SMALL_INTEGER);
			typesMapping.put("text", ColumnValueType.TEXT);
			typesMapping.put("memo", ColumnValueType.MEMO);
			typesMapping.put("time", ColumnValueType.TIME);
		}
		
		 public void startElement(  
			        String uri,  
			        String localName,  
			        String tag,  
			        Attributes attributes)  
			        throws SAXException  {  
			      
			  
			    	if (tag.equals("dataset")) {
			    		
			    			String name = attributes.getValue(attributes.getIndex("name"));
			    			// case sensitive
			    			atual = model.getDataSetModel(name);
			    			
			    			if (atual == null){
			    				atual = new HashDatasetModel();
			    				atual.setName(name);
			    				model.addDatasetModel(atual);
			    			}
			    			
			    			actualHardNames = new HashSet<String>();
			    			actualLogicNames = new HashSet<String>();
			    			
			    			for (int i=0; i < attributes.getLength() ; i++) {
			    				ReflectedProperty pa = inspectorDataSet.inspect().properties().named(attributes.getLocalName(i)).retrive();
			    				
			    				if (pa != null){
				    				pa.setValue(atual, attributes.getValue(i) /*.toLowerCase()*/);
				    			}
			    			}

			    	} else if (tag.equals("column")) {
			    		String name = attributes.getValue(attributes.getIndex("name"));
		    			
			    		EditableDatasetColumnModel cm = atual.getColumn(name);
			    		
			    		if (cm == null) {
			    			cm = new HashDatasetColumnModel();
			    			cm.setName(name);
			    			atual.addDatasetColumnModel(cm);
			    		}
			    		
			    		for (int i=0; i < attributes.getLength() ; i++) {
			    			final String attributeName = attributes.getLocalName(i);
			    			
			    			final String attributeValue = attributes.getValue(i);
							if ("type".equals(attributeName)){
			    				ReflectedProperty pa = inspectorColumn.inspect().properties().named("valueType").retrive();
				    			
			    				if (pa != null){
			    					final ColumnValueType value = this.typesMapping.get(attributeValue);
									if (pa.getValue(cm) == null){
			    						pa.setValue(cm, value);
			    					} else {
			    						
			    						final ColumnValueType oldValue = (ColumnValueType)pa.getValue(cm);
										if (!oldValue.isCompatible(value)){
			    							throw new IllegalModelStateException("Incompatible types " + value + " is not compatible with " + oldValue + " for field " + cm.getHardName());
			    						} else {
			    							pa.setValue(cm, value);
			    						}
			    						
			    					}
			    				}
			    			} else {
			    				ReflectedProperty pa = inspectorColumn.inspect().properties().named(attributeName).retrive();
				    			
				    			if (pa != null){
			    					pa.setValue(cm, attributeValue /*.toLowerCase()*/);
				    			}
				    			
				    			if ("hardname".equals(attributeName)){
				    				if (!actualHardNames.add(attributeValue)){
				    					throw new IllegalModelStateException("Hardname " + attributeValue + " is duplicated in " + atual.getHardName());
				    				}
				    			} else if ("name".equals(attributeName)){
				    				if (!actualLogicNames.add(attributeValue)){
				    					throw new IllegalModelStateException("Name " + attributeValue + " is duplicated in " + atual.getHardName());
				    				}
				    			}
			    			}
							
			    			
		    			}

			    	} else if (tag.equals("type")){
			    		
			    		UserType ut = new UserType();
			    		
			    		for (int i=0; i < attributes.getLength() ; i++) {
			    			ReflectedProperty pa = inspectorUserType.inspect().properties().named(attributes.getLocalName(i)).retrive();
			    			
			    			if (pa != null){
			    				pa.setValue(ut, attributes.getValue(i).toLowerCase());
			    			}
			    			
		    			}
			    		
			    		userTypes.put(ut.getName(), ut);
			    	}
			      
			    } 
	
			    public void endElement(String uri, String localName, String tag)  
			        throws SAXException  {  
			     
			    	// no-op
			    }  
			    
			    
	}
	
}
