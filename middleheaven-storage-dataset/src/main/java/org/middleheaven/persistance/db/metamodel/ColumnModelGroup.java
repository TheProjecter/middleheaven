package org.middleheaven.persistance.db.metamodel;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;


public class ColumnModelGroup implements Iterable<DBColumnModel> {

	private Map<String,DBColumnModel> columns = new LinkedHashMap<String,DBColumnModel>();
	private String name;
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addColumn(DBColumnModel column){
		columns.put(column.getLogicName(), column);
	}
	
	public void removeColumn(DBColumnModel column){
		columns.remove(column.getLogicName());
	}
	
	
	@Override
	public Iterator<DBColumnModel> iterator() {
		return columns.values().iterator();
	}

	/**
	 * 
	 * @return
	 */
	public boolean isEmpty(){
		return columns.isEmpty();
	}
	
	/**
	 * 
	 * @return
	 */
	public int size(){
		return columns.size();
	}

	/**
	 * @param cm
	 * @return
	 */
	public boolean contains(DBColumnModel cm) {
		return columns.containsKey(cm.getLogicName());
	}
}
