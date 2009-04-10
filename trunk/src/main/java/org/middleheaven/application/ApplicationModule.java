package org.middleheaven.application;

public interface ApplicationModule {


	public ApplicationID getApplicationID();
	
	public ModuleID getModuleID();
	
	public void load(ApplicationContext context);
	
	public void unload(ApplicationContext context);
	
}