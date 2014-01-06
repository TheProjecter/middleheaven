package org.middleheaven.reflection;


public interface BeanAssembler {

	
	public <B> B assemble (Class<B> type);
}
