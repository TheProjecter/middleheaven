package org.middleheaven.pagination;

import java.util.Collection;
import java.util.Collections;

public class Paginator<E> {

	private final PaginatorModel<E> model;
	private final int itemsPerPage;
	
	private int currentPageIndex = -1;
	
	protected Paginator(PaginatorModel<E> model, int itemsPerPage){
		this.model = model;
		this.itemsPerPage = itemsPerPage;
	}
	
	public int getPageCount(){
		final long count = model.totalCount();
		return (int)(count / itemsPerPage + (count % itemsPerPage == 0 ? 0: 1));
	}
	
	public int getCurrentPageIndex(){
		return currentPageIndex;
	}
	
	public void setCurrentPageIndex(int currentPageIndex){
		this.currentPageIndex = currentPageIndex;
	}
	
	public boolean hasNextPage(){
		return this.currentPageIndex < this.getPageCount();
	}
	
	public boolean hasPreviousPage(){
		return this.currentPageIndex > 1;
	}
	
	/**
	 * First page has index 1.
	 * @return
	 */
	public boolean isFirstPage(){
		return this.currentPageIndex == 1;
	}
	
	public boolean isLastPage(){
		return this.getPageCount()==0 || this.currentPageIndex == this.getPageCount();
	}
	
	public void moveToFirstPage(){
		this.moveToPage(1);
	}
	
	public void moveToLastPage(){
		this.moveToPage(this.getPageCount());
	}
	
	public void moveToNextPage(){
		int count = this.getPageCount();
		
		this.moveToPage(this.currentPageIndex == count ? count : this.currentPageIndex +1);
	}
	
	public void moveToPreviousPage(){
		this.moveToPage(this.currentPageIndex == 1 ? 1 : this.currentPageIndex -1);
	}
	
	public void moveToPage(int pageIndex){
		
		this.currentPageIndex = pageIndex;

	}
	
	public Collection<E> getPageItens(){
		
		if (currentPageIndex < 1 || currentPageIndex > this.getPageCount() + 1 ) {
			return Collections.emptySet();
		}
		int startAt = (this.currentPageIndex-1) * this.itemsPerPage;
		
		return model.getRange(startAt, itemsPerPage);

	}
}
