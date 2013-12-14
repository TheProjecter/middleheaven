package org.middleheaven.namedirectory.jndi;

import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Properties;

import javax.naming.Binding;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import org.middleheaven.namedirectory.NameDirectoryService;
import org.middleheaven.namedirectory.NameObjectEntry;
import org.middleheaven.namedirectory.NameTypeEntry;
import org.middleheaven.namedirectory.NamingDirectoryException;
import org.middleheaven.reflection.SignatureProxy;
import org.middleheaven.reflection.inspection.ClassIntrospector;
import org.middleheaven.reflection.inspection.Introspector;


public class JNDINameDirectoryService implements NameDirectoryService{

	private Properties properties;

	public JNDINameDirectoryService(){
		this.properties = System.getProperties();
	}
	
	public JNDINameDirectoryService(Properties properties){
		this.properties = properties;
	}

	public Properties getProperties(){
		return properties;
	}

	public InitialContext getContext() throws NamingException{
		return new InitialContext();
	}

	public <T> T lookup(String name, Class<T> type) throws NamingDirectoryException {

		Object object;
		try {
			object = getContext().lookup(name);
			if(object==null){
				return null;
			}

			final ClassIntrospector<T> introspector = Introspector.of(type);
			
			if (introspector.getIntrospected().isInstance(object)){
				return type.cast(object);
			} else if (object instanceof java.rmi.Remote){
				return type.cast(PortableRemoteObject.narrow(object, type));
			} else if (!Modifier.isFinal(type.getModifiers())){
				return introspector.newProxyInstance(new SignatureProxy(object));
			} else {
				throw new ClassCastException("Cannot cast " + object + " to " + type);
			}
		} catch (NamingException e) {
			throw new NamingDirectoryException(e);
		} catch (ClassCastException e) {
			throw new NamingDirectoryException(e);
		}


	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<NameTypeEntry> listTypes(String nameFilter) throws NamingDirectoryException {

		try {
			NamingEnumeration<NameClassPair> enumeration = getContext().list(nameFilter);
			
			if(enumeration==null){
				return Collections.emptySet();
			}
			
			Collection<NameTypeEntry>  result = new LinkedList<NameTypeEntry>();
			
			while(enumeration.hasMoreElements()){
				NameClassPair pair = enumeration.nextElement();
				
				result.add(new DetachedNameTypeEntry(pair.getName(), pair.getClassName()));
			}
			
			
			return result;
		} catch (NamingException e) {
			throw new NamingDirectoryException(e);
		} 


	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<NameObjectEntry> listObjects(String nameFilter)
			throws NamingDirectoryException {
		try {
			NamingEnumeration<Binding> enumeration = getContext().listBindings(nameFilter);
			
			if(enumeration==null){
				return Collections.emptySet();
			}
			
			Collection<NameObjectEntry>  result = new LinkedList<NameObjectEntry>();
			
			while(enumeration.hasMoreElements()){
				Binding binding = enumeration.nextElement();
				
				result.add(new DetachedNameObjectEntry(binding.getName(), binding.getClassName(), binding.getObject()));
			}
			
			
			return result;
		} catch (NamingException e) {
			throw new NamingDirectoryException(e);
		} 
	}


}
