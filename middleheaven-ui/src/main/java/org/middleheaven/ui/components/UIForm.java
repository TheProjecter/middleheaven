package org.middleheaven.ui.components;

import java.util.List;

import org.middleheaven.ui.models.form.UIFormSheetModel;


public interface UIForm extends UIContainer{

	
	public UICommandSet getCommandSet();
	
	public List<UIFormSheetModel> getFormSheets();
	
	
}
