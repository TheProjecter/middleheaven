package org.middleheaven.core.bootstrap;

import org.middleheaven.core.Container;

public interface BootstrapService {

	
	public Container getContainer();

	public void addListener(BootstapListener listener);
	public void removeListener(BootstapListener listener);
}