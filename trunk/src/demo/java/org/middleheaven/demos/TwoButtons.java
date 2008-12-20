package org.middleheaven.demos;
import org.middleheaven.application.ApplicationLoadingService;
import org.middleheaven.core.bootstrap.client.DesktopStarter;
import org.middleheaven.core.wiring.Wire;


public class TwoButtons extends DesktopStarter {

	public static void main (String[] args){
		
		new TwoButtons().execute(args);
		
	}

	@Wire
	public void setAppService(ApplicationLoadingService cycleService) {
		
		cycleService.getApplicationContext().addModule(new TwoButtonsApplication());
	}
}
