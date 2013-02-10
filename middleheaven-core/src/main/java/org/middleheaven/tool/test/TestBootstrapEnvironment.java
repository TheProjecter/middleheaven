package org.middleheaven.tool.test;

import org.middleheaven.core.bootstrap.client.AbstractStandaloneBootstrapEnvironment;
import org.middleheaven.core.services.Service;
import org.middleheaven.core.services.ServiceSpecification;

public class TestBootstrapEnvironment extends AbstractStandaloneBootstrapEnvironment {

	protected TestBootstrapEnvironment(){
		super();
	}
	
    @Override
    public String getName() {
        return "Test Container";
    }


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Service resolverRequestedService(ServiceSpecification spec) {
		return null;
	}
}
