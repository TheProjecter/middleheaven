package org.middleheaven.ui.web.html;

import org.middleheaven.ui.SceneNavigator;
import org.middleheaven.ui.UIClient;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.components.UICommand;
import org.middleheaven.ui.components.UICommandSet;
import org.middleheaven.ui.components.UIForm;
import org.middleheaven.ui.components.UIImage;
import org.middleheaven.ui.components.UILabel;
import org.middleheaven.ui.components.UILayout;
import org.middleheaven.ui.components.UILayoutManager;
import org.middleheaven.ui.components.UISecretField;
import org.middleheaven.ui.components.UISelectOne;
import org.middleheaven.ui.components.UITextField;
import org.middleheaven.ui.components.UIView;
import org.middleheaven.ui.components.UIWindow;
import org.middleheaven.ui.rendering.AbstractRenderKit;
import org.middleheaven.ui.rendering.RenderKit;
import org.middleheaven.ui.rendering.RenderingContext;
import org.middleheaven.ui.rendering.UIRender;
import org.middleheaven.ui.rendering.UIUnitConverter;

/**
 * Html {@link RenderKit}.
 */
public class HtmlRenderKit extends AbstractRenderKit {

	
	private static final long serialVersionUID = 8348353086003428862L;

	public HtmlRenderKit(){
		
		

		this.addRender(new HtmlClientRender(), UIClient.class);
		this.addRender(new HtmlWindowRender(), UIWindow.class);
		this.addRender(new HtmlViewRender(), UIView.class);
		
		
		this.addRender(new HtmlLabelRender(), UILabel.class);
		this.addRender(new HtmlImageRender(), UIImage.class);
		this.addRender(new HtmlFormRender(), UIForm.class);
		
		this.addRender(new HtmlTextInputRender(), UITextField.class);
		this.addRender(new HtmlSecretRender(), UISecretField.class);
		
		final HtmlTabsLayoutRender tabsRender = new HtmlTabsLayoutRender();
		
		this.addRender(tabsRender, UILayout.class, "tabs");
		this.addRender(tabsRender, UILayout.class, "tabs:vertical");
		this.addRender(tabsRender, UILayout.class, "tabs:horizontal");

		this.addRender( new HtmlBorderLayoutRender(), UILayout.class, "border");

		this.addRender(new HtmlLayoutRender(), UILayout.class);
		
		
		UIRender ddr = new HtmlDropDownRender();
		this.addRender(ddr, UISelectOne.class);
		this.addRender(ddr, UISelectOne.class, "dropdown");
		
		this.addRender(new HtmlCommandSetRender(), UICommandSet.class);
		
		final HtmlCommandButtonRender render = new HtmlCommandButtonRender();
		this.addRender(render, UICommand.class);
		this.addRender(render, UICommand.class, "button");
		
		this.addRender(new HtmlCommandHiperLinkRender(), UICommand.class, "link");
		
		
	}
	

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public UIUnitConverter getUnitConverted() {
		// TODO implement RenderKit.getUnitConverted
		return null;
	}




	/**
	 * {@inheritDoc}
	 */
	@Override
	public SceneNavigator getSceneNavigator() {
		return new SceneNavigator() {
			
			@Override
			public void dispose(UIComponent component) {
				//no-op
				component.getVisibleProperty().set(false);
			}

			
			@Override
			public void show(UIComponent component) {
				component.getVisibleProperty().set(true);
			}

		};
	}



}
