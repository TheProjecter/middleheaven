package org.middleheaven.persistance.db.metamodel;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.middleheaven.collections.CollectionUtils;
import org.middleheaven.collections.enumerable.Enumerable;
import org.middleheaven.collections.enumerable.Enumerables;
import org.middleheaven.util.Hash;

public class EditableDBTableModel implements DataBaseObjectModel, DBTableModel {

	private String tableHardName;
	private Map<String, EditableColumnModel> columns = new LinkedHashMap<String, EditableColumnModel>();
	private Map<String, ColumnModelGroup> uniqueGroups = new HashMap<String, ColumnModelGroup> ();
	private ColumnModelGroup keyGroup = new ColumnModelGroup();
	
	
	public static EditableDBTableModel valueOf(DBTableModel tm){
		if (tm instanceof EditableDBTableModel){
			return (EditableDBTableModel) tm;
		} else {
			return new EditableDBTableModel(tm);
		}
	}
	
	
	public EditableDBTableModel(){
		super();
	}
	
	/**
	 * 
	 * Copy Constructor.
	 * @param other the  {@link DBTableModel} to copy.
	 */
	private EditableDBTableModel(DBTableModel other){
		super();
		
		for (DBColumnModel cm : other){
			this.addColumn(new EditableColumnModel(cm));
		}
		
	}
	
	public EditableDBTableModel(String name) {
		this.tableHardName = name;
		this.keyGroup.setName("keyGroup");
	}
	
	public void addColumn(EditableColumnModel columnModel) {
		if (columnModel.getLogicName() == null){
			throw new IllegalArgumentException("Column name cannot be null");
		}
		
		if (columnModel.getTableModel() != null && columnModel.getTableModel() != this) {
			throw new IllegalStateException("Column already belongs to a table ");
		}
		columnModel.setTableModel(this);

		columns.put ( columnModel.getLogicName() , columnModel);
	
		if(columnModel.isKey()) {
			addKeyColumn(columnModel);
		}
		
		if (columnModel.isIndexed()){
			
			addUniqueColumn(columnModel , columnModel.getUniqueGroupName());
			
		}
		

	}
	
	private void addKeyColumn(DBColumnModel fm) {

		this.keyGroup.addColumn(fm);
		addUniqueColumn(fm,"_keys_");
	}
	
	private void addUniqueColumn(DBColumnModel fm, String uniqueGroupName) {
		
		if (uniqueGroupName == null){
			uniqueGroupName = fm.getUniqueGroupName();
			if (uniqueGroupName == null){
				return;
			}
		}
		
		
		ColumnModelGroup group = this.uniqueGroups.get(uniqueGroupName);
		
		if (group == null){
			group = new ColumnModelGroup();
			group.setName(fm.getUniqueGroupName());
			
			this.uniqueGroups.put(group.getName(), group);
		}
		
		group.addColumn(fm);
	}
	

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return tableHardName;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<DBColumnModel> iterator() {
		return CollectionUtils.secureCoerce(columns.values().iterator(), DBColumnModel.class);
	}
	
	
	@Override
	public DataBaseObjectType getType() {
		return DataBaseObjectType.TABLE;
	}

	/**
	 * Create a table model with the columns of {@code this} that does not exist on {@code other}	
	 * @param other the model to compare
	 * @return
	 */
	public EditableDBTableModel differenceTo(EditableDBTableModel other) {
		// Create a table model with the columns that eBeanColumnModeler this model that not exist on the other 
		EditableDBTableModel res = new EditableDBTableModel(this.tableHardName);
		
		outter: 
		for (DBColumnModel c : this.columns.values()){
			// find a match in this columns set 
			for (Entry<String, EditableColumnModel> entry : other.columns.entrySet()){
				// must do a case insensitive search
				if (entry.getValue().getName().getDesignation().equalsIgnoreCase(c.getName().getDesignation())){ 
					continue outter;
				}
			}
			// no match found
			EditableColumnModel nc = new EditableColumnModel(c);
			res.addColumn(nc);
			
		}
		
		return res;
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object other){
		return other instanceof DBTableModel && ((DBTableModel) other).getName().equalsIgnoreCase(this.tableHardName);
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode(){
		return Hash.hash(this.tableHardName).hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ColumnModelGroup identityColumns() {
		return this.keyGroup;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<ColumnModelGroup> uniqueColumns() {
		return uniqueGroups.values();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<ColumnModelGroup> indexedColumns() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enumerable<DBColumnModel> columns() {
		return Enumerables.asEnumerable(CollectionUtils.secureCoerce(this.columns.values(), DBColumnModel.class));
				
				
	}

	public void setName(String name) {
		this.tableHardName = name;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return columns.isEmpty();
	}


	/**
	 * @param name2
	 * @return
	 */
	public EditableColumnModel getColumnModel(String columnName) {
		return this.columns.get(columnName);
		
	}



}

