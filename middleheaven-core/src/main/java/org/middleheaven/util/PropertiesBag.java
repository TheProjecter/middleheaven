/**
 * 
 */
package org.middleheaven.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.middleheaven.collections.AbstractEnumerableAdapter;
import org.middleheaven.collections.TransformedIterator;
import org.middleheaven.util.coersion.TypeCoercing;
import org.middleheaven.util.function.Mapper;

/**
 * 
 */
public class PropertiesBag extends AbstractEnumerableAdapter<PropertiesBag.Entry>{

	
	/**
	 * 
	 */
	public interface Entry {

		public String getPropertyName();
		public String getPropertyValue();
		
	}

	public static PropertiesBag bagOfSystemProperties(){
		return bagOf(System.getProperties());
	}
	
	public static PropertiesBag newBag(){
		return new PropertiesBag();
	}
	
	public static PropertiesBag bagOf(Properties p){
		return new PropertiesBag( new JavaPropertiesBagStategy(p));
	}
	
	public static PropertiesBag bagOf(Map<String, String> p){
		return new PropertiesBag(new MapPropertiesBagStategy(p));
	}
	
	private PropertiesBagStategy bag;
	
	private PropertiesBag (){
		this.bag = new MapPropertiesBagStategy(new HashMap<String,String>());
	}
	
	private PropertiesBag (PropertiesBagStategy bag){
		this.bag = bag;
	}
	
	public final <T> T getProperty(String name, Class<T> type){
		return getProperty(name, null, type);
	}
	
	public <T> T getProperty(String name, T defaultValue, Class<T> type){
		Object value = this.bag.get(name);
		if (value == null) {
			return defaultValue;
		}
		return TypeCoercing.coerce(value, type);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<Entry> iterator() {
		return bag.iterator();
	}
	
	/**
	 * Set a property value. 
	 * This action can be presented by a running {@link SecurityManager}.
	 * 
	 * @param name the name of the property.
	 * @param value the property value. This object must be coercible to {@link String}.
	 */
	public void setProperty(String name, Object value){

		this.bag.set(name, TypeCoercing.coerce(value, String.class));
	}
	
	private static interface PropertiesBagStategy{
		
		public String get(String name);
		public void set(String name, String value);
		/**
		 * @return
		 */
		public Iterator<Entry> iterator();
		/**
		 * @return
		 */
		public int size();
		/**
		 * @return
		 */
		public boolean isEmpty();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return bag.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return bag.isEmpty();
	}
	
	private static class JavaPropertiesBagStategy implements PropertiesBagStategy{

		
		private Properties properties;

		public JavaPropertiesBagStategy (Properties p){
			this.properties = p;
		}
		/**
		 * {@inheritDoc}
		 */
		@Override
		public String get(String name) {
			return properties.getProperty(name);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void set(String name, String value) {
			properties.setProperty(name, value);
		}
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Iterator<Entry> iterator() {
			return TransformedIterator.transform(this.properties.entrySet().iterator(), entryMapper);
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public int size() {
			return properties.size();
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isEmpty() {
			return properties.isEmpty();
		}
		
	}
	
	private static final EntryMapper entryMapper = new EntryMapper();
	
	private static class EntryMapper<K,V> implements Mapper<Entry, Map.Entry<K, V>> {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Entry apply(java.util.Map.Entry<K, V> next) {
			return new StringEntry (next.getKey().toString(), next.getValue().toString());
		}
		
	}
	private static class MapPropertiesBagStategy implements PropertiesBagStategy{

		
		private Map<String, String> properties;

		public MapPropertiesBagStategy (Map<String, String> p){
			this.properties = p;
		}
		/**
		 * {@inheritDoc}
		 */
		@Override
		public String get(String name) {
			return properties.get(name);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void set(String name, String value) {
			properties.put(name, value);
		}
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Iterator<Entry> iterator() {
			return TransformedIterator.transform(this.properties.entrySet().iterator(), entryMapper);
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public int size() {
			return properties.size();
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isEmpty() {
			return properties.isEmpty();
		}
		
	}

	private static class StringEntry implements Entry {

		private String name;
		private String value;

		public StringEntry (String name, String value){
			this.name = name;
			this.value = value;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getPropertyName() {
			return name;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getPropertyValue() {
			return value;
		}
		
	}





	
}
