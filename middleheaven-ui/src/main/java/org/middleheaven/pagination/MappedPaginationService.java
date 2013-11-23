package org.middleheaven.pagination;

import java.util.HashMap;
import java.util.Map;


public class MappedPaginationService implements PaginationService {

	private Map<String, Integer> sizes = new HashMap<String,Integer>();

	private int defaultPageSize;

	public MappedPaginationService(){
		this(20);
	}

	public MappedPaginationService(int defaultPageSize){
		this.defaultPageSize = defaultPageSize;
	}

	public void setPageSize(String name, int size){
		sizes.put(name, size);
	}

	@Override
	public <T> Paginator<T> paginate(PaginatorModel<T> model) {
		return paginate(model, "");
	}

	@Override
	public <T> Paginator<T> paginate(PaginatorModel<T> model, String name) {
		Integer size = sizes.get(name);

		if (size ==null){
			size = defaultPageSize;
		}

		return paginate(model, size);
	}

	@Override
	public <T> Paginator<T> paginate(PaginatorModel<T> model, int itemsPerPage) {

		return new Paginator<T>(model,itemsPerPage);

	}

}
