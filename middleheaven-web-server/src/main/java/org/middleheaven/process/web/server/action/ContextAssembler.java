package org.middleheaven.process.web.server.action;

import org.middleheaven.core.reflection.BeanAssembler;
import org.middleheaven.core.reflection.PropertyHandler;
import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.process.AttributeContext;
import org.middleheaven.process.ContextScope;
import org.middleheaven.util.function.Block;

public class ContextAssembler implements BeanAssembler {

	AttributeContext context;
	ContextScope scope;
	private WiringService wiringService;
	private String objectName;
	
	public ContextAssembler(WiringService wiringService,AttributeContext context, ContextScope scope, String objectName) {
		this.context = context;
		this.scope = scope;
		this.wiringService = wiringService;
		this.objectName = objectName;
	}

	@Override
	public <B> B assemble(final Class<B> type) {
		
		final B instance = Introspector.of(type).newInstance();
		
		Introspector.of(type).inspect().properties().each(new Block<PropertyHandler>(){

			@Override
			public void apply(PropertyHandler acessor) {
				acessor.setValue(instance,context.getAttribute(
						scope,
						(objectName ==null ? "" : objectName + ".") + acessor.getName(), 
						acessor.getValueType()
						)
				);
			}
			
		});

		return instance;
	}

}
