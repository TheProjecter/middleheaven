package org.middleheaven.util;

public interface Incrementable <I> {

	
	public <T extends Incrementable<I>> T incrementBy(I increment);
}
