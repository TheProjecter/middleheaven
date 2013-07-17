package org.middleheaven.ui.web.tags;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;

import org.middleheaven.core.bootstrap.ServiceRegistry;
import org.middleheaven.global.Culture;
import org.middleheaven.global.LocalizationService;
import org.middleheaven.global.text.LocalizableText;
import org.middleheaven.ui.components.MenuItem;

public class MenuTag extends AbstractBodyTagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3570953084157671714L;

	static final String SUB_MENU_TAG = "<submenu/>";
	
	StackItem masterStack;
	StackItem parentStack;
	private String varName = "menu"; 

	Deque<StackItem> iteratorStack = new LinkedList<StackItem>();
	private String rootClass;
	private String selectedItemClass;

	public void setVar(String varName){
		this.varName = varName;
	}

	public void setRootClass(String rootClass){
		this.rootClass = rootClass;
	}
	
	public void setSelectedItemClass(String selectedItemClass){
		this.selectedItemClass = selectedItemClass;
	}

	public MenuItem getCurrentMenuItem(){
		return this.current.menu;
	}

	public void setRoot(MenuItem menu){
		if (menu != null){
			masterStack = new StackItem(menu);
		}  
	}

	private LocalizationService localizationService;
	
	public int doStartTag() throws JspException{

		if (masterStack==null){
			return SKIP_BODY;
		} 

		this.localizationService = ServiceRegistry.getService(LocalizationService.class);
		
		buffer.delete(0, buffer.length());
		write("<ul ");
		if (id!=null){
			writeAttribute("id", id);
		}
		if (!(rootClass==null || rootClass.isEmpty())){
			writeAttribute("class", rootClass);
		}
		write(">");
		if (masterStack.iterator.hasNext()){ // primeiro
			return EVAL_BODY_BUFFERED;
		}
		return SKIP_BODY;
	}

	public void doInitBody (){
		iteratorStack.addFirst(masterStack);
		exposeMenu(masterStack.iterator.next());
	}

	private void exposeMenu(MenuItem item){
		current = new StackItem(item);
		if(!item.isTitleLocalized()){
			Culture culture = new TagContext(pageContext).getCulture();
			localizationService.getMessage(LocalizableText.valueOf(item.getTitle()), culture);
		} else {
			item.setLabel(item.getTitle());
		}
		pageContext.setAttribute(varName, item);
	}

	StackItem current;

	StringBuilder buffer = new StringBuilder();

	
	private static class StackItem {
		public StackItem(MenuItem menu) {
			this.menu = menu;
			this.iterator = menu.getChildren().iterator();
			this.count = menu.getChildrenCount();
			if ( count > 0){
				widths = BigInteger.valueOf(100L).divideAndRemainder(BigInteger.valueOf(this.count));
			}
			
		}
		
		public boolean hasSubItems(){
			return count > 0;
		}
		private int indicator = 0;
		private BigInteger[] widths;
		private MenuItem menu;
		private Iterator<MenuItem> iterator;
		private int count;
		
		
	}
	
	public int doAfterBody() throws JspException{

		try {
			BodyContent bc = getBodyContent();
			// get the bc as string
		
			buffer.append ("\n<li>");
			buffer.append(bc.getString());
			buffer.append ("\n</li>");
			// clean up
			bc.clearBody();

			getBodyContent().writeOut(getPreviousOut());
			getBodyContent().clear();

			if (current!=null  && current.hasSubItems()){
				buffer.append("\n<ul>");
				iteratorStack.addFirst(current);

			}

			// while the iterators stack is not empty
			while (!iteratorStack.isEmpty()){
				StackItem top = iteratorStack.getFirst();

				if (top.iterator.hasNext()){
					// process next item of same menu
					
					exposeMenu(top.iterator.next());
					return EVAL_BODY_BUFFERED;

				} else {
					// menu has no more items. terminate menu
					balanceTag(buffer, "ul");
					// remove iterador 
					iteratorStack.removeFirst();
				}
			}
			return SKIP_BODY;

	} catch (IOException e) {
		throw new JspTagException(e.getMessage());
	}

}

private StringBuilder balanceTag(StringBuilder buffer, String tag) {
	int pos = buffer.indexOf("<" + tag + ">");
	int count =0;
	while (pos >=0){
		count++;
		 pos = buffer.indexOf("<" + tag + ">", pos + 1);
	}
	
	pos = buffer.indexOf("</" + tag + ">");
	while (pos >=0){
		count--;
		 pos = buffer.indexOf("</" + tag + ">", pos + 1);
	}
	
	if (count >0){
		for (int i=0; i < count; i++){
			buffer.append("\n</" + tag + ">" );
		}
	} else if (count < 0){
		for (int i=0; i < -count; i++){
			buffer.insert(0, "\n<" + tag + ">" );
		}
	}
	
	return buffer;
}

public int doEndTag() throws JspTagException{

	writeLine(processBuffer(buffer));
	writeLine("</ul>");
	return EVAL_PAGE;
}

private String processBuffer(StringBuilder buffer) {

	BufferedReader reader = new BufferedReader(new StringReader(buffer.toString()));
	String line;
	RMenuItem root = new RMenuItem();
	RMenuItem currentRItem =null;
	RMenuItem parent = root;
	try {
		String[] stoppers = {"<li", "</li" , "<ul" , "</ul" };
		
		while ( (line = reader.readLine())!=null){
			if (line.trim().isEmpty()){
				continue;
			}
			
			int[] res = find(line, stoppers,0);
			
			if (res[0]>=0){
				do {
					if (res[1]==0){ //<li
						// start 
						currentRItem = new RMenuItem();
						currentRItem.addLine(line);
					} else if (res[1]==1){ //</li
						currentRItem.addLine(line);
						parent.addChild(currentRItem);
					} else if (res[1]==2){ //<ul
	
						parent = currentRItem;
	
					} else if (res[1]==3){ //</ul
						parent = parent.parent;
					}
					res = find(line, stoppers,res[0]+1);
				} while (res[0]>=0);
			} else {
				if (currentRItem!=null){
					currentRItem.addLine(line);
				}
			}
			
		}
	} catch (IOException e) {
		// no-op
	}

	StringBuilder buffer2 = new StringBuilder();
	printMenu(buffer2, root);
	return buffer2.toString();
}

private static int[] find(String s , String[] options , int fromIndex){
	for (int i=0; i < options.length; i++){
		int pos = s.indexOf(options[i], fromIndex);
		if (pos>=0){
			return new int[]{pos,i};
		}
	}
	return new int[]{-1,-1};
}

private void printMenu (StringBuilder builder , RMenuItem item ){

	if (!item.children.isEmpty()){
		StringBuilder subItens = new StringBuilder();
		for (RMenuItem sub : item.children){
			printMenu(subItens, sub);
		}
	
		int pos = item.itemBuilder.indexOf(SUB_MENU_TAG);
		if (pos>0){
			item.itemBuilder.replace(pos, pos+SUB_MENU_TAG.length(),ensureOneSurroundByTag(balanceTag(subItens, "ul").toString(), "ul"));
		} else {
			item.itemBuilder.append(subItens.toString());
		}

	} 

	builder.append(item.itemBuilder);


}
private String ensureOneSurroundByTag(String content, String tag) {
	if (content.startsWith("<" + tag + ">") && content.endsWith("</" + tag + ">")){
		return content;
	} else {
		return "<" + tag + ">" + content + "</" + tag + ">";
	}
}

public void releaseState(){
	this.masterStack = null;
	this.buffer.delete(0, buffer.length());
}

private static class RMenuItem {
	StringBuilder itemBuilder = new StringBuilder();
	RMenuItem parent;
	List<RMenuItem> children = new LinkedList<RMenuItem>();

	public RMenuItem(){}

	public void addLine(String line){
		itemBuilder.append(line).append("\n");
	}
	public void addChild(RMenuItem item){
		item.parent = this;
		children.add(item);
	}

	public String toString(){
		StringBuilder toStringBuilder = new StringBuilder();
		toString(toStringBuilder,this,0);
		return toStringBuilder.toString();
	}

	private static void toString(StringBuilder builder , RMenuItem item, int tabs){
		for (int i=0; i < tabs; i++){
			builder.append("---");
		}
		builder.append(item.itemBuilder.toString().trim()).append("\n");
		for (RMenuItem m : item.children){
			toString(builder,m, tabs+1);
		}
	}
}
}
