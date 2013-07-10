package org.middleheaven.ui.web.vaadin;
import org.middleheaven.ui.SceneNavigator;
import org.middleheaven.ui.UIClient;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.components.UICommand;
import org.middleheaven.ui.components.UICommandSet;
import org.middleheaven.ui.components.UIForm;
import org.middleheaven.ui.components.UIImage;
import org.middleheaven.ui.components.UILabel;
import org.middleheaven.ui.components.UILayout;
import org.middleheaven.ui.components.UISecretField;
import org.middleheaven.ui.components.UITextField;
import org.middleheaven.ui.components.UIView;
import org.middleheaven.ui.components.UIWindow;
import org.middleheaven.ui.rendering.AbstractRenderKit;
import org.middleheaven.ui.rendering.RenderKit;
import org.middleheaven.ui.rendering.UIUnitConverter;

/**
 * 
 */

/**
 * Vaadin based {@link RenderKit}.
 */
public class VaadinRenderKit extends AbstractRenderKit {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final VaddinSceneNavigator vaddinSceneNavigator = new VaddinSceneNavigator();
	
	public VaadinRenderKit(){
		
// TODO add render by gid
		
		this.addRender(new VaadinClientRender(), UIClient.class);
		this.addRender(new VaadinWindowRender(), UIWindow.class);
		this.addRender(new VaadinViewRender(), UIView.class);
		
		this.addRender(new VaadinLabelRender(), UILabel.class);
		this.addRender(new VaadinImageRender(), UIImage.class);
		this.addRender(new VaddinFormRender(), UIForm.class);
		
		this.addRender(new VaadinTextInputRender(), UITextField.class);
		this.addRender(new VaadinSecretInputRender(), UISecretField.class);
	
		final VaadinTabsLayoutRender tabsRender = new VaadinTabsLayoutRender();
		
		this.addRender(tabsRender, UILayout.class, "tabs");
		this.addRender(tabsRender, UILayout.class, "tabs:vertical");
		this.addRender(tabsRender, UILayout.class, "tabs:horizontal");

		this.addRender( new VaadinBorderLayoutRender(), UILayout.class, "border");

		final VaadinFlowLayoutRender flowLayout = new VaadinFlowLayoutRender();
		this.addRender(flowLayout, UILayout.class);
		this.addRender(flowLayout, UILayout.class, "flow");
		this.addRender(flowLayout, UILayout.class, "flow:vertical");
		this.addRender(flowLayout, UILayout.class, "flow:horizontal");
		
//		
//		
//		UIRender ddr = new HtmlDropDownRender();
//		this.addRender(ddr, UISelectOne.class);
//		this.addRender(ddr, UISelectOne.class, "dropdown");
	
		this.addRender(new VaadinCommandSetRender(), UICommandSet.class);
		
		final VaadinCommandButtonRender render = new VaadinCommandButtonRender();
		this.addRender(render, UICommand.class);
		this.addRender(render, UICommand.class, "button");

		this.addRender(new VaadinLinkButtonRender(), UICommand.class, "link");
//		
		
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public UIUnitConverter getUnitConverted() {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SceneNavigator getSceneNavigator() {
		return vaddinSceneNavigator;
	}

	private static class VaddinSceneNavigator implements SceneNavigator{

		@Override
		public void dispose(UIComponent component) {
			//no-op
			component.getVisibleProperty().set(false);
		}


		@Override
		public void show(UIComponent component) {
			component.getVisibleProperty().set(true);
		}
	}



}
