package org.middleheaven.ui.web.tags;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import org.middleheaven.global.text.LocalizableText;
import org.middleheaven.pagination.Paginator;
import org.middleheaven.process.ContextScope;

public class TBodyTag  extends AbstractBodyTagSupport {

	private static final long serialVersionUID = -1704506701460018890L;
	
	private Iterator<?> iterator;
	private Paginator<?> paginator;

	private String varPage = "page";
	private String varName = "item"; 

	
	public TBodyTag (){}
	
	public void setVar(String varName){
		this.varName = varName;
	}

	public void setPageNumber(String varPage){
		this.varPage = varPage;
	}

	public void setPaginator(Paginator<?> paginator){
		this.paginator = paginator;
	}

	public void setItems(Iterable<?> iterable){
		this.iterator = iterable==null? null : iterable.iterator();
	}

	public int doStartTag() throws JspException{
		write("<tbody>");
	
		if(isPaginator() && paginator.getPageCount() > 0 ){
			TagContext  context = new TagContext(pageContext);
			Integer page = context.getAttribute(ContextScope.REQUEST, varPage, Integer.class);
			paginator.moveToPage( (page == null) ? 1 : page);
			setItems(paginator.getPageItens());
			return EVAL_BODY_BUFFERED;
		}else if ( isIteratorNotNull() && iterator.hasNext()){
			return EVAL_BODY_BUFFERED;
		}

		// print no items message
		if(isIteratorNotNull() || isPaginator()){
			writeLine("<tr><td colspan='10' align='center'>");
			writeLine(this.localize(LocalizableText.valueOf("ui:listing.no.items.found")));
			writeLine("</td></tr>");
		}

		return SKIP_BODY;
	}

	public void doInitBody (){

		if(isIteratorNotNull() && iterator.hasNext()){
			Object obj = iterator.next();
			pageContext.setAttribute(varName, obj);
		}
	}

	public int doAfterBody() throws JspException{
		try {
			getBodyContent().writeOut(getPreviousOut());
			getBodyContent().clear();

			if (isIteratorNotNull() && iterator.hasNext()){
				Object obj = iterator.next();
				pageContext.setAttribute(varName, obj);
				return EVAL_BODY_BUFFERED;
			}

			return SKIP_BODY;
		} catch (IOException e) {
			throw new JspTagException(e.getMessage());
		}
	}

	public int doEndTag() throws JspException{
		write("</tbody>");

		if(isPaginator()){
			createTFoot();
		}

		return EVAL_PAGE;
	}

	private boolean isPaginator(){
		return paginator != null;
	}

	private boolean isIteratorNotNull(){
		return iterator != null;
	}

	private void createTFoot() throws JspException{
		TagContext  context = new TagContext(pageContext);
		String url = context.getRequestUrl().toString();
		int beginIndex = url.lastIndexOf('/') + 1;
		int endIndex = url.lastIndexOf('.');

		if(url.indexOf(".paginator.") < 0){
			url = url.substring(beginIndex, endIndex) + ".paginator.srv";
		}

		write("<tfoot class=\"navigation_bar\"><tr><td colspan='10' align='center'><br/>");

		final int NUMBER_OF_PAGES_TO_SHOW = 5;
		int pageCount = paginator.getPageCount();
		int currentPage = paginator.getCurrentPageIndex();


		int nextLink = paginator.hasNextPage() ? currentPage+1 : currentPage;

		int backLink = paginator.hasPreviousPage() ? currentPage-1 : currentPage;

		int firstPageLink = 1;
		int lastPageLink = pageCount;


		int firstDisplayNumber = currentPage - (NUMBER_OF_PAGES_TO_SHOW/2);
		if(firstDisplayNumber < 1){
			firstDisplayNumber = 1;
		}

		int endDisplayNumber = Math.min((firstDisplayNumber-1) + NUMBER_OF_PAGES_TO_SHOW , paginator.getPageCount()); 


		if(!paginator.isFirstPage()){
			write("<a href='"+url+"?"+varPage+"="+firstPageLink+"'><span class=\"nav_go_first\">&lt;&lt;</span></a>");
		}

		if(paginator.hasPreviousPage()){	
			write("&nbsp<a href='"+url+"?"+varPage+"="+backLink+"'><span class=\"nav_go_previous\">&lt;</span></a>");
		}

		for(int i=firstDisplayNumber; i <= endDisplayNumber ; i++){
			if(i != paginator.getCurrentPageIndex()){
				write("&nbsp<a href='"+url+"?"+varPage+"="+i+"'><span class=\"nav_to_page\">"+i+"</span></a>");
			}else{
				write("&nbsp<span class=\"nav_current_page\">"+i+"</span></b>");
			}
		}

		if(paginator.hasNextPage()){
			write("&nbsp<a href='"+url+"?"+varPage+"="+nextLink+"'><span class=\"nav_go_next\">&gt;</span></a>");
		}

		if (!paginator.isLastPage()){
			write("&nbsp<a href='"+url+"?"+varPage+"="+lastPageLink+"'><span class=\"nav_go_last\">&gt;&gt;</span></a>");
		}

		write("</td></tr></tfoot>");
	}

	@Override
	public void releaseState() {
		this.iterator = null;
		this.paginator = null;
	}
}
