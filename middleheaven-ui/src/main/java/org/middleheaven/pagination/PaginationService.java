package org.middleheaven.pagination;



public interface PaginationService {

	
	public <T> Paginator<T> paginate(PaginatorModel<T> model);
	public <T> Paginator<T> paginate(PaginatorModel<T> model, String name);
	public <T> Paginator<T> paginate(PaginatorModel<T> model, int itemsPerPage);
}
