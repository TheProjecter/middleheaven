package org.middleheaven.core.wiring;


public final class InterceptionContext {

	private Object object;
	private WiringQuery query;
	protected ResolutionContext relContext;

	public InterceptionContext(ResolutionContext relContext, WiringQuery query) {
		this.query = query;
		this.relContext = relContext;
	}

	public String getScopeName(){
		return relContext.getScopeName();
	}
	
	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public WiringQuery getWiringQuery(){
		return query;
	}
	
	public Class<?> getTarget() {
		return query.getContract();
	}

}
