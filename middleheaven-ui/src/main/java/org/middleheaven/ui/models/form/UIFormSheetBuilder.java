/**
 * 
 */
package org.middleheaven.ui.models.form;

import org.middleheaven.global.text.LocalizableText;
import org.middleheaven.ui.UIComponent;

/**
 * 
 */
public class UIFormSheetBuilder {

	private UIFormSheetModel uiFormSheetModel;

	/**
	 * Constructor.
	 * 
	 * @param uiFormSheetModel
	 */
	public UIFormSheetBuilder(UIFormSheetModel uiFormSheetModel) {
		this.uiFormSheetModel = uiFormSheetModel;
	}

	/**
	 * @param id
	 * @param valueOf
	 * @return
	 */
	public UIFormSheetBuilder addField(Class<? extends UIComponent> type, String id, LocalizableText valueOf) {


		return this;
	}

}
