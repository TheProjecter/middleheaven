/**
 * 
 */
package org.middleheaven.persistance.model;

import java.util.HashMap;
import java.util.Map;

import org.middleheaven.reflection.MethodDelegator;
import org.middleheaven.reflection.ProxyHandler;
import org.middleheaven.reflection.inspection.Introspector;
import org.middleheaven.util.QualifiedName;


/**
 * 
 */
public final class DataSetDefinitions {

	
	private DataSetDefinitions(){}
	
	/**
	 * 
	 * 
	 * @param class1
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static < X , D extends DataSetDefinition<X>> X define(final Class<D> type){
		
		return (X) Introspector.of(type).newProxyInstance(
				new DataSetDefinitionProxyHandler(type),
				DataSetDefinition.class
		);

	}

	private static class DataSetDefinitionProxyHandler implements ProxyHandler {

		
		private Class type;
		private Map<String, TypeDefinition> typeDefinitions = new HashMap<String, TypeDefinition>();
		
		public DataSetDefinitionProxyHandler (Class type){
			this.type = type;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Object invoke(Object proxy, Object[] args,  MethodDelegator delegator) throws Throwable {
			final String name = delegator.getName().toLowerCase(); 
			
			TypeDefinition def = typeDefinitions.get(name);
			
			if (def == null) {
				def = new TypeDefinition() {

					@Override
					public QualifiedName getQualifiedName() {
						return QualifiedName.qualify(type.getSimpleName().toLowerCase(), name);
					}
					
				};
			}
			
			return def;
		}
		
	}
	
	
}
