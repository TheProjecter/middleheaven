package org.middleheaven.demos;

import java.io.File;

import org.middleheaven.application.ApplicationContext;
import org.middleheaven.application.ApplicationID;
import org.middleheaven.application.MainApplicationModule;
import org.middleheaven.core.wiring.Wire;
import org.middleheaven.ui.UIEnvironment;
import org.middleheaven.ui.UIService;
import org.middleheaven.ui.XMLUIComponentBuilder;
import org.middleheaven.util.Version;

public class TwoButtonsApplication extends MainApplicationModule{

	private UIService service;

	public TwoButtonsApplication() {
		super(new ApplicationID("twobuttons", Version.from(0, 0, 0)));
	
	}

	@Wire
	public void setUIService(UIService service){
		this.service = service;
	}
	
	@Override
	public void load(ApplicationContext context) {
		XMLUIComponentBuilder xmlBuilder = new XMLUIComponentBuilder();
		UIEnvironment root = xmlBuilder.buildFrom(new File("./src/demo/java/org/middleheaven/demos/ui.xml"));
		
		service.registerEnvironment(root, true);
	}

	@Override
	public void unload(ApplicationContext context) {
		// no-op
	}

}
