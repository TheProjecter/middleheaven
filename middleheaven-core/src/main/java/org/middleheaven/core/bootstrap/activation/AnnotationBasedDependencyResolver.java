package org.middleheaven.core.bootstrap.activation;

import org.middleheaven.core.services.ServiceActivator;
import org.middleheaven.core.wiring.BeanDependencyModel;
import org.middleheaven.core.wiring.DefaultWiringModelParser;
import org.middleheaven.reflection.Reflector;

public class AnnotationBasedDependencyResolver implements ActivatorDependencyResolver{

	@Override
	public void resolveDependency(Class<? extends ServiceActivator> activatorType,BeanDependencyModel model) {
		
		new DefaultWiringModelParser().readBeanModel(activatorType, model);
		

		
	}

}
