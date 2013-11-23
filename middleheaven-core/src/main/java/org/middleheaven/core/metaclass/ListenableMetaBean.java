package org.middleheaven.core.metaclass;

/**
 * 
 */
public class ListenableMetaBean implements MetaBean {

	
	private MetaBean original;

	public ListenableMetaBean (MetaBean original){
		this.original = original; 
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public MetaClass getMetaClass() {
		return original.getMetaClass();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object get(String name) {
		return original.get(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void set(String name, Object value) {
		this.original.set(name, value);
		this.onModified(name, value);
	}
	
	protected void onModified(String name, Object value){
		
	}

}
