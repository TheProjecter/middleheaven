package org.middleheaven.pagination;

import java.util.Collection;

public interface PaginatorModel<E> {

	long totalCount();
	public Collection<E> getRange(int startAt, int itemsPerPage);
	
}
