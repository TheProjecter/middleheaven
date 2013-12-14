package org.middleheaven.domain.model;

import org.middleheaven.model.ModelSet;
import org.middleheaven.reflection.ClassSet;


public interface ModelBuilder <M , MODELSET extends ModelSet<M> >{

	public  abstract MODELSET build(ClassSet classes);
	
}
