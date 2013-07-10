package org.middleheaven.ui.web.tags;

import javax.servlet.jsp.JspException;


public class CheckedInputTag extends InputTag{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4476077241367385067L;

	public CheckedInputTag() {
		super("checkbox");
	}

	private String name;
	private Object value;
	private Boolean checked;
	private String text;

	protected boolean isChecked() {
		return checked;
	}

	protected void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public int doStartTag() throws JspException {


		// read from list
		CkeckedListTag list = this.findAncestorTag(CkeckedListTag.class);
		if (list!=null){ // list exists
			this.name = list.getName();

			CheckableListItem item = (CheckableListItem)pageContext.getAttribute(list.getVar());
			if (item!=null){
				this.checked = item.isCheck();
				this.value = item.getId();
				this.text = item.getValue();
			}
		} // else standalone


		write("<input ");
		writeAttribute("type","checkbox");
		if (id!=null){
			writeAttribute("id",id);
		}
		writeAttribute("name",name);
		writeAttribute("value",value);
		if (this.checked != null && this.checked.booleanValue()){
			writeAttribute("checked", "checked");
		}
		writeLine(" />");
		write("<span>");
		write(this.text);
		write("</span><br/>");
		return SKIP_BODY;
	}

	public int doEndTag() {
		return EVAL_PAGE;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void release(){
		this.name = null;
	}
}
