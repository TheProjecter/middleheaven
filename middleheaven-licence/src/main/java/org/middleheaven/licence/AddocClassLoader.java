package org.middleheaven.licence;

import java.util.Hashtable;
import java.util.Map;


public class AddocClassLoader extends ClassLoader {

	private Map<String,Class> classes = new Hashtable<String,Class>();

	public AddocClassLoader() {
		super(AddocClassLoader.class.getClassLoader());
	}

	public Class<?> loadClass(String className) throws ClassNotFoundException {
		return findClass(className);
	}


	public Class<?> findClass(String className){

		Class<?> result= classes.get(className);
		if(result != null){
			return result;
		}

		try{
			return super.findSystemClass(className);
		}catch(Exception e){
			return null;
		}

	}

	
	
	protected void addClassData(String className, byte[] classByte){
		Class<?> result = defineClass(className,classByte,0,classByte.length,null);
		classes.put(className,result);
	}
	
}
