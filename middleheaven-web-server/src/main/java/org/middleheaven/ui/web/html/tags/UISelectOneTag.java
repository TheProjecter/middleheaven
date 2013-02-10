package org.middleheaven.ui.web.html.tags;


import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.components.UISelectOne;
import org.middleheaven.ui.data.UIDataContainer;
import org.middleheaven.ui.data.UIDataItem;
import org.middleheaven.ui.web.tags.TagContext;

public class UISelectOneTag extends AbstractUIComponentIterationTagSupport  {


	private static final long serialVersionUID = -5139062294190732749L;
	private UIDataItem selected;
	private UIDataContainer data;
	

	public void setSelectedItemValue(UIDataItem selected){
		this.selected = selected;
	}
	
	public void dataContainer(UIDataContainer data){
		this.data = data;
	}

	public void releaseState(){
		super.releaseState();
		this.selected = null;
	}

	protected boolean beforeStart() {
		if(this.data != null){
			return false; // do not iterate
		}
	//	this.model = new HtmlSelectionModel();
		return true;
	}
	
	@Override
	public Class<? extends UIComponent> getComponentType() {
		return UISelectOne.class;
	}
	
	protected void prepareRender(TagContext attributeContext, UIComponent template) {
		super.prepareRender(attributeContext, template);
		
		UISelectOne select = (UISelectOne) template;
		select.setUIDataContainer(data);
		
		select.getNameProperty().set(this.getName());

		if(selected!=null && select.isSelectionEmpty()){
			
			int index = select.indexOf(selected);
			
			if(index  < 0 ){
				select.setSelectionInterval(0, 0);
			} else {
				select.setSelectionInterval(index, index);
			}
			
		}
	}
	
	public void addElement(String caption , Object value){
		// TODO
		// data.add(new UIDataItem(caption, value.toString(), false)); // false = not selected.
	
		
	}

	




}
