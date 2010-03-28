package org.middleheaven.storage.db;

import org.middleheaven.domain.DataType;
import org.middleheaven.storage.StorableFieldModel;
import org.middleheaven.storage.criteria.CriterionOperator;
import org.middleheaven.storage.criteria.FieldValueHolder;


public class ColumnValueHolder {

	Object value;
	StorableFieldModel fm;
	CriterionOperator op  = CriterionOperator.EQUAL;

	public ColumnValueHolder(Object value, StorableFieldModel fm) {
		this.value = value;
		this.fm = fm;
	}

	public ColumnValueHolder(FieldValueHolder vholder,StorableFieldModel fm, CriterionOperator op) {
		this.value = vholder.getValue();
		this.fm = fm;
		this.op = op;
	}

	public Object getValue() {
		return value;
	}

	public StorableFieldModel getStorableFieldModel() {
		return fm;
	}

	protected CriterionOperator getOperator() {
		return op;
	}

}