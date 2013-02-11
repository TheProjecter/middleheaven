package org.middleheaven.text.indexing;

public final class SimpleDocumentFieldModel implements DocumentFieldModel {

	private String name;
	private boolean isStorable;
	private boolean isIndexable;
	private boolean isAnalizable = true;
	private boolean isNormUsable = true;
	
	public static SimpleDocumentFieldModel field(String name,boolean isStorable){
		return field(name, isStorable,true);
	}
	
	
	public static SimpleDocumentFieldModel field(String name,boolean isStorable, boolean isIndexable){
		return new SimpleDocumentFieldModel(name,isStorable,isIndexable);
	}
	
	
	private SimpleDocumentFieldModel(String name,boolean isStorable, boolean isIndexable){
		this.name = name;
		this.isStorable = isStorable;
		this.isIndexable = isIndexable;
	}
	
	/**
	 * {@inherit}
	 */
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * {@inherit}
	 */
	public boolean isStorable() {
		return isStorable;
	}
	public void setStorable(boolean isStorable) {
		this.isStorable = isStorable;
	}
	/**
	 * {@inherit}
	 */
	public boolean isIndexable() {
		return isIndexable;
	}
	public void setIndexable(boolean isIndexable) {
		this.isIndexable = isIndexable;
	}
	/**
	 * {@inherit}
	 */
	public boolean isAnalizable() {
		return isAnalizable;
	}
	public void setAnalizable(boolean isAnalizable) {
		this.isAnalizable = isAnalizable;
	}
	/**
	 * {@inherit}
	 */
	public boolean isNormUsable() {
		return isNormUsable;
	}
	public void setNormUsable(boolean isNormUsable) {
		this.isNormUsable = isNormUsable;
	}
	
	
}
